package org.domainexpert.survivor;

import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Expr;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

/**
 * Sample very busy expressions dataflow analysis implemented using Soot.
 * This is part of the complete code for Section 5 of "A Survivor's Guide
 * to Java Program Analysis in Soot"
 * 
 * A very busy expression at a control point is an expression that will
 * be evaluated later without definition of its arguments from the current
 * control point to the point of its evaluation. This is useful, e.g., for
 * code motion to move expressions to before a loop such that they need not
 * be evaluated at every iteration.
 * 
 * A very busy expressions analysis is a backward analysis collecting
 * expressions encountered and remove them from the flow whenever any of
 * its arguments is defined.
 * 
 * @author andrew
 *
 */
public class VeryBusyExpressionsMain {
	private static String CLASS_NAME =
			"com.domainexpert.survivor.test.VeryBusyExpressionTest";
	private static String METHOD_NAME = "main";
	
	/**
	 * This executes the analysis and outputs the IN/OUT set
	 * for every statement (Jimple unit). Note that very busy
	 * expressions is a backward analysis.
	 * 
	 * @param args the command-line arguments for Soot
	 */
	public static void main(String[] args) {

		soot.options.Options.v().parse(args);
		
		// Load all the necessary classes based on the options
		Scene.v().loadNecessaryClasses();

		// Set up the class we�re working with
		SootClass c = Scene.v().loadClassAndSupport(CLASS_NAME);
		c.setApplicationClass();
		
		// Retrieve the method and its body
		SootMethod m = c.getMethodByName(METHOD_NAME);
		Body b = m.retrieveActiveBody();

		// Build the CFG and run the analysis
		UnitGraph g = new ExceptionalUnitGraph(b);
		VeryBusyExpressions an = new SimpleVeryBusyExpressions(g);

		// Iterate over the results
		Iterator<Unit> i = g.iterator();
		while (i.hasNext()) {
			Unit u = i.next();
			List<Expr> OUT = an.getBusyExpressionsBefore(u);
			List<Expr> IN = an.getBusyExpressionsAfter(u);
			
			// Do something clever with the results
			G.v().out.println("\nUnit: " + u.toString());
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
