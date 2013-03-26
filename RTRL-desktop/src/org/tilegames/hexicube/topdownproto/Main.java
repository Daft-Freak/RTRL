package org.tilegames.hexicube.topdownproto;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main
{
	public static LwjglApplicationConfiguration config;
	
	public static void main(String[] args)
	{
		config = new LwjglApplicationConfiguration();

		config.title = Game.gameName;

		config.width = 800;
		config.height = 600;
		config.useGL20 = false;
		
		new LwjglApplication(new Game(), config);
	}
}
