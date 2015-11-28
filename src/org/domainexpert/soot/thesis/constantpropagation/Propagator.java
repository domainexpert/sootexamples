package org.domainexpert.soot.thesis.constantpropagation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.JimpleBody;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.util.Chain;

/**
 * The propagator example from the Vallee-Rai's thesis's constant
 * propagation example.
 * 
 * @author andrew
 *
 */
public class Propagator extends BodyTransformer {

	private static Propagator instance = new Propagator();
	private Propagator() {}
	
	/**
	 * Returns the instance of Propagator class
	 *  
	 * @return the instance of the Propagator class
	 */
	public static Propagator v() {
		return instance;
	}
	
	@Override
	protected void internalTransform(Body b, String phaseName,
			Map<String, String> options) {
		if (soot.options.Options.v().verbose())
			G.v().out.println("[" + b.getMethod().getName() + "] Propagating constants...");

		JimpleBody body = (JimpleBody) b;
		
		Chain<Unit> units = body.getUnits();
		CompleteUnitGraph stmtGraph = new CompleteUnitGraph(body);
	
		LocalDefs localDefs = new SimpleLocalDefs(stmtGraph);
		Iterator<Unit> stmtIt = units.iterator();
		
		while (stmtIt.hasNext()) {
			Unit stmt = stmtIt.next();
			Iterator<ValueBox> useBoxIt = stmt.getUseBoxes().iterator();
			
			while (useBoxIt.hasNext()) {
				ValueBox useBox = useBoxIt.next();
				
				if (useBox.getValue() instanceof Local) {
					Local l = (Local) useBox.getValue();
					List<Unit> defsOfUse = localDefs.getDefsOfAt(l, stmt);
					if (defsOfUse.size() == 1) {
						DefinitionStmt def = (DefinitionStmt)
								defsOfUse.get(0);
						
						if (def.getRightOp() instanceof Constant) {
							if (useBox.canContainValue(def.getRightOp())) {
								Value oldValue = useBox.getValue();
								
								useBox.setValue(def.getRightOp());
								G.v().out.println("Replacement of " +
										oldValue + " with " +
										useBox.getValue() + " in statement " +
										stmt + " of method " + b.getMethod().getName());
							}
						}
					}
				}
			}
		}
	}

}
