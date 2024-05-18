package steve_gall.minecolonies_compatibility.module.common.farmersdelight.entity.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.colony.permissions.Action;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.entity.ai.statemachine.AIEventTarget;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIBlockingEventType;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.entity.citizen.VisibleCitizenStatus;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.MessageUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.WorldUtil;
import com.minecolonies.api.util.constant.CitizenConstants;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.StatisticsConstants;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.api.util.constant.TranslationConstants;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.buildings.modules.ItemListModule;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingCook;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;
import com.minecolonies.core.colony.jobs.JobCook;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAISkill;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;
import steve_gall.minecolonies_compatibility.core.common.item.ItemHandlerHelper2;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CookingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CookingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.job.JobFarmersCook;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizableRecipeStorage;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class EntityAIWorkFarmersCook extends AbstractEntityAISkill<JobFarmersCook, BuildingCook>
{
	public static final int SATURATION_TO_SERVE = 16;
	public static final double BASE_XP_GAIN = 2;
	public static final int LEVEL_TO_FEED_PLAYER = 10;
	public static final VisibleCitizenStatus COOK_ICON = new VisibleCitizenStatus(new ResourceLocation(Constants.MOD_ID, "textures/icons/work/cook.png"), "com.minecolonies.gui.visiblestatus.cook");

	private CookingRecipeStorage selectedRecipe = null;
	private BlockPos cookingPotPosition = null;

	private final List<ItemStorage> needsStorages = new ArrayList<>();
	private final Map<ItemStorage, ItemStorage> totalNeeds = new HashMap<>();
	private final List<IRecipeStorage> availableRecipes = new ArrayList<>();

	private final List<AbstractEntityCitizen> citizenToServe = new ArrayList<>();
	private final List<Player> playerToServe = new ArrayList<>();
	private Set<ItemStorage> reservedItemCache = new HashSet<>();

	@SuppressWarnings({"rawtypes", "unchecked"})
	public EntityAIWorkFarmersCook(@NotNull JobFarmersCook job)
	{
		super(job);
		super.registerTargets(//
				new AITarget(AIWorkerState.IDLE, AIWorkerState.START_WORKING, STANDARD_DELAY), //
				new AITarget(AIWorkerState.START_WORKING, this::startWorking, STANDARD_DELAY), //
				new AITarget(FarmersCookAIState.INSERT_INPUT, this::insertInput, STANDARD_DELAY), //
				new AITarget(FarmersCookAIState.EXTRACT_OUTPUT, this::extractOutput, STANDARD_DELAY), //
				new AITarget(AIWorkerState.COOK_SERVE_FOOD_TO_CITIZEN, this::serveFood, 30), //
				new AIEventTarget(AIBlockingEventType.AI_BLOCKING, this::accelerateCookingPots, Constants.TICKS_SECOND)//
		);

		this.worker.setCanPickUpLoot(true);
	}

	public IAIState startWorking()
	{
		this.cookingPotPosition = null;

		if (this.walkToBuilding())
		{
			return this.getState();
		}

		var citizen = this.worker.getCitizenData();
		citizen.setVisibleStatus(VisibleCitizenStatus.WORKING);

		var cookingModule = this.building.getFirstModuleOccurance(CookingCraftingModule.class);
		cookingModule.requestFindWorkingBlocks(this.worker);

		var cookingPots = cookingModule.getWorkingBlocks(this.world).toList();

		if (cookingPots.size() == 0)
		{
			citizen.triggerInteraction(new StandardInteraction(cookingModule.getWorkingBlockNotFoundMessage(), ChatPriority.BLOCKING));
			return AIWorkerState.START_WORKING;
		}

		var checkCompleted = this.checkCompletedCookingPots(cookingPots);

		if (checkCompleted != null)
		{
			return checkCompleted;
		}

		var checkServe = this.checkNeedServe();

		if (checkServe != null)
		{
			return checkServe;
		}

		if (cookingModule.getRecipes().size() == 0)
		{
			citizen.triggerInteraction(new StandardInteraction(Component.translatable("minecolonies_compatibility.interaction.no_farmers_cooking_recipe"), ChatPriority.BLOCKING));
			this.walkToBuilding();
			return this.getState();
		}

		var checkIdling = this.checkIdlingCookingPots(cookingPots);

		if (checkIdling != null)
		{
			return checkIdling;
		}

		return AIWorkerState.START_WORKING;
	}

	protected IAIState checkCompletedCookingPots(List<BlockPos> cookingPots)
	{
		var completedPosition = this.findCompletedCookingPots(cookingPots).findAny();

		if (completedPosition.isPresent())
		{
			if (this.world.getBlockEntity(completedPosition.get()) instanceof CookingPotBlockEntity cookingPot)
			{
				var potInventory = cookingPot.getInventory();

				if (potInventory.getStackInSlot(CookingPotBlockEntity.OUTPUT_SLOT).isEmpty())
				{
					this.needsStorages.add(new ItemStorage(cookingPot.getContainer()));
				}

				this.cookingPotPosition = completedPosition.get();
				return FarmersCookAIState.EXTRACT_OUTPUT;
			}

		}

		return null;
	}

	protected IAIState checkNeedServe()
	{
		this.citizenToServe.clear();
		this.playerToServe.clear();

		var hasFoodInBuilding = false;
		var ownInventory = this.worker.getInventoryCitizen();

		for (var citizen : WorldUtil.getEntitiesWithinBuilding(this.world, EntityCitizen.class, this.building, null))
		{
			var job = citizen.getCitizenJobHandler().getColonyJob();

			if ((job instanceof JobCook || job instanceof JobFarmersCook) || !this.shouldBeFed(citizen) || InventoryUtils.hasItemInItemHandler(citizen.getItemHandlerCitizen(), stack -> ItemStackUtils.CAN_EAT.test(stack) && this.canEat(citizen, stack)))
			{
				continue;
			}

			Predicate<ItemStack> foodPredicate = stack -> ItemStackUtils.CAN_EAT.test(stack) && this.canEat(citizen, stack) && !this.isItemStackForAssistant(stack);

			if (InventoryUtils.hasItemInItemHandler(ownInventory, foodPredicate))
			{
				this.citizenToServe.add(citizen);
			}
			else if (InventoryUtils.hasItemInProvider(this.building, foodPredicate))
			{
				hasFoodInBuilding = true;
				this.needsCurrently = new Tuple<>(foodPredicate, Constants.STACKSIZE);
			}

		}

		for (var player : WorldUtil.getEntitiesWithinBuilding(this.world, Player.class, this.building, null))
		{
			if (player.getFoodData().getFoodLevel() >= LEVEL_TO_FEED_PLAYER || !this.building.getColony().getPermissions().hasPermission(player, Action.MANAGE_HUTS) || InventoryUtils.hasItemInItemHandler(new InvWrapper(player.getInventory()), stack -> ItemStackUtils.CAN_EAT.test(stack) && this.canEat(player, stack)))
			{
				continue;
			}

			Predicate<ItemStack> foodPredicate = stack -> ItemStackUtils.CAN_EAT.test(stack) && !this.isItemStackForAssistant(stack);

			if (InventoryUtils.hasItemInItemHandler(ownInventory, foodPredicate))
			{
				this.playerToServe.add(player);
			}
			else if (InventoryUtils.hasItemInProvider(this.building, foodPredicate))
			{
				hasFoodInBuilding = true;
				this.needsCurrently = new Tuple<>(foodPredicate, Constants.STACKSIZE);
			}

		}

		if (hasFoodInBuilding)
		{
			return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
		}

		if (!this.citizenToServe.isEmpty() || !this.playerToServe.isEmpty())
		{
			return AIWorkerState.COOK_SERVE_FOOD_TO_CITIZEN;
		}

		return null;
	}

	protected IAIState checkIdlingCookingPots(List<BlockPos> cookingPots)
	{
		var idlingPosition = this.findIdlingCookingPots(cookingPots).findAny();

		if (idlingPosition.isPresent())
		{
			this.selectedRecipe = null;
			this.requestTotalNeeds();

			if (this.availableRecipes.size() > 0)
			{
				var recipe = this.availableRecipes.get(0);
				this.selectedRecipe = recipe instanceof ICustomizableRecipeStorage r1 && r1.getImpl() instanceof CookingRecipeStorage r2 ? r2 : null;
				this.needsStorages.addAll(recipe.getCleanedInput());
				this.cookingPotPosition = idlingPosition.get();
				return FarmersCookAIState.INSERT_INPUT;
			}

		}

		return null;
	}

	protected IAIState insertInput()
	{
		if (this.cookingPotPosition == null || this.selectedRecipe == null)
		{
			return AIWorkerState.START_WORKING;
		}

		var getNeeds = this.getNeeds();

		if (getNeeds != null)
		{
			return getNeeds;
		}

		if (this.walkToBlock(this.cookingPotPosition) || this.hasNotDelayed(STANDARD_DELAY))
		{
			return this.getState();
		}

		var worker = this.worker;
		var recipe = this.selectedRecipe;
		var ownInventory = worker.getInventoryCitizen();
		var requiredTool = recipe.getRequiredTool();
		var toolSlot = -1;

		if (requiredTool != ToolType.NONE)
		{
			toolSlot = InventoryUtils.findFirstSlotInItemHandlerWith(ownInventory, stack -> ItemStackUtils.isTool(stack, requiredTool));
		}

		var hand = InteractionHand.MAIN_HAND;

		if (toolSlot != -1)
		{
			ownInventory.setHeldItem(hand, toolSlot);
			worker.setItemInHand(hand, ownInventory.getStackInSlot(toolSlot));
		}
		else
		{
			worker.setItemInHand(hand, ItemStack.EMPTY);
		}

		if (this.world.getBlockEntity(this.cookingPotPosition) instanceof CookingPotBlockEntity cookingPot)
		{
			if (this.isIdling(cookingPot))
			{
				var potInventory = cookingPot.getInventory();
				this.extractFromPotInventory(potInventory);

				if (!this.insertIntoPotInventory(potInventory, recipe))
				{
					this.extractFromPotInventory(potInventory);
				}

			}

		}

		return AIWorkerState.START_WORKING;
	}

	protected IAIState extractOutput()
	{
		if (this.cookingPotPosition == null)
		{
			return AIWorkerState.START_WORKING;
		}

		var getNeeds = this.getNeeds();

		if (getNeeds != null)
		{
			return getNeeds;
		}

		if (this.walkToBlock(this.cookingPotPosition) || this.hasNotDelayed(STANDARD_DELAY))
		{
			return this.getState();
		}

		if (this.world.getBlockEntity(this.cookingPotPosition) instanceof CookingPotBlockEntity cookingPot)
		{
			var potInventory = cookingPot.getInventory();
			var output = potInventory.getStackInSlot(CookingPotBlockEntity.OUTPUT_SLOT);

			if (!output.isEmpty() && InventoryUtils.transferItemStackIntoNextFreeSlotInItemHandler(potInventory, CookingPotBlockEntity.OUTPUT_SLOT, this.getInventory()))
			{
				this.worker.getCitizenExperienceHandler().addExperience(BASE_XP_GAIN);
				this.incrementActionsDoneAndDecSaturation();
				return AIWorkerState.INVENTORY_FULL;
			}

			var container = cookingPot.getContainer();

			if (!container.isEmpty())
			{
				this.extractOrThrow(potInventory, CookingPotBlockEntity.CONTAINER_SLOT);

				if (ItemHandlerHelper2.move(this.worker.getItemHandlerCitizen(), potInventory, CookingPotBlockEntity.CONTAINER_SLOT, Arrays.asList(new ItemStorage(container))))
				{
					return this.getState();
				}

			}

		}

		return AIWorkerState.START_WORKING;
	}

	protected IAIState getNeeds()
	{
		var needs = this.needsStorages;

		while (needs.size() > 0)
		{
			var need = needs.get(0);
			var countInInventory = InventoryUtils.getItemCountInItemHandler(this.worker.getInventoryCitizen(), stack -> ItemStorageHelper.matches(need, stack, true));
			var countInBuilding = InventoryUtils.getCountFromBuilding(this.building, need);

			if (countInInventory >= need.getAmount())
			{
				needs.remove(0);
			}
			else if ((countInInventory + countInBuilding) >= need.getAmount())
			{
				this.needsCurrently = new Tuple<>(stack -> ItemStorageHelper.matches(need, stack, false), need.getAmount());
				return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
			}
			else
			{
				return AIWorkerState.START_WORKING;
			}

		}

		return null;
	}

	protected IAIState serveFood()
	{
		if (this.citizenToServe.isEmpty() && this.playerToServe.isEmpty())
		{
			return AIWorkerState.START_WORKING;
		}

		this.worker.getCitizenData().setVisibleStatus(COOK_ICON);

		Player player = null;
		AbstractEntityCitizen citizen = null;
		LivingEntity living;
		IItemHandler handler;
		var list = this.playerToServe.isEmpty() ? this.citizenToServe : this.playerToServe;

		if (list == this.citizenToServe)
		{
			citizen = this.citizenToServe.get(0);
			living = citizen;
			handler = citizen.getInventoryCitizen();
		}
		else
		{
			player = this.playerToServe.get(0);
			living = player;
			handler = new InvWrapper(player.getInventory());
		}

		if (!this.building.isInBuilding(living.blockPosition()) || living.isDeadOrDying())
		{
			list.remove(0);
			return AIWorkerState.START_WORKING;
		}

		if (this.walkToBlock(living.blockPosition()))
		{
			return this.getState();
		}

		var ownInventory = this.worker.getInventoryCitizen();
		var colony = this.worker.getCitizenColonyHandler().getColony();

		if (InventoryUtils.isItemHandlerFull(handler))
		{
			if (citizen != null)
			{
				var foodSlot = InventoryUtils.findFirstSlotInItemHandlerWith(handler, stack -> ItemStackUtils.CAN_EAT.test(stack) && this.canEat(living, stack));

				if (foodSlot != -1)
				{
					var stack = ownInventory.extractItem(foodSlot, 1, true);

					if (stack.isEdible())
					{
						ownInventory.extractItem(foodSlot, 1, false);
						citizen.getCitizenData().increaseSaturation(stack.getItem().getFoodProperties(stack, this.worker).getNutrition() * 0.5D);
					}

					colony.getStatisticsManager().increment(StatisticsConstants.FOOD_SERVED, colony.getDay());
				}

			}

			list.remove(0);
			return this.getState();
		}
		else if (InventoryUtils.hasItemInItemHandler(handler, stack -> ItemStackUtils.CAN_EAT.test(stack) && this.canEat(living, stack)))
		{
			list.remove(0);
			return getState();
		}

		var foodCount = InventoryUtils.transferFoodUpToSaturation(this.worker, handler, this.building.getBuildingLevel() * SATURATION_TO_SERVE, stack -> ItemStackUtils.CAN_EAT.test(stack) && this.canEat(living, stack));

		if (foodCount <= 0)
		{
			list.remove(0);
			return this.getState();
		}

		colony.getStatisticsManager().incrementBy(StatisticsConstants.FOOD_SERVED, foodCount, worker.getCitizenColonyHandler().getColony().getDay());

		if (player != null)
		{
			MessageUtils.format(TranslationConstants.MESSAGE_INFO_CITIZEN_COOK_SERVE_PLAYER, worker.getName().getString()).sendTo(player);
		}

		list.remove(0);

		this.worker.getCitizenExperienceHandler().addExperience(BASE_XP_GAIN);
		this.incrementActionsDoneAndDecSaturation();
		return AIWorkerState.START_WORKING;
	}

	protected IAIState accelerateCookingPots()
	{
		var skillLevel = this.worker.getCitizenData().getCitizenSkillHandler().getLevel(this.getModuleForJob().getPrimarySkill());
		var accelerationTicks = (int) (skillLevel * MineColoniesCompatibilityConfigServer.INSTANCE.jobs.farmersCook.acceleratePerSkillLevel.get().doubleValue());
		var world = this.building.getColony().getWorld();

		var cookingModule = this.building.getFirstModuleOccurance(CookingCraftingModule.class);
		var cookingPots = cookingModule.getWorkingBlocks(this.world).toList();

		for (var pos : cookingPots)
		{
			if (WorldUtil.isBlockLoaded(world, pos))
			{
				if (world.getBlockEntity(pos) instanceof CookingPotBlockEntity cookingPot)
				{
					for (int i = 0; i < accelerationTicks; i++)
					{
						CookingPotBlockEntity.cookingTick(world, pos, world.getBlockState(pos), cookingPot);
					}

				}

			}

		}

		return null;
	}

	protected void requestTotalNeeds()
	{
		this.totalNeeds.clear();
		this.availableRecipes.clear();

		var cookingModule = this.building.getFirstModuleOccurance(CookingCraftingModule.class);
		var ownInventory = this.worker.getInventoryCitizen();

		for (var recipeToken : cookingModule.getRecipes())
		{
			if (cookingModule.isDisabled(recipeToken))
			{
				continue;
			}

			var recipe = IColonyManager.getInstance().getRecipeManager().getRecipes().get(recipeToken);
			var isReady = true;

			{
				var output = new ItemStorage(recipe.getPrimaryOutput());
				var countInInventory = InventoryUtils.getItemCountInItemHandler(ownInventory, stack -> ItemStorageHelper.matches(output, stack, true));
				var countInBuilding = InventoryUtils.getCountFromBuilding(this.building, output);

				if (countInInventory + countInBuilding >= output.getItemStack().getMaxStackSize())
				{
					continue;
				}

			}

			for (var ingredient : recipe.getCleanedInput())
			{
				var countInInventory = InventoryUtils.getItemCountInItemHandler(ownInventory, stack -> ItemStorageHelper.matches(ingredient, stack, true));
				var countInBuilding = InventoryUtils.getCountFromBuilding(this.building, ingredient);
				var needCount = ingredient.getAmount() - (countInInventory + countInBuilding);

				if (needCount > 0)
				{
					isReady = false;
					var totallyNeedCount = this.totalNeeds.computeIfAbsent(ingredient, ItemStorage::copy);

					if (needCount > totallyNeedCount.getAmount())
					{
						totallyNeedCount.setAmount(needCount);
					}

				}

			}

			if (isReady)
			{
				this.availableRecipes.add(recipe);
			}

		}

		for (var needs : this.totalNeeds.values())
		{
			this.checkIfRequestForItemExistOrCreateAsync(needs.getItemStack(), needs.getAmount(), 1, !needs.ignoreDamageValue());
		}

	}

	protected boolean shouldBeFed(AbstractEntityCitizen citizen)
	{
		var citizenData = citizen.getCitizenData();
		return citizenData != null && !citizenData.isWorking() && citizenData.getSaturation() <= CitizenConstants.AVERAGE_SATURATION && !citizenData.justAte();
	}

	protected boolean canEat(LivingEntity entity, ItemStack stack)
	{
		var foodExclusionListModule = this.worker.getCitizenData().getWorkBuilding().getModuleMatching(ItemListModule.class, m -> m.getId().equals(BuildingCook.FOOD_EXCLUSION_LIST));

		if (foodExclusionListModule.isItemInList(new ItemStorage(stack)))
		{
			return false;
		}

		if (entity instanceof AbstractEntityCitizen citizen)
		{
			var building = citizen.getCitizenData().getWorkBuilding();

			if (building != null)
			{
				return building.canEat(stack);
			}

		}

		return true;
	}

	protected boolean isItemStackForAssistant(ItemStack stack)
	{
		if (this.reservedItemCache.isEmpty())
		{
			this.reservedItemCache.addAll(this.building.getModule(BuildingModules.COOKASSISTENT_CRAFT).reservedStacks().keySet());
		}

		return reservedItemCache.contains(new ItemStorage(stack));
	}

	protected boolean insertIntoPotInventory(IItemHandlerModifiable potInventory, CookingRecipeStorage recipe)
	{
		var ownInventory = this.worker.getInventoryCitizen();

		if (!ItemHandlerHelper2.move(ownInventory, potInventory, 0, recipe.getIngredients()))
		{
			return false;
		}
		else if (!recipe.getContainer().isEmpty() && !ItemHandlerHelper2.move(ownInventory, potInventory, CookingPotBlockEntity.CONTAINER_SLOT, Arrays.asList(recipe.getContainer())))
		{
			return false;
		}

		return true;
	}

	protected void extractFromPotInventory(IItemHandlerModifiable potInventory)
	{
		for (var i = 0; i < CookingPotBlockEntity.MEAL_DISPLAY_SLOT; i++)
		{
			this.extractOrThrow(potInventory, i);
		}

		this.extractOrThrow(potInventory, CookingPotBlockEntity.CONTAINER_SLOT);
	}

	protected void extractOrThrow(IItemHandlerModifiable potInventory, int potSot)
	{
		if (!InventoryUtils.transferItemStackIntoNextFreeSlotInItemHandler(potInventory, potSot, this.worker.getInventoryCitizen()))
		{
			var remained = potInventory.extractItem(potSot, Integer.MAX_VALUE, false).copy();
			BehaviorUtils.throwItem(this.worker, remained, this.worker.position());
		}

	}

	public Stream<BlockPos> findCompletedCookingPots(List<BlockPos> positions)
	{
		return positions.stream().filter(pos ->
		{
			return this.world.getBlockEntity(pos) instanceof CookingPotBlockEntity cookingPot && this.isCompleted(cookingPot);
		});
	}

	public boolean isCompleted(CookingPotBlockEntity cookingPot)
	{
		var potInventory = cookingPot.getInventory();
		var output = potInventory.getStackInSlot(CookingPotBlockEntity.OUTPUT_SLOT);
		var display = potInventory.getStackInSlot(CookingPotBlockEntity.MEAL_DISPLAY_SLOT);
		return !output.isEmpty() || !display.isEmpty();
	}

	public Stream<BlockPos> findHeatedCookingPots(List<BlockPos> positions)
	{
		return positions.stream().filter(pos ->
		{
			return this.world.getBlockEntity(pos) instanceof CookingPotBlockEntity cookingPot && cookingPot.isHeated();
		});
	}

	public Stream<BlockPos> findIdlingCookingPots(List<BlockPos> positions)
	{
		return positions.stream().filter(pos ->
		{
			return this.world.getBlockEntity(pos) instanceof CookingPotBlockEntity cookingPot && this.isIdling(cookingPot);
		});
	}

	public boolean isIdling(CookingPotBlockEntity cookingPot)
	{
		var wrapper = new RecipeWrapper(cookingPot.getInventory());
		return this.world.getRecipeManager().getRecipeFor(ModRecipeTypes.COOKING.get(), wrapper, this.world).isEmpty();
	}

	@Override
	public Class<BuildingCook> getExpectedBuildingClass()
	{
		return BuildingCook.class;
	}

}
