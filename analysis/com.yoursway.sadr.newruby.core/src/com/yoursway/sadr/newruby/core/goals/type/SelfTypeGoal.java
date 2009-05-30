package com.yoursway.sadr.newruby.core.goals.type;

import java.util.List;

import com.yoursway.sadr.newruby.core.cfg.CFGNode;
import com.yoursway.sadr.newruby.core.goals.atomic.EnclosingMethod;
import com.yoursway.sadr.newruby.core.goals.atomic.IncludesOf;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.FileLevelCallable;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.IncludeInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.InstanceMethodDefinition;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.MethodDeclaration;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.ModuleMethodDefinition;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.SingletonMethodDefinition;
import com.yoursway.sadr.newruby.core.types.ConcreteClass;
import com.yoursway.sadr.newruby.core.types.InstanceOfContreteClass;
import com.yoursway.sadr.newruby.core.types.TypeDescription;

public class SelfTypeGoal extends TypeGoal {

	private CFGNode node;
	private VariableReference var;

	public SelfTypeGoal(CFGNode node) {
		this.node = node;
		result = new TypeDescription();
	}
	
	public SelfTypeGoal(VariableReference var) {
		this.var = var;
		result = new TypeDescription();
	}
	
	@Override
	protected void evaluate() {
		MethodDeclaration method = null;
		if (var != null)
			method = resultOf(new EnclosingMethod(var));
		else if (node  != null)
			method = resultOf(new EnclosingMethod(node));
		
		if (method == null)
			return;
		
		
		if (method instanceof FileLevelCallable) {
			result = new TypeDescription(new ConcreteClass("Object"));
		} else if (method instanceof InstanceMethodDefinition) {
			InstanceMethodDefinition m = (InstanceMethodDefinition) method;
			ConstantReference klass = m.classDefinition();
			result = new TypeDescription(new InstanceOfContreteClass(new ConcreteClass(klass.name())));			
		} else if (method instanceof ModuleMethodDefinition) {
			ModuleMethodDefinition m = (ModuleMethodDefinition) method;
			ConstantReference module = m.classDefinition();
			List<IncludeInstruction> includes = resultOf(new IncludesOf(module));
			for (IncludeInstruction i : includes) {
				ConstantReference extendedClass = i.extendedClass();
				TypeDescription t = new TypeDescription(new InstanceOfContreteClass(new ConcreteClass(extendedClass.name())));
				result.intersectWith(t);
			}
		} else if (method instanceof SingletonMethodDefinition) {
			SingletonMethodDefinition m = (SingletonMethodDefinition) method;
			VariableReference var = m.extendedObject();
			result = resultOf(TypeGoal.create(var));
		}
	}

}
