/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author SACHIN
 */


public interface ControlHandler { 
    public ControlState controlState = new ControlState();
    public boolean connected = false;
    public int[] keymappings = new int[5];
    //replaced
    //public Map<Integer, Object> keymappings = new HashMap<>();
    
    //boolean initialize();
    public void update();
    public void close();
    public ControlState getControlState();
}