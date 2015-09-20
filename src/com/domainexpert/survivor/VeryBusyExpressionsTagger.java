package com.domainexpert.survivor;

import soot.BodyTransformer;

public class VeryBusyExpressionsTagger {

	private static BodyTransformer bodyTransformer =
			new VeryBusyExpressionsTransformer();
	
	public static void main(String[] args) {
		
		soot.Main.main(args);
	}

}
