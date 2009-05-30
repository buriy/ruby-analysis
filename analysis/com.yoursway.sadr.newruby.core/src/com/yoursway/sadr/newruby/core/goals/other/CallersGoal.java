package com.yoursway.sadr.newruby.core.goals.other;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.goals.atomic.GlobalCallsWith;
import com.yoursway.sadr.newruby.core.goals.atomic.YieldCalls;
import com.yoursway.sadr.newruby.core.goals.type.SelfTypeGoal;
import com.yoursway.sadr.newruby.core.goals.type.TypeGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.RubyBlock;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.MethodDeclaration;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class CallersGoal extends AbstractGoal<List<Call>> {

	private final Callable callable;

	public CallersGoal(Callable callable) {
		this.callable = callable;
		result = new ArrayList<Call>();
	}
	
	@Override
	protected void evaluate() {
		result = new ArrayList<Call>();
		if (callable instanceof MethodDeclaration){
		    MethodDeclaration declaration = (MethodDeclaration) callable;
			TypeDescription selfType = resultOf(new SelfTypeGoal(declaration));
		    List<Call> calls = resultOf(new GlobalCallsWith(declaration.name()));
		    ArrayList<Call> matching = new ArrayList<Call>();
		    for (Call c: calls) {
		        TypeDescription r = resultOf(TypeGoal.create(c));//? c. 
		    	if (r.hasNonEmptyIntersectionWith(selfType)){
			    		//? remove u from usages
		    		matching.add(c);
		    	}
		    	result.addAll(matching);
		    }
		} else if (callable instanceof RubyBlock){
			RubyBlock rubyBlock = (RubyBlock) callable;
			Call parentCall = rubyBlock.parentCall();
		    if("".getClass() == null){
		    	//passed_to_new_Proc_or_Lambda){
//		        Collection<Call> usages = resultOf(new UsagesGoal(parentCall, CallPattern)); // list of Calls
//		        result.addAll(usages);
		    } else {
		        List<Callable> resp = resultOf(new RespondersGoal(parentCall));
		        for (Callable r: resp) {
		        	if(r instanceof MethodDeclaration) {
		        		List<Call> y = resultOf(new YieldCalls((MethodDeclaration) r));
		        		result.addAll(y);
		            }
		        }
		    }
		}
//		scope = resultOf(EnclosingMethodOrClosure(node)) // InstMethDef or SinglMethDef or Block
//		result = []
//		if scope is InstMethDef or SinglMethDef
//		    self = resultOf(SelfType(node))
//		    calls = resultOf(GlobalCallsWith(scope.name))
//		    matching = []
//		    selfType = resultOf(SelfType(node))
//		    for c in calls do
//		        r = resultOf(Type(c.))
//		        if r.hasNonEmptyIntersectionWith(selfType)
//		            matching.add(c)
//		    result.addAll(matching)
//		if scope is Block
//		    parentCall = parentCall(scope)
//		    if passed to new Proc or Lambda
//		        usages = resultOf(Usages(parentCall, CallPattern)) // list of Calls
//		        result.addAll(usages)
//		    else
//		        resp = resultOf(Responders(parentCall))
//		        for r in resp do
//		            y = resultOf(YieldCalls(r))
//		            result.addAll(y)
	}

}
