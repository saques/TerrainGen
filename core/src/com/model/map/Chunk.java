package com.model.map;

import com.model.exception.CompleteChunkException;
import com.model.exception.RepeatedTriangleException;
import com.model.math.Cell;
import com.model.math.Matrix;

public class Chunk {
	
	private int size ;
	private Matrix<Cell> cells ;
	private int ncells ;
	private int cadded = 0;
	
	public Chunk(int size){
		if (size <=0 || size % 2 != 0){
			throw new IllegalArgumentException("Size must be even "
					+ "and greater than zero");
		}
		this.size = size ;
		this.ncells = size*size ;
		this.cells = new Matrix<Cell>(size);
	}
	
	public void addCell (Cell cell){
		if (cadded == ncells) {
			throw new CompleteChunkException("This chunk is complete");
		}
		if(cells.contains(cell)) {
			throw new RepeatedTriangleException("A chunk cannot contain "
					+ "two identical cells") ;
		}
		cells.set(((int)cell.getp1().x)%size,
				 ((int)cell.getp1().y)%size,cell);
		cadded ++ ;
	}
	
	public Matrix<Cell> getCells(){
		return cells;
	}
	
	public int getAdded(){
		return cadded;
	}
	
}
