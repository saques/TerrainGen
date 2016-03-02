package com.mygdx.vc;


import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
	private CameraInputController camController ;
	private ModelBatch batch ;
	
	public MainScreen() {	
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), 
										   Gdx.graphics.getHeight());
		camera.lookAt(new Vector3(10, 10, 0));
		camera.direction.set(-1, -1, -1);
		camera.position.set(100, 100, 100);
		camera.near = 0f;
		camera.far = 1000f;
		camera.update();
		viewport = new ScreenViewport(camera);
		camController = new CameraInputController(camera);
	    Gdx.input.setInputProcessor(camController);
		
		world = new World(6,2,15,20,33,30);
		Matrix<Chunk> m = world.getChunks() ;
		MeshBuilder build = new MeshBuilder() ;
		build.begin(Usage.Position | Usage.ColorPacked | Usage.Normal, GL20.GL_LINES);
		for (Chunk c : m){
			Set<Triangle> triangles = c.getTriangles();
			for (Triangle t: triangles) {
				VertexInfo p1,p2,p3 ;
				Vector3 nor = t.getNormal();
				p1 = new VertexInfo() ;
				p1.setPos(t.getp1()).setCol(getColor(t.getp1())).setNor(nor);
				p2 = new VertexInfo() ;
				p2.setPos(t.getp2()).setCol(getColor(t.getp2())).setNor(nor);
				p3 = new VertexInfo() ;
				p3.setPos(t.getp3()).setCol(getColor(t.getp3())).setNor(nor);
				build.triangle(p1,p2,p3);
			}
		}
		mesh = build.end();
		
		renderable = new Renderable();
		renderable.meshPart.mesh=mesh;
		renderable.meshPart.offset=0;
		renderable.meshPart.size=mesh.getNumIndices();
		renderable.meshPart.primitiveType=GL20.GL_LINES;
		batch = new ModelBatch() ;
		
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
	   	
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	    batch.begin(camera);
	    batch.render(renderable);
	    batch.end();
	    camController.update();
	    camera.update();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
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
	}

}
