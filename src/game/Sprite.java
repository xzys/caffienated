/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;


/** Physics/state model of character
 *
 * @author SACHIN
 */
public class Sprite extends CircularBody {
    //this.getClass() = name; of actual Character
    //these are all pubic because they are needed for animation with Animator
    private Player player;
    //rotation based on which direction contact is
    public float rotation;
    //-1 is left, 1 is right, which way facing
    public int direction;
    //get these from the phsics body
    
    //what all the Character is touching
    //should already have a contacts array
    //if it is a wall to the side of him that is flat, wallride
    //other things like jump impulse and such
    //this varies based on character you choose
    //fil in character's setup from file
    Map<String, Float> consts;
    
    //how many frames since last state change?
    private long lastStateChange;
    //what animation frame do you use depending on state
    public float stateTime;
    
    //states and state reference
    public int state;
    //control of player? ragdoll? temporarily limp? jumping?
    private static final int STANDING = 0;
    private static final int MIDAIR = 1;
    private static final int JUMPING = 2;
    private static final int RUNNING = 3;
    private static final int SKIDDING = 4;
    private static final int WALLRIDING = 5;
    /** Constructor: initializes sprite
     * 
     * @param player circular reference to the player this sprite belongs to
     * @param consts HashMap of consts for movement, etc
     */
    public Sprite(Player player, HashMap<String, Float> consts) {
        super(true);
        //make sure there is a folder in the current directory with this name
        //import image sheet
        
        this.player = player;//circular reference
        this.consts = consts;
        //bascally remove any max values here
        consts.put("max_walk_x", Float.MAX_VALUE);
        consts.put("max_air_x", Float.MAX_VALUE);
        setRadius(consts.get("radius"));
        translateToOrigin();
    }
    
    /** corrects the direction sprite is facing based on vel*/
    //TODO magic numbers
    public void checkDirection() {
        //facing correct direction
        //some leeway
        if(vel.x < -.01) direction = -1;
        if(vel.x > .01) direction = 1;
    }
    
    /** processes and changes state based on the controlState*/
    //TODO always make sure this works properly
    //TODO properly comment/refactor if necessary
    public void processState(ControlState cs) {
        //catch errors
        
        if(state < 0 || state > 5) {
            throw new RuntimeException();
            //setState(STANDING);
        }
        
        float touchingGround;
        
        //if(touchingSide() != 0) System.out.println(touchingSide());
        //these are all gonna be somewhat different so replicating code ehhhh
        switch(state) {
            case STANDING://standing, for sake of animation
                //slide on the ground and make dust if you get pushed
                //maybe if you fall and are already moving
                checkDirection();
                
                //falling
                touchingGround = touchingGround();
                if(touchingGround == 1) setState(MIDAIR);
                
                if(touchingGround == 1) rotation = 0;
                else rotation = (float)(touchingGround + Math.PI * .5f);
                
                
                //jump from standing
                //if(cs.joys[1] > consts.get("deadzone y")) {//up on joystick
                if(cs.buts[0]) {//pressing up
                    setState(JUMPING);
                    Vector2 jump = new Vector2((float) Math.cos(touchingGround + Math.PI), (float) Math.sin(touchingGround + Math.PI));
                    //applyImpulse(jump.multiply(consts.get("jump")));
                    applyImpulse(new Vector2(0, consts.get("jump")));
                }
                
                //walking
                //set state only on button, because you might be sliding
                //dont need to worry about max_x because you already have skidding
                if(Math.abs(cs.joys[0]) > consts.get("deadzone")) {
                    setState(RUNNING);
                    applyImpulse(new Vector2(consts.get("walk_impulse") * ((cs.joys[0] < 0)? -1 : 1), 0));
                } else {
                    //not pushing any buttons
                    //if fast show skiddig animation
                    //if slow still skid beecause you need to stop
                    if(Math.abs(vel.x) > consts.get("skidzone_x")) {
                        setState(SKIDDING);
                    } else {
                        applyImpulse(new Vector2(consts.get("skidding")*vel.x, 0));
                    }
                }
                
                //animation for standing
                //stateTime += Gdx.graphics.getDeltaTime();
                //static standing frame
                stateTime = 0;
                break;
                
            case MIDAIR://in midair but not jumping
                checkDirection();
                
                //to wall riding, only when falling no jumping
                int touching = touchingSide();//either 1 or -1
                //if touching is teh same side as the pushing
                if(vel.y < 0 &&  Math.abs(cs.joys[0]) > consts.get("deadzone") && touching == (cs.joys[0] < 0? -1 : 1)) {
                    setState(WALLRIDING);
                    direction = touching;//force direction
                } else if(touchingGround()!= 1) {
                    //landed, only if touching ground
                    setState(STANDING);
                }
                
                //if touching side you can walljump
                if(touching != 0 && cs.buts[0]) {
                    setState(JUMPING);
                    //make direction side opposite of touching
                    direction = touching * 1;
                    //apply impusle in current direction
                    applyImpulse(new Vector2(direction*consts.get("walljump_x"), consts.get("walljump_y")));
                }
                
                //midair movement
                //slower than when on land
                //I feel like max air_x should be super super high
                if(Math.abs(vel.x) < consts.get("max_air_x")) {
                    if(cs.joys[0] > consts.get("deadzone")) // left
                        applyImpulse(new Vector2(consts.get("air_impulse"), 0));
                    if(cs.joys[0] < consts.get("deadzone")*-1)// right
                        applyImpulse(new Vector2(consts.get("air_impulse")*-1, 0));
                }
                
                //if traveling upwards, jumping animation
                //if falling go through falling animation
                //another constant here
                
                //if(vel.y > 0) stateTime = 0;
                //else stateTime += Gdx.graphics.getDeltaTime() * Math.abs(vel.y);
                stateTime = 0;
                break;
            case JUMPING://jumping
                checkDirection();
                //hit something in midair, cancel jump or if falling
                if(contacts.size() > 0 || vel.y < 0) {
                    //EXCEPTOIN MAJOR NO-NO
                    //let me explain, because keys are event based, after max time you set it to false
                    //then only way to jump again is to keyDown again
                    //do it for all state changes from jumping
                    //if you already jumped. hence you are here, you canot again
                    cs.buts[0] = false;
                    setState(MIDAIR);
                    break;
                }
                
                //holding jump
                if(cs.buts[0]) {
                    applyImpulse(new Vector2(0, consts.get("midjump")));
                } else {
                    //stop jumping
                    cs.buts[0] = false;
                    setState(MIDAIR);
                    break;
                }

                //limit jump time in nanoseconds
                if(lastStateChange - System.nanoTime() > consts.get("max_jump_time")) {
                    cs.buts[0] = false;
                    setState(MIDAIR);
                }

                //midair movement
                //slower than when on land
                //I feel like max air_x should be super super high
                if(Math.abs(vel.x) < consts.get("max_air_x")) {
                    if(cs.joys[0] > consts.get("deadzone")) // left
                        applyImpulse(new Vector2(consts.get("air_impulse"), 0));
                    if(cs.joys[0] < consts.get("deadzone")*-1)// right
                        applyImpulse(new Vector2(consts.get("air_impulse")*-1, 0));
                }
                
                stateTime = 0;
                break;
            case RUNNING://walking
                checkDirection();
                //falling
                touchingGround = touchingGround();
                if(touchingGround == 1) setState(MIDAIR);
                
                if(touchingGround == 1) rotation = 0;
                else rotation = (float)(touchingGround + Math.PI * .5f);
                
                //jump from standing
                //if(cs.joys[1] > consts.get("deadzone y")) {//up on joystick
                if(cs.buts[0]) {//pressing up
                    setState(JUMPING);
                    Vector2 jump = new Vector2((float) Math.cos(touchingGround + Math.PI), (float) Math.sin(touchingGround + Math.PI));
                    //applyImpulse(jump.multiply(consts.get("jump")));
                    applyImpulse(new Vector2(0, consts.get("jump")));
                }
                
                //walking, also braking
                //set state only on button, because you might be sliding
                if(Math.abs(cs.joys[0]) > consts.get("deadzone")) {//if pushing button
                    //if same sign
                    if(((vel.x < 0)? -1 : 1) == ((cs.joys[0] < 0)? -1 : 1)) {
                        if(Math.abs(vel.x) < consts.get("max_walk_x")){
                            applyImpulse(new Vector2(consts.get("walk_impulse") * ((cs.joys[0] < 0)? -1 : 1), 0));
                        }
                    } else {//pushing opposite direction, different sign
                        applyImpulse(new Vector2(consts.get("brake_impulse") * ((cs.joys[0] < 0)? -1 : 1), 0));
                    }
                } else {//not pushing any buttons
                    //apply some friction if not walking
                    setState(SKIDDING);
                }
                
                /*if(cs.joys[0] > consts.get("deadzone")) {//pressing left
                    if(vel.x > 0) {
                        applyImpulse(new Vector2(consts.get("walk_impulse"), 0));
                    } else {
                        applyImpulse(new Vector2(consts.get("brake_impulse"), 0));
                    }
                } else if(cs.joys[0] < consts.get("deadzone")*-1) {//pressig right
                    if(vel.x < 0) {
                        applyImpulse(new Vector2(consts.get("walk_impulse")*-1, 0));
                    } else {
                        applyImpulse(new Vector2(consts.get("brake_impulse")*-1, 0));
                    }
                }*/
                
                //the running animation based on vel.x
                stateTime += Math.abs(Gdx.graphics.getDeltaTime() * .01 * vel.x);
                break;
            case SKIDDING:
                checkDirection();
                //falling
                touchingGround = touchingGround();
                if(touchingGround == 1) setState(MIDAIR);
                
                if(touchingGround == 1) rotation = 0;
                else rotation = (float)(touchingGround + Math.PI * .5f);
                
                //if moving too slow, go to walking
                if(Math.abs(vel.x) < consts.get("skidzone_x")) {
                    setState(STANDING);
                }
                
                //jump from skidding
                //if(cs.joys[1] > consts.get("deadzone y")) {//up on joystick
                if(cs.buts[0]) {//pressing up
                    setState(JUMPING);
                    Vector2 jump = new Vector2((float) Math.cos(touchingGround + Math.PI), (float) Math.sin(touchingGround + Math.PI));
                    //applyImpulse(jump.multiply(consts.get("jump")));
                    applyImpulse(new Vector2(0, consts.get("jump")));
                }
                
                
                //to walking, also braking
                //set state only on button, because you might be sliding
                if(Math.abs(cs.joys[0]) > consts.get("deadzone")) {//if pushing button
                    //if same sign
                    if(((vel.x < 0)? -1 : 1) == ((cs.joys[0] < 0)? -1 : 1)) {
                        if(Math.abs(vel.x) < consts.get("max_walk_x")){
                            setState(RUNNING);
                            applyImpulse(new Vector2(consts.get("walk_impulse") * ((cs.joys[0] < 0)? -1 : 1), 0));
                        }
                    } else {//pushing opposite direction, different sign
                        applyImpulse(new Vector2(consts.get("brake_impulse") * ((cs.joys[0] < 0)? -1 : 1), 0));
                    }
                } else {// no buttons skidding
                    applyImpulse(new Vector2(consts.get("skidding")*vel.x, 0));
                }
                //static animation
                stateTime = 0;
                break;
            case WALLRIDING://wall riding
                //TODO
                //override direction here because wall is more important
                //no checkDirection()';
                
                //falling off wall
                //actually dont have to hold key, just dont push other way
                //pushing key opposite to direction
                if(Math.abs(cs.joys[0]) > consts.get("deadzone") && direction != (cs.joys[0] < 0? -1 : 1)) {
                    setState(MIDAIR);
                    break;
                //jumping
                } else if(cs.buts[0]) {
                    setState(JUMPING);
                    //switch direction side
                    direction *= -1;
                    //apply impusle in current direction
                    applyImpulse(new Vector2(direction*consts.get("walljump_x"), consts.get("walljump_y")));
                //wall sticking
                } else {
                    applyImpulse(new Vector2(consts.get("wallride_x")*direction, consts.get("wallride_y")*Math.abs(vel.y)));
                }
                
                
                //falling if not touching right side or no sides
                //System.out.println(touchingSide());
                if(touchingSide() != direction) setState(MIDAIR);
                
                //static animation
                stateTime = 0;
                //maybe if there are particle effects in the animaton
                //stateTime += Gdx.graphics.getDeltaTime();
                break;
        }
    }
    
    /** checks if sprite is touching ground 
     * 
     * @return angle to ground, if not standing, returns 1
     */
    public float touchingGround() {
        float standing = 1;//deault, not touching ground
        for(Collision ce : contacts) {
            //below x axis, negative
            float angle = angleToContact(ce);
            if(angle < -.01 && angle > -1*Math.PI+.01) standing = angle;
            
        }
        return standing; 
    }
    
    /** checks if sprite is touching a vertical side wall
     * 
     * @return 1 if right wall, -1 if left wall, 0 if not touching
     */
    public int touchingSide() {
        //contacts from body that you are extending
        for(Collision ce : contacts) {
            double angle = angleToContact(ce);
            if(Math.abs(angle) < .1) return 1;//touching right side
            if(Math.abs(angle) > Math.PI - .1) return -1;//touching right side
            //could do getOther.edges but you need to do this math anyway
        }
        return 0;
    }
    /** finds angle to center other tile
     * 
     * @param c collision
     * @return angle to contact
     */
    //TODO bad logic, angle to other center of tile not always true
    //have to check edge or corners or maybe normal
    public float angleToContact(Collision c) {
        return (float)Math.atan2(c.body2.pos.y - pos.y, c.body2.pos.x - pos.x);
    }
    
    /** changes state and updates lastStateChange and stateTime*/
    public void setState(int s) {
        //System.out.println("set state from " + state + " to " + s);
        state = s;
        lastStateChange = System.nanoTime();
        stateTime = 0;
    }
}