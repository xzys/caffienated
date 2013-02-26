/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.Arrays;
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
    
    private World world;

    private Tilemap tilemap;
    private Player[] players;
    
    private int state;//the game state
    private long last = 0;//last millis() count
    
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
        
        //all game objects
        world = new World(width, height);
        world.setGravity(new Vector2(0, -0.1f));
        //our collision resolver class

        tilemap = new Tilemap(16, 12, (float) (50), new Color(.3f, .3f, .3f, 1));
        tilemap.initialize(world, "tilemap.txt");
        //print out tilemap
        for(int i=0;i < tilemap.tmap.length;i++)
            System.out.println(Arrays.toString(tilemap.tmap[i]));

        
        //players = new Player[]{new Player("pl1", 1, 0, 0, world),new Player("pl2", 1, 0, 0, world)};
        players = new Player[]{new Player("pl1", 1, 0, 0, world)};
        //players = new Player[0];

        state = 0;
    }

    /** main game loop*/
    public void render () {        
        switch(state) {
            case 0://main game loop, othercases are for menus, levels etc
                long diff = System.nanoTime() - last;
                last = System.nanoTime();
                //update physics world
                world.run(diff * 0.000000001f);
                world.detect();
                world.solve();
                if(players[0].sprite.contacts.size() > 0) players[0].sprite.contacts.get(0).resolve();
                
                //update players this is movement not collision detection
                for(Player player : players) player.run();
                
                //graphics
                //draw background
                Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
                batch.begin();
                batch.setColor(.9f, .9f, .9f, 1);
                batch.draw(wp, 0, 0, 800, 600);
                batch.end();
                //TODO you have to enable blen for shpren? I think
                Gdx.gl.glEnable(GL10.GL_BLEND);
                Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                tilemap.render(shpren);
                world.renderCollisions(shpren);
                for(Player player : players) {
                    player.render_debug(shpren);
                    player.render(batch);
                }
                Gdx.gl.glDisable(GL10.GL_BLEND);
                
                
                //System.out.println((System.nanoTime() - last));
                System.out.println(players[0].sprite.touchingGround());
                
        }
    }
    
    public void dispose () { 
    }
}
