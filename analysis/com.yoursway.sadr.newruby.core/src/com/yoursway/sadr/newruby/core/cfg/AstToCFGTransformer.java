package com.yoursway.sadr.newruby.core.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jruby.ast.ArgsNode;
import org.jruby.ast.ArgumentNode;
import org.jruby.ast.ArrayNode;
import org.jruby.ast.AssignableNode;
import org.jruby.ast.BackRefNode;
import org.jruby.ast.BignumNode;
import org.jruby.ast.BlockArgNode;
import org.jruby.ast.BlockNode;
import org.jruby.ast.BreakNode;
import org.jruby.ast.CallNode;
import org.jruby.ast.ClassNode;
import org.jruby.ast.ClassVarAsgnNode;
import org.jruby.ast.ClassVarDeclNode;
import org.jruby.ast.ClassVarNode;
import org.jruby.ast.Colon2ConstNode;
import org.jruby.ast.Colon2ImplicitNode;
import org.jruby.ast.Colon3Node;
import org.jruby.ast.ConstDeclNode;
import org.jruby.ast.ConstNode;
import org.jruby.ast.DAsgnNode;
import org.jruby.ast.DRegexpNode;
import org.jruby.ast.DStrNode;
import org.jruby.ast.DSymbolNode;
import org.jruby.ast.DVarNode;
import org.jruby.ast.DefsNode;
import org.jruby.ast.FalseNode;
import org.jruby.ast.FileNode;
import org.jruby.ast.FixnumNode;
import org.jruby.ast.FloatNode;
import org.jruby.ast.GlobalAsgnNode;
import org.jruby.ast.GlobalVarNode;
import org.jruby.ast.HashNode;
import org.jruby.ast.IfNode;
import org.jruby.ast.InstAsgnNode;
import org.jruby.ast.IterNode;
import org.jruby.ast.ListNode;
import org.jruby.ast.LocalAsgnNode;
import org.jruby.ast.LocalVarNode;
import org.jruby.ast.MethodDefNode;
import org.jruby.ast.ModuleNode;
import org.jruby.ast.NewlineNode;
import org.jruby.ast.NextNode;
import org.jruby.ast.NilNode;
import org.jruby.ast.Node;
import org.jruby.ast.RedoNode;
import org.jruby.ast.RegexpNode;
import org.jruby.ast.ReturnNode;
import org.jruby.ast.RootNode;
import org.jruby.ast.SClassNode;
import org.jruby.ast.SelfNode;
import org.jruby.ast.StrNode;
import org.jruby.ast.SymbolNode;
import org.jruby.ast.TrueNode;
import org.jruby.ast.UntilNode;
import org.jruby.ast.WhileNode;
import org.jruby.ast.ZArrayNode;

import com.yoursway.sadr.newruby.core.ir.BlockArgument;
import com.yoursway.sadr.newruby.core.ir.BlockArgumentReference;
import com.yoursway.sadr.newruby.core.ir.Call;
import com.yoursway.sadr.newruby.core.ir.ClassVarReference;
import com.yoursway.sadr.newruby.core.ir.CodeBlock;
import com.yoursway.sadr.newruby.core.ir.ConstantReference;
import com.yoursway.sadr.newruby.core.ir.GlobalVarReference;
import com.yoursway.sadr.newruby.core.ir.IRRHS;
import com.yoursway.sadr.newruby.core.ir.InstanceVarReference;
import com.yoursway.sadr.newruby.core.ir.Literal;
import com.yoursway.sadr.newruby.core.ir.LocalVarReference;
import com.yoursway.sadr.newruby.core.ir.MethodArguments;
import com.yoursway.sadr.newruby.core.ir.MethodCall;
import com.yoursway.sadr.newruby.core.ir.NamedArgument;
import com.yoursway.sadr.newruby.core.ir.OptionalArgument;
import com.yoursway.sadr.newruby.core.ir.RubyBlock;
import com.yoursway.sadr.newruby.core.ir.RubyFile;
import com.yoursway.sadr.newruby.core.ir.SelfReference;
import com.yoursway.sadr.newruby.core.ir.VarArgument;
import com.yoursway.sadr.newruby.core.ir.VariableReference;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.Assignment;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.BreakInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.IfElseInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.IncludeInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.InstanceMethodDefinition;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.NextInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.RedoInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.RequireInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.ReturnInstruction;
import com.yoursway.sadr.newruby.core.ir.cfgnodes.SingletonMethodDefinition;
import com.yoursway.sadr.newruby.core.util.Graph;
import com.yoursway.sadr.newruby.core.valuesandtypes.ClassValue;
import com.yoursway.sadr.newruby.core.valuesandtypes.ValueSet;

