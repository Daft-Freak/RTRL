package org.tilegames.hexicube.topdownproto.map;

import org.tilegames.hexicube.topdownproto.Game;
import org.tilegames.hexicube.topdownproto.entity.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Tile
{
	public abstract boolean onWalkAttempt(Entity entity);
	
	public abstract void render(SpriteBatch batch, int x, int y);
	
	public abstract boolean setCurrentEntity(Entity entity);
	
	public abstract Entity getCurrentEntity();
	
	public abstract boolean givesLight();
	
	public abstract boolean takesLight();
	
	public abstract void use(Entity entity);
	
	protected void renderBaseTile(SpriteBatch batch, int x, int y, int sheetX, int sheetY)
	{
		batch.draw(Game.tileTex, Game.xOffset + x, Game.yOffset + y, 32, 32, sheetX, sheetY, 32, 32, false, false);
	}
	
	public int[] lightLevel = new int[3];
	public int[] lightSource = new int[3];
	
	public Map map;
}