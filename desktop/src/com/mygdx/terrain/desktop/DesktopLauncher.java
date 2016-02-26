package com.mygdx.terrain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.vc.TerrainGen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.width = 864;
		config.height = 864;
		config.vSyncEnabled = true;
		config.useHDPI = true;
		config.resizable = true ;
		new LwjglApplication(new TerrainGen(), config);
	}
}
