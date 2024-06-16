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
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class Assets {
    private static final Map<String, Texture> textures = new HashMap<String, Texture>();


    /**
     * Returns the texture at the specified path.
     * If the texture has not been loaded yet, it is loaded and stored in the textures map.
     *
     * @param path the path to the texture
     * @return the texture at the specified path
     */
    public static Texture get(String path) {
        Texture texture = textures.get(path);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            textures.put(path, texture);
        }
        return texture;
    }

    /**
     * Disposes of all loaded textures.
     */
    public static void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }

}