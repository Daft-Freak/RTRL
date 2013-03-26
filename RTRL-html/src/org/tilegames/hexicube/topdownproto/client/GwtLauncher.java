package org.tilegames.hexicube.topdownproto.client;

import org.tilegames.hexicube.topdownproto.Game;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderCallback;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;

public class GwtLauncher extends GwtApplication
{
	
	long loadStart = TimeUtils.nanoTime();
	
	@Override
	public GwtApplicationConfiguration getConfig ()
	{
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800, 600);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener()
	{
		return new Game();
	}
	
	public PreloaderCallback getPreloaderCallback()
	{
		final Canvas canvas = Canvas.createIfSupported();
		
		final int canvasWidth = 800;
		final int canvasHeight = 600;
		
		final int progressWidth = canvasWidth - 50;
		final int progressHeight = 70;
		
		canvas.setWidth("" + canvasWidth + "px");
		canvas.setHeight("" + canvasHeight + "px");
		canvas.setCoordinateSpaceWidth(canvasWidth);
		canvas.setCoordinateSpaceHeight(canvasHeight);
		
		getRootPanel().add(canvas);
		
		final Context2d context = canvas.getContext2d();
		context.setTextAlign(TextAlign.CENTER);
		context.setTextBaseline(TextBaseline.MIDDLE);
		

		return new PreloaderCallback()
		{
			@Override
			public void done()
			{
				
			}

			@Override
			public void loaded (String file, int loaded, int total)
			{
				context.setFillStyle(Pixmap.make(0, 0, 0, 1));
				context.fillRect(0, 0, canvasWidth, canvasHeight);
				
				int x = canvasWidth/2 - progressWidth/2;
				int y = canvasHeight/2 - progressHeight/2;
				
				System.out.println("Loaded " + file + "," + loaded + "/" + total);
				String colour = Pixmap.make(30, 30, 30, 1);
				String fillColour = Pixmap.make(20, 20, 170, 1);
				
				//progress border/bg
				context.setFillStyle(colour);
				context.setStrokeStyle(fillColour);
				context.setLineWidth(3);
				context.fillRect(x + 3, y + 3, progressWidth - 6, progressHeight - 6);
				context.strokeRect(x + 3, y + 3, progressWidth - 6, progressHeight - 6);
				
				//progress fill
				context.setFillStyle(fillColour);
				context.fillRect(x + 3, y + 3, (progressWidth - 6) * (loaded / (float)total), progressHeight - 6);

				context.setFillStyle(Pixmap.make(255, 255, 255, 1));
				context.setFont("20px sans-serif");
				context.fillText("Loading", x + progressWidth / 2, y + 25);
				
				context.setFont("15px sans-serif");
				context.fillText(file, x + progressWidth / 2, y + 40);
			}

			@Override
			public void error(String file)
			{
				System.out.println("Error loading: " + file);
			}
		};
	}
}