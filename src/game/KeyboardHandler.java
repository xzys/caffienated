/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/** Handles a keyboard combining event driven and polling style
 *
 * @author SACHIN
 */

import com.badlogic.gdx.Input.Keys;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class KeyboardHandler implements ControlHandler, KeyEventDispatcher {
    //errors mean this is working
    //but if they are already initialized can you just delete these 
    //controlstate contains all controls to be used in the game
    public ControlState controlState;
    public boolean connected;
    //the problem with this implementation is that though you can access joys, the changed value doesn't stick
    //good idea for python maybe, but bad design choie for java
    //public Map<Integer, Object[]> keymappings;
    //map keycodes to objects, the controls
    //keymappings = new HashMap<>();
    //keymappings.put(37, new Object[]{controlState.joys[0], 100, 0});//right 
    //keymappings.put(38, new Object[]{controlState.joys[1], 100, 0});//up
    //keymappings.put(39, new Object[]{controlState.joys[0], -100, 0});//left
    //keymappings.put(40, new Object[]{controlState.joys[1], -100, 0});//down
    //keymappings.put(90, new Object[]{controlState.buts[0], true, false});//button 1
    //keymappings.put(88, new Object[]{controlState.buts[1], true, false});//button 2
    public int[] keymappings;
    public List<Integer> heldKeys;
    /** Constructor: initializes things*/
    public KeyboardHandler() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        heldKeys = new ArrayList<>();
        connected = true;
        controlState = new ControlState();
        //these are the keymappings if you are using KeyboardFocusmanger
        //TODO why isn't that working
        //keymappings = new int[]{37, 38, 39, 40, 90, 88};
        //key mappings using libgdx
        keymappings = new int[]{Keys.LEFT, Keys.RIGHT, Keys.UP, Keys.DOWN, Keys.Z, Keys.X};
    }

    /** Updates controlstate with information from keymappings.
     * Unnecessary (maybe) but allows for polling style keyboard response.
     * Also ignores updating jump all the time b/c we turn it off on a timer
     */
    //TODO move this to block below so that you can disable jump
    //don't really need this because it updates via event
    public void update() {
        
        if(heldKeys.contains(keymappings[0])) controlState.joys[0] = -100;
        else if(heldKeys.contains(keymappings[1])) controlState.joys[0] = 100;
        else controlState.joys[0] = 0;
        
        if(heldKeys.contains(keymappings[2])) controlState.joys[1] = -100;
        else if(heldKeys.contains(keymappings[3])) controlState.joys[1] = 100;
        else controlState.joys[1] = 0;
        
        if(heldKeys.contains(keymappings[4])) controlState.buts[0] = true;
        else controlState.buts[0] = false;
        if(heldKeys.contains(keymappings[5])) controlState.buts[1] = true;
        else controlState.buts[1] = false;
    }
    
    /*public void update() {
        for(int key : keymappings.keySet()) {
            //System.out.println(key);
            if(heldKeys.contains(key)) {
                //set value
                keymappings.get(key)[0] = keymappings.get(key)[1];
                //System.out.println(keymappings.get(key)[0]);
            } else {
                //default value
                keymappings.get(key)[0] = keymappings.get(key)[2];
                //System.out.println(keymappings.get(key)[0]);
            }
        }
    }
    */
    
    //TODO need to make this schronized
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        int kc = e.getKeyCode();
        
        
        if (e.getID() == KeyEvent.KEY_PRESSED && !heldKeys.contains(kc)) {
            heldKeys.add(kc);
            System.out.println(heldKeys);
        }
        
        if (e.getID() == KeyEvent.KEY_RELEASED && heldKeys.contains(kc)) {
            heldKeys.remove(heldKeys.indexOf((Integer) kc));
            //System.out.println(heldKeys);
        }        
        return false;
    }
    
    //need to remove after usage
    @Override
    public void close() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
        connected = false;
    }
    
    public ControlState getControlState() { return controlState; }
    
}
