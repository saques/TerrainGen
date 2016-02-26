package com.mygdx.vc;


import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.model.map.Chunk;
import com.model.map.World;
import com.model.math.Matrix;
import com.model.math.Triangle;

public class MainScreen implements Screen {
	
	private Camera camera ;
	private Viewport viewport ;
	private World world ;
	private Mesh mesh ;
	private Renderable renderable ;
	private Shader shader ;
	private RenderContext renderContext;
	
	public MainScreen() {	
		camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), 
										   Gdx.graphics.getHeight());
		camera.position.set(2f, 2f, 2f);
	    camera.lookAt(0,0,0);
	    camera.near = 1f;
	    camera.far = 300f;
	    camera.update();
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), 
									  Gdx.graphics.getHeight(),camera);
		world = new World(5,2,15,48,83,131);
		
		Matrix<Chunk> m = world.getChunks() ;
		MeshBuilder build = new MeshBuilder() ;
		build.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3, "a_position"),
										 new VertexAttribute(Usage.ColorPacked, 4, "a_color")),
										 GL20.GL_TRIANGLES);
		for (Chunk c : m){
			Set<Triangle> triangles = c.getTriangles();
			for (Triangle t: triangles) {
				VertexInfo i1,i2,i3 ;
				Vector3 normal = t.getNormal();
				i1 = new VertexInfo().set(t.getp1(), normal, getColor(t.getp1()), new Vector2(1,1));
				i2 = new VertexInfo().set(t.getp2(), normal, getColor(t.getp2()), new Vector2(1,1));
				i3 = new VertexInfo().set(t.getp3(), normal, getColor(t.getp3()), new Vector2(1,1));
				build.triangle(i1, i2, i3);
			}
		}
		mesh = build.end();
		
		renderable = new Renderable();
		renderable.material = new Material();
		renderable.mesh = this.mesh ;
		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
		shader = new DefaultShader(renderable);
		shader.init();
	}
	
	private Color getColor(Vector3 p){
		int c = (int)p.z ;
		if (c>=0 && c<30){
			return Color.CYAN;
		} else if (c>=30 && c<40){
			return Color.YELLOW;
		} else if (c>=40 && c<80) {
			return Color.GREEN ;
		} else if (c>=80 && c<130) {
			return Color.LIGHT_GRAY;
		} else if (c>=130 && c<190) {
			return Color.GRAY ;
		} else if (c>=190 && c<230) {
			return Color.DARK_GRAY ;
		}
		return Color.WHITE ;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.53f,0.80f,0.98f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		renderContext.begin();
	    shader.begin(camera, renderContext);
	    shader.render(renderable);
	    shader.end();
	    renderContext.end();
	    camera.update();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
