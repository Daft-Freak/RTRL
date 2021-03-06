package org.tilegames.hexicube.topdownproto.entity;

import java.util.ArrayList;

import org.tilegames.hexicube.topdownproto.Game;
import org.tilegames.hexicube.topdownproto.item.Item;
import org.tilegames.hexicube.topdownproto.item.weapon.DamageType;
import org.tilegames.hexicube.topdownproto.map.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntitySkeleton extends EntityLiving
{
	private static Texture tex = Game.loadImage("entity/skeleton");
	
	private int movementTimer;
	
	private boolean chasing;
	
	private Direction facingDir;
	
	public EntitySkeleton(int x, int y)
	{
		health = healthMax = 20;
		alive = true;
		xPos = x;
		yPos = y;

		facingDir = Direction.UP;
		effects = new ArrayList<Effect>();
		
		moveTransitionMaxTime = 45;
	}
	
	@Override
	public double damageAfterResistance(double damage, DamageType type)
	{
		if(type == DamageType.ACID) return damage * 2;
		if(type == DamageType.BLUNT) return damage * 1.5;
		if(type == DamageType.CRUSHING) return damage * 2;
		if(type == DamageType.EXPLOSIVE) return damage * 5;
		if(type == DamageType.FALLING) return damage * 2;
		if(type == DamageType.FIRE) return 0;
		if(type == DamageType.GENERIC) return damage;
		if(type == DamageType.ICE) return damage * 0.4;
		if(type == DamageType.SHARP) return damage * 0.2;
		return 0;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(!alive)
		{
			ArrayList<Item> items = new ArrayList<Item>();
			Game.insertRandomLoot(items, this);
			Map map = this.map;
			Game.removeEntity(this);
			Game.addEntity(new EntityChest(xPos, yPos, items), map, true);
			return;
		}
		if(movementTimer == 0)
		{
			movementTimer = 45;
			int xDist = Game.player.xPos - xPos;
			int yDist = Game.player.yPos - yPos;
			int dist = Math.abs(xDist) + Math.abs(yDist);
			if(!Game.player.alive) dist = 999999;
			if(!Game.player.visible(this)) dist = 999999;
			if(Game.player.map != map) dist = 999999;
			if(dist < 6) chasing = true;
			if(dist > 10) chasing = false;
			if(chasing)
			{
				if(dist == 1)
				{
					if(yDist == 0)
					{
						facingDir = (xDist > 0) ? Direction.RIGHT : Direction.LEFT;
					}
					else
					{
						facingDir = (yDist > 0) ? Direction.UP : Direction.DOWN;
					}
					Game.player.hurt(Game.rollDice(4, 1), DamageType.BLUNT);
				}
				else
				{
					if(yDist == 0)
					{
						facingDir = (xDist > 0) ? Direction.RIGHT : Direction.LEFT;
						move(facingDir, true);
					}
					else if(xDist == 0)
					{
						facingDir = (yDist > 0) ? Direction.UP : Direction.DOWN;
						move(facingDir, true);
					}
					else if(Game.rand.nextBoolean())
					{
						int oldX = xPos;
						facingDir = (xDist > 0) ? Direction.RIGHT : Direction.LEFT;
						move(facingDir, true);
						if(oldX == xPos)
						{
							facingDir = (yDist > 0) ? Direction.UP : Direction.DOWN;
							move(facingDir, true);
						}
					}
					else
					{
						int oldY = yPos;
						facingDir = (yDist > 0) ? Direction.UP : Direction.DOWN;
						move(facingDir, true);
						if(oldY == yPos)
						{
							facingDir = (xDist > 0) ? Direction.RIGHT : Direction.LEFT;
							move(facingDir, true);
						}
					}
				}
			}
			else
			{
				int oldX = xPos, oldY = yPos;
				for(int a = 0; a < 5; a++)
				{
					int rand = Game.rand.nextInt(4);
					if(rand == 0) facingDir = Direction.UP;
					else if(rand == 1) facingDir = Direction.DOWN;
					else if(rand == 2) facingDir = Direction.LEFT;
					else if(rand == 3) facingDir = Direction.RIGHT;
					move(facingDir, true);
					if(xPos != oldX || yPos != oldY) break;
				}
			}
		}
		else movementTimer--;
		
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
		
		int texX = 0, texY = 0;
		if(facingDir == Direction.DOWN || facingDir == Direction.RIGHT) texX += 32;
		if(facingDir == Direction.LEFT || facingDir == Direction.DOWN) texY += 32;
		batch.draw(tex, x, y, 32, 32, texX, texY, 32, 32, false, false);
	}
	
	@Override
	public void collide(Entity entity)
	{}
	
	@Override
	public boolean mountable(Entity mounter)
	{
		// TODO: check this
		return false;
	}
}
