package com.cs3750.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cs3750.Slapjack;

public class DesktopLauncher {
	
	private static final int SCREEN_HEIGHT = 607;
	private static final int SCREEN_WIDTH = 1080;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Slap Jack";
		config.height = SCREEN_HEIGHT;
        config.width = SCREEN_WIDTH;
        config.resizable = false;
		new LwjglApplication(new Slapjack(), config);
	}
}
