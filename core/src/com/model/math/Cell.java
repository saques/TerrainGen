package com.model.math;

import com.badlogic.gdx.math.Vector3;

public class Cell {
	//Top left corner
	private Vector3 p1 ;
	//Top right corner
	private Vector3 p2 ;
	//Bottom left corner
	private Vector3 p3 ;
	//Bottom right corner
	private Vector3 p4 ;
	
	public Cell(Vector3 p1,Vector3 p2,Vector3 p3,Vector3 p4){
		this.p1 = p1 ;
		this.p2 = p2 ;
		this.p3 = p3 ;
		this.p4 = p4 ;
	}

	public Vector3 getp1() {
		return p1;
	}

	public Vector3 getp2() {
		return p2;
	}

	public Vector3 getp3() {
		return p3;
	}

	public Vector3 getp4() {
		return p4;
	}
	
	public Triangle gett1(){
		return new Triangle(p1, p2, p3) ;
	}
	
	public Triangle gett2(){
		return new Triangle(p2,p3,p4) ;
	}
	
	public boolean equals(Object o){
		if (this == o){
			return true ;
		} else if (o == null) {
			return false ;
		} else if (!this.getClass().equals(o.getClass())){
			return false;
		} else {
			Cell c = (Cell)o;
			if (c.getp1().equals(this.p1)&&
				c.getp2().equals(this.p2)&&
				c.getp3().equals(this.p3)&&
				c.getp4().equals(this.p4)) {
				return true ;
			}
		}
		return false ;
	}
	
	public int hashCode(){
		int hashCode = 1 ;
		hashCode *= p1.hashCode() ;
		hashCode *= p2.hashCode() ;
		hashCode *= p3.hashCode() ;
		hashCode *= p4.hashCode() ;
		return hashCode ;
	}
	
}