public class AstToCFGTransformer {

	private static int exprNo = 0;
	private final String nameScope;
	private final ConstantReference classRef;
	private final ValueSet selfOverride;
	
	private NBRResolver nbrResolver;

	protected static String nextExprVarName() {
		exprNo++;
		return "sadr_ruby_expr_" + exprNo;
	}

	protected static String returnValueVarName() {
		return "sadr_ruby_return_val";
	}
	
	protected static CFGNode dumbCFGNode() {
		return new CFGNode() {

			public Set<VariableReference> uses() {
				return Collections.emptySet();
			}
			
		};
	}

	public static Graph<CFGNode> fileCFG(RubyFile file, RootNode node) {
		Node bodyNode = node.getBodyNode();

			CFGBuilderImpl builderImpl = new CFGBuilderImpl(new FileEntryNode(file));
			
			ConstantReference cf = ConstantReference.resolved("::Object");
			ValueSet valueSet = new ValueSet(new ClassValue(cf));
			
			AstToCFGTransformer transformer = new AstToCFGTransformer("", cf, valueSet);
			
			if (bodyNode instanceof BlockNode)
				transformer.processBlockNode((BlockNode) bodyNode, builderImpl);
			else 
				transformer.processNode(bodyNode, builderImpl);
			
			builderImpl.appendNode(new FileExitNode(file));
			
			return builderImpl.graph();
			
	}

	public static Graph<CFGNode> blockCFG(AstToCFGTransformer t, IterNode node) {
		Node bodyNode = node.getBodyNode();
		
		return methodCFG(t, bodyNode);
	}

	public static Graph<CFGNode> methodCFG(AstToCFGTransformer t, Node bodyNode) {
		CFGBuilderImpl builderImpl = new CFGBuilderImpl(dumbCFGNode());
		
		AstToCFGTransformer transformer = new AstToCFGTransformer(t.nameScope, t.classRef, t.selfOverride);
		
		if (bodyNode instanceof BlockNode)
			transformer.processBlockNode((BlockNode) bodyNode, builderImpl);
		else 
			transformer.processNode(bodyNode, builderImpl);
		
		builderImpl.appendNode(dumbCFGNode());
		
		return builderImpl.graph();
	}

	public static void classCFG(AstToCFGTransformer t,
			Node bodyNode, CFGBuilder builder, ConstantReference constantReference) {
		String ns;
		if (constantReference.name().startsWith("::"))
			ns = constantReference.name();
		else
			ns = t.nameScope + "::" + constantReference.name();
		
		ValueSet valueSet = new ValueSet(new ClassValue(constantReference));
		
		AstToCFGTransformer transformer = new AstToCFGTransformer(ns, constantReference, valueSet);
		
		if (bodyNode instanceof BlockNode)
			transformer.processBlockNode((BlockNode) bodyNode, builder);
		else 
			transformer.processNode(bodyNode, builder);
	}

	public static void sClassCFG(AstToCFGTransformer astToCFGTransformer,
			Node node, CFGBuilder builder, VariableReference obj) {
	}

	public static void moduleCFG(AstToCFGTransformer t,
			Node bodyNode, CFGBuilder builder, ConstantReference constantReference) {
		String ns;
		if (constantReference.name().startsWith("::"))
			ns = constantReference.name();
		else
			ns = t.nameScope + "::" + constantReference.name();
		
		ValueSet valueSet = new ValueSet(new ClassValue(constantReference));
		
		AstToCFGTransformer transformer = new AstToCFGTransformer(ns, constantReference, valueSet);
		
		if (bodyNode instanceof BlockNode)
			transformer.processBlockNode((BlockNode) bodyNode, builder);
		else 
			transformer.processNode(bodyNode, builder);
	}

