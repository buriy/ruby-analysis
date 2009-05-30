package com.yoursway.sadr.newruby.core.goals.other;

import java.util.Collection;

import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.goals.patterns.INodePattern;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.ddp.AbstractGoal;
import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.goals.atomic.Defs;
import com.yoursway.sadr.newruby.core.goals.atomic.EnclosingCallable;
import com.yoursway.sadr.newruby.core.goals.atomic.GlobalUsagesOf;
import com.yoursway.sadr.newruby.core.goals.atomic.InternalArgument;
import com.yoursway.sadr.newruby.core.goals.patterns.INodePattern;
import com.yoursway.sadr.newruby.core.goals.type.SelfTypeGoal;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.ClassVarReference;
import com.yoursway.sadr.newruby.core.ir.InstanceVarReference;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Callable;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.ReturnInstruction;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class UsagesGoal extends AbstractGoal<Collection<CFGNode>>{
	private final Assignment def;
	private final INodePattern pattern;
	
	public UsagesGoal(Assignment def, INodePattern pattern){
		this.def = def;
		this.pattern = pattern;
		result = new ArrayList<CFGNode>();
	}
	
	public void evaluate(){
		result = new ArrayList<CFGNode>();
		VariableReference var = def.lhs();
		if (var instanceof LocalVarReference) {
//			if def.lhs is localvar {
//		    for d in resultOf(Defs(def.lhs)) do
//		        if pattern.matches(d)
//		            result.add(d)
			for (Assignment d: resultOf(new Defs((LocalVarReference) var))) {
				if(pattern.matches(d)){
					result.add(d);
				}
			}
		} else {
//		    usages = resultOf(GlobalUsagesOf(def.lhs.name))
//		    if def.lhs is instance or class var
//	        selfType = resultOf(SelfType(def))
//		    for u in usages do
//		        s = resultOf(SelfType(u))
//		        if !s.hasNonEmptyIntersectionWith(selfType)
//		            remove u from usages
		    List<CFGNode> usages = resultOf(new GlobalUsagesOf(var.name()));
		    TypeDescription selfType = null;
		    
		    if(var instanceof ClassVarReference ||var instanceof InstanceVarReference){
		    	selfType = resultOf(new SelfTypeGoal(def));
		    }
		    for (CFGNode u: usages) {
		    	if (selfType != null){
			    	TypeDescription s = resultOf(new SelfTypeGoal(u));
			    	if (!s.hasNonEmptyIntersectionWith(selfType))
			    		//? remove u from usages
			    		continue;
		    	}
//			    for u in usages do        
//			        if pattern.matches
//			            result.add(u)
//			        if u is assignment from def.lhs to another var
//			            result.addAll(resultOf(Usages(u, pattern)))
		    	if (pattern.matches(u)){
		    		result.add(u);
		    	}
		    	if (u instanceof Assignment){
					Assignment assignment = (Assignment) u;
					if(assignment.lhs().equals(var)){
						result.addAll(resultOf(new UsagesGoal(assignment, pattern)));
					}
		    	}
		    	if (u instanceof Call){
//			        if u is call and passed as argument
//		            resp = resultOf(Responders(u))
//		            for r in resp do
//		                ass = resultOf(InternalArg(argNo(u), r))
//		                u2 = resultOf(Usages(ass, pattern))
//		                result.addAll(u2)
					Call call = (Call) u;
		    		if(call.hasArgument(var)){
		    			int i=0;
		    			for(VariableReference arg: call.arguments()){
		    				if(arg.equals(var)){
			    				List<Callable> resp = resultOf(new RespondersGoal(call));
			    				for(Callable r: resp){
			    					Assignment ass = resultOf(new InternalArgument(call, i, r));
			    					Collection<CFGNode> u2 = resultOf(new UsagesGoal(ass, pattern));
			    					result.addAll(u2);
			    				}
		    				}
		    				i++;
		    			}
		    		}
		    	}
		    	if (u instanceof ReturnInstruction){
//			        if u returns
//		            calls = resultOf(Callers(u))
//		            for c in calls do
//		                u2 = resultOf(Usages(c, pattern)) // that call is a def of another var
//		                result.addAll(u2)
		    		ReturnInstruction returned = ((ReturnInstruction) u);
		    		Callable callable = resultOf(new EnclosingCallable(returned));
		    		List<Call> calls = resultOf(new CallersGoal(callable));
		    		
		    		for(Call c: calls){
		    			Collection<CFGNode> u2 = resultOf(new UsagesGoal(c.parentAssignment(), pattern));
		    			result.addAll(u2);
		    		}
		    	}
		    }
		    
	    }
		}
	
	
	
}
