package mcr.gdx.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * The Assets class is used to load and store textures for the game.
 * It ensures that each texture is only loaded once and that it is disposed of when the game is closed.
 *
 * @version 1.0
 * @author Edwin Haeffner
 * @author Esteban Logo
 * @author Junod Arthur
 * @author Yanis Ouadahi
 */
public class Assets {
    private static final Map<String, Texture> textures = new HashMap<String, Texture>();

    public static Texture get(String path) {
        Texture texture = textures.get(path);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            textures.put(path, texture);
        }
        return texture;
    }

    public static void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }

}