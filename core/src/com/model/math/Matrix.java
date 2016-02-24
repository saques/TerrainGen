package com.model.math;

import java.util.ArrayList;
import java.util.List;

/**
 * A matrix implementation for the Diamond Square
 * Algorithm
 * @author asaques
 *
 */
public abstract class Matrix {
	private List<List<Integer>> matrix ;
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
		matrix = new ArrayList<List<Integer>>(dimX);
		this.dimX = dimX ;
		this.dimY = dimY ;
		for (int i=0 ; i<dimX ; i++){
			List<Integer> nlist = new ArrayList<Integer>(dimY) ;
			matrix.add(i,nlist);
			for (int j=0;j<dimY; j++){
				nlist.add(0);
			}
		}
	}
	
	public int dimX(){
		return dimX;
	}
	
	public int dimY(){
		return dimY;
	}
	
	public Integer get(int i,int j){
		if (i<0 || i>dimX || j<0 || j>dimY){
			throw new IllegalArgumentException("Wrong indices");
		}
		return matrix.get(i).get(j) ;
	}
	
	public void set(int i,int j,int val){
		if (i<0 || i>dimX || j<0 || j>dimY){
			throw new IllegalArgumentException("Wrong indices");
		}
		matrix.get(i).set(j, val);
	}
	
	public String toString(){
		String s = "";
		for (List<Integer> l : matrix){
			s+=l.toString()+"\n" ;
		}
		return s ;
	}
}
