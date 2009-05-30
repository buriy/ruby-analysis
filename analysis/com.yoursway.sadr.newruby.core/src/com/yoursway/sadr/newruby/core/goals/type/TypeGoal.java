package com.yoursway.sadr.newruby.core.goals.type;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.ir.BlockArgumentReference;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.ClassVarReference;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.GlobalVarReference;
import com.yoursway.sadr.newruby.core.ir.IRRHS;
import com.yoursway.sadr.newruby.core.ir.InstanceVarReference;
import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.SelfReference;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.YieldCall;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public abstract class TypeGoal extends AbstractGoal<TypeDescription> {

	// I could get if all this instancesof's but, it would require me to tie
	// goals and IR what I don't want to do

	public static TypeGoal create(IRRHS rhs) {
		if (rhs instanceof Literal)
			return create((Literal) rhs);
		if (rhs instanceof VariableReference)
			return create((VariableReference) rhs);
		if (rhs instanceof Call)
			return create((Call) rhs);
		throw new UnsupportedOperationException("Unknown kind of rhs");
	}

	public static TypeGoal create(Literal literal) {
		return new LiteralTypeGoal(literal);
	}

	public static TypeGoal create(VariableReference var) {
		if (var instanceof SelfReference) {
		
		} else if (var instanceof LocalVarReference) {

		} else if (var instanceof InstanceVarReference
				|| var instanceof ClassVarReference) {

		} else if (var instanceof GlobalVarReference) {

		} else if (var instanceof ConstantReference) {

		} else if (var instanceof BlockArgumentReference) {
			
		}
		throw new UnsupportedOperationException("Unknown kind of variable");
	}

	public static TypeGoal create(Call call) {
		if (call instanceof MethodCall) {

		} else if (call instanceof YieldCall) {

		}
		throw new UnsupportedOperationException(
				"Can't evaluate type of not method or yeild call");
	}
	
}
