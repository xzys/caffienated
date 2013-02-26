/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/** Serves as a linkage between the model (sprite), view (Animator), 
 * and Controller (ControlHandler).
 *
 * @author SACHIN
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Player {//
    public String plname;
    public int plnum;
        
    public ControlHandler control;//controls
    public Sprite sprite;
    private Animator animator;
    
    public Player(String plname, int plnum, int controlType, int which, World world) {//chosen sprite too
        this.plname = plname;
        this.plnum = plnum;
        
        //load Character data from file with constants
        //depends on which sprite
        sprite = new Sprite(this, importConsts("consts" + ".txt"));
        animator = new Animator(new Texture(Gdx.files.internal("sheet.png")), 6);
        world.add(sprite);
        //control = new KeyboardHandler();
        control = new GdxKeyboardHandler();
    }
    
    /** Gets character constants from file.
     * 
     * @param filename consts filename
     * @return constants HashMap<name, number>
     */
    private HashMap<String, Float> importConsts(String filename) {
        HashMap<String, Float> hashmap = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            //assume the format deadzone = 1913.120
            for(String s : lines) {
                String[] spl = s.split("=");
                //all the other lines become comment lines
                if(spl.length == 2) hashmap.put(spl[0].trim(), Float.parseFloat(spl[1].trim()));
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return hashmap;
    }
    
    /** process Sprite.state with current controlState */
    public void run() {
        //polling or event based?
        //control.update();
        
        //pass sprite controls
        sprite.processState(control.getControlState());
    }
    /** renders the sprite with Animator*/
    public void render(SpriteBatch batch) {
        animator.render(batch, sprite.pos, sprite.state, -1* sprite.direction, (float)sprite.stateTime*80, 50);
        //animator.render(batch, ptwtrans.sum(sprite.pos), 0, -1* sprite.direction, (float)sprite.stateTime, 45);
    }
    /** simple rendering where colors represnet different sprite states*/
    public void render_debug(ShapeRenderer g) {
        g.begin(ShapeRenderer.ShapeType.FilledCircle);
        g.setColor(sprite.state/5f, sprite.state/5f, sprite.state/5f, .1f);
        g.filledCircle((float) sprite.pos.x, (float) sprite.pos.y, (float) sprite.consts.get("radius"));
        
        g.end();
        
        //all lines here
        //g.begin(ShapeRenderer.ShapeType.Line);
        //g.setColor(Color.BLACK);
        //g.line((float) sprite.pos.x, (float) sprite.pos.y, (float) (sprite.pos.x + sprite.vel.x), (float) (sprite.pos.y + sprite.vel.y));
        //g.end();
    }
}
