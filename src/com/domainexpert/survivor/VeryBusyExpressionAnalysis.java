package com.domainexpert.survivor;

import soot.EquivTo;
import soot.G;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AddExpr;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.UnopExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

public class VeryBusyExpressionAnalysis
	extends BackwardFlowAnalysis<Unit, FlowSet<Object>> {

	public VeryBusyExpressionAnalysis(UnitGraph graph) {
		super(graph);
		doAnalysis();
	}

	@Override
	protected void flowThrough(FlowSet<Object> in, Unit node, FlowSet<Object> out) {
		Unit u = node;
		kill(in, u, out);
		gen(out, u);		
	}

	private void gen(FlowSet<Object> outSet, Unit u) {		
		G.v().out.println("Executing gen on unit " + u.toString());
		G.v().out.println("Type: " + u.getClass().getName());
		G.v().out.flush();
		
		if (u instanceof AssignStmt) {
			Value v = ((AssignStmt) u).getRightOp();
			if (v instanceof BinopExpr) {
				Value opValue1 = ((BinopExpr) v).getOp1();
				Value opValue2 = ((BinopExpr) v).getOp2();
				G.v().out.println("Binary operand types: " + opValue1.getClass().getName()
						+ ", " + opValue2.getClass().getName());
			} else if (v instanceof UnopExpr) {
				Value opValue = ((UnopExpr) v).getOp();
				G.v().out.println("Unary operand type: " + opValue.getClass().getName());
			}
		}
	}

	private void kill(FlowSet<Object> inSet, Unit u, FlowSet<Object> outSet) {
		G.v().out.println("Executing kill on unit " + u.toString());
		G.v().out.flush();
	}

	@Override
	protected void copy(FlowSet<Object> source, FlowSet<Object> dest) {
		source.copy(dest);
	}

	@Override
	protected FlowSet<Object> entryInitialFlow() {
		return new ValueArraySparseSet<Object>();
	}


	@Override
	protected void merge(FlowSet<Object> in1, FlowSet<Object> in2, FlowSet<Object> out) {
		in1.intersection(in2, out);
	}

	@Override
	protected FlowSet<Object> newInitialFlow() {
		return new ValueArraySparseSet<Object>();
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