	private AstToCFGTransformer(String nameScope, ConstantReference classRef,
			ValueSet selfOverride) {
		this.nameScope = nameScope;
		this.classRef = classRef;
		this.selfOverride = selfOverride;
	}

	protected void processBlockNode(BlockNode node, final CFGBuilder builder) {
		for (Node n : node.childNodes()) {
			processNode(n, builder);
		}
	}

	protected boolean processSpecialCall(Call call, final CFGBuilder builder) {
		if (call instanceof MethodCall) {
			MethodCall methodCall = (MethodCall) call;
			if (methodCall.receiver() == null
					&& methodCall.selector().equals("require")) {
				builder.appendNode(new RequireInstruction(methodCall
						.arguments().get(0)));
				return true;
			}
			if (methodCall.receiver() == null
					&& methodCall.selector().equals("include")) {
				builder.appendNode(new IncludeInstruction(methodCall
						.arguments().get(0)));
				return true;
			}
		}
		return false;
	}

	protected void processNode(Node node, final CFGBuilder builder) {
		if (node instanceof NewlineNode) {
			node = ((NewlineNode) node).getNextNode();
		}
		
		final boolean wasNotExpr[] = new boolean[] { false };
		expression(node, false, true, true, builder, new AERA() {
			@Override
			public void resultIs(Call call) {
				if (!processSpecialCall(call, builder))
					builder.appendNode(new Assignment(new LocalVarReference(
							returnValueVarName()), call));
			}

			@Override
			public void resultIs(Literal literal) {
				builder.appendNode(new Assignment(new LocalVarReference(
						returnValueVarName()), literal));
			}

			@Override
			public void resultIs(VariableReference var) {
				builder.appendNode(new Assignment(new LocalVarReference(
						returnValueVarName()), var));
			}

			@Override
			public void crap() {
				wasNotExpr[0] = true;
			}
		});
		if (!wasNotExpr[0])
			return;

		if (node instanceof AssignableNode) {
			assignment((AssignableNode) node, builder);
		} else if (node instanceof IfNode) {
			ifElse((IfNode) node, builder);
		} else if (node instanceof WhileNode) {
			whileNode((WhileNode) node, builder);
		} else if (node instanceof ReturnNode) {
			returnNode((ReturnNode) node, builder);
		} else if (node instanceof ClassNode) {
			classDef((ClassNode) node, builder);
		} else if (node instanceof SClassNode) {
			sClassDef((ClassNode) node, builder);
		} else if (node instanceof ModuleNode) {
			moduleDef((ModuleNode) node, builder);
		} else if (node instanceof MethodDefNode) {
			methodDef((MethodDefNode) node, builder);
		} else if (node instanceof UntilNode) {
			// TODO
		} else if (node instanceof BreakNode) {
			if (nbrResolver != null) {
				BreakInstruction instruction = new BreakInstruction();
				builder.appendNode(instruction);
				nbrResolver.breakNode(instruction);
			}
		} else if (node instanceof NextNode) {
			if (nbrResolver != null) {
				NextInstruction instruction = new NextInstruction();
				builder.appendNode(instruction);
				nbrResolver.nextNode(instruction);
			}
		} else if (node instanceof RedoNode) {
			if (nbrResolver != null) {
				RedoInstruction instruction = new RedoInstruction();
				builder.appendNode(instruction);
				nbrResolver.redoNode(instruction);
			}
		}
	}

