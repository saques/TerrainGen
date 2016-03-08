package com.model.math;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;
import com.model.exception.AlgorithmNotPerformedException;
import com.model.map.Chunk;

/**
 * A Matrix with all the behaviour needed for
 * the Diamond Square Algorithm for
 * terrain generation
 * @author saques
 */
public class DiamondSquareAlgorithm  {
	private static final int MAX_HEIGHT = 256 ;
	private static final int SMOOTHENING_RADIUS = 1 ;
	private static final float TOLERANCE = 1f ;
	private static final float REDUCTION_FACTOR = 0.6f ;
	private float epsilon = 15f ;
	private int exponent ;
	private boolean performed ;
	private Matrix<Integer> m ;
	
	/**
	 * Initializes all the fields needed to
	 * perform the algorithm
	 * @param exponent The exponent
	 * @param s00 Upper left seed
	 * @param s01 Upper right seed
	 * @param s10 Bottom left seed
	 * @param s11 Bottom right seed
	 */
	public DiamondSquareAlgorithm(int exponent,int s00, int s01,int s10, int s11){
		this.exponent = exponent;
		this.performed = false ;
		this.epsilon = 0.25f * MAX_HEIGHT ;
		this.m = new Matrix<Integer>((int)Math.pow(2, exponent)+1);
		
		m.set(0,0,s00);
		m.set(0,m.dimX()-1,s01);
		m.set(m.dimX()-1,0,s10);
		m.set(m.dimX()-1,m.dimX()-1,s11);
	}
	
	/**
	 * Perform the Diamond-Square algorithm on this matrix
	 */
	public void diamondSquare(){
		
		for (int nstep = 0 ; nstep < exponent ; nstep ++){
			
			int nsquares = (int) Math.pow(4, nstep) ;
			int q ; //Squares in a row
			int side ; //Dim of the side of each subsquare
			if (nstep == 0) {
				q = 1 ;
				side = m.dimX() ;
			} else {
				q = (int) Math.sqrt(nsquares);
				side = (m.dimX()+(q-1))/q ;
			}
			int dist ; //The distance to the corners to average
			dist = side/2;
			Set<Point> squares = subSquares(nstep);
			int noise ;
			noise = diamondStep(squares,nstep,dist);
			squareStep(squares,nstep,dist,noise);
			epsilon*=REDUCTION_FACTOR ;
		}
		performed = true ;
	}
	/**
	 * @param nstep The current step number
	 * @return A list with the coordinates of the
	 * centres of the sub-squares for the current step
	 */
	private Set<Point> subSquares(int nstep){
		if (nstep <0 || nstep >= exponent){
			throw new IllegalArgumentException("Invalid step number");
		}
		if (nstep == 0){
			Set<Point> r = new HashSet<Point>() ;
			r.add(new Point(m.dimX()/2,m.dimX()/2));
			return r;
		}
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		q = (int) Math.sqrt(nsquares);
		Set<Point> ans = new HashSet<Point>(nsquares) ;
		int side ; //Side of each square
		side = (m.dimX()+(q-1))/q ;
		int c ; // Centre of the first square from the upper left corner
		c = side / 2 ;
		for (int i = 0 ; i<q ; i++){
			for (int j = 0 ; j<q ; j++){
				ans.add(new Point(c+i*(side-1),c+j*(side-1)));
			}
		}
		return ans ;
	}
	/**
	 * Performs the diamond step for the given
	 * step number
	 * @param squares The subSquares for this step
	 * @param nstep The subSquares for this step
	 * @param dist The distance from the centre of each
	 * subSquare to its corners
	 * @return The last noise generated
	 */
	private int diamondStep(Set<Point> squares,int nstep,int dist) {
		RandomXS128 r = new RandomXS128() ;
		int tmp = r.nextInt();
		int prevRandom = (int)(tmp*Math.signum(tmp));
		for(Point v : squares){
			int ans = 0 ;
			ans += m.get(v.x-dist,v.y-dist) ;
			ans += m.get(v.x-dist,v.y+dist) ;
			ans += m.get(v.x+dist,v.y-dist) ;
			ans += m.get(v.x+dist,v.y+dist) ;
			int avg = (int)(ans/4);
			int toAdd = (avg + prevRandom) % MAX_HEIGHT ;
			if (exceedsTolerance(toAdd, avg)){
				toAdd = avg ;
				System.out.println(true);
			}
			m.set(v.x,v.y,toAdd) ;
			prevRandom = r.nextInt(prevRandom)+1 ;
		}
		return prevRandom;
	}
	/**
	 * Performs the square step for the given 
	 * step number
	 * @param squares The subSquares for this step
	 * @param nstep The subSquares for this step
	 * @param dist The distance from the centre of each
	 * @param noise The noise to start from
	 * diamond to its corners
	 */
	private void squareStep(Set<Point> squares,int nstep,int dist,int noise){
		RandomXS128 r = new RandomXS128() ;
		int prevRandom = noise;
		
		Set<Point> diamonds = new HashSet<Point>() ;
		for(Point v : squares){
			diamonds.add(new Point(v.x-dist,v.y));
			diamonds.add(new Point(v.x+dist,v.y));
			diamonds.add(new Point(v.x,v.y-dist));
			diamonds.add(new Point(v.x,v.y+dist));
		}
		for(Point v : diamonds){
			int ans=0;
			int div = 4 ;
			try{
				ans += m.get(v.x-dist,v.y) ;
			} catch (Exception e){
				div = 3;
			}
			try {
				ans += m.get(v.x+dist,v.y) ;
			} catch (Exception e){
				div = 3;
			}
			try {
				ans += m.get(v.x,v.y-dist) ;
			} catch (Exception e){
				div = 3;
			}
			try {
				ans += m.get(v.x,v.y+dist) ;
			} catch (Exception e){
				div = 3;
			}
			int avg = (int)(ans/div);
			int toAdd = (avg + prevRandom) % MAX_HEIGHT ;
			if (exceedsTolerance(toAdd, avg)){
				toAdd = avg ;
				System.out.println(true);
			}
			m.set(v.x,v.y,toAdd) ;
			prevRandom = r.nextInt(prevRandom)+ 1 ;
		}
	}
	
