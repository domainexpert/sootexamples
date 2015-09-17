package com.domainexpert.survivor;

import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class VeryBusyExpressionsMain {
	public static void main(String[] args) {

		// Set the arguments
		soot.Main.main(args);
		
		// Set up the class we’re working with
		SootClass c = Scene.v().loadClassAndSupport("com.domainexpert.survivor.test.VeryBusyExpressionTest");
		c.setApplicationClass();

		// Retrieve the method and its body
		SootMethod m = c.getMethodByName("main");
		Body b = m.retrieveActiveBody();

		// Build the CFG and run the analysis
		UnitGraph g = new ExceptionalUnitGraph(b);
		VeryBusyExpressions an = new SimpleVeryBusyExpressions(g);

		// Iterate over the results
		Iterator<Unit> i = g.iterator();
		while (i.hasNext()) {
			Unit u = i.next();
			List<Object> IN = an.getBusyExpressionsBefore(u);
			List<Object> OUT = an.getBusyExpressionsAfter(u);
			
			// Do something clever with the results
			G.v().out.println("Result for unit " + u.toString());
			G.v().out.println("IN contains:");
			for (Object item: IN) {
				G.v().out.println("\t" + item.toString());
			}
			G.v().out.println("OUT contains:");
			for (Object item: OUT) {
				G.v().out.println("\t" + item.toString());
			}
		}
	}
}
