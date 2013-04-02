/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.math.Vector2;

/** A general case collision between any 2 objects, only temporary object
 *
 * @author SACHIN
 */
public class Collision {
    Body body1;
    Body body2;
    //penetration
    //remember collision noraml is different than tile normal
    Vector2 normal;
    Vector2[] axes;
    float depth;
    
    private static final float bounceAmount = 0;
    private static final float frictionAmount = 1;
    
    boolean solved;
    boolean colliding;
    boolean willCollide;
    
    /** Constructor creates collision.
     * 
     * @param body1 usually the dynamic body
     * @param body2 usually tile for the most part, but can handle all other types
     */
    public Collision(Body body1, Body body2) {
        this.body1 = body1;
        this.body2 = body2;
        
        solved = false;
        colliding = true;//assume colliding then prove false
        willCollide = true;
        
    }
    
    
    /** Decide which axes the need to be tested for SAT.
     * Preferred that body1 is moving and body2 is static
     * @return array of axes to be tested 
     */
    protected Vector2[] generateAxes() {
        //only colliding 2 circles, you need only one axis between them
        if(body1 instanceof CircularBody && body2 instanceof CircularBody) {
            axes = new Vector2[1];
            axes[0] = body1.pos.cpy().sub(body2.pos).nor();
        } else if(body1 instanceof Sprite && body2 instanceof Tile) {//size constraints
            //TODO optimize this urgently
            //it's because you dont know what voronoi region you're in
            axes = new Vector2[3 + ((PolygonBody)body2).vertexes.length];
            //use the precalculated normals here
            //make sure that you are suing the correct normals because it matters if they are positive/negative 
            axes[0] = ((Tile)body2).getNormal();
            axes[1] = body1.pos.x > body2.pos.x + 25 ? new Vector2(1, 0) : new Vector2(-1, 0);
            axes[2] = body1.pos.y > body2.pos.y + 25 ? new Vector2(0, 1) : new Vector2(0, -1);
            
            for(int i=0;i < ((PolygonBody)body2).vertexes.length;i++) {
                Vector2 vertex = ((PolygonBody)body2).vertexes[i];
                //for each absolute axis
                axes[3 + i] = body1.pos.cpy().sub(
                                    body2.pos.cpy().add(vertex))
                                    .nor();
            }
            
            
            
        } else {//default
            //circular Body return 0 axes here
            Vector2[] body1axes = getNormals(body1);
            Vector2[] body2axes = getNormals(body2);
            axes = new Vector2[body1axes.length + body2axes.length];
            System.arraycopy(body1axes, 0, axes, 0, body1axes.length);
            System.arraycopy(body2axes, 0, axes, body1axes.length, body2axes.length);
        }
        return axes;
    }
    /** calculate normals for body. expensive, when possible use pre-calculated
     * normals.
     */
    protected Vector2[] getNormals(Body body) {
        if(body instanceof PolygonBody) {
            //you need as many axes as there are vertexes in body
            Vector2[] normals = new Vector2[((PolygonBody)body).vertexes.length];
            for(int i = 0;i < normals.length;i++) {
                //get vector between this vertex and the next vertex, even if it's 0
                //this gives you all normals of body
                Vector2 edgenor = ((PolygonBody)body).vertexes[(i+1)
                        %((PolygonBody)body).vertexes.length].cpy()
                        .sub(((PolygonBody)body).vertexes[i]).nor();
                normals[i] = new Vector2(-edgenor.y, edgenor.x);
            }

            return normals;
        } else {
            return new Vector2[0];
        }
    }
    
    /** solves the collision using SAT */
    public void solve() {
        //no need to solve again
        assert !solved;
        //mportant step that depends on collision type
        Vector2[] axes = generateAxes();
        
        //default axis inforation get pushed to correct axis
        float minInterval = Float.MAX_VALUE;
        Vector2 minAxis = new Vector2(0, 0);
        for(Vector2 axis : axes) {
            float[] body1Interval = projectBody(axis, body1);
            float[] body2Interval = projectBody(axis, body2);
            
            float interval = intervalDistance(body1Interval[0], body1Interval[1], body2Interval[0], body2Interval[1]);
            //reaplced by BoundsCollision, basically to check inside the body or ourside?
            //colliding = checkInterval(interval);
            colliding = false;
            
            /* ---------will this collide when you add velocity??---------- */
            // Project the velocity on the current axis
            float velocityProjection = axis.dot(body1.vel);
            
            // Get the projection of polygon A during the movement
            body1Interval[0] += velocityProjection;
            body1Interval[1] += velocityProjection;
            
            interval = intervalDistance(body1Interval[0], body1Interval[1], body2Interval[0], body2Interval[1]);
            willCollide = checkInterval(interval);
            
            //If the polygons are not intersecting and won't intersect, exit the loop
            //INGORNING COLLIDING
            //if(!colliding && !willCollide)
            if(!willCollide) 
                break;
            
            // Check if the current interval distance is the minimum one. If so store
            // the interval distance and the current distance.
            // This will be used to calculate the minimum translation vector
            interval = Math.abs(interval);
            if(interval < minInterval) {
                //the axis and depth to project out of
                minInterval = interval;
                minAxis = axis;
            }
        }
        if(colliding || willCollide) {
            depth = minInterval;
            normal = minAxis;
        }        
        solved = true;
    }
    
    /** this is only here to be replaced by boundsCollision*/
    protected boolean checkInterval(float interval) {
        return interval < 0;
    }
    
    /** resolves the collision using minimum translation vector*/
    public void resolve() {
        //TODO in world process, do you keep collisiosn even though you know they do not collde?
        assert colliding || willCollide;
        if(colliding || willCollide) {
            //System.out.println(this.getClass().getName());

            //move sprite out of collision
            
            assert normal.equals(normal.nor());
            
            Vector2 response = normal.cpy().mul(depth);
            /*//bounce is along normal
            Vector2 bounce = new Vector2(normal.x, normal.y).mul(body1.vel.dot(new Vector2(normal.x, normal.y)));
            bounce.mul(bounceAmount);
            //projection on to the other perpendicular vector
            Vector2 friction = new Vector2(-normal.y, normal.x).mul(-body1.vel.dot(new Vector2(-normal.y, normal.x)));
            friction.mul(frictionAmount);
            */
            //this is all you need?
            body1.vel.add(response);
            //body1.vel.add(friction);
            //body1.vel.add(bounce);
        }
    }
    
    
    //these methods are public in caes, maybe they are used outside this class for some reason
    /** project a body, can be either circular or polygon */
    public static float[] projectBody(Vector2 axis, Body body) {
        if(body instanceof CircularBody) {
            return projectCircle(axis, body.pos, ((CircularBody) body).radius);        
        } else {
            return projectPolygon(axis, body.pos, ((PolygonBody) body).vertexes);
        }
    }
    
    //projection of a circle on axis
    public static float[] projectCircle(Vector2 axis, Vector2 pos, float radius) {
        float dotProduct = axis.dot(pos);
        return new float[]{dotProduct - radius, dotProduct + radius};
    }
    
    
    // Calculate the projection of a polygon on an axis
    // and returns it as a [min, max] interval
    //shoud be able to do points as well with empty vertexes
    //TODO optimize with voronoi regions 
    public static float[] projectPolygon(Vector2 axis, Vector2 pos, Vector2[] vertexes) {
        float[] result;
        //TODO delete these optimizations. they don't work like expected
        //Or replace them by looking closely at what .dot() does and replicating it
        /*
        //if axis is horizontal optimization
        if(axis.y == 0) {
            result = new float[]{pos.x, pos.x};
            for(int i=1; i < vertexes.length; i++) {
                if(pos.x + vertexes[i].x < result[0]) {
                    result[0] = pos.x + vertexes[i].x;
                } else if(pos.x + vertexes[i].x > result[1]) {
                    result[1] = pos.x + vertexes[i].x;
                }
            }
        //or vertical
        } else if(axis.x == 0) {
            result = new float[]{pos.y + vertexes[0].y, pos.y + vertexes[0].y};
            for(int i=1; i < vertexes.length; i++) {
                if(pos.y + vertexes[i].y < result[0]) {
                    result[0] = pos.y + vertexes[i].y;
                } else if(vertexes[i].y > result[1]) {
                    result[1] = pos.y + vertexes[i].y;
                }
            }
        //else some odd angle
        } else {*/

        //To project a point on an axis just use this use the dot product
        //this is proper?? make sure that the vertex does not change after this happens
        float dotProduct = axis.dot(vertexes[0].cpy().add(pos));
        //min, max
        result = new float[]{dotProduct, dotProduct};
        for(int i=1; i < vertexes.length; i++) {
            //aren you supposed to add the position here?
            dotProduct = vertexes[i].cpy().add(pos).dot(axis);
            if(dotProduct < result[0]) {
                result[0] = dotProduct;
            } else if(dotProduct > result[1]) {
                result[1] = dotProduct;
            }
        }
        return result;
    }
    
    /** Calculate the distance between [minA, maxA] and [minB, maxB].
     * The distance will be negative if the intervals overlap 
     */
    public static float intervalDistance(float minA, float maxA, float minB, float maxB) {
        if (minA < minB) {
            return minB - maxA;
        } else {
            return minA - maxB;
        }
    }
    
    /** inside polygon?
     * fast helper function from http://processing.org/discourse/yabb2/YaBB.pl?board=Programs;action=display;num=1189178826
     */
    public static boolean insidePoly(float x, float y, Vector2[] p){
      int i, j, c = 0;
      for (i = 0, j = p.length-1; i < p.length; j = i++) {
        if ((((p[i].y <= y) && (y < p[j].y)) ||
        ((p[j].y <= y) && (y < p[i].y))) &&
        (x < (p[j].x - p[i].x) * (y - p[i].y) / (p[j].y - p[i].y) + p[i].x))
        c = (c+1)%2;
      }
      return c==1;
    }
    /** if points are closer than length?
     * fast helper function from http://processing.org/discourse/yabb2/YaBB.pl?board=Programs;action=display;num=1189178826
     */
    public static boolean proximity(float x0, float y0, float x1, float y1, float len){
      return (x1-x0)*(x1-x0)+(y1-y0)*(y1-y0) <= len*len;
    }

    /** two circles overlap?
     * fast helper function from http://processing.org/discourse/yabb2/YaBB.pl?board=Programs;action=display;num=1189178826
     */
    public static boolean circleOverlap(float x0, float y0, float r0, float x1, float y1, float r1){
      return (x1-x0)*(x1-x0)+(y1-y0)*(y1-y0) <= (r0+r1)*(r0+r1);
    }
    
}

