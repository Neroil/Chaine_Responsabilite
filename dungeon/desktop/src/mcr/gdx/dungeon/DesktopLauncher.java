package mcr.gdx.dungeon;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import mcr.gdx.dungeon.GdxDungeon;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Dongeon!!");
		config.setWindowedMode(800, 480);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("gdx-dungeon-mcr-responsibility-chain");
		new Lwjgl3Application(new GdxDungeon(), config);
	}
}
