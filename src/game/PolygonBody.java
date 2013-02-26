/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import processing.core.PGraphics;

/** Body made up of set of vertices
 *
 * @author SACHIN
 */
public class PolygonBody extends Body {
    Vector2[] vertexes;
    
    /** Constructor: just calls super
     * 
     * @param dynamic is body moving?
     */
    public PolygonBody(boolean dynamic) {
        super(dynamic);
        //vertexes should already be initialzed to null or something
    }
    /** Constructor: calls super and sets vertices
     * 
     * @param dynamic is body moving?
     * @param vertexes array of Vector2 vertices
     */
    public PolygonBody(boolean dynamic, Vector2[] vertexes) {
        super(dynamic);
        this.vertexes = vertexes;
    }
    /** sets vertices */
    public void setVertexes(Vector2[] vertexes) {
        System.out.println("changing vertexes");
        this.vertexes = vertexes;
    }
    /** render simple polygon with shpren */
    //TODO actually render polygon bro
    public void render(ShapeRenderer g) {
        //just draw a circle at pos with radius X       
        g.begin(ShapeRenderer.ShapeType.FilledCircle);
        g.setColor(.90f, .90f, .90f, 1);
        g.filledCircle((float) pos.x, (float) pos.y, (float) 100*2);
        
        g.end();
        
    }
    
    /*
    //basic rener mthods
    public void render(PGraphics g) {     
        g.beginDraw();
        //constant
        g.fill(10);
        g.ellipse(pos.x, pos.y, 100, 100);
        g.endDraw();
    }
    */
    
    
}
