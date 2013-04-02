/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** A helper class to keep track of all libgdx animations and textures.
 * @author SACHIN
 */
public class Animator {
    //stores all animations for each state
    private Animation[] animations;
    private TextureRegion currentFrame;
    
    /** Constructor: 
     * @param sheet is the entire texture that contains all frames
     * @param states number of states to hold
     */
    //TODO automate this function
    public Animator(Texture sheet, int states) {
        //this.states = states;
        //how many states are there
        animations = new Animation[states];
        //eentually get this from a file
        //get these numbers from the file later
        //fill(sheet, 50);
        
        fill(animations, sheet, 0, 3, 1, 1, 1, 100);
        fill(animations, sheet, 1, 0, 1, 1, 1, 100);
        fill(animations, sheet, 2, 0, 1, 1, 1, 100);
        fill(animations, sheet, 3, 0, 0, 1, 15, 100);
        fill(animations, sheet, 4, 1, 1, 1, 1, 100);
        fill(animations, sheet, 5, 2, 1, 1, 1, 100);
    }
    
    /** Helper fuction to fill animations[] with TextureRegions from sheet
     * 
     * @param animations Animation[] to fill
     * @param sheet Texture to get TextureRegions from
     * @param state which state this represents
     * @param x starting x/size
     * @param y starting y/size
     * @param rows of frames (up to down)
     * @param cols of frames (side to side)
     * @param size of each frame; assume square
     */
    //TODO throws IllegalArgumentException or NullPointer?
    private static void fill(Animation[] animations, Texture sheet, 
            int state, int x, int y, int rows, int cols, int size) {
        //for each animation in the sheet fill texture
        TextureRegion[] frames = new TextureRegion[rows * cols];
        //fill frames from texture based on the info
        for(int i = 0;i < frames.length;i++) {
            float startx = (x + i) * (float)size/sheet.getWidth();
            float starty = ((y + i)/cols) * (float)size/sheet.getHeight();
            //System.out.println(startx + " " + starty);
            frames[i] = new TextureRegion(sheet, startx, starty, startx + (float)size/sheet.getWidth(), starty + (float)size/sheet.getHeight());
        }
        //TODO .05 is a magic number (length of each frame)
        animations[state] = new Animation(.05f, frames);
    }
    
    /** Renders correct animation frame from data given
     * 
     * @param batch passed SpriteBatch
     * @param translate translated to this Vector2
     * @param state which state to draw
     * @param direction +1 is right, -1 is left
     * @param stateTime used to determine which animation frame to use; must be positive
     * @param radius how big to draw
     */
    public void render(SpriteBatch batch, Vector2 translate, int state, int direction, float rotation, float stateTime, float radius) 
            throws IllegalArgumentException{
        //load this constant from file too
        if(stateTime < 0) throw new IllegalArgumentException();
        
        currentFrame = animations[state].getKeyFrame(stateTime, true);
        batch.begin();
        batch.draw(currentFrame, 
                (float)(translate.x + direction*radius*.5), (float)(translate.y - radius*.5) + radius - 40,
                0, 0,
                -direction*radius, radius,
                1, 1,
                -rotation);
        //batch.draw(currentFrame,
        //        (float)(translate.x + direction*radius*.5), (float)(translate.y - radius*.5) + radius - 50,
        //        -direction*radius, radius);
        batch.end();
    }
}