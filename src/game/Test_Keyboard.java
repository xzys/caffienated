/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author SACHIN
 */
import com.badlogic.gdx.ApplicationAdapter;

public class Test_Keyboard extends ApplicationAdapter {
    public ControlHandler control;

    @Override
    public void create() {
        control = new GdxKeyboardHandler();
        control = new KeyboardHandler();
    }
    
    @Override
    public void render() {
        
    }
    

}