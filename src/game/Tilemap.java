/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/** 2D array of tile bodies
 *
 * @author SACHIN
 */
public class Tilemap {
    //number of cols (x) and rows (y)
    private int tx;
    private int ty;
    public float tsize;
    
    //TODO temporary testing background colors
    //TODO shoul never need this because map is fg.png and bg.png
    private Color tcolor;    
    boolean initialized;
    //just a reference of input from mapdata without accessing actual tiles
    char[][] tmap;
    Tile[][] tiles;
    //four bounding boxes
    PolygonBody[] bounds;
    
    Tilemap(int tx, int ty, float tsize, Color tcolor) {
        this.tx = tx;
        this.ty = ty;
        this.tsize = tsize;
        this.tcolor = tcolor;
        initialized = false;
    }
    /** inits tmap reference and also tiles[][]
     * 
     * @param world world to add tilemap to 
     * @param filename filename of mapData
     */
    public void initialize(World world, String filename) throws IllegalArgumentException {
        makeReferenceArray(getMapdata(readLines(filename)));
        makeTileBodies(world);
        processTileEdges();
        
        initialized = true;
    }
    /** gets the first rows*cols characters from lines.
     * 
     * @param lines of mapData file
     * @return mapData
     */
    //TODO fix spec here
    private String getMapdata(List<String> lines) {
        String mapdata = "";
        //go through all lines and characters in each line
        for(String line : lines) {
            for(char c : line.toCharArray()) {
                if(mapdata.length() >= tx*ty) break;
                //add character to mapata
                if("0123456789ABCDEFGH".contains(String.valueOf(c))) {
                    mapdata += c;
                }
                else return null;    
            }
            if(mapdata.length() >= tx*ty) break;
        }
        if(mapdata.length() == tx*ty) return mapdata;
        else return null;
    }
    /** reads lines from filename */
    private static List<String> readLines(String filename) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);       
            return lines;
        } catch(IOException e) {
            System.out.println("Tilemap " + filename + " not found.");
            return null;
        }
        
    }
    /** checks if tile x and y are inside the dimensions of tilemap*/
    public boolean isValidTile(int x, int y) {
        return x >= 0 && x < tx && y >= 0 && y < ty;
    }
    
    /** creates char[][] tmap from mapData */
    public void makeReferenceArray(String mapdata) throws IllegalArgumentException {
        System.out.println("Creating Tile reference array.");
        if(ty*tx != mapdata.length()) {
            throw new IllegalArgumentException("Tilemap data is formatted incorrectly. Wrong number of tiles detected.");
        }
        //load chars into tmap
        tmap = new char[tx][ty];
        for(int i=0;i < mapdata.length();i++) {
            //ERROR PRONE
            tmap[i%tx][ty-(i/tx)-1] = mapdata.charAt(i);
            //System.out.println(i + " " + i%tx + " " + i/tx);
        }
        System.out.println("Tile reference array created.");
    }
    /** makes Tile[][] tiles from char[][] tmap*/
    public void makeTileBodies(World world) {
        System.out.println("Creating Tile bodies.");
        tiles = new Tile[tx][ty];
        for(int x=0;x < tx; x++) {
            for(int y=0;y < ty;y++) {
                if(tmap[x][y] != '0') {
                    tiles[x][y] = new Tile(getTIleTypeFromChar(tmap[x][y]), tsize);
                    //move tile to place in world
                    //y axis is reversed
                    //System.out.print((x - tx * .5) * tsize + " ");
                    tiles[x][y].translateToOrigin();
                    tiles[x][y].translate(x * tsize, y * tsize);
                    //tilemap will be added to world later on 
                }
            }
        }
        world.add(this);
        System.out.println("Tile bodies array created.");
        //Make bound bodies
        //0,1,2,3 is left,down, right, up
        bounds = new PolygonBody[4];
        bounds[0] = new PolygonBody(false, new Vector2[]{new Vector2(-tsize,0),
                                                         new Vector2(0, 0),
                                                         new Vector2(0, ty*tsize),
                                                         new Vector2(-tsize, ty*tsize)});
        bounds[1] = new PolygonBody(false, new Vector2[]{new Vector2(0,-tsize),
                                                         new Vector2(tx*tsize,-tsize),
                                                         new Vector2(tx*tsize,0),
                                                         new Vector2(0,0)});        
        bounds[2] = new PolygonBody(false, new Vector2[]{new Vector2(tx*tsize,0),
                                                         new Vector2(tx*tsize+tsize,0),
                                                         new Vector2(tx*tsize+tsize, ty*tsize),
                                                         new Vector2(tx*tsize, ty*tsize)});
        bounds[3] = new PolygonBody(false, new Vector2[]{new Vector2(0,ty*tsize),
                                                         new Vector2(tx*tsize,ty*tsize),
                                                         new Vector2(tx*tsize,ty*tsize+tsize),
                                                         new Vector2(0,ty*tsize+tsize)});
    }
    /** Recursively remove unnecessary edges in tilemap. 
     * 1. two solid edges facing eachother == both empty
     */
    private void processTileEdges() {
        //TODO write more better later
        for(int x=0;x < tx;x++) {
            for(int y=0;y< ty;y++) {
                if(tiles[x][y] == null)
                    continue;//skip
                
                //over all 4 directions
                for(int addx=-1;addx <= 1;addx+=2) {
                    if(isValidTile(x+addx, y) &&//valid
                            tiles[x+addx][y] != null &&//not empty
                            tiles[x+addx][y].edges[1-addx] == 1 && tiles[x][y].edges[1+addx] == 1) {
                        tiles[x][y].edges[1+addx] = 0;
                        tiles[x+addx][y].edges[1-addx] = 0;
                    }                    
                }
                for(int addy=-1;addy <= 1;addy+=2) {
                    if(isValidTile(x, y+addy) &&//valid
                            tiles[x][y+addy] != null &&//not empty
                            tiles[x][y+addy].edges[2-addy] == 1 && tiles[x][y].edges[2+addy] == 1) {
                        tiles[x][y].edges[2+addy] = 0;
                        tiles[x][y+addy].edges[2-addy] = 0;
                    }
                }   
            }
        }
    }
    /** */
    public void renderEdges(ShapeRenderer g) {
        g.begin(ShapeRenderer.ShapeType.FilledRectangle);
        for(int x=0;x < tx;x++) {
            for(int y=0;y< ty;y++) {
                if(tiles[x][y] == null)
                    continue;//skip
                
                //case for which side
                for(int edgeIndex=0;edgeIndex < 4;edgeIndex++) {
                    switch(tiles[x][y].edges[edgeIndex]) {
                        case 0:
                            g.setColor(1, 1, 1, 1);
                            break;
                        case 1:
                            g.setColor(1, 0, 0, 1);
                            break;
                        case 2:
                            g.setColor(.5f, .5f, .5f, 1);
                            break;
                    }
                    
                    switch(edgeIndex) {
                        case 0:
                            g.filledRect(tiles[x][y].pos.x, tiles[x][y].pos.y, 5, tsize);
                            break;
                        case 1:
                            g.filledRect(tiles[x][y].pos.x, tiles[x][y].pos.y, tsize, 5);
                            break;
                        case 2:
                            g.filledRect(tiles[x][y].pos.x + tsize - 5, tiles[x][y].pos.y, 5, tsize);
                            break;
                        case 3:
                            g.filledRect(tiles[x][y].pos.x, tiles[x][y].pos.y + tsize - 5, tsize, 5);
                            break;
                    }
                }
            }
        }
        g.end();
    }
    
    /** render tilemap using shpren*/
    public void render(ShapeRenderer g) {
        for(int x=0;x < tx;x++) {
            for(int y=0;y< ty;y++) {
                if(tiles[x][y] == null)
                    continue;//skip
                    
                Vector2[] tv = tiles[x][y].vertexes;
                if(tv.length == 3) {
                    g.begin(ShapeRenderer.ShapeType.FilledTriangle);
                    g.setColor(tcolor);
                    g.translate((float)tiles[x][y].pos.x, (float)tiles[x][y].pos.y, 0);//reverse arrays crap;
                    g.filledTriangle((float)tv[0].x, (float)tv[0].y, 
                                     (float)tv[1].x, (float)tv[1].y, 
                                     (float)tv[2].x, (float)tv[2].y);
                    g.translate(-(float)tiles[x][y].pos.x, -(float)tiles[x][y].pos.y, 0);
                    g.end();
                } else if(tv.length == 4) {
                    g.begin(ShapeRenderer.ShapeType.FilledTriangle);
                    g.setColor(tcolor);
                    g.translate((float)tiles[x][y].pos.x, (float)tiles[x][y].pos.y, 0);//reverse arrays crap;
                    g.filledTriangle((float)tv[0].x, (float)tv[0].y, 
                                     (float)tv[1].x, (float)tv[1].y, 
                                     (float)tv[3].x, (float)tv[3].y);
                    g.filledTriangle((float)tv[1].x, (float)tv[1].y, 
                                     (float)tv[3].x, (float)tv[3].y, 
                                     (float)tv[2].x, (float)tv[2].y);
                    g.translate(-(float)tiles[x][y].pos.x, -(float)tiles[x][y].pos.y, 0);
                    g.end();
                }
            }
        }
        
    }
    
    /** converts char of mapData to Tile.Type*/
    //TODO combine the reference array and tiles maybe?
    public Tile.Type getTIleTypeFromChar(char c) {
        switch(c) {
            case '1':
                return Tile.Type.FULL;
            case '2':
                return Tile.Type.BIG_TRI_BL;
            case '3':
                return Tile.Type.BIG_TRI_BR;
            case '4':
                return Tile.Type.BIG_TRI_TL;
            case '5':
                return Tile.Type.BIG_TRI_TR;
            case '6':
                return Tile.Type.HF_B;
            case '7':
                return Tile.Type.HF_T;
            case '8':
                return Tile.Type.HF_L;
            case '9':
                return Tile.Type.HF_R;
            case 'A':
                return Tile.Type.HF_FL_TRI_BL;
            case 'B':
                return Tile.Type.HF_FL_TRI_BR;
            case 'C':
                return Tile.Type.HF_FL_TRI_TL;
            case 'D':
                return Tile.Type.HF_FL_TRI_TR;
            case 'E':
                return Tile.Type.HF_TL_TRI_BL;
            case 'F':
                return Tile.Type.HF_TL_TRI_BR;
            case 'G':
                return Tile.Type.HF_TL_TRI_TL;
            case 'H':
                return Tile.Type.HF_TL_TRI_TR;
            default:
                //return Tile.Type.EMPTY;
                return null;
        }
    }
}