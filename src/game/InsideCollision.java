/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.math.Vector2;


/* Class meant to test a case when a sprite is inside a tile and colliding it.
 * 
 */
public class InsideCollision extends Collision {
    /** Constructor:
     * 
     * @param body1 dynamic body
     * @param tile the tile it is inside
     */
    public InsideCollision(Body body1, Body tile) {
        //TODO assert tile instanceof Tile;
        super(body1, tile);
        //assuming that we are only solcing collisions between spirte/particle and tilemap
        assert body1 instanceof Sprite && body2 instanceof Tile : 
               "you are colliding " + body1.getClass().getName() + " and " + body2.getClass().getName(); 
    }
    /** If this is a rectangular Tlie.FUll then test axes, otherwise, it has to
     * be a triangular tile and only test the hypotenuse normal
     * 
     * @return array of aces to be tested
     */
    @Override
    protected Vector2[] generateAxes() {
        assert body2 instanceof Tile;
        //FULL doesn't really have a normal
        if(((Tile)body2).type == Tile.Type.FULL) {
            axes = new Vector2[2];
            //choose axes that project the correct way
            axes[0] = body1.pos.x > body2.pos.x + 25 ? new Vector2(1, 0) : new Vector2(-1, 0);
            axes[1] = body1.pos.y > body2.pos.y + 25 ? new Vector2(0, 1) : new Vector2(0, -1);
        } else {
            //check against aleady calculated normal of polygon triangle side + x + y axis
            //you don't really need to try against the x y axes if ou alreay know that you are inside this tile
            axes = new Vector2[]{((Tile)body2).getNormal()};//perpendicular to angle
        }
        return axes;
    }
}
