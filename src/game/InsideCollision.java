/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.math.Vector2;


/* Class meant to test a case when a sprite is inside a tile and colliding it
 * 
 */
public class InsideCollision extends Collision {
    
    public InsideCollision(Body body1, Body tile) {
        super(body1, tile);
        //assuming that we are only solcing collisions between spirte/particle and tilemap
        assert body1 instanceof Sprite && body2 instanceof Tile : 
               "you are colliding " + body1.getClass().getName() + " and " + body2.getClass().getName(); 
    }

    @Override
    protected Vector2[] generateAxes() {
        //you have to catch the exception if this is not true
        //just a reference
        Tile tile = (Tile)body2;
        //doesn't really have a normal
        if(tile.type == Tile.Type.FULL) {
            axes = new Vector2[2];
            //choose axes that project the correct way
            axes[0] = body1.pos.x > body2.pos.x + 25 ? new Vector2(1, 0) : new Vector2(-1, 0);
            axes[1] = body1.pos.y > body2.pos.y + 25 ? new Vector2(0, 1) : new Vector2(0, -1);
        } else {
            axes = new Vector2[1];
            //check against aleady calculated normal of polygon triangle side + x + y axis
            //you don't really need to try against the x y axes if ou alreay know that you are inside this tile
            axes[0] = tile.getNormal();//perpendicular to angle
            
        }
        return axes;
    }
}