	protected void methodDef(final MethodDefNode node, final CFGBuilder builder) {
		String name = node.getName();
		ArgsNode argsNode = node.getArgsNode();
		Node bodyNode = node.getBodyNode();

		ListNode pre = argsNode.getPre();
		ListNode opt = argsNode.getOptArgs();
		ArgumentNode restArgNode = argsNode.getRestArgNode();
		BlockArgNode block = argsNode.getBlock();

		List<NamedArgument> preArgs = new ArrayList<NamedArgument>();
		for (Node n : pre.childNodes()) {
			if (n instanceof ArgumentNode) {
				ArgumentNode argumentNode = (ArgumentNode) n;
				preArgs.add(new NamedArgument(argumentNode.getName()));
			} else
				throw new AssertionError("unexpected pre argument node: " + n);
		}

		List<OptionalArgument> optArgs = new ArrayList<OptionalArgument>();
		for (Node n : opt.childNodes()) {
			if (n instanceof LocalAsgnNode) {
				// TODO
				optArgs.add(new OptionalArgument(((LocalAsgnNode) n).getName(),
						null, null));
			} else
				throw new AssertionError("unexpected opt argument node: " + n);
		}

		VarArgument varArgument = new VarArgument(restArgNode.getName());
		BlockArgument blockArgument = new BlockArgument(block.getName());

		MethodArguments args = new MethodArguments(preArgs, optArgs,
				varArgument, blockArgument);

		CodeBlock code = new CodeBlock(bodyNode);

		if (bodyNode != null) {
			Graph<CFGNode> methodCFG = methodCFG(this, bodyNode);
			code.cfg().setFrom(methodCFG);
		}

		if (node instanceof DefsNode) {
			DefsNode defsNode = (DefsNode) node;
			Node receiverNode = defsNode.getReceiverNode();
			VariableReference receiverVar = varFromExpr(receiverNode, builder);

			builder.appendNode(new SingletonMethodDefinition(receiverVar, name,
					args, code));
		} else {
			builder.appendNode(new InstanceMethodDefinition(classRef, name,
					args, code));
		}
	}

	protected void moduleDef(final ModuleNode node, final CFGBuilder builder) {
		Colon3Node path = node.getCPath();
		Node bodyNode = node.getBodyNode();

		VariableReference cv = variableReference(path);
		if (!(cv instanceof ConstantReference))
			throw new AssertionError("module name isn't a costant: " + node);

		ConstantReference constantReference = (ConstantReference) cv;

		Assignment ca = new Assignment(cv, new MethodCall(
				ConstantReference.resolved("::Module"), "new", Collections
						.<VariableReference> emptyList(), null));
		builder.appendNode(ca);

		moduleCFG(this, bodyNode, builder, constantReference);
	}

	protected void sClassDef(final ClassNode node, final CFGBuilder builder) {
		// TODO
	}

	protected void classDef(final ClassNode node, final CFGBuilder builder) {
		Colon3Node path = node.getCPath();
		Node superNode = node.getSuperNode();
		Node bodyNode = node.getBodyNode();

		VariableReference cv = variableReference(path);
		if (!(cv instanceof ConstantReference))
			throw new AssertionError("class name isn't a costant: " + node);

		ConstantReference constantReference = (ConstantReference) cv;

		VariableReference superVar = varFromExpr(superNode, builder);

		Assignment ca = new Assignment(cv, new MethodCall(
				ConstantReference.resolved("::Class"), "new", Collections
						.singletonList(superVar), null));
		builder.appendNode(ca);

		classCFG(this, bodyNode, builder, constantReference);
	}

	protected void returnNode(ReturnNode node, final CFGBuilder builder) {
		Node valueNode = node.getValueNode();

		VariableReference var = varFromExpr(valueNode, builder);

		builder.appendNode(new ReturnInstruction(var));
	}

	protected void whileNode(WhileNode node, final CFGBuilder builder) {
		Node conditionNode = node.getConditionNode();
		Node bodyNode = node.getBodyNode();

		int marker = builder.setMarker();

		VariableReference cond = varFromExpr(conditionNode, builder);

		final CFGNode whileStart = builder.marker(marker);
		final Set<CFGNode> lastNodes = new HashSet<CFGNode>();

		final IfElseInstruction ifElseInstruction = new IfElseInstruction(cond);
		lastNodes.add(ifElseInstruction);

		builder.appendNode(ifElseInstruction);

		NBRResolver old = nbrResolver;
		nbrResolver = new NBRResolver() {

			public void breakNode(CFGNode node) {
				lastNodes.add(node);
			}

			public void nextNode(CFGNode node) {
				builder.addEdge(node, whileStart);
			}

			public void redoNode(CFGNode node) {
				builder.addEdge(node, ifElseInstruction);
			}

		};

		processBlockNode((BlockNode) bodyNode, builder);

		builder.bindLastNodesTo(whileStart);

		builder.setLastNodes(lastNodes);

		nbrResolver = old;
	}

