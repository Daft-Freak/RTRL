package org.tilegames.hexicube.topdownproto.entity;

import org.tilegames.hexicube.topdownproto.map.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity
{
	public int xPos, yPos;
	
	protected boolean movementTransition = false;
	protected int oldXPos, oldYPos, transXPos, transYPos;
	protected int moveTransitionTime, moveTransitionMaxTime = 15;
	
	public Map map;
	
	public Entity rider, riding;
	
	public void tick()
	{
		if(movementTransition)
		{
			moveTransitionTime++;
			
			float s = (float)moveTransitionTime / (float)moveTransitionMaxTime;
			
			if(oldXPos == xPos)
				transXPos = xPos * 32;
			else
			{
				float oldXPixels = (float)oldXPos * 32.0f;
				float newXPixels = (float)xPos * 32.0f;
				transXPos = (int)(oldXPixels * (1.0f - s) + (newXPixels * s));
			}
			if(oldYPos == yPos)
				transYPos = yPos * 32;
			else
			{
				float oldYPixels = (float)oldYPos * 32.0f;
				float newYPixels = (float)yPos * 32.0f;
				transYPos = (int)(oldYPixels * (1.0f - s) + (newYPixels * s));
			}
			
			if(moveTransitionTime == moveTransitionMaxTime)
				movementTransition = false;
		}
	}
	
	public abstract void render(SpriteBatch batch, int camX, int camY);
	
	public abstract void collide(Entity entity);
	
	public abstract boolean visible(Entity looker);
	
	public void move(Direction dir, boolean transition)
	{
		if(transition)
		{
			movementTransition = true;
			moveTransitionTime = 0;
			oldXPos = xPos;
			oldYPos = yPos;
		}
		
		move(dir);
		
		if(transition && oldXPos == xPos && oldYPos == yPos)
			movementTransition = false; //no movement actually happened
	}
	
	public void move(Direction dir)
	{
		boolean horizontal;
		int amount;
		if(dir == Direction.UP)
		{
			horizontal = false;
			amount = 1;
		}
		else if(dir == Direction.DOWN)
		{
			horizontal = false;
			amount = -1;
		}
		else if(dir == Direction.RIGHT)
		{
			horizontal = true;
			amount = 1;
		}
		else if(dir == Direction.LEFT)
		{
			horizontal = true;
			amount = -1;
		}
		else return;
		int dirX = 0, dirY = 0;
		if(horizontal) dirX = amount / Math.abs(amount);
		else dirY = amount / Math.abs(amount);
		for(int a = 0; a < Math.abs(amount); a++)
		{
			int newX = xPos + dirX;
			if(newX < 0 || newX >= map.tiles.length) break;
			int newY = yPos + dirY;
			if(newY < 0 || newY >= map.tiles[newX].length) break;
			Entity e = map.tiles[newX][newY].getCurrentEntity();
			if(e != null)
			{
				this.collide(e);
				e.collide(this);
			}
			else if(map.tiles[newX][newY].onWalkAttempt(this))
			{
				map.tiles[xPos][yPos].setCurrentEntity(null);
				map.tiles[newX][newY].setCurrentEntity(this);
				xPos = newX;
				yPos = newY;
			}
		}
	}
}
