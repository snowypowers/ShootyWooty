package com.eye7.ShootyWooty.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eye7.ShootyWooty.main;
/*
 *  Default resolution of 960 * 540.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Shooty";
        config.width = 960;
        config.height = 540;
		new LwjglApplication(new main(), config);
	}
}
