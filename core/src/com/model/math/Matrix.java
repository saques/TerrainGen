package com.model.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.naming.OperationNotSupportedException;

/**
 * A matrix implementation for the Diamond Square
 * Algorithm
 * @author asaques
 *
 */
public class Matrix<N> implements Iterable<N> {
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
		if (outOfBounds(i, j)){
			throw new IllegalArgumentException("Wrong indices");
		}
		return matrix.get(i).get(j) ;
	}
	
	protected void set(int i,int j,N val){
		if (outOfBounds(i, j)){
			throw new IllegalArgumentException("Wrong indices");
		}
		matrix.get(i).set(j, val);
	}
	
	protected boolean outOfBounds(int i,int j){
		if (i<0 || i>=dimX || j<0 || j>=dimY) {
			return true ;
		}
		return false ;
	}
	
	public String toString(){
		String s = "";
		for (List<N> l : matrix){
			s+=l.toString()+"\n" ;
		}
		return s ;
	}

	@Override
	public Iterator<N> iterator() {
		return new MatrixIterator(this.matrix);
	}
	
	private class MatrixIterator implements Iterator<N>{
		
		private List<Iterator<N>> iterators ;
		private int size ;
		private int current ;
		public MatrixIterator(List<List<N>> matrix) {
			iterators = new ArrayList<Iterator<N>>();
			for (List<N> l : matrix){
				iterators.add(l.iterator());
			}
			this.size = matrix.size();
			this.current = 0;
		}
		
		@Override
		public boolean hasNext() {
			if (current>=size){
				return false ;
			} else if (!iterators.get(current).hasNext()) {
				current++;
				return hasNext();
			}
			return iterators.get(current).hasNext();
		}

		@Override
		public N next() {
			if (!hasNext()){
				throw new NoSuchElementException();
			}
			return iterators.get(current).next();
		}

		@Override
		public void remove() {
			try {
				throw new OperationNotSupportedException();
			} catch (OperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
