/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 *
 * @author SACHIN
 */
public class Test_Collisions extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shpren;
    private OrthographicCamera camera;
    
    private Texture wp;
    private CircularBody body;
    private World world;
    private Tilemap tilemap;
    
    @Override
    public void create() {
        wp = new Texture(Gdx.files.internal("whitepixel.png"));
        batch = new SpriteBatch();
        shpren = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        
        body = new CircularBody(true, 25);
        
        world = new World(800, 600);
        world.add(body);
        tilemap = new Tilemap(16, 12, (float) (50), new Color(.3f, .3f, .3f, .9f));
        tilemap.initialize(world, "tilemap.txt");
        world.add(tilemap);
    }
    
    @Override
    public void render() {
        //clear buffer, necessary to allow fast rendering
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        body.translateToOrigin();
        body.translate(Gdx.input.getX(), 600-Gdx.input.getY());
        body.vel.mul(.99f);
         
        //world.run(0);
        world.detect();
        world.solve();
        
        batch.begin();
        batch.setColor(.9f, .9f, .9f, 1);
        //this is how you draw backgrounds
        batch.draw(wp, 0, 0, 800, 600);
        batch.end();
        
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        tilemap.render(shpren);
        body.render(shpren);
        world.renderCollisions(shpren);
        Gdx.gl.glDisable(GL10.GL_BLEND);
        
        
    }
    

}