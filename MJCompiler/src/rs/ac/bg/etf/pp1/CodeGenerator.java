package rs.ac.bg.etf.pp1;

import java.util.Iterator;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AddTermListing;
import rs.ac.bg.etf.pp1.ast.AddTermSolo;
import rs.ac.bg.etf.pp1.ast.Assignment;
import rs.ac.bg.etf.pp1.ast.BoolInit;
import rs.ac.bg.etf.pp1.ast.CharInit;
import rs.ac.bg.etf.pp1.ast.Const;
import rs.ac.bg.etf.pp1.ast.DesStmtActPars;
import rs.ac.bg.etf.pp1.ast.DesStmtExpr;
import rs.ac.bg.etf.pp1.ast.DesStmtIncr;
import rs.ac.bg.etf.pp1.ast.DesignListOnlyExpr;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DestStmtDecr;
import rs.ac.bg.etf.pp1.ast.DivOp;
import rs.ac.bg.etf.pp1.ast.DotIdent;
import rs.ac.bg.etf.pp1.ast.FactorListing;
import rs.ac.bg.etf.pp1.ast.FactorOnlyExpr;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.IntInit;
import rs.ac.bg.etf.pp1.ast.LBracket;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTName;
import rs.ac.bg.etf.pp1.ast.MethodVoidName;
import rs.ac.bg.etf.pp1.ast.Minoper;
import rs.ac.bg.etf.pp1.ast.MinusTerm;
import rs.ac.bg.etf.pp1.ast.ModOp;
import rs.ac.bg.etf.pp1.ast.Mulop;
import rs.ac.bg.etf.pp1.ast.Mulopp;
import rs.ac.bg.etf.pp1.ast.NewNoExpr;
import rs.ac.bg.etf.pp1.ast.NewWExpr;
import rs.ac.bg.etf.pp1.ast.NoDesignAdds;
import rs.ac.bg.etf.pp1.ast.PrintStmt;
import rs.ac.bg.etf.pp1.ast.RBracket;
import rs.ac.bg.etf.pp1.ast.SingleFormalParamDecl;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Var;
import rs.ac.bg.etf.pp1.ast.VarName;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int varCount;

	private int paramCnt;

	private int mainPc;

	private boolean indexing = false;

	public int getMainPc() {
		return mainPc;
	}

	// public int nVars(Obj o) {
	// int n = 0;
	// Iterator<Obj> iter = o.getLocalSymbols().iterator();
	// for (int size = o.getLevel();n < size; n++) {
	// iter.next();
	// }
	//
	// return n;
	// }
	//
	// public void visit(Program program) {
	// Code.dataSize=nVars(program.getProgName().obj);
	// }

	public void visit(MethodVoidName methVoidName) {
		if ("main".equalsIgnoreCase(methVoidName.getMethName()))
			mainPc = Code.pc;
		// Obj oMeth = Tab.find((methVoidName).getMethName());
		Obj oMeth = methVoidName.obj;
		oMeth.setAdr(Code.pc);

		// Collect arguments and local variables.
		SyntaxNode methodNode = methVoidName.getParent().getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);

		// Generate the entry.
		Code.put(Code.enter);
		Code.put(oMeth.getLevel());
		// Code.put(fpCnt.getCount());
		//Code.put(varCnt.getCount() + fpCnt.getCount());
		Code.put(methVoidName.obj.getLocalSymbols().size()-oMeth.getLevel());
	}

	@Override
	public void visit(MethodTName methodTName) {
		// Obj oMeth = Tab.find(methodTName.getMethName());
		Obj oMeth = methodTName.obj;
		oMeth.setAdr(Code.pc);

		// Collect arguments and local variables.
		SyntaxNode methodNode = methodTName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);

		// Generate the entry.
		Code.put(Code.enter);
		Code.put(oMeth.getLevel());
		// Code.put(fpCnt.getCount());
		//Code.put(varCnt.getCount() + fpCnt.getCount());
		Code.put(methodTName.obj.getLocalSymbols().size()-oMeth.getLevel());
	}

	@Override
	public void visit(VarName VarDecl) {
		varCount++;
	}

	@Override
	public void visit(SingleFormalParamDecl FormalParam) {
		paramCnt++;
	}

	@Override
	public void visit(MethodDecl MethodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	// @Override
	// public void visit(ReturnExpr ReturnExpr) {
	// Code.put(Code.exit);
	// Code.put(Code.return_);
	// }
	//
	// @Override
	// public void visit(ReturnNoExpr ReturnNoExpr) {
	// Code.put(Code.exit);
	// Code.put(Code.return_);
	// }

	// @Override
	// public void visit(Assignment Assignment) {
	// if (Assignment.getDesignatorStatement() instanceof DesStmtActPars) {
	// Code.store((((DesStmtActPars)
	// Assignment.getDesignatorStatement()).getDesignator().obj));
	// }
	// }

	@Override
	public void visit(Const Const) {
		// if(!indexing) {
		if (Const.getRhs() instanceof IntInit) {
			Code.load(new Obj(Obj.Con, "$", Tab.intType, ((IntInit) Const.getRhs()).getNumConst(), 0));
		}

		if (Const.getRhs() instanceof CharInit) {
			Code.load(new Obj(Obj.Con, "$", Tab.charType, ((CharInit) Const.getRhs()).getCharConst(), 0));
		}

		if (Const.getRhs() instanceof BoolInit) {
			Code.load(new Obj(Obj.Con, "$", SemanticPass.boolType, ((BoolInit) Const.getRhs()).getBoolConst() ? 1 : 0,
					0));
		}
		// }
	}

	public void visit(LBracket lBracket) {
		indexing = true;
		// String
		// check=((Designator)lBracket.getParent().getParent()).getDesName();
		// test comments
		// Obj
		// lbrObj=Tab.find(((Designator)lBracket.getParent().getParent()).getDesName());
		// nije radilo zbog tab find
		Obj desObj = ((Designator) lBracket.getParent().getParent()).obj;
		if (desObj.getType().getKind() == Struct.Array)
			Code.load(desObj);
		
	}

	public void visit(RBracket rBracket) {
		indexing = false;
	}

	// @Override
	 public void visit(Designator designator) {
		 if (designator.obj.getType().getKind() == Struct.Array) {
				if (designator.getDesignAdds() instanceof DesignListOnlyExpr) {
					designator.obj = new Obj(Obj.Elem, "$",
							((DesignListOnlyExpr) designator.getDesignAdds()).getExpr().struct);
				}
			}
	 }

//	public void visit(FactorOnlyExpr factorOnlyExpr) {
//		factorOnlyExpr.struct = factorOnlyExpr.getExpr().struct;
//	}

	@Override
	public void visit(FuncCall FuncCall) {
		Obj functionObj = FuncCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}

	@Override
	public void visit(PrintStmt PrintStmt) {
		if(PrintStmt.getExpr().struct==Tab.intType){
			Code.put(Code.const_5);
			Code.put(Code.print);
		}else{
			Code.put(Code.const_5);
			Code.put(Code.bprint);
		}
	}

	public void visit(AddTermListing addTermListing) {
		if (addTermListing.getAddop() instanceof Minoper) {
			Code.put(Code.neg);
		}
		Code.put(Code.add);
	}

	public void visit(AddTermSolo addTermSolo) {
		if (addTermSolo.getAddop() instanceof Minoper) {
			Code.put(Code.neg);
		}
		Code.put(Code.add);
	}

	public void visit(MinusTerm minusTerm) {
		Code.put(Code.neg);
	}
	
	public void visit(FactorListing factorListing) { // za mul
		Mulop mul = factorListing.getMulop();
		if (mul instanceof Mulopp) {
			Code.put(Code.mul);
		}
		if (mul instanceof DivOp) {
			Code.put(Code.div);
		}
		if (mul instanceof ModOp) {
			Code.put(Code.rem);
		}
	}

	public void visit(Var var) {
		if (var.getDesignator().getDesignAdds() instanceof DesignListOnlyExpr
				|| var.getDesignator().getDesignAdds() instanceof DotIdent) {
			Code.load(var.getDesignator().getDesignAdds().obj);
		} else
			Code.load(var.getDesignator().obj);
	}

	//////////////////// DestSTMTSbegin

	public void visit(DesStmtIncr desStmtIncr) {
		// if(desStmtIncr.getDesignator().getDesignAdds().obj!=null)
		if (desStmtIncr.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
			// Code.load(desStmtIncr.getDesignator().getDesignAdds().obj);
		}
		// else
		Code.load(desStmtIncr.getDesignator().obj);
		Code.load(new Obj(Obj.Con, "Incr", Tab.intType, 1, 0));
		Code.put(Code.add);
		Code.store(desStmtIncr.getDesignator().obj);
	}

	public void visit(DestStmtDecr destStmtDecr) {

		if (destStmtDecr.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}

		Code.load(destStmtDecr.getDesignator().obj);
		Code.load(new Obj(Obj.Con, "Decr", Tab.intType, 1, 0));
		Code.put(Code.sub);
		Code.store(destStmtDecr.getDesignator().obj);
	}

	public void visit(DesStmtExpr destStmtExpr) {
		if (destStmtExpr.getDesignator().getDesignAdds() instanceof NoDesignAdds)
			Code.store(destStmtExpr.getDesignator().obj);
		else
			Code.store(destStmtExpr.getDesignator().getDesignAdds().obj);
	}

	public void visit(DesStmtActPars desStmtActPars) {
//		if (!(desStmtActPars.getParent() instanceof Assignment))
//			Code.store(desStmtActPars.getDesignator().obj);
//		else {
		
			Obj functionObj = desStmtActPars.getDesignator().obj;
			int offset = functionObj.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
			if (desStmtActPars.getDesignator().obj.getType() != Tab.noType)
	        {
	            Code.put(Code.pop);
	        }
//		}
	}

	// public void visit(DotIdent dotIdent) {
	// Code.load(dotIdent.obj);
	// }

	//////////////////// DestSTMTSend

	//////////////////// NEWBEGIN

	public void visit(NewWExpr newWExpr) {
		Code.put(Code.newarray);
		if (newWExpr.getType().struct == Tab.charType) {
			Code.put(0);
		} else
			Code.put(1);
	}

	public void visit(NewNoExpr newNoExpr) {
		Code.put(Code.newarray);
		if (newNoExpr.getType().struct == Tab.charType) {
			Code.put(0);
		} else
			Code.put(1);
	}

	//////////////////// NEWEND

}
