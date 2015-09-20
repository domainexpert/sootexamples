package com.domainexpert.survivor;

import soot.EquivTo;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

public class VeryBusyExpressionAnalysis
	extends BackwardFlowAnalysis<Unit, FlowSet<Expr>> {

	public VeryBusyExpressionAnalysis(UnitGraph graph) {
		super(graph);
		doAnalysis();
	}

	@Override
	protected void flowThrough(FlowSet<Expr> in, Unit node, FlowSet<Expr> out) {
		Unit u = node;
		kill(in, u, out);
		gen(out, u);		
	}

	/**
	 * The gen operation of the dataflow
	 * 
	 * @param outSet the output set
	 * @param u the unit
	 */
	private void gen(FlowSet<Expr> outSet, Unit u) {		
		if (u instanceof AssignStmt) {
			Value v = ((AssignStmt) u).getRightOp();
			if (v instanceof Expr) {
				outSet.add((Expr) v);
			}
		}
	}

	/**
	 * The kill operation of the dataflow
	 * 
	 * @param inSet the input set
	 * @param u the unit
	 * @param outSet the output set
	 */
	private void kill(FlowSet<Expr> inSet, Unit u, FlowSet<Expr> outSet) {
		if (u instanceof AssignStmt) {
			Value v = ((AssignStmt) u).getLeftOp();
			for (Expr e: inSet) {
				boolean toBeAdded = true;
				for (soot.ValueBox b : e.getUseBoxes()) {
					if (((EquivTo) v).equivTo(b.getValue())) {
						toBeAdded = false;
						break;
					}
				}
				if (toBeAdded) {
					outSet.add(e);
				}
			}
		} else {
			inSet.copy(outSet);
		}
	}

	@Override
	protected void copy(FlowSet<Expr> source, FlowSet<Expr> dest) {
		source.copy(dest);
	}

	@Override
	protected FlowSet<Expr> entryInitialFlow() {
		return new ValueArraySparseSet<Expr>();
	}


	@Override
	protected void merge(FlowSet<Expr> in1, FlowSet<Expr> in2, FlowSet<Expr> out) {
		in1.intersection(in2, out);
	}

	@Override
	protected FlowSet<Expr> newInitialFlow() {
		return new ValueArraySparseSet<Expr>();
	}

	private class ValueArraySparseSet<T> extends ArraySparseSet<T> {
		@Override
		public boolean contains(Object obj) {
			for (int i = 0; i < numElements; i++) {
				if (elements[i] instanceof EquivTo
						&& ((EquivTo) elements[i]).equivTo(obj))
					return true;
				else if (elements[i].equals(obj))
					return true;
			}
			return false;
		}
	}

}
