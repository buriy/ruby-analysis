package com.yoursway.sadr.newruby.core.ir.cfgnodes;

import com.yoursway.sadr.newruby.core.ir.CodeBlock;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.MethodArguments;

public class ModuleMethodDefinition extends InstanceMethodDefinition {

	public ModuleMethodDefinition(ConstantReference moduleDefinition, String methodName,
			MethodArguments args, CodeBlock code) {
		super(moduleDefinition, methodName, args, code);
	}

}
