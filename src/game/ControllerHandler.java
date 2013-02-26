/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author SACHIN
 */

import java.util.Map;
import net.java.games.input.Controller;
/** Controller object for gamepads, uses JInput and JINputJoystick
 * 
 * @author SACHIN
 */
public class ControllerHandler implements ControlHandler {
    //Vector2 LeftJoystick;
    //Vector2 RightJoystick;
    //check these for values
    //this will be replaced by keyboard in Keyboard Handler 
    public ControlState controlState;
    public boolean connected;
    public Map<Integer, Object> keymappings;
    
    //has gamepad, which has controller actually
    private JInputJoystick gamepad;
    /** Constructor: makes sure that controller is connected */
    public ControllerHandler() {//also take in key bindings
        //initilaize gamepad
        
        //check for bot but we really only need gamepads
        //it searches for gamepad first
        gamepad = new JInputJoystick(Controller.Type.GAMEPAD, Controller.Type.STICK);
        
        //what to do if Joystick not found
        //TODO error handling, throw an exception!
        if( !gamepad.isControllerConnected() ){
            System.out.println("No controller found! Replace with Keyboard and Mouse controls.");
            connected = false;
        } else {
            connected = true;
        }
        
        //if controller is a joystick, you have to set keyboard button replacements 
        if(gamepad.getControllerType() == Controller.Type.STICK) {
            connected = false;
        }
        
    }
    
    /** Gets input information from JInputJoyStick*/
    //TODO everything here
    public void update() {
        if( !gamepad.pollController() ) {
            System.out.println("Error: Controller disconnected!");
            connected = false;
            return;
        }
        //assume you are using the left joystick for controls change to use right?
        controlState.joys[0] = gamepad.getX_LeftJoystick_Percentage();
        controlState.joys[1] = gamepad.getY_LeftJoystick_Percentage();
        //INCORRECT
        //need to use keymappings
        //controlState.buts[0] = gamepad.getButtonsValues().get(KEYMAPPING);
        //controlState.buts[1] = gamepad.getButtonsValues().get(KEYMAPPING);
        
        //we don't need all the right controller joystick and stuff but just keep it here for resuability
        //replace joystick with gamepad it makes sense
        /*
        // Left controller joystick
        int xValuePercentageLeftJoystick = joystick.getX_LeftJoystick_Percentage();
        int yValuePercentageLeftJoystick = joystick.getY_LeftJoystick_Percentage();
        //window.setXYAxis(xValuePercentageLeftJoystick, yValuePercentageLeftJoystick);
        
        // Right controller joystick
        int xValuePercentageRightJoystick = joystick.getX_RightJoystick_Percentage();
        int yValuePercentageRightJoystick = joystick.getY_RightJoystick_Percentage();
        //window.setZAxis(xValuePercentageRightJoystick);
        //window.setZRotation(yValuePercentageRightJoystick);
        
        // If controller is a gamepad type. 
        if(joystick.getControllerType() == Controller.Type.GAMEPAD)
        { // Must check if controller is a gamepad, because stick type controller also have Z axis but it's for right controller joystick.
            // If Z Axis exists.
            if(joystick.componentExists(Component.Identifier.Axis.Z)){
                int zAxisValuePercentage = joystick.getZAxisPercentage();
                window.setZAxisGamepad(zAxisValuePercentage);
            }
        }
        
        // Sets controller buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        buttonsPanel.setBounds(6, 19, 246, 110);
        ArrayList<Boolean> buttonsValues = joystick.getButtonsValues();
        for(int i=0; i < buttonsValues.size(); i++) {
            JToggleButton aToggleButton = new JToggleButton(""+(i+1), buttonsValues.get(i));
            aToggleButton.setPreferredSize(new Dimension(48, 25));
            aToggleButton.setEnabled(false);
            buttonsPanel.add(aToggleButton);
        }
        window.setControllerButtons(buttonsPanel);
        
        // Hat Switch
        float hatSwitchPosition = joystick.getHatSwitchPosition();
        window.setHatSwitch(hatSwitchPosition);
        
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(JoystickTest.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public void close() {
        //TODO close connection to gamepad
    }
    
    public ControlState getControlState() { return controlState; }
}