	protected void ifElse(IfNode node, final CFGBuilder builder) {
		Node condition = node.getCondition();
		Node thenBody = node.getThenBody();
		Node elseBody = node.getElseBody();

		VariableReference cond = varFromExpr(condition, builder);

		IfElseInstruction ifElseInstruction = new IfElseInstruction(cond);

		processBlockNode((BlockNode) thenBody, builder);

		Set<CFGNode> last1 = builder.lastNodes();

		builder
				.setLastNodes(Collections
						.<CFGNode> singleton(ifElseInstruction));

		processBlockNode((BlockNode) elseBody, builder);

		Set<CFGNode> last2 = builder.lastNodes();

		Set<CFGNode> newLast = new HashSet<CFGNode>();
		newLast.addAll(last1);
		newLast.addAll(last2);

		builder.setLastNodes(newLast);
	}

	protected Assignment assignment(AssignableNode node,
			final CFGBuilder builder) {
		VariableReference lhs = null;
		if (node instanceof ConstDeclNode) {
			lhs = ConstantReference.unresolved(((ConstDeclNode) node).getName(), nameScope);
		} else if (node instanceof LocalAsgnNode) {
			lhs = new LocalVarReference(((LocalAsgnNode) node).getName());
		} else if (node instanceof DAsgnNode) {
			lhs = new BlockArgumentReference(((DAsgnNode) node).getName());
		} else if (node instanceof ClassVarAsgnNode) {
			lhs = new ClassVarReference(((ClassVarAsgnNode) node).getName());
		} else if (node instanceof ClassVarDeclNode) {
			lhs = new ClassVarReference(((ClassVarDeclNode) node).getName());
		} else if (node instanceof InstAsgnNode) {
			lhs = new InstanceVarReference(((InstAsgnNode) node).getName());
		} else if (node instanceof GlobalAsgnNode) {
			lhs = new GlobalVarReference(((GlobalAsgnNode) node).getName());
		}
		if (lhs == null)
			throw new RuntimeException("Failed to transform assignement lhs: "
					+ node);

		final IRRHS rhs[] = new IRRHS[1];
		Node valueNode = node.getValueNode();

		rhsExpression(valueNode, true, true, builder, new AERA() {
			@Override
			public void resultIs(Call call) {
				rhs[0] = call;
			}

			@Override
			public void resultIs(Literal literal) {
				rhs[0] = literal;
			}

			@Override
			public void resultIs(VariableReference var) {
				rhs[0] = var;
			}
		});

		if (rhs[0] == null)
			throw new RuntimeException("Failed to transform assignement rhs: "
					+ node);

		Assignment assignment = new Assignment(lhs, rhs[0]);

		builder.appendNode(assignment);

		return assignment;
	}

	protected VariableReference varFromExpr(Node node, final CFGBuilder builder) {
		final VariableReference res[] = new VariableReference[1];

		expression(node, true, false, false, builder, new AERA() {
			@Override
			public void resultIs(VariableReference var) {
				res[0] = var;
			}
		});

		return res[0];
	}

	protected void rhsExpression(Node node, final boolean canBeCall,
			boolean canBeLiteral, final CFGBuilder builder,
			final ExpressionResultAcceptor acceptor) {
		expression(node, true, canBeCall, canBeLiteral, builder, acceptor);
	}

	protected void expression(Node node, boolean needRHS,
			final boolean canBeCall, boolean canBeLiteral,
			final CFGBuilder builder, final ExpressionResultAcceptor acceptor) {
		VariableReference var = variableReference(node);
		if (var != null) {
			acceptor.resultIs(var);
			return;
		}
		Literal literal = literal(node);
		if (literal != null) {
			if (canBeLiteral) {
				acceptor.resultIs(literal);
				return;
			}
			LocalVarReference lvar = new LocalVarReference(nextExprVarName());
			builder.appendNode(new Assignment(lvar, literal));
			acceptor.resultIs(lvar);
			return;
		}
		if (node instanceof CallNode) {
			call((CallNode) node, builder, new AERA() {
				@Override
				public void resultIs(Call call) {
					if (canBeCall) {
						acceptor.resultIs(call);
					} else {
						LocalVarReference lvar = new LocalVarReference(
								nextExprVarName());
						builder.appendNode(new Assignment(lvar, call));
						acceptor.resultIs(lvar);
					}
				}
			});
			return;
		}

		if (!needRHS)
			return;

		if (node instanceof AssignableNode) {
			Assignment assignment = assignment((AssignableNode) node, builder);
			acceptor.resultIs(assignment.lhs());
			return;
		}
		acceptor.crap();
	}

