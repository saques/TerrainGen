package model.math;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.math.RandomXS128;

/**
 * An utility class for performing the
 * Diamond Square Algorithm for procedural
 * terrain generation
 * @author saques
 */
public class DiamondSquareMatrix {
	private static final int MAX_HEIGHT = 256 ;
	private List<List<Integer>> matrix ;
	private int dim ;
	private int exponent ;
	
	/**
	 * Creates a square matrix with a dimension of
	 * pow(2,exponent)+1 , with each element set to 0
	 * @param exponent The exponent
	 */
	public DiamondSquareMatrix(int exponent){
		if (exponent < 1) {
			throw new IllegalArgumentException("Exponent must be positive") ;
		}
		this.exponent = exponent;
		dim = (int) (Math.pow(2, exponent)+1) ;
		matrix = new ArrayList<List<Integer>>(dim) ;
		for (int i=0 ; i<dim ; i++){
			matrix.add(i, new ArrayList<Integer>(dim));
			for (int j=0;j<dim; j++){
				matrix.get(i).add(0);
			}
		}
	}
	
	private Integer get(int i,int j){
		if (i<0 || i>dim || j<0 || j>dim){
			throw new IllegalArgumentException("Wrong indices");
		}
		return matrix.get(i).get(j) ;
	}
	
	private void set(int i,int j,int val){
		if (i<0 || i>dim || j<0 || j>dim){
			throw new IllegalArgumentException("Wrong indices");
		}
		matrix.get(i).set(j, val);
	}
	/**
	 * @param nstep The current step number
	 * @return A list with the coordinates of the
	 * centres of the sub-squares for the current step
	 */
	public Set<Point> subSquares(int nstep){
		if (nstep <0 || nstep >= exponent){
			throw new IllegalArgumentException("Invalid step number");
		}
		if (nstep == 0){
			Set<Point> r = new HashSet<Point>() ;
			r.add(new Point(dim/2,dim/2));
			return r;
		}
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		q = (int) Math.sqrt(nsquares);
		Set<Point> ans = new HashSet<Point>(nsquares) ;
		int side ; //Side of each square
		side = (dim+(q-1))/q ;
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
	 * Perform the Diamond-Square algorithm on this matrix
	 * @param s00 Upper left seed
	 * @param s01 Upper right seed
	 * @param s10 Bottom left seed
	 * @param s11 Bottom right seed
	 * @return this matrix for chaining
	 */
	public DiamondSquareMatrix diamondSquare(int s00, int s01,int s10, int s11){
		set(0,0,s00);
		set(0,dim-1,s01);
		set(dim-1,0,s10);
		set(dim-1,dim-1,s11);
		
		for (int nstep = 0 ; nstep < exponent ; nstep ++){
			squareStep(diamondStep(nstep),nstep);
		}
		return this;
	}
	
	private Set<Point> diamondStep(int nstep) {
		RandomXS128 r = new RandomXS128() ;
		int tmp = r.nextInt() ;
		int prevRandom = (int) (tmp*Math.signum(tmp)) % MAX_HEIGHT + 1;
		
		// This is horrible, should be extracted to a method
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		int side ; //Dim of the side of each subsquare
		if (nstep == 0) {
			q = 1 ;
			side = dim ;
		} else {
			q = (int) Math.sqrt(nsquares);
			side = (dim+(q-1))/q ;
		}
		int dist ; //The distance to the corners to average
		dist = side/2;
		
		Set<Point> centres = subSquares(nstep);
		for(Point v : centres){
			int ans = 0 ;
			ans += get(v.x-dist,v.y-dist) ;
			ans += get(v.x-dist,v.y+dist) ;
			ans += get(v.x+dist,v.y-dist) ;
			ans += get(v.x+dist,v.y+dist) ;
			set(v.x,v.y,((ans)/4 + prevRandom)%MAX_HEIGHT) ;
			prevRandom = r.nextInt(prevRandom)%MAX_HEIGHT + 1 ;
		}
		return centres ;
	}
	
	private void squareStep(Set<Point> squares,int nstep){
		RandomXS128 r = new RandomXS128() ;
		int tmp = r.nextInt() ;
		int prevRandom = (int) (tmp*Math.signum(tmp)) % MAX_HEIGHT  + 1;
		
		// This is horrible, should be extracted to a method
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		int side ; //Dim of the side of each subsquare
		if (nstep == 0) {
			q = 1 ;
			side = dim ;
		} else {
			q = (int) Math.sqrt(nsquares);
			side = (dim+(q-1))/q ;
		}
		int dist ; //The distance to the corners to average
		dist = side/2;
		
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
			prevRandom = r.nextInt(prevRandom) % MAX_HEIGHT + 1 ;
		}
	}
	
	public String toString(){
		String s = "";
		for (List<Integer> l : matrix){
			s+=l.toString()+"\n" ;
		}
		return s ;
	}
}
