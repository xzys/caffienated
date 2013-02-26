/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/** Represents a snapshot of which buttons are currently pressed,
 * but it's a permanent object in whichever ControlHandler
 *
 * @author SACHIN
 */
public class ControlState {
    //very minimal
    //I want to be able to pass references to these objects to controllers
    public Integer[] joys;//joystick
    //constant number of buttons
    public Boolean[] buts;//buttons
    /** Constructor: initializes joys and buts arrays */
    //TODO change here if you add more controls
    public ControlState () {
        this.joys = new Integer[]{0, 0};
        this.buts = new Boolean[]{false, false};
        //buts[0] = false;
        //buts[1] = false;
    }
}
