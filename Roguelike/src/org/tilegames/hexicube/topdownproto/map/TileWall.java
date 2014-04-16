package org.tilegames.hexicube.topdownproto.map;

import org.tilegames.hexicube.topdownproto.entity.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileWall extends Tile
{
	public TileWall()
	{
		lightSource = new int[]
		{
				15, 15, 15
		};
	}
	
	@Override
	public boolean onWalkAttempt(Entity entity)
	{
		return false;
	}
	
	@Override
	public void render(SpriteBatch batch, int x, int y)
	{
		renderBaseTile(batch, x, y, 32, 0);
	}
	
	@Override
	public boolean setCurrentEntity(Entity entity)
	{
		return false;
	}
	
	@Override
	public Entity getCurrentEntity()
	{
		return null;
	}
	
	@Override
	public boolean givesLight()
	{
		return false;
	}
	
	@Override
	public boolean takesLight()
	{
		return false;
	}
	
	@Override
	public void use(Entity entity)
	{}
}