	private boolean exceedsTolerance(int v1,int v2){
		float g,s;
		if (v1>v2){
			g=v1 ; s=v2 ;
		} else {
			g=v2 ; s=v1 ;
		}
		
		return ((g-s)/epsilon) > TOLERANCE ;
	}
	
	private Matrix<Integer> smoothen(int radius){
		if (radius<0){
			throw new IllegalArgumentException();
		} else if (radius == 0){
			return m ;
		}
		Matrix<Integer> ans = new Matrix<Integer>((int)Math.pow(2, exponent)+1);
		if (!performed){
			throw new AlgorithmNotPerformedException();
		}
		for (int i=0; i<m.dimX(); i++){
			for(int j=0;j<m.dimX();j++){
				ans.set(i, j, roundAvg(i, j));
			}
		}
		
		return ans;
	}
	
	private int roundAvg(int i,int j){
		int ans = 0 ;
		int count = 0 ;
		for (int k = i-1 ; k<=i+1 ; k++){
			for (int q = j-1 ; q<=j+1;q++){
				if (!m.outOfBounds(k, q)){
					ans += m.get(k, q) ;
					count ++ ;
				}
			}
		}
		return ans/count;
	}
	
	
	/**
	 * Splits this matrix into Math.pow(4,exponent) chunks
	 * @param exp The exponent
	 * @return A Matrix with the chunks
	 */
	public Matrix<Chunk> splitIntoChunks(int exp){
		if (!performed){
			throw new AlgorithmNotPerformedException();
		}
		if (exp<0 || exp >exponent){
			throw new IllegalArgumentException("Illegal exponent");
		}
		Matrix<Integer> smoothened = smoothen(SMOOTHENING_RADIUS);
		int nchunks = (int)Math.pow(4, exp);
		int q ; //Chunks in a row
		q = (int)Math.sqrt(nchunks);
		int side ; //Side of each chunk
		side = (m.dimX()-1)/q ;
		Matrix<Chunk> ans = new Matrix<Chunk>(q,q);
		for (int i=0;i<q;i++){
			for(int j=0;j<q;j++){
			ans.set(i,j,new Chunk(side));
			}
		}
		for (int i=0;i<m.dimX()-1;i++){
			for (int j=0;j<m.dimX()-1;j++){
				//The vertices of the two triangles to add
				Vector3 c1,c2,c3,c4;
				c1 = new Vector3(i,j,smoothened.get(i,j));
				c2 = new Vector3(i,j+1,smoothened.get(i,j+1));
				c3 = new Vector3(i+1,j,smoothened.get(i+1,j));
				c4 = new Vector3(i+1,j+1,smoothened.get(i+1,j+1));
				Chunk c = ans.get(i/side,j/side);
				c.addCell(new Cell(c1, c2, c3, c4)) ;
			}
		}
		return ans;
	}
}
