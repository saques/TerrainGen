package com.model.map;

import com.model.math.DiamondSquareMatrix;
import com.model.math.Matrix;

public class World {
	private Matrix<Chunk> chunks ;
	private static final int SMOOTHENING_RADIUS = 3 ;
	
	/**
	 * Creates a new map with the given seeds
	 * @param exp The exponent for the base matrix
	 * @param cexp The exponent for chunk division
	 * @param s00 Upper left seed
	 * @param s01 Upper right seed
	 * @param s10 Bottom left seed
	 * @param s11 Bottom right seed
	 */
	public World(int exp,int cexp,int s00,int s01,int s10,int s11){
		DiamondSquareMatrix m = new DiamondSquareMatrix(exp);
		m.diamondSquare(s00, s01, s10, s11);
		chunks = m.splitIntoChunks(cexp,SMOOTHENING_RADIUS) ;
	}
	
	public Matrix<Chunk> getChunks(){
		return chunks;
	}
	
}
