package com.domainexpert.survivor;

import java.util.List;

import soot.Unit;

public interface VeryBusyExpressions {

	public List<Object> getBusyExpressionsBefore(Unit s);
	
	public List<Object> getBusyExpressionsAfter(Unit s);
}
