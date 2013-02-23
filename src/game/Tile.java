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

public class Tile extends PolygonBody {
    //all the different types of tiles that exist//all the different types of tiles that exist
    public static enum Type {
        //EMPTY TILE should be null
        EMPTY          (new Vector2[0], 
                        new int[0],
                        new Vector2(0,0)),
        //FULL TILE
        FULL           (new Vector2[]{new Vector2(0, 0), 
                                      new Vector2(0, 1),
                                      new Vector2(1, 1),
                                      new Vector2(1, 0)},
                        new int[]{1, 1, 1, 1},
                        new Vector2(0,0)),
        //BIG TRIANGLES
        BIG_TRI_BL     (new Vector2[]{new Vector2(0, 0),
                                      new Vector2(0, 1),
                                      new Vector2(1, 0)},
                        new int[]{1, 1, 0, 0},
                        new Vector2(1, 1)),
        BIG_TRI_BR     (new Vector2[]{new Vector2(0, 0),
                                      new Vector2(1, 1),
                                      new Vector2(1, 0)},
                        new int[]{0, 1, 1, 0},
                        new Vector2(-1, 1)),
        BIG_TRI_TL     (new Vector2[]{new Vector2(0, 0), 
                                      new Vector2(0, 1), 
                                      new Vector2(1, 1)}, 
                        new int[]{1, 0, 0, 1},
                        new Vector2(1, -1)), 
        BIG_TRI_TR     (new Vector2[]{new Vector2(1, 0),
                                      new Vector2(0, 1),
                                      new Vector2(1, 1)}, 
                        new int[]{0, 0, 1, 1},
                        new Vector2(-1, -1)),
        //HALF SQAURE BLOCKS
        HF_B           (new Vector2[]{new Vector2(0, 0), 
                                      new Vector2(0, .5f), 
                                      new Vector2(1, .5f),
                                      new Vector2(1, 0)},
                        new int[]{2, 1, 2, 0},
                        new Vector2(0,1)),
        HF_T           (new Vector2[]{new Vector2(0, .5f), 
                                      new Vector2(0, 1), 
                                      new Vector2(1, 1),
                                      new Vector2(1, .5f)},
                        new int[]{2, 0, 2, 1},
                        new Vector2(0,-1)),
        HF_L           (new Vector2[]{new Vector2(0, 0), 
                                      new Vector2(0, 1), 
                                      new Vector2(.5f, 1),
                                      new Vector2(.5f, 0)},
                        new int[]{1, 2, 0, 2},
                        new Vector2(1,0)),
        HF_R           (new Vector2[]{new Vector2(1, 0), 
                                      new Vector2(1, 1), 
                                      new Vector2(.5f, 1),
                                      new Vector2(.5f, 0)},
                        new int[]{0, 2, 1, 2},
                        new Vector2(-1,0)),
        //HALF FLAT TRIANGLES
        HF_FL_TRI_BL   (new Vector2[]{new Vector2(0, 0), 
                                      new Vector2(0, .5f), 
                                      new Vector2(1, 0)},
                        new int[]{2, 0, 0, 1},
                        new Vector2(.5f, 1)), 
        HF_FL_TRI_BR   (new Vector2[]{new Vector2(0, 0),
                                      new Vector2(1, .5f),
                                      new Vector2(1, 0)},
                        new int[]{0, 0, 2, 1},
                        new Vector2(-.5f, 1)), 
        HF_FL_TRI_TL   (new Vector2[]{new Vector2(0, 1), 
                                      new Vector2(1, 1), 
                                      new Vector2(0, .5f)},
                        new int[]{2, 1, 0, 0},
                        new Vector2(.5f, -1)),
        HF_FL_TRI_TR   (new Vector2[]{new Vector2(0, 1), 
                                      new Vector2(1, 1),
                                      new Vector2(1, .5f)},
                        new int[]{0, 1, 2, 0},
                        new Vector2(-.5f, -1)),
        //HALF TALL TRIANGLES
        HF_TL_TRI_BL   (new Vector2[]{new Vector2(0, 0), 
                                      new Vector2(0, 1), 
                                      new Vector2(.5f, 0)},
                        new int[]{1, 0, 0, 2},
                        new Vector2(1, .5f)), 
        HF_TL_TRI_BR   (new Vector2[]{new Vector2(1, 0),
                                      new Vector2(.5f, 0),
                                      new Vector2(1, 1)},
                        new int[]{0, 0, 1, 2},
                        new Vector2(-1, .5f)), 
        HF_TL_TRI_TL   (new Vector2[]{new Vector2(0, 0), 
                                      new Vector2(0, 1), 
                                      new Vector2(.5f, 1)},
                        new int[]{1, 2, 0, 0},
                        new Vector2(1, -.5f)),
        HF_TL_TRI_TR   (new Vector2[]{new Vector2(.5f, 1), 
                                      new Vector2(1, 1),
                                      new Vector2(1, 0)},
                        new int[]{0, 1, 2, 0},
                        new Vector2(-1, -.5f)),
        FL_QUAD_BL,//FLAT QUADS
        FL_QUAD_BR,
        FL_QUAD_TL,
        FL_QUAD_TR,
        TL_QUAD_BL, //TALL QUADS
        TL_QUAD_BR,
        TL_QUAD_TL,
        TL_QUAD_TR;
        
        public final Vector2[] vertexes;
        //l,d,r,u order
        //0 is none, 1 is solid, 2 is interesting
        public final int[] edges;
        public final Vector2 normal;
        
        private Type() {
            this(new Vector2[0], new int[0], new Vector2(0, 0));
        }
        
        private Type(Vector2[] vertexes, int[] edges, Vector2 normal) {
            this.vertexes = vertexes;
            this.edges = edges;
            this.normal = normal;
        }
    
    };         
    
    Tile.Type type;
    int[] edges;//not implemented yet
    //vertexes belongs to polygonBody
    //this is the normal to the one angled tile there is
    private Vector2 normal;
    
    Tile(Tile.Type type, float tsize) {
        super(false, type.vertexes);
        this.type = type;
        this.edges = type.edges;
        
        //size vertexes from 1 to tsize
        Vector2[] sizedVertexes = new Vector2[type.vertexes.length];
        for(int i=0;i < type.vertexes.length;i++) {
            sizedVertexes[i] = type.vertexes[i].cpy().mul(tsize);
            //System.out.println(sizedVertexes[i]);
        }
        this.vertexes = sizedVertexes;
        
        this.normal = type.normal.nor();
    }
    
    private void calculateNormal() {
        this.normal = new Vector2(0, 0);
        for(int i = 0;i < type.vertexes.length;i++) {
            //get vector between this vertex and the next vertex, even if it's 0
            Vector2 edge = type.vertexes[(i+1)%type.vertexes.length].cpy().sub(type.vertexes[i]);
            if(edge.angle()%90 != 0) {
                //if the angle is not vertical or horizontal
                //get perpendicular to edge
                System.out.println(edge.angle() + "  "  + edge.x + " " + edge.y + " " + type);
                normal.set(-edge.y, edge.x);
            }
        }
    }
    
    public Vector2 getNormal() {
        return normal;
    }
}

