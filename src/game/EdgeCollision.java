/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author SACHIN
 */
//collision with more information
//this is a collision witha flat horizontal or vertical edge of a tile
public class EdgeCollision extends Collision {
    int edge;
    public EdgeCollision(Body body1, Body tile, int edge) {
        //some way to make sure that tile is actaulyl a tile and not some other object
        super(body1, tile);
        //l, d, r, u = 0,1,2,3
        //this is a good design choie because they reflect the information already stored in Tile
        this.edge = edge;
    }
    //basically a circle versus aabb box
    //also you already know the edge to test
    //if in the edge region of a polygon this should pick it up
    @Override
    protected Vector2[] generateAxes() {
        //you shouuld need to only test this one axis
        //axes = new Vector2[]{(edge == 1 || edge == 3) ? new Vector2(1, 0) : new Vector2(0, 1)};
        switch(edge) {
            case 0:
                return axes = new Vector2[]{new Vector2(-1, 0)};
            case 1:
                return axes = new Vector2[]{new Vector2(0, -1)};
            case 2:
                return axes = new Vector2[]{new Vector2(1, 0)};
            case 3:
                return axes = new Vector2[]{new Vector2(0, 1)};
            default:
                return axes = null;
        }
        
    }
    
}