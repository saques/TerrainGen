package com.model.math;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;

/**
 * A 3 dimensional Triangle class
 * @author saques
 *
 */
public class Triangle {
	// The three vertices of this triangle
	private Vector3 p1 ;
	private Vector3 p2 ;
	private Vector3 p3 ;
	private Vector3 normal ;
	private float avgHeight;
	
	public Triangle(Vector3 p1,Vector3 p2,Vector3 p3){
		this.p1=p1;
		this.p2=p2;
		this.p3=p3;
		Plane p = new Plane(p1,p2,p3);
		this.normal = p.getNormal() ;
		this.avgHeight = (p1.z + p2.z + p3.z)/3 ;
		
	}
	public Triangle (List<Vector3> vertices){
		if (vertices.size() != 3){
			throw new IllegalArgumentException("Wrong number of "
					+ "vertices in the given list");
		}
		this.p1 = vertices.get(0);
		this.p2 = vertices.get(1);
		this.p3 = vertices.get(2);
		avgHeight = (p1.z + p2.z + p3.z)/3 ;
	}
	
	public float avgHeight(){
		return avgHeight;
	}
	
	public Vector3 getp1(){
		return p1;
	}
	public Vector3 getp2(){
		return p2;
	}
	public Vector3 getp3(){
		return p3;
	}
	
	public Vector3 getNormal(){
		return normal ;
	}
	
	public List<Vector3> getVertices(){
		List<Vector3> ans = new ArrayList<Vector3>(3);
		ans.add(p1);
		ans.add(p2);
		ans.add(p3);
		return ans;
	}
	public boolean equals(Object o){
		if (this == o){
			return true ;
		} else if (o == null){
			return false ;
		} else if (!o.getClass().equals(this.getClass())){
			return false ;
		} else {
			Triangle other = (Triangle)o;
			if (p1.equals(other.getp1()) &&
				p2.equals(other.getp2()) &&
				p3.equals(other.getp3())) {
				return true ;
			}
		}
		return false ;
	}
	
	public int hashCode(){
		int hashCode = 1;
		hashCode = 31*hashCode + p1.hashCode();
		hashCode = 31*hashCode + p2.hashCode();
		hashCode = 31*hashCode + p3.hashCode();
		return hashCode;
	}

}
