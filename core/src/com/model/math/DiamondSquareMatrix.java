package com.model.math;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class DiamondSquareMatrix extends Matrix {
	private static final int MAX_HEIGHT = 256 ;
	private int exponent ;
	private boolean performed ;
	
	/**
	 * Creates a square matrix with a dimension of
	 * pow(2,exponent)+1
	 * @param exponent The exponent
	 */
	public DiamondSquareMatrix(int exponent){
		super((int)Math.pow(2, exponent)+1,
			  (int)Math.pow(2, exponent)+1) ;
		this.exponent = exponent;
		this.performed = false ;
	}
	
	/**
	 * Perform the Diamond-Square algorithm on this matrix
	 * @param s00 Upper left seed
	 * @param s01 Upper right seed
	 * @param s10 Bottom left seed
	 * @param s11 Bottom right seed
	 * @return this matrix for chaining
	 */
	public DiamondSquareMatrix diamondSquare(int s00, int s01,int s10, int s11){
		set(0,0,s00);
		set(0,dimX()-1,s01);
		set(dimX()-1,0,s10);
		set(dimX()-1,dimX()-1,s11);
		
		for (int nstep = 0 ; nstep < exponent ; nstep ++){
			
			int nsquares = (int) Math.pow(4, nstep) ;
			int q ; //Squares in a row
			int side ; //Dim of the side of each subsquare
			if (nstep == 0) {
				q = 1 ;
				side = dimX() ;
			} else {
				q = (int) Math.sqrt(nsquares);
				side = (dimX()+(q-1))/q ;
			}
			int dist ; //The distance to the corners to average
			dist = side/2;
			Set<Point> squares = subSquares(nstep);
			diamondStep(squares,nstep,dist);
			squareStep(squares,nstep,dist);
		}
		performed = true ;
		return this;
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
			r.add(new Point(dimX()/2,dimX()/2));
			return r;
		}
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		q = (int) Math.sqrt(nsquares);
		Set<Point> ans = new HashSet<Point>(nsquares) ;
		int side ; //Side of each square
		side = (dimX()+(q-1))/q ;
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
	 */
	private void diamondStep(Set<Point> squares,int nstep,int dist) {
		RandomXS128 r = new RandomXS128() ;
		int tmp = r.nextInt() ;
		int prevRandom = (int) (tmp*Math.signum(tmp)) % MAX_HEIGHT + 1;
		for(Point v : squares){
			int ans = 0 ;
			ans += get(v.x-dist,v.y-dist) ;
			ans += get(v.x-dist,v.y+dist) ;
			ans += get(v.x+dist,v.y-dist) ;
			ans += get(v.x+dist,v.y+dist) ;
			set(v.x,v.y,((ans)/4 + prevRandom)%MAX_HEIGHT) ;
			prevRandom = r.nextInt(prevRandom)%MAX_HEIGHT + 1 ;
		}
	}
	/**
	 * Performs the square step for the given 
	 * step number
	 * @param squares The subSquares for this step
	 * @param nstep The subSquares for this step
	 * @param dist The distance from the centre of each
	 * diamond to its corners
	 */
	private void squareStep(Set<Point> squares,int nstep,int dist){
		RandomXS128 r = new RandomXS128() ;
		int tmp = r.nextInt() ;
		int prevRandom = (int) (tmp*Math.signum(tmp)) % MAX_HEIGHT  + 1;
		
		Set<Point> diamonds = new HashSet<Point>() ;
		for(Point v : squares){
			diamonds.add(new Point(v.x-dist,v.y));
			diamonds.add(new Point(v.x+dist,v.y));
			diamonds.add(new Point(v.x,v.y-dist));
			diamonds.add(new Point(v.x,v.y+dist));
		}
		for(Point v : diamonds){
			int ans=0;
			try{
				ans += get(v.x-dist,v.y) ;
			} catch (Exception e){
				
			}
			try {
				ans += get(v.x+dist,v.y) ;
			} catch (Exception e){
				
			}
			try {
				ans += get(v.x,v.y-dist) ;
			} catch (Exception e){
				
			}
			try {
				ans += get(v.x,v.y+dist) ;
			} catch (Exception e){
				
			}
			set(v.x,v.y,((ans)/4 + prevRandom)%MAX_HEIGHT) ;
			prevRandom = r.nextInt(prevRandom)%MAX_HEIGHT + 1 ;
		}
	}
	/**
	 * Splits this matrix into Math.pow(4,exponent) chunks
	 * @param exp The exponent
	 * @return A list with the chunks
	 */
	public List<Chunk> splitIntoChunks(int exp){
		if (!performed){
			throw new AlgorithmNotPerformedException();
		}
		if (exp<0 || exp >exponent){
			throw new IllegalArgumentException("Illegal exponent");
		}
		int nchunks = (int)Math.pow(4, exp);
		int q ; //Chunks in a row
		q = (int)Math.sqrt(nchunks);
		int side ; //Side of each chunk
		side = (dimX()-1)/q ;
		List<Chunk> ans = new ArrayList<Chunk>(nchunks);
		for (int i=0;i<nchunks;i++){
			ans.add(new Chunk(side));
		}
		for (int i=0;i<dimX()-1;i++){
			for (int j=0;j<dimX()-1;j++){
				//The vertices of the two triangles to add
				Vector3 c1,c2,c3,c4;
				c1 = new Vector3(i,j,get(i,j));
				c2 = new Vector3(i,j+1,get(i,j+1));
				c3 = new Vector3(i+1,j,get(i+1,j));
				c4 = new Vector3(i+1,j+1,get(i+1,j+1));
				Triangle t1,t2;
				t1 = new Triangle(c1,c2,c3);
				t2 = new Triangle(c2,c3,c4);
				Chunk c = ans.get((i/side)+((j/side))*2);
				c.addTriangle(t1);
				c.addTriangle(t2);
			}
		}
		return ans;
	}
}
