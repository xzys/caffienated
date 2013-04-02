/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.Arrays;

/** The window of the actual playing game, apart from the menus and selection screens
 *
 * @author SACHIN
 */
public class GameWindow {
    private World world;
    private Tilemap tilemap;
    private Player[] players;    
    private long last = 0;//last millis() count

    
    public GameWindow() {
        
    }
    /** initialize all game objects */
    public void initialize(int tx, int ty, int tsize) {
        world = new World(tx*tsize, ty*tsize);
        world.setGravity(new Vector2(0, -0.1f));
        //our collision resolver class

        tilemap = new Tilemap(tx, ty, tsize, new Color(.3f, .3f, .3f, 1));
        tilemap.initialize(world, "tilemap.txt");
        //print out tilemap
        for(int i=0;i < tilemap.tmap.length;i++)
            System.out.println(Arrays.toString(tilemap.tmap[i]));
        
        //players = new Player[]{new Player("pl1", 1, 0, 0, world),new Player("pl2", 1, 0, 0, world)};
        players = new Player[]{new Player("pl1", 1, 0, 0, world)};
        
        players[0].sprite.translate(400, 100);
        players[0].sprite.vel.add(0, -10);
    }
    /** the game loop*/
    public void run() {
        long diff = System.nanoTime() - last;
        last = System.nanoTime();
        
        //update physics world
        world.detect();
        //debugging
        /*for(Collision c : world.collisions) {
            if(c instanceof CornerCollision) {
                System.out.print("");
            }
        }*/               

        world.solve();
        world.run(diff * 0.000000001f);

        //update players this is movement not collision detection
        for(Player player : players) player.run();
    }
    
    /** Game Graphics*/
    public void render(ShapeRenderer shpren, SpriteBatch batch,Texture wp) {
        //draw background
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setColor(.9f, .9f, .9f, 1);
        batch.draw(wp, 0, 0, 800, 600);
        batch.end();
        
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        tilemap.render(shpren);
        //tilemap.renderEdges(shpren);
        //world.renderActiveTiles(shpren, players[0]);
        //world.renderCollisions(shpren);
        for(Player player : players) {
            //player.render_debug(shpren);
            player.render(batch);
        }
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }
    
    public void destroy() {
        
    }
}