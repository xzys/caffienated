/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author SACHIN
 */
public class World {
    private ArrayList<Body> bodies;
    //all these should already be inside bodies
    private Tilemap tilemap;
    //all the collision you need to test
    //maybe make this a queue or something 
    public ArrayList<Collision> collisions;
    private ArrayList<Collision> contacts;
    public float width;
    public float height;
    private Vector2 gravity;//static
    //simplify things and make everythin in pixels
    
    
    public World(float width, float height) {
        this.width = width;
        this.height = height;
        bodies = new ArrayList<Body>();//TODO choose a good number later
        collisions = new ArrayList<Collision>();
        contacts = new ArrayList<Collision>();
        gravity = new Vector2(0, -.01f);
    }
    
    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }
    
    /** add body to bodies. should be dynamic*/
    public void add(Body body) {
        assert body.dynamic;
        bodies.add(body);
    }
    
    public void add(Tilemap tilemap) {
        this.tilemap = tilemap;
    }
    
    //TODO How to use step???
    public void run(float step) {
        for(Body b : bodies) {
            //sprite vs tiles
            if(b.dynamic) {
                b.acc.add(gravity);
                b.vel.add(b.acc);
                b.pos.add(b.vel);
                b.acc.mul(0);
            }
        }
    }
    
    /** collide bodies versus tilemap */
    public void detect() {
        //BROADPHASE
        //fill arraylist collisions
        //bu before that clear last frames arraylist
        collisions.clear();
        for(Body b : bodies) {
            //TODO is this right? draw each bodys all contacts
            b.clearContacts();
            if(b instanceof CircularBody) {
                checkBroadphase(b);
            }    
            //projectile vs tilemap
            //need to do stepwise collisions based on velocity
            else if(b instanceof Projectile) {
                float magsqrd = b.vel.x*b.vel.x + b.vel.y*b.vel.y;
                //TODO choose a goot number here
                if(magsqrd > 1.0 * 1.0) {
                    checkSensitiveBroadphase(b);
                } else {
                    checkBroadphase(b);
                }
            }
            //TODO just check current tile, these are just pointbodies
            else if(b instanceof Particle) {
            }
        }
    }
    
    /** run through collision[] and solve each, remove noncolliding.
     * also resolve at same time so you don't get multiple collisions
     * trying to fix same collision.
     */
    public void solve() {
        //NARROWPHASE
        //do seperting axis theorem here
        //fill depth and normal information here
        for(int i=0;i < collisions.size();i++) {
            Collision c = collisions.get(i);
            c.solve();
            if(c.colliding || c.willCollide) {
                c.body1.addContact(c);
                //c.resolve();
            } else {
                //remove not colliding
                //keep for now so you can graph them
                collisions.remove(i);
                i--;
            }
        }
    }
    
    /*
    public void resolve() {
        //TODO need to handle resting contacts better, at least test them
        //all collision that get here are either colliding or will collide
        for(Collision c : collisions) {
            c.body1.addContact(c);
            c.resolve();
        }
    }*/
    
    
    //TODO
    private void checkSensitiveBroadphase(Body b) {
        //you need to check according to the metanet powerpoints
        //based on velocty, check broadphase every x velocity added so you cannot pass through an other objects
        checkBroadphase(b);
    }
    
    //assuming all sprites are the size of a tile or less
    /** checking broadphase by checking 4 tiles around body for tiles and making collisions*/
    private void checkBroadphase(Body b) {
        //you can calculate current either this way, or by getting pos of tile
        //tliemap.tiles[current.x][current.y].pos.x
        //quadrant is either -1/1 in both x and y axes
        Vector2 current = new Vector2((int) (b.pos.x/tilemap.tsize), (int) (b.pos.y/tilemap.tsize));
        Vector2 quadrant = new Vector2((b.pos.x - current.x*tilemap.tsize < tilemap.tsize*.5) ? -1 : 1,
                                       (b.pos.y - current.y*tilemap.tsize < tilemap.tsize*.5) ? -1 : 1);
        
        //get tile sprite is in
        //get the 3 other tiles based on what quadrant sprite is in
        //but only if they are vlid tile, ie. in bounds
        Tile[] chktiles = new Tile[4];
        if(tilemap.isValidTile((int) current.x, (int) current.y))        
            chktiles[0] = tilemap.tiles[(int) current.x][(int) current.y];
        if(tilemap.isValidTile((int) (current.x + quadrant.x), (int) current.y))
            chktiles[1] = tilemap.tiles[(int) (current.x + quadrant.x)][(int) current.y];
        if(tilemap.isValidTile((int) current.x, (int) (current.y + quadrant.y)))
            chktiles[2] = tilemap.tiles[(int) current.x][(int) (current.y + quadrant.y)];
        if(tilemap.isValidTile((int) (current.x + quadrant.x), (int) (current.y + quadrant.y)))
            chktiles[3] = tilemap.tiles[(int) (current.x + quadrant.x)][(int) (current.y + quadrant.y)];
        
        //use only normal collisions, debugging
        /*
        for(Tile cht : chktiles) {
            if(cht != null) collisions.add(new Collision(b, cht));
        }*/
        
        
        
        //if this tile is not empty, add to collision
        //empty tiles are null in the tilemap and alo null if not valid
        if(chktiles[0] != null) { 
            collisions.add(new InsideCollision(b, chktiles[0]));
        }
        
        //if any of 6 edges are true, add special edge collision
        //edges are l,d,ru
        
        //in x
        if(chktiles[1] != null) { 
            //if this is left, get the right edge
            if(chktiles[1].edges[(int) (1 - quadrant.x)] == 1) {
                collisions.add(new EdgeCollision(b, chktiles[1], (int) (1 - quadrant.x)));
            } else {
                collisions.add(new Collision(b, chktiles[1]));
            }
        }
        //in y
        if(chktiles[2] != null) { 
            //if this is above, get the bottom edge
            if(chktiles[2].edges[(int) (2 - quadrant.y)] == 1) {
                collisions.add(new EdgeCollision(b, chktiles[2], (int) (2 - quadrant.y)));
            } else {
                collisions.add(new Collision(b, chktiles[2]));
            }
        }
        
        //this is a somewhat special case
        //basically means you are inside voronoi region the diagonal tile
        if(chktiles[3] != null) { 
            //if the edge that is facing you  is true
            //                          horizontal                                        vertical
            if(chktiles[3].edges[(int) (1 - quadrant.x)] == 1 || chktiles[3].edges[(int) (2 - quadrant.y)] == 1) {
                //at least one solid, project from corner
                //bottom left = 0, clockwise
                //collisions.add(new CornerCollision(b, chktiles[3], quadrant.cpy().mul(-tilemap.tsize)));
                
                collisions.add(new CornerCollision(b, chktiles[3], 
                        quadrant.cpy().mul(tilemap.tsize * -.5f).add(tilemap.tsize * .5f, tilemap.tsize *.5f)));
                
            //interesting edges? do full collision
            //the edges might be on the opposite side that you are on
            } else if(chktiles[3].edges[(int) (1 - quadrant.x)] == 2 || chktiles[3].edges[(int) (2 - quadrant.y)] == 2) {
                collisions.add(new Collision(b, chktiles[3]));
            } else {
                //no collision
                //there are no interesting or solid sides facing you
                //basically impossible bc these are only triangles at best
            }
        }
    }
    
    /** render all the collisions with shpren*/
    public void renderCollisions(ShapeRenderer shpren) {
        for(Collision c : collisions) {
            //trying to see where the collisions are occuring
            shpren.begin(ShapeRenderer.ShapeType.FilledRectangle);

            switch(c.getClass().getName()) {
                case "game.InsideCollision":
                    shpren.setColor(1, 0, 0, .5f);
                    break;
                case "game.EdgeCollision":
                    shpren.setColor(0, 0, 0, 1);
                    //l, d, r, u
                    if(((EdgeCollision)c).edge == 0) 
                        shpren.filledRect(c.body2.pos.x, c.body2.pos.y, 5, tilemap.tsize);
                    if(((EdgeCollision)c).edge == 1) 
                        shpren.filledRect(c.body2.pos.x, c.body2.pos.y, tilemap.tsize, 5);
                    if(((EdgeCollision)c).edge == 2) 
                        shpren.filledRect(c.body2.pos.x + tilemap.tsize - 5, c.body2.pos.y, 5, tilemap.tsize);
                    if(((EdgeCollision)c).edge == 3) 
                        shpren.filledRect(c.body2.pos.x, c.body2.pos.y + tilemap.tsize - 5, tilemap.tsize, 5);
                    
                    shpren.setColor(0, 1, 0, .5f);
                    break;
                case "game.CornerCollision":
                    shpren.setColor(0, 0, 0, 1);
                    //shpren.filledRect(
                    
                    //((CornerCollision)c).corner.x
                    //((CornerCollision)c).corner.y
                    shpren.setColor(0, 0, 1, .5f);
                    break;
                default:
                    shpren.setColor(0, 0, 0, .5f);
            }

            shpren.filledRect(c.body2.pos.x, c.body2.pos.y, tilemap.tsize, tilemap.tsize);
            shpren.end();
            
            
            if(c.solved) {
                shpren.begin(ShapeRenderer.ShapeType.Line);
                //draw all the axes that are being testd
                shpren.setColor(1, .5f, 0, 1);
                for(Vector2 axis : c.axes) {
                    shpren.line(c.body1.pos.x, c.body1.pos.y,
                            c.body1.pos.x + axis.x*100, c.body1.pos.y + axis.y*100);
                }
                
                //draw the final axis that is used to resolve the conflict
                shpren.setColor(0, 0, 0, 1);
                if(c.colliding || c.willCollide) {
                    shpren.line(c.body1.pos.x, c.body1.pos.y,
                            c.body1.pos.x + c.normal.x*100, c.body1.pos.y + c.normal.y*100);
                }
                shpren.end();
            }
            
        }
    }
    
}