let fs = require('fs');

function generate(name, foods) {
	let jsons = {};
	for (let namespace in foods) {
		let key = namespace == 'minecraft' ? namespace : 'mods';
		let json = jsons[key] ?? (jsons[key] = { 'values': [] });

		for (let id of foods[namespace].toSorted().map(i => namespace + ":" + i)) {
			if (namespace == 'minecraft') {
				json.values.push(id);
			} else {
				json.values.push({ 'required': false, 'id': id });
			}
		}
	}
	for (let namespace in jsons) {
		fs.writeFile(namespace + '/' + name + '.json', JSON.stringify(jsons[namespace]), 'utf8', () => { });
	}
}

function sort(obj) {
	let json = {};
	let keys = [];
	for (let key in obj) {
		keys.push(key);
	}
	for (let key of keys.toSorted(compareNamespace)) {
		json[key] = obj[key].map(i => i.substring(i.indexOf(':') + 1)).toSorted();
	}
	return json;
}

function compareNamespace(o1, o2) {
	if (o1 == 'minecraft') {
		return o1 == o2 ? 0 : -1;
	} else if (o2 == 'minecraft') {
		return 1;
	} else {
		return o1.localeCompare(o2);
	}
}

let fine_food = {
	"minecraft": [
		"baked_potato",
		"bread",
		"cooked_beef",
		"cooked_chicken",
		"cooked_cod",
		"cooked_mutton",
		"cooked_porkchop",
		"cooked_rabbit",
		"cooked_salmon",
		"cookie",
		"honey_bottle",
		"mushroom_stew"
	],
	"create": [
		"bar_of_chocolate",
		"builders_tea",
		"honeyed_apple",
		"sweet_roll"
	],
	"culturaldelights": [
		"avocado_toast",
		"calamari_roll",
		"cooked_squid",
		"egg_roll",
		"pufferfish_roll",
		"rice_ball",
		"smoked_cut_eggplant",
		"smoked_tomato",
		"smoked_white_eggplant",
		"tortilla",
		"tropical_roll"
	],
	"delightful": [
		"cactus_steak",
		"cantaloupe_bread",
		"cooked_marshmallow_stick",
		"roasted_acorn"
	],
	"farmersdelight": [
		"bacon_sandwich",
		"barbecue_stick",
		"cabbage_rolls",
		"chicken_sandwich",
		"cod_roll",
		"cooked_rice",
		"egg_sandwich",
		"ham",
		"honey_cookie",
		"mixed_salad",
		"mutton_wrap",
		"nether_salad",
		"salmon_roll",
		"smoked_ham",
		"sweet_berry_cookie",
		"tomato_sauce"
	],
	"farmersrespite": [
		"green_tea_cookie"
	],
	"largemeals": [
		"cooked_mutton_rack"
	],
	"moredelight": [
		"hamburger_with_egg",
		"omelette",
		"simple_hamburger",
		"toast",
		"toast_with_blueberries",
		"toast_with_chocolate",
		"toast_with_egg",
		"toast_with_glow_berries",
		"toast_with_honey",
		"toast_with_sweet_berries",
		"tomato_sandwich"
	],
	"tconstruct": [
		"bacon",
		"cheese_ingot"
	],
	"thermal": [
		"cheese_wedge",
		"pbj_sandwich",
		"sushi_maki"
	],
	"tinkersdelight": [
		"cheesecake",
		"cheesecake_slice",
		"honey_pie",
		"honey_pie_slice"
	],
	"veggiesdelight": [
		"baked_sweet_potato",
		"cauliflower_bread",
		"smoked_bellpepper"
	]
};
let decent_food = {
	"minecraft": [
		"beetroot_soup",
		"golden_apple",
		"golden_carrot",
		"pumpkin_pie",
		"rabbit_stew"
	],
	"create": [
		"chocolate_glazed_berries"
	],
	"culturaldelights": [
		"beef_burrito",
		"chicken_roll",
		"chicken_roll_slice",
		"creamed_corn",
		"eggplant_burger",
		"elote",
		"empanada",
		"fish_taco",
		"fried_eggplant_pasta",
		"hearty_salad",
		"midori_roll",
		"midori_roll_slice",
		"mutton_sandwich"
	],
	"delightful": [
		"cantaloupe_gummy",
		"cantaloupe_popsicle",
		"ender_nectar",
		"glow_jam_jar",
		"jam_jar",
		"matcha_gummy",
		"matcha_latte",
		"nut_butter_bottle",
		"rock_candy",
		"salmonberry_gummy",
		"salmonberry_pie",
		"salmonberry_pie_slice",
		"wrapped_cantaloupe"
	],
	"farmersdelight": [
		"apple_pie_slice",
		"bacon_and_eggs",
		"beef_stew",
		"bone_broth",
		"cake_slice",
		"chocolate_pie",
		"chocolate_pie_slice",
		"dumplings",
		"fish_stew",
		"fruit_salad",
		"glow_berry_custard",
		"grilled_salmon",
		"hamburger",
		"kelp_roll",
		"kelp_roll_slice",
		"melon_popsicle",
		"mushroom_rice",
		"ratatouille",
		"stuffed_potato",
		"sweet_berry_cheesecake",
		"sweet_berry_cheesecake_slice",
		"vegetable_soup"
	],
	"farmersrespite": [
		"black_cod",
		"black_tea",
		"coffee",
		"coffee_cake",
		"coffee_cake_slice",
		"dandelion_tea",
		"gamblers_tea",
		"green_tea",
		"long_apple_cider",
		"long_black_tea",
		"long_coffee",
		"long_dandelion_tea",
		"long_gamblers_tea",
		"long_green_tea",
		"long_purulent_tea",
		"long_rose_hip_tea",
		"long_yellow_tea",
		"purulent_tea",
		"rose_hip_pie",
		"rose_hip_pie_slice",
		"rose_hip_tea",
		"strong_apple_cider",
		"strong_black_tea",
		"strong_coffee",
		"strong_gamblers_tea",
		"strong_green_tea",
		"strong_hot_cocoa",
		"strong_melon_juice",
		"strong_purulent_tea",
		"strong_rose_hip_tea",
		"strong_yellow_tea",
		"yellow_tea"
	],
	"largemeals": [
		"cod_deluxe",
		"potato_soup",
		"red_soup",
		"rice_pudding",
		"sweet_berry_custard",
		"tomato_egg_soup"
	],
	"moredelight": [
		"carrot_soup",
		"chicken_salad",
		"chicken_sandwich_with_egg_and_tomato",
		"cooked_rice_with_beef",
		"cooked_rice_with_chicken_cuts",
		"cooked_rice_with_porkchop",
		"diced_potatoes_with_beef",
		"diced_potatoes_with_chicken_cuts",
		"diced_potatoes_with_egg_and_tomato",
		"diced_potatoes_with_porkchop",
		"egg_with_bacon_sandwich",
		"mashed_potatoes",
		"porkchop_sandwich",
		"potato_salad",
		"steak_sandwich"
	],
	"tconstruct": [
		"jeweled_apple"
	],
	"thermal": [
		"stuffed_pepper"
	],
	"tinkersdelight": [
		"crimson_broth",
		"ichor_salad",
		"tinkers_stew"
	],
	"veggiesdelight": [
		"beetroot_brownie",
		"beetroot_brownie_tray",
		"cacciatore",
		"carrot_cake",
		"carrot_cake_slice",
		"carrot_juice",
		"cauliflower_burger",
		"cauliflower_soup",
		"cesar_salad",
		"chicken_fajitas_wrap",
		"dandelion_and_eggs",
		"dandelion_juice",
		"fermented_garlic_honey",
		"fish_and_chips",
		"garlic_baked_cod",
		"garlic_bread",
		"garlic_stuffed_mushrooms",
		"mashed_potatoes",
		"mhadjeb",
		"potato_noodles",
		"shakshouka",
		"stuffed_bellpeppers",
		"sweet_potato_cupcake",
		"sweet_potato_mochi",
		"sweet_potato_pie",
		"sweet_potato_pie_slice",
		"whole_roasted_cauliflower"
	]
};
let great_food = {
	"minecraft": [],
	"culturaldelights": [
		"chicken_taco",
		"poached_eggplants",
		"spicy_curry"
	],
	"delightful": [
		"baklava",
		"baklava_slice",
		"berry_matcha_latte",
		"cactus_chili",
		"cactus_soup",
		"cheeseburger",
		"deluxe_cheeseburger",
		"field_salad",
		"matcha_ice_cream",
		"matcha_milkshake",
		"nut_butter_and_jam_sandwich",
		"salmonberry_ice_cream",
		"salmonberry_milkshake",
		"sinigang",
		"smore",
		"stuffed_cantaloupe",
		"stuffed_cantaloupe_block"
	],
	"farmersdelight": [
		"baked_cod_stew",
		"chicken_soup",
		"fried_rice",
		"honey_glazed_ham",
		"honey_glazed_ham_block",
		"noodle_soup",
		"pasta_with_meatballs",
		"pasta_with_mutton_chop",
		"pumpkin_soup",
		"roast_chicken",
		"roast_chicken_block",
		"roasted_mutton_chops",
		"shepherds_pie",
		"shepherds_pie_block",
		"squid_ink_pasta",
		"steak_and_potatoes",
		"stuffed_pumpkin",
		"stuffed_pumpkin_block",
		"vegetable_noodles"
	],
	"farmersrespite": [
		"blazing_chili",
		"tea_curry"
	],
	"largemeals": [
		"chicken_curry",
		"hearty_lunch",
		"mushroom_pot_pie",
		"mushroom_pot_pie_block",
		"omurice",
		"omurice_block",
		"pasta_with_mushroom_sauce",
		"pufferfish_broth",
		"roasted_mutton_rack",
		"roasted_mutton_rack_block"
	],
	"moredelight": [
		"creamy_pasta_with_chicken_cuts",
		"creamy_pasta_with_ham",
		"loaded_hamburger"
	],
	"thermal": [
		"hearty_stew",
		"spring_salad",
		"xp_stew"
	],
	"tinkersdelight": [
		"jewel_fruit_salad"
	],
	"veggiesdelight": [
		"garlic_chicken_stew",
		"roasted_vegetables",
		"vegan_pizza",
		"vegan_pizza_slice"
	]
};


generate("fine_food", fine_food);
generate("decent_food", decent_food);
generate("great_food", great_food);