	protected void call(CallNode node, CFGBuilder builder,
			ExpressionResultAcceptor acceptor) {
		Node receiverNode = node.getReceiverNode();
		String name = node.getName();
		Node argsNode = node.getArgsNode();
		Node iterNode = node.getIterNode();

		final VariableReference receiver[] = new VariableReference[1];
		final List<VariableReference> arguments = new ArrayList<VariableReference>();
		RubyBlock blockArg = null;

		rhsExpression(receiverNode, false, false, builder, new AERA() {
			@Override
			public void resultIs(VariableReference var) {
				receiver[0] = var;
			}
		});

		if (argsNode instanceof ArrayNode) {
			ArrayNode arrayNode = (ArrayNode) argsNode;
			int size = arrayNode.size();
			for (int i = 0; i < size; i++) {
				Node a = arrayNode.get(i);
				rhsExpression(a, false, false, builder, new AERA() {
					@Override
					public void resultIs(VariableReference var) {
						arguments.add(var);
					}
				});
			}
		}

		if (iterNode instanceof IterNode) {
			// TODO: BLLOCK ARGS
			blockArg = blockArg((IterNode) iterNode);
		}

		acceptor
				.resultIs(new MethodCall(receiver[0], name, arguments, blockArg));
	}

	protected RubyBlock blockArg(IterNode iterNode) {
		Graph<CFGNode> blockCFG = blockCFG(this, iterNode);
		return new RubyBlock(iterNode, blockCFG);
	}

	protected VariableReference variableReference(Node n) {
		if (n instanceof BackRefNode) {
			return new GlobalVarReference("$backref");
		} else if (n instanceof FileNode) {
			return new GlobalVarReference("__FILE__");
		} else if (n instanceof GlobalVarNode) {
			return new GlobalVarReference(((GlobalVarNode) n).getName());
		} else if (n instanceof ClassVarNode) {
			return new ClassVarReference(((ClassVarNode) n).getName());
		} else if (n instanceof InstAsgnNode) {
			return new InstanceVarReference(((InstAsgnNode) n).getName());
		} else if (n instanceof LocalVarNode) {
			return new LocalVarReference(((LocalVarNode) n).getName());
		} else if (n instanceof SelfNode) {
			SelfReference selfReference = new SelfReference();
			selfReference.setValue(selfOverride);
			return selfReference;
		} else if (n instanceof DVarNode) {
			return new BlockArgumentReference(((DVarNode) n).getName());
		} else if (n instanceof Colon2ImplicitNode) { // BAr::
			return ConstantReference.unresolved(((Colon2ImplicitNode) n).getName(), nameScope);
		} else if (n instanceof Colon2ConstNode) { // Smt::BAr
			return null; // TODO
		} else if (n instanceof Colon3Node) { // ::Foo
			return ConstantReference.resolved("::" + ((Colon3Node) n).getName());
		} else if (n instanceof ConstNode) { // Fooo
			return ConstantReference.unresolved(((ConstNode) n).getName(), nameScope);
		} 
		return null;
	}

	protected Literal literal(Node node) {
		if (node instanceof ArrayNode) {
			return new Literal(node);
		} else if (node instanceof BignumNode) {
			return new Literal(node);
		} else if (node instanceof DStrNode) {
			return new Literal(node);
		} else if (node instanceof DSymbolNode) {
			return new Literal(node);
		} else if (node instanceof TrueNode) {
			return new Literal(node);
		} else if (node instanceof FalseNode) {
			return new Literal(node);
		} else if (node instanceof FixnumNode) {
			return new Literal(node);
		} else if (node instanceof FloatNode) {
			return new Literal(node);
		} else if (node instanceof HashNode) {
			return new Literal(node);
		} else if (node instanceof NilNode) {
			return new Literal(node);
		} else if (node instanceof StrNode) {
			return new Literal(node);
		} else if (node instanceof SymbolNode) {
			return new Literal(node);
		} else if (node instanceof ZArrayNode) {
			return new Literal(node);
		} else if (node instanceof DRegexpNode) {
			return new Literal(node);
		} else if (node instanceof RegexpNode) {
			return new Literal(node);
		}
		return null;
	}

}
