package org.domainexpert.survivor;

import soot.PackManager;
import soot.Transform;

public class VeryBusyExpressionsTagger {
	
	public static void main(String[] args) {
		PackManager.v().getPack("jtp").add(new
				Transform("jtp." + VeryBusyExpressionsTransformer.PHASE_NAME,
						new VeryBusyExpressionsTransformer()));

		soot.Main.main(args);
	}

}
