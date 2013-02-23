/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import processing.core.PGraphics;

/** Circular body with radius
 * @author SACHIN
 */
public class CircularBody extends Body {
    float radius;
    /** Constructor: see new Body(boolean) */
    public CircularBody(boolean dynamic) {
        this(dynamic, 10);//default
    }
    /** Constructor: specify raidus) */
    public CircularBody(boolean dynamic, float radius) {
        super(dynamic);
        setRadius(radius);
    }
    
    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    /** Render circle using shaperendeer*/
    public void render(ShapeRenderer g) {
        //just draw a circle at pos with radius X       
        g.begin(ShapeRenderer.ShapeType.FilledCircle);
        g.setColor(.60f, .60f, .60f, .8f);
        g.filledCircle((float) pos.x, (float) pos.y, (float) radius);
        
        g.end();
        
        //all lines here
        g.begin(ShapeRenderer.ShapeType.Line);
        g.setColor(Color.BLACK);
        g.line((float) pos.x, (float) pos.y, (float) (pos.x + vel.x), (float) (pos.y + vel.y));
        g.end();
    }
    
    /** Render circle using PGraphics*/
    /*
    public void render(PGraphics g) {
        //just draw a circle at pos with radius X       
        g.beginDraw();
        //constant
        g.fill(100, 100);
        g.ellipse(pos.x, pos.y, radius*2, radius*2);
        g.endDraw();
    }*/
    
}
