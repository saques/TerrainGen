package com.model.map;

import java.util.HashSet;
import java.util.Set;
import com.model.exception.CompleteChunkException;
import com.model.exception.RepeatedTriangleException;
import com.model.math.Triangle;

public class Chunk {
	
	private int size ;
	private Set<Triangle> triangles ;
	private int ntriangles ;
	private int added = 0;
	
	public Chunk(int size){
		if (size <=0 || size % 2 != 0){
			throw new IllegalArgumentException("Size must be even "
					+ "and greater than zero");
		}
		this.size = size ;
		//Each slot of a square contains two triangles
		this.ntriangles = (size*size)*2 ;
		this.triangles = new HashSet<Triangle>(ntriangles);
	}
	public void addTriangle(Triangle t){
		if (added == ntriangles){
			throw new CompleteChunkException("This chunk is complete");
		}
		if (triangles.contains(t)){
			throw new RepeatedTriangleException("A chunk cannot contain "
					+ "two identical triangles");
		}
		triangles.add(t);
		added++;
	}
	/**
	 * This method filters the triangles in this Chunk
	 * by height
	 * @param height0 Lower height limit
	 * @param height1 Upper height limit
	 * @return The Triangles that respond to the given heights
	 */
	public Set<Triangle> filter(float height0,float height1){
		if (height0>height1){
			throw new IllegalArgumentException("Invalid heights");
		}
		Set<Triangle> ans = new HashSet<Triangle>() ;
		for (Triangle t:triangles){
			float avgHeight = t.avgHeight();
			if (avgHeight >= height0 && avgHeight < height1){
				ans.add(t);
			}
		}
		return ans ;
	}
	public Set<Triangle> getTriangles(){
		return triangles ;
	}
	public int getSize(){
		return size ;
	}
}
