/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/** Test the game and features before moving on to actual 
 * 
 * @author SACHIN
 */
public class Test_Game extends ApplicationAdapter {
    private static final int width = 800;
    private static final int height = 600;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shpren;
    private Texture wp;
    private int state;//the game state
    
    private GameWindow game;
    
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        
        cfg.title = "Game";
        cfg.useGL20 = true;
        cfg.width = width;
        cfg.height = height;
        new LwjglApplication(new Test_Game(), cfg);
    }
    
    public void create () {
        camera = new OrthographicCamera();
        //true is yaxis pointing down
        camera.setToOrtho(false, width, height);
        batch = new SpriteBatch();
        shpren = new ShapeRenderer();//for debugging
        wp = new Texture(Gdx.files.internal("whitepixel.png"));
        
        game = new GameWindow();
        game.initialize(16, 12, 50);
        //game state
        state = 0;
    }

    /** main game loop*/
    public void render () {      
        switch(state) {
            case 0://main game loop, othercases are for menus, levels etc
                if(Gdx.input.isKeyPressed(Keys.SPACE))
                    System.out.print("");
                game.run();
                game.render(shpren, batch, wp);
        }
    }
    
    public void dispose () { 
        
    }
}
