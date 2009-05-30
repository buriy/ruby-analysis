package com.yoursway.sadr.newruby.core.cfg;

import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.VariableReference;

public interface ExpressionResultAcceptor {

	void resultIs(VariableReference var);
	
	void resultIs(Call call);
	
	void resultIs(Literal literal);

	void crap();
	
}
