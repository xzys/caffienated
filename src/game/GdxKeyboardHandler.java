/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author SACHIN
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import java.util.ArrayList;
import java.util.List;

public class GdxKeyboardHandler implements ControlHandler, InputProcessor {
    //errors mean this is working
    //but if they are already initialized can you just delete these 
    //controlstate contains all controls to be used in the game
    public ControlState controlState;
    public boolean connected;
    public int[] keymappings;
    public List<Integer> heldKeys;
    
    public GdxKeyboardHandler() {
        //replacing the keyboardFocus manger I guess
        Gdx.input.setInputProcessor(this);
        heldKeys = new ArrayList<>();
        connected = true;
        controlState = new ControlState();
        keymappings = new int[]{Keys.LEFT, Keys.RIGHT, Keys.UP, Keys.DOWN, Keys.Z, Keys.X};
    }
    
    //this class specificlaly implements not auto jumping
    public void update() {
        if(heldKeys.contains(keymappings[0])) controlState.joys[0] = -100;
        else if(heldKeys.contains(keymappings[1])) controlState.joys[0] = 100;
        else controlState.joys[0] = 0;
        
        if(heldKeys.contains(keymappings[2])) controlState.joys[1] = -100;
        else if(heldKeys.contains(keymappings[3])) controlState.joys[1] = 100;
        else controlState.joys[1] = 0;
        
        //update this elseware because we are gonna play with it a little
        //we change the button in sprite processState()
        //only change button if you actually press it
        //move this to keyDown/keyUp methods
        //if(heldKeys.contains(keymappings[4])) controlState.buts[0] = true;
        //else controlState.buts[0] = false;
        if(heldKeys.contains(keymappings[5])) controlState.buts[1] = true;
        else controlState.buts[1] = false;
    }
    
    @Override
    public boolean keyDown (int keycode) {
        heldKeys.add(keycode);
        System.out.println(keycode);
        update();
        if(keycode == keymappings[4]) controlState.buts[0] = true;
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {
        if(heldKeys.contains(keycode)) {
            heldKeys.remove(heldKeys.indexOf((Integer) keycode));
        }
        update();
        if(keycode == keymappings[4]) controlState.buts[0] = false;
        return true;
    }
    
    public ControlState getControlState() { return controlState; }
    public void close() {}
    
    //everything else unnecessary

    @Override
    public boolean keyTyped (char character) {
    return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
    return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
    return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
    return false;
    }

    @Override
    public boolean mouseMoved (int x, int y) {
    return false;
    }

    @Override
    public boolean scrolled (int amount) {
    return false;
    }
}
