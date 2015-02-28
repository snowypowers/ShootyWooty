package com.sutd.shooty.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sutd.shooty.MyShooty;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Shooty";
        config.width = 500;
        config.height = 400;
		new LwjglApplication(new MyShooty(), config);
	}
}
