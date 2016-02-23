package model.math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
/**
 * An utility class for performing the
 * Diamond Square Algorithm for procedural
 * terrain generation
 * @author saques
 */
public class DiamondSquareMatrix {
	
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
				matrix.get(i).add(j, 0);
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
		matrix.get(i).add(j,val);
	}
	/**
	 * @param nstep The current step number
	 * @return A list with the coordinates of the
	 * centres of the sub-squares for the current step
	 */
	public Set<Vector2> subSquares(int nstep){
		if (nstep <0 || nstep >= exponent){
			throw new IllegalArgumentException("Invalid step number");
		}
		if (nstep == 0){
			Set<Vector2> r = new HashSet<Vector2>() ;
			r.add(new Vector2((int)(dim/2),(int)(dim/2)));
			return r;
		}
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		q = (int) (Math.log(nsquares)/Math.log(2));
		Set<Vector2> ans = new HashSet<Vector2>(nsquares) ;
		int side ; //Side of each square
		side = ((dim%q)*q+dim)/q ;
		int c ; // Centre of the first square from the upper left corner
		c = side / 2 ;
		for (int i = 0 ; i<q ; i++){
			for (int j = 0 ; j<q ; j++){
				ans.add(new Vector2(c+i*(side-1),c+j*(side-1)));
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
		return diamondSquareRec(0) ;
	}
	
	private Set<Vector2> diamondStep(int nstep) {
		Random r = new Random() ;
		int tmp = r.nextInt() ;
		int prevRandom = (int) (tmp*Math.signum(tmp));;
		
		// This is horrible, should be extracted to a method
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		int side ; //Side of each square
		if (nstep == 0) {
			q = 1 ;
			side = dim/2 ;
		} else {
			q = (int) (Math.log(nsquares)/Math.log(2));
			side = ((dim%q)*q+dim)/q ;
		}
		int dist ; //The distance to the corners to average
		dist = side/2;
		
		Set<Vector2> centres = subSquares(nstep);
		for(Vector2 v : centres){
			int ans = 0 ;
			ans += get((int)(v.x)-dist,(int)(v.y)-dist) ;
			ans += get((int)(v.x)-dist,(int)(v.y)+dist) ;
			ans += get((int)(v.x)+dist,(int)(v.y)-dist) ;
			ans += get((int)(v.x)+dist,(int)(v.y)+dist) ;
			set((int)(v.x),(int)(v.y),(ans)/4 + prevRandom) ;
			prevRandom = r.nextInt(prevRandom) ;
		}
		return centres ;
	}
	
	private void squareStep(Set<Vector2> squares,int nstep){
		Random r = new Random() ;
		int tmp = r.nextInt() ;
		int prevRandom = (int) (tmp*Math.signum(tmp));
		
		// This is horrible, should be extracted to a method
		int nsquares = (int) Math.pow(4, nstep) ;
		int q ; //Squares in a row
		int side ; //Side of each square
		if (nstep == 0) {
			q = 1 ;
			side = dim/2 ;
		} else {
			q = (int) (Math.log(nsquares)/Math.log(2));
			side = ((dim%q)*q+dim)/q ;
		}
		int dist ; //The distance to the corners to average
		dist = side/2;
		
		Set<Vector2> diamonds = new HashSet<Vector2>() ;
		for(Vector2 v : squares){
			diamonds.add(new Vector2((int)(v.x)-dist,(int)(v.y)));
			diamonds.add(new Vector2((int)(v.x)+dist,(int)(v.y)));
			diamonds.add(new Vector2((int)(v.x),(int)(v.y)-dist));
			diamonds.add(new Vector2((int)(v.x),(int)(v.y)+dist));
		}
		for(Vector2 v : diamonds){
			int ans=0;
			try{
				ans += get((int)(v.x)-dist,(int)(v.y)) ;
			} catch (IllegalArgumentException e){
				
			}
			try {
				ans += get((int)(v.x)+dist,(int)(v.y)) ;
			} catch (IllegalArgumentException e){
				
			}
			try {
				ans += get((int)(v.x),(int)(v.y)-dist) ;
			} catch (IllegalArgumentException e){
				
			}
			try {
				ans += get((int)(v.x),(int)(v.y)+dist) ;
			} catch (IllegalArgumentException e){
				
			}
			set((int)(v.x),(int)(v.y),(ans)/4 + prevRandom) ;
			prevRandom = r.nextInt(prevRandom) ;
		}
	}
	
	private DiamondSquareMatrix diamondSquareRec(int nstep){
		if (nstep == (exponent - 1) ){
			return this ;
		}
		squareStep(diamondStep(nstep),nstep);
		return diamondSquareRec(nstep+1);
	}
	
	public String toString(){
		String s = "";
		for (List<Integer> l : matrix){
			s+=l.toString()+"\n" ;
		}
		return s ;
	}
}
