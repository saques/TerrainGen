package com.model.math;

import com.model.map.Chunk;

public class DiamondSquareTest {

	public static void main(String[] args) {
		DiamondSquareMatrix m = new DiamondSquareMatrix(5);
		m.diamondSquare(97, 25, 48, 13);
		Matrix<Chunk> a = m.splitIntoChunks(2);
		for (int i=0;i<a.dimX();i++){
			for(int j=0;j<a.dimY();j++){
				System.out.println(a.get(i, j).getAdded());
			}
		}
	}

}
