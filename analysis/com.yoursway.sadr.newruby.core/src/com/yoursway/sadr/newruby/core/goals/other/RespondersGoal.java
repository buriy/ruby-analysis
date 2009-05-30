package com.yoursway.sadr.newruby.core.goals.other;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.goals.atomic.EnclosingCallable;
import com.yoursway.sadr.newruby.core.goals.atomic.GlobalDeclsWith;
import com.yoursway.sadr.newruby.core.goals.type.MethodCallTypeGoal;
import com.yoursway.sadr.newruby.core.goals.type.SelfTypeGoal;
import com.yoursway.sadr.newruby.core.goals.type.TypeGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.RubyBlock;
import com.yoursway.sadr.newruby.core.ir.YieldCall;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class RespondersGoal extends AbstractGoal<List<Callable>> {

	private final Call call;

	public RespondersGoal(Call call) {
		this.call = call;
		result = new ArrayList<Callable>();
	}
	
	@Override
	protected void evaluate() { // result is list of method defs or closures
		result = new ArrayList<Callable>();
		if(call instanceof MethodCall){
			MethodCall methodCall = (MethodCall) call;
			TypeDescription rt = resultOf(TypeGoal.create(methodCall.receiver()));
			List<Callable> resp = resultOf(new GlobalDeclsWith(methodCall.name()));
			for(Callable r: resp){
				if(rt.hasNonEmptyIntersectionWith(resultOf(new SelfTypeGoal(r))))
					result.add(r);
			}
		}else if(call instanceof YieldCall){
			Callable callable = resultOf(new EnclosingCallable((YieldCall)call));
			List<Call> callers = resultOf(new CallersGoal(callable));
			for(Call c: callers){
				RubyBlock lastArg = c.passedBlock();
				if(lastArg != null){
					result.add(lastArg);
				}
			}
		}
	}
//			if (call is method call){
//			    rt = resultOf(Type(call.receiver))
//			    resp = resultOf(GlobalDeclsWith(call.name))
//			    for r in resp do
//			        if rt.hasNonEmptyIntersectionWith(r.declaredType)
//			            result.add(r)
//			}
//
//			if call is yield
//			    callers = resultOf(Callers(call))
//			    for c in callers do
//			        lastArg = c.lastArg
//			        if lastArg is Block
//			            result.add(lastArg)
//			            }

}
