/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.math.Vector2;

/** Collision with a corner of a tile
 *
 * @author SACHIN
 */

public class CornerCollision extends Collision {
    Vector2 corner;
    
    public CornerCollision(Body body1, Body tile, Vector2 corner) {
        //amke sure tha ttole is actually a tile
        super(body1, tile);
        this.corner = corner;
    }
    /** Only gets the axis that is from corner of tile to the center of circle
     * @return array of axes to be tested 
     */
    @Override
    protected Vector2[] generateAxes() {
        //you shouuld need to only test this one axis
        //get real life cordinates
        
        //old way
        //corner.add(body2.pos);
        //axes = new Vector2[]{corner.sub(body1.pos).mul(-1.nor()};
        
        //this way keeps corner intact even after 
        //the axis between the corner and the center of the sprite
        axes = new Vector2[]{corner.cpy().add(body2.pos).sub(body1.pos).mul(-1).nor()};
        //axes = new Vector2[]{body1.pos.cpy().sub(corner).nor()};
        
        return axes;
    }
}