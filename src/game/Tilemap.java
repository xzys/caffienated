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

/**
 *
 * @author SACHIN
 */
//implementation that extends body
public class Tilemap {
    //number of cols (x) and rows (y)
    private int tx;
    private int ty;
    public float tsize;
    
    //temporary testing background colors
    //shoul never need this because map is fg.png and bg.png
    private Color tcolor;    
    boolean initialized;
    //just a reference of input from mapdata without accessing actual tiles
    char[][] tmap;
    Tile[][] tiles;
    
    Tilemap(int tx, int ty, float tsize, Color tcolor) {
        this.tx = tx;
        this.ty = ty;
        this.tsize = tsize;
        this.tcolor = tcolor;
        initialized = false;
    }
    
    public void initialize(World world, String filename) {
        if(!makeReferenceArray(getMapdata(readLines(filename))))
                System.out.println("failed to make reference array from sequence :" + readLines(filename).get(0));
        if(!makeTileBodies(world))
                System.out.println("failed to make tile bodies");
        initialized = true;
    }
    
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
    
    public boolean isValidTile(int x, int y) {
        return x >= 0 && x < tx && y >= 0 && y < ty;
    }
    
    
    //if you are using processing, use this
    /*public List<String> readLines(String filename) {
        List<String> lines = Arrays.asList(loadStrings(filename));
        return lines;
    }
    */
    
    //mapdata is all on one line
    public boolean makeReferenceArray(String mapdata) {
        System.out.println("Creating Tile reference array.");
        if(ty*tx != mapdata.length()) {
            System.out.println("Error: Tilemap data is formatted incorrectly. Wrong number of tiles detected.");
            //return false;
        }
        //load chars into tmap
        tmap = new char[tx][ty];
        for(int i=0;i < mapdata.length();i++) {
            //ERROR PRONE
            tmap[i%tx][i/tx] = mapdata.charAt(i);
            //System.out.println(i + " " + i%tx + " " + i/tx);
        }
        System.out.println("Tile reference array created.");
        return true;
    }
    
    public boolean makeTileBodies(World world) {
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
        return true;
    }
    
    
    //for libgdx
    public void render(ShapeRenderer g) {
        for(int x=0;x < tx;x++) {
            for(int y=0;y< ty;y++) {
                //maybe use continue here instead
                if(tiles[x][y] != null) {
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
        
    }
    
    //processing version, to be used with PGraphics
    /*public void render(PGraphics g) {
        g.beginDraw();
        //set colors and everthing here
        //g.background(bgcolor);
        //g.background(200);
        //g.fill(tcolor);
        g.fill(100);
        g.noStroke();
        
        for(int x=0;x < tx;x++) {
            for(int y=0;y< ty;y++) {
                //maybe use continue here instead
                if(tiles[x][y] != null) {
                    g.pushMatrix();
                    //println(loc.x + " " + loc.y);
                    g.translate((float) tiles[x][y].pos.x, (float) tiles[x][y].pos.y);//reverse arrays crap;
                    
                    g.beginShape();
                    //use tmap here not tiles because tiles has null sections
                    
                    for(int i=0;i < tiles[x][y].vertexes.length;i++) 
                        g.vertex((float) tiles[x][y].vertexes[i].x, (float) tiles[x][y].vertexes[i].y);
                    
                    g.endShape();
                    g.popMatrix();
                }   
            }
        }
        g.endDraw(); 
    }*/
    
    public Tile.Type getTIleTypeFromChar(char c) {
        switch(c) {
            case '0':
                return Tile.Type.EMPTY;
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
                return Tile.Type.EMPTY;
        }
    }
}