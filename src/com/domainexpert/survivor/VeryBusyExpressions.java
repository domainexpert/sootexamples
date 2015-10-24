package com.domainexpert.survivor;

import java.util.List;

import soot.Unit;
import soot.jimple.Expr;

public interface VeryBusyExpressions {

	public List<Expr> getBusyExpressionsBefore(Unit s);
	
	public List<Expr> getBusyExpressionsAfter(Unit s);
}
