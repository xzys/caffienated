/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/** simple animator test
 *
 * @author SACHIN
 */
public class Test_Animator extends ApplicationAdapter {
    private Animator animator;
    private SpriteBatch batch;
    private Texture wp;
    
    @Override
    public void create() {
        animator = new Animator(new Texture(Gdx.files.internal("running3.png")), 1);
        wp = new Texture(Gdx.files.internal("whitepixel.png"));
        batch = new SpriteBatch();
    }
    
    @Override
    public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        batch.setColor(new Color(240, 240, 240, 255));
        batch.draw(wp, 0, 0, 800, 600);
        batch.end();
        animator.render(batch, new Vector2(400, 300), 0, 1, 0, 1, 50);
        
    }
    

}