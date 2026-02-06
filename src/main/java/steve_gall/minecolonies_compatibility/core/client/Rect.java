package steve_gall.minecolonies_compatibility.core.client;

import net.minecraft.client.renderer.Rect2i;

public class Rect
{
	private int x;
	private int y;
	private int width;
	private int height;

	public Rect()
	{

	}

	public Rect(int x, int y, int width, int height)
	{
		this.setPosition(x, y);
		this.setSize(width, height);
	}

	public boolean contains(double x, double y)
	{
		return this.x <= x && x < this.x + this.width && this.y <= y && y < this.y + this.height;
	}

	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height)
	{
		this.width = Math.max(width, 0);
		this.height = Math.max(height, 0);
	}

	public Rect2i toRect2i()
	{
		return new Rect2i(this.x, this.y, this.width, this.height);
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

}
