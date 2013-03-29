package org.tilegames.hexicube.topdownproto.entity;

import org.tilegames.hexicube.topdownproto.Game;
import org.tilegames.hexicube.topdownproto.item.weapon.DamageType;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityLeechBolt extends Entity
{
	private static Texture tex = Game.loadImage("entity/leechbolt");
	
	private Direction direction;
	private int timer, flightTime;
	private Entity source;
	
	public EntityLeechBolt(Direction dir, Entity src, int x, int y)
	{
		direction = dir;
		source = src;
		timer = 5;
		xPos = x;
		yPos = y;
		flightTime = 0;
		
		moveTransitionMaxTime = 5;
	}
	
	@Override
	public void tick()
	{
		if(map == null) return;
		timer--;
		if(timer == 0)
		{
			timer = 5;
			int oldX = xPos, oldY = yPos;
			move(direction, true);
			flightTime++;
			if((oldX == xPos && oldY == yPos) || flightTime >= 10) Game.removeEntity(this);
		}
		
		super.tick();
	}
	
	@Override
	public void render(SpriteBatch batch, int camX, int camY)
	{
		int x = Game.xOffset + (xPos * 32) - camX;
		int y = Game.yOffset + (yPos * 32) - camY;
		
		if(movementTransition)
		{
			x = Game.xOffset + transXPos - camX;
			y = Game.yOffset + transYPos - camY;
		}
		
		batch.draw(tex, x, y);
	}
	
	@Override
	public void collide(Entity entity)
	{
		if(entity instanceof EntityLiving)
		{
			double dmg = ((EntityLiving) entity).hurt(Game.rollDice(10, 2), DamageType.GENERIC);
			if(source instanceof EntityLiving) ((EntityLiving)source).heal(dmg);
		}
		Game.removeEntity(this);
	}
	
	@Override
	public boolean visible(Entity looker)
	{
		return true;
	}
}
