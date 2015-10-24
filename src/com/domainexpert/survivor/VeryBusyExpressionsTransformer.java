package com.domainexpert.survivor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.EquivTo;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.Expr;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class VeryBusyExpressionsTransformer 
	extends BodyTransformer {

	public static final String PHASE_NAME = "vbetagger";

	@Override
	protected void internalTransform(Body b, String phaseName,
			Map<String, String> options) {

		// Build the CFG and run the analysis
		UnitGraph g = new ExceptionalUnitGraph(b);
		VeryBusyExpressions an = new SimpleVeryBusyExpressions(g);
		
		// Iterate over the results
		Iterator<Unit> i = g.iterator();
		while (i.hasNext()) {
			Unit ut = i.next();
			List<Expr> veryBusyExps = an.getBusyExpressionsAfter(ut);
			
			for (Expr e: veryBusyExps) {
				ut.addTag(new StringTag("Busy expression: " + e.toString()));
				List<ValueBox> uses = ut.getUseBoxes();
				
				for (ValueBox u: uses) {
					if (((EquivTo) u.getValue()).equivTo(e)) {
						u.addTag(new ColorTag(ColorTag.RED));
					}
				}
			}
		}
	}

}
