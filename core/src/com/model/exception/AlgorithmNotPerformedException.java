package com.model.exception;

public class AlgorithmNotPerformedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7063794457823567421L;
	
	public AlgorithmNotPerformedException(){
		super("The Diamond Square Algorithm has not "
				+ "been performed yet on this matrix");
	}

}
