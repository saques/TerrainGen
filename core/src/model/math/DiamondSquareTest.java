package model.math;

public class DiamondSquareTest {

	public static void main(String[] args) {
		DiamondSquareMatrix m = new DiamondSquareMatrix(4);
		m.diamondSquare(56, 446, 546, 45);
		System.out.println(m.toString());
	}

}
