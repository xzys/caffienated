/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.math.Vector2;

/** A hacky Collision between a body and the bounds box
 *
 * @author SACHIN
 */
public class BoundsCollision extends Collision {
    int bEdge;
    public BoundsCollision(Body body1, Body body2, int bEdge) {
        //TODO some way to make sure that tile is actaulyl a tile and not some other object
        super(body1, body2);
        this.bEdge = bEdge;
    }

    /**
     * Returns the axis in the direction of the tile's bEdge
     * same as bEdge collision
     * @return array of axes to be tested
     */
    @Override
    protected Vector2[] generateAxes() {
        //you shouuld need to only test this one axis
        //axes = new Vector2[]{(bEdge == 1 || bEdge == 3) ? new Vector2(1, 0) : new Vector2(0, 1)};
        switch (bEdge) {
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
    
    /** because bounds collsions are weird, they are colliding if the box is 
     * outside the bounds box, you have to account for this
     */
    /** this is only here to be replaced by boundsCollision*/
    @Override
    protected boolean checkInterval(float interval) {
        return interval > 0;
    }
}
