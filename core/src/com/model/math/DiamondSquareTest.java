package com.model.math;

import java.util.List;
import com.model.map.Chunk;

public class DiamondSquareTest {

	public static void main(String[] args) {
		DiamondSquareMatrix m = new DiamondSquareMatrix(5);
		m.diamondSquare(97, 25, 48, 13);
		List<Chunk> a = m.splitIntoChunks(1);
		for (Chunk c:a){
			System.out.println(c.getAdded());
		}
	}

}
