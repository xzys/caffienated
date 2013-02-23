/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/** Basic physics body that can be dynamic or static
 *
 * @author SACHIN
 */
public class Body {
    Vector2 pos, vel, acc;
    ArrayList<Collision> contacts;
    //TODO if dynamic, vel/acc not used
    boolean dynamic;
    //is this a circle or what type of shape??
    /** for this problem what we are going to do is 
        have booleans telling you whether or not this shape is a cirlc or not
        and also telling us whether this body is movableo or not.
        some things may be static for some time and then start moving or etc.
        as for vertices, even if we have non circular, still good to use the circular 
        shape for broadphase like AABB or something*/
    /** Basic constructor
     * @param dynamic moving or static
     */
    public Body(boolean dynamic) {
        pos = new Vector2(0, 0);
        this.dynamic = dynamic;
        this.contacts = new ArrayList<Collision>(); 
        if(dynamic) {
            vel = new Vector2(0, 0);
            acc = new Vector2(0, 0);
        } else {
            //they wont be used, but better to do this so they dont give null pointer?
            vel = null;
            acc = null;
        }
    }
    
    /** Add c to contacts */
    public void addContact(Collision c) {
        contacts.add(c);
    }
    /** Remove c from contacts */
    public void removeContact(Collision c) {
        contacts.remove(contacts.indexOf(c));
    }
    /** clear contacts every frame*/
    public void clearContacts() {
        contacts.clear();
    }
    /** Add impulse to acc. Should be the only way you every interact with movement*/
    public void applyImpulse(Vector2 impulse) {
        acc.add(impulse);
    }
    /** Translate to origin*/
    public void translateToOrigin() {
        pos.set(0, 0);
    }
    /** Translate to translation vector*/
    public void translate(Vector2 translation) {
        translate(translation.x, translation.y);
    }
    /** Translate to translation vector*/
    public void translate(float x, float y) {
        pos.x += x;
        pos.y += y;
    }
    /** Renders body, not circle/polygon specific*/
    //TODO how to render this?
    public void render(SpriteBatch batch ) {
        assert false : "Use a more precise Body render method";
    }
}