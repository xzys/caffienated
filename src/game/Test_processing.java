/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
package game;

import com.badlogic.gdx.graphics.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import processing.core.*;
import java.util.Arrays;
import java.util.List;
import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import processing.core.*;


public class Processing_test extends PApplet {
    private World world;
    private Player[] players;
    private Tilemap tilemap;
    
    private PGraphics gb;//graphics buffer
    private Vector2 ptwtrans;//pixels to world
    private Vector2 ptwscale;

    private int state;
    private float last = 0;//last millis() count
    
    public static void main(String[] args) {
        PApplet.main(new String[] { "game.Physics_test" });
    }
    
    public void setup() {
        size(500, 500);
        state = 0;
        gb = createGraphics(500, 500);
        
        //frameRate(1);
    }

    public void draw() {
        //background(240);
        switch(state) {
            case 0://setup
                ptwtrans = new Vector2(width * .5, height * .5);
                ptwscale = new Vector2(1, -1);
                
                world = new World(new AxisAlignedBounds(500, 500));
                world.setGravity(new Vector2(0, -20*9.8));
                //our collision resolver class
                world.addListener(new CollisionResolver());
                
                tilemap = new Tilemap(10, 10, (float) (50), new Color(240, 240, 240, 255), new Color(100, 100, 100, 255));
                //tile objets in first line of tilemap.txt
                if(tilemap.makeReferenceArray(readLines("tilemap.txt").get(0)))
                    tilemap.makeTileBodies(world);

                //players = new Player[]{new Player("pl1", 1, 0, 0, world),
                                       //new Player("pl2", 1, 0, 0, world)};
                players = new Player[]{new Player("pl1", 1, 0, 0, world)};
                
                //players = new Player[0];

                //print out 
                for(int i=0;i < tilemap.tmap.length;i++)
                    System.out.println(Arrays.toString(tilemap.tmap[i]));

                state++;
                break;
            case 1:
                //update physics world
                float diff = millis() - last;

                world.update(diff * .001);
                //update players
                for(Player player : players) player.run();
                
                
                
                //graphics
                //translate to world coordiantes and flip upside down
                last = millis();
                //tilemap.render(gb, ptwtrans, ptwscale);
                //for(Player player : players) player.render(gb, ptwtrans, ptwscale);
                
                //System.out.println(millis() - last);
                image(gb, 0, 0);
                
                //Vector2 loc = co.getTransform().getTranslation();
                //fill(0, 200, 0);
                //ellipse((float) loc.x + width * .5, (float) loc.y*-1 + height*.5, 20, 20);
                //println(loc.x + " " + loc.y);

                //boolean[][] new_active = new boolean[tilemap.tx][tilemap.ty];
                //for(boolean[] b1 : new_active) for(boolean b : b1) b = false;
                //new_active[int( mouseX/tilemap.tsize)][int( mouseY/tilemap.tsize)] = true;
                //tilemap.calculateActiveTiles(world, new_active);

        }
    }
    
    public List<String> readLines(String filename) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);       
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}

*/