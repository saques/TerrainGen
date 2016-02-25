package com.model.math;

import java.util.ArrayList;
import java.util.List;

/**
 * A matrix implementation for the Diamond Square
 * Algorithm
 * @author asaques
 *
 */
public class Matrix<N> {
	private List<List<N>> matrix ;
	private int dimX ;
	private int dimY ;
	
	/**
	 * Creates a Matrix with the given dimension
	 * @param dimX
	 * @param dimY
	 */
	public Matrix(int dimX,int dimY){
		if (dimX<0 || dimY<0){
			throw new IllegalArgumentException("Illegal dimension");
		}
		matrix = new ArrayList<List<N>>(dimX);
		this.dimX = dimX ;
		this.dimY = dimY ;
		for (int i=0 ; i<dimX ; i++){
			List<N> nlist = new ArrayList<N>(dimY) ;
			matrix.add(i,nlist);
			for (int j=0;j<dimY; j++){
				nlist.add(null);
			}
		}
	}
	
	public int dimX(){
		return dimX;
	}
	
	public int dimY(){
		return dimY;
	}
	
	public N get(int i,int j){
		if (i<0 || i>dimX || j<0 || j>dimY){
			throw new IllegalArgumentException("Wrong indices");
		}
		return matrix.get(i).get(j) ;
	}
	
	public void set(int i,int j,N val){
		if (i<0 || i>dimX || j<0 || j>dimY){
			throw new IllegalArgumentException("Wrong indices");
		}
		matrix.get(i).set(j, val);
	}
	
	public String toString(){
		String s = "";
		for (List<N> l : matrix){
			s+=l.toString()+"\n" ;
		}
		return s ;
	}
}
