/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.math.Vector2;

/** Collision with a corner of a tile, basically a double edge collision
 *
 * @author SACHIN
 */

public class CornerCollision extends Collision {
    Vector2 corner;
    
    public CornerCollision(Body body1, Body body2, Vector2 corner) {
        //amke sure tha ttole is actually a tile
        super(body1, body2);
        this.corner = corner;
    }
    /** Only gets the axis that is from corner of tile to the center of circle
     * @return array of axes to be tested 
     */
    @Override
    protected Vector2[] generateAxes() {
        //revised algorithm
        //edges are l,d,r,u
        
        int hedge = ((Tile)body2).edges[(int) (1 + corner.x)];
        int vedge = ((Tile)body2).edges[(int) (2 + corner.y)];
        //be cause of broadphase, assume that neither edge is interesting
        assert !(hedge == 2 || vedge == 2);
        
        axes = new Vector2[1 + hedge + vedge];
        axes[0] = body1.pos.cpy().sub(body2.pos.cpy().add(25, 25).sub(corner.x*25, corner.y*25)).nor();
        //but 2nd (or last if that is what is is
        if(hedge == 1)
            axes[1] = corner.x < 0  ? new Vector2(-1, 0) : new Vector2(1, 0);
        //put in the last place, even if that is 2nd
        if(vedge == 1)
            axes[axes.length - 1] = corner.y < 0  ? new Vector2(0, -1) : new Vector2(0, 1);
        
        return axes;
    }
}