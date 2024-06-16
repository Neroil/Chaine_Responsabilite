package mcr.gdx.dungeon;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import mcr.gdx.dungeon.GdxDungeon;
import mcr.gdx.dungeon.Constants;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("The Edaryaban Dungeon");
		config.setWindowedMode(Constants.BASE_X_RESOLUTION, Constants.BASE_Y_RESOLUTION);
		config.useVsync(true);
		config.setForegroundFPS(Constants.TARGET_FPS);
		new Lwjgl3Application(new GdxDungeon(), config);
	}
}
