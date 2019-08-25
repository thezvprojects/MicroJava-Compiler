package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticPass extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	// private Struct factorListStruct = Tab.noType;
	private Struct var_type = Tab.noType;
	// private Struct termOperStruct = Tab.noType;
	boolean returnFound = false;
	boolean returnVoid = false;

	int nFPars = 0;
	int nVars;
	int varDeclCount = 0;

	ArrayList<Obj> enumMembers = new ArrayList<>();
	int enumCnt = 0;
	int allCnt = 0;
	Stack<Integer> actParsStack = new Stack<Integer>();
	Stack<Struct> curAP = new Stack<Struct>();

	Integer actParsCnt = 0;

	////// za booltype, da li moze ovo?

	static Struct boolType = new Struct(Struct.Bool);
	Struct enumType = new Struct(Struct.Enum);
	Struct arrayType = new Struct(Struct.Array);

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		Tab.insert(Obj.Type, "Boolean", boolType);
		Tab.openScope();
	}

	//////////////// FORMPARS

	public void visit(FormParams formParams) {
		if (currentMethod != Tab.noObj) {
			currentMethod.setLevel(nFPars);
			currentMethod.setLocals(Tab.currentScope.getLocals());

			nFPars = 0;
		} else {
			report_error("Greska sa currentMethod", null);
		}
	};

	public void visit(FormalParamDeclSingle fpd) {

		String varName = fpd.getFName();

		if (Tab.currentScope().findSymbol(varName) != null) {
			report_error("Vec postoji formalni parametar sa tim imenom!", null);
		}

		if (currentMethod.getName() == varName) {
			report_error("Ime parametra isto kao method name!", null);
		}

		Obj obj = Tab.insert(Obj.Var, varName, var_type);
		nFPars++;

	}

	public void visit(FormalParamDeclAr fpda) {

		String varName = fpda.getFName();

		if (Tab.currentScope().findSymbol(varName) != null) {
			report_error("Vec postoji formalni parametar sa tim imenom!", null);
		}

		if (currentMethod.getName() == varName) {
			report_error("Ime parametra isto kao method name!", null);
		}

		Obj obj = Tab.insert(Obj.Var, varName, new Struct(Struct.Array, var_type));
		nFPars++;
	}

	//////////////// FORMPARS END

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
			var_type = type.struct = Tab.noType;
		} else {
			if (typeNode.getType() == enumType) {
				var_type = type.struct = enumType;
			}

			else if (Obj.Type == typeNode.getKind()) {
				if (typeNode.getType().getKind() == Struct.Int)
					var_type = type.struct = Tab.intType;
				if (typeNode.getType().getKind() == Struct.Char)
					var_type = type.struct = Tab.charType;
			} else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
				var_type = type.struct = Tab.noType;
			}
		}
	}

	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName()
					+ " nema return iskaz!", null);
		}
		currentMethod.setFpPos(0);
		// currentMethod.setLevel(0);
		currentMethod.setLevel(currentMethod.getLevel());

		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

		returnFound = false;
		returnVoid = false;
		currentMethod = null;
	}

	public void visit(MethodTName methodTName) {
		currentMethod = Tab.insert(Obj.Meth, methodTName.getMethName(), methodTName.getType().struct);
		methodTName.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTName.getMethName(), methodTName);
		// TODO: set flag if main
		// TODO: check if already exists
	}

	public void visit(MethodVoidName methodVoidName) {
		returnVoid = true;
		currentMethod = Tab.insert(Obj.Meth, methodVoidName.getMethName(), Tab.noType);
		methodVoidName.obj = currentMethod;
		Tab.openScope();
		report_info("Obradjuje se funkcija " + methodVoidName.getMethName(), methodVoidName);
	}

	public void visit(ReturnExpr returnExpr) {
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
			report_error("Greska na liniji " + returnExpr.getLine() + " : "
					+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
					+ currentMethod.getName(), null);
		}
	}

	public void visit(ReturnNoExpr returnNoExpr) {
		if (!returnVoid) {
			report_error(
					"Greska na liniji " + returnNoExpr.getLine() + " : "
							+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije, ocekivan VOID",
					null);
		}
	}

	// public void visit(ProcCall procCall) {
	// Obj func =procCall.getDesignator().obj;
	// if (Obj.Meth == func.getKind()) {
	// report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji "
	// + procCall.getLine(), null); RESULT = func.getType(); }
	// else {
	// report_error("Greska na liniji " + procCall.getLine() + " : ime " +
	// func.getName() + " nije funkcija!", null); RESULT = Tab.noType; } }

	/////////////////////// CONST

	public void visit(ConstDeclFull constDF) {
		var_type = Tab.noType;
	}

	public void visit(ConstPart constPart) {
		String varName = constPart.getConstName();
		Obj varObj = Tab.find(varName);
		if (varObj != Tab.noObj) {
			report_error("Greska na liniji " + constPart.getLine() + ": vec deklarisan var", null);
			return;
		}
		if (var_type == Tab.noType) {
			report_error("Greska na liniji " + constPart.getLine() + ": nedostaje type", null);
			return;
		}
		varDeclCount++;
		varObj = Tab.insert(Obj.Con, varName, var_type);
		if (constPart.getRhs().getClass() == IntInit.class)
			varObj.setAdr(((IntInit) constPart.getRhs()).getNumConst());
	}

	////////////////// RHS

	// public void visit(IntInit intInit) {
	// if (var_type == Tab.noType) {
	// report_error("Greska na liniji " + intInit.getLine() + ": nedostaje
	// type", null);
	// return;
	// }
	//
	// if (var_type != Tab.intType) {
	// report_error("Greska na liniji " + intInit.getLine() + ": pogresan type",
	// null);
	// return;
	// }
	// }
	//
	// public void visit(BoolInit boolInit) {
	// if (var_type == Tab.noType) {
	// report_error("Greska na liniji " + boolInit.getLine() + ": nedostaje
	// type", null);
	// return;
	// }
	//
	// if (var_type != boolType) {
	// report_error("Greska na liniji " + boolInit.getLine() + ": pogresan
	// type", null);
	// return;
	// }
	// }
	//
	// public void visit(CharInit charInit) {
	// if (var_type == Tab.noType) {
	// report_error("Greska na liniji " + charInit.getLine() + ": nedostaje
	// type", null);
	// return;
	// }
	// if (var_type != Tab.charType) {
	// report_error("Greska na liniji " + charInit.getLine() + ": pogresan
	// typewww", null);
	// return;
	// }
	//
	// }

	//////////////////// RHS END

	///////////////////// CONSTEND

	////////////////////// VARS

	public void visit(VarDecl varDecl) {
		var_type = Tab.noType;
	}

	public void visit(DotIdent dotIdent) {
		Obj objenum = Tab.find(((Designator) dotIdent.getParent()).getDesName()).getType().getMembersTable()
				.searchKey(dotIdent.getI1());
		if(objenum==null)
			report_error("Greska, nema trazenog elementa enuma na liniji: "+dotIdent.getLine(),null);
		// ((Designator) dotIdent.getParent()).obj = new Obj(Obj.Con, "$", new
		// Struct(Struct.Int), objenum.getAdr(),
		// objenum.getLevel());
		else
		dotIdent.obj = new Obj(Obj.Con, dotIdent.getI1(), Tab.intType, objenum.getAdr(), objenum.getLevel());

	}

	public void visit(VarId varId) {
		String varName = varId.getVarName().getVarName();
		// Obj varObj = Tab.find(varName);
		Obj varObj = Tab.currentScope.findSymbol(varName);
		// if (varObj != Tab.noObj ) {
		if (varObj != null) {
			report_error("Greska na liniji " + varId.getLine() + ": vec deklarisan var", null);
			return;
		}
		if (var_type == Tab.noType) {
			report_error("Greska na liniji " + varId.getLine() + ": nedostaje type", null);
			return;
		}

		Tab.insert(Obj.Var, varName, var_type);

		varDeclCount++;
	}

	public void visit(VarArrId varArrId) {
		String varName = varArrId.getVarName().getVarName();
		Obj varObj = Tab.find(varName);
		if (varObj != Tab.noObj) {
			report_error("Greska na liniji " + varArrId.getLine() + ": vec deklarisan var", null);
			return;
		}
		if (var_type == Tab.noType) {
			report_error("Greska na liniji " + varArrId.getLine() + ": nedostaje type", null);
			return;
		}
		varDeclCount++;
		Tab.insert(Obj.Var, varName, new Struct(Struct.Array, var_type));

		Obj objdbg = Tab.find(varName);
		report_info("VarArrId deklarisan", null);
	}

	////////////////////// VARSEND
	////////////////////// DESIGNATORS

	public void visit(Designator designator) {
		Obj obj = Tab.find(designator.getDesName());
		if (obj == Tab.noObj) {
			report_error("Greska na liniji " + designator.getLine() + " : ime " + designator.getDesName()
					+ " nije deklarisano! ", null);
		}

		// if (designator.getDesignAdds() instanceof DesignListOnlyExpr) {
		// DesignListOnlyExpr desOnlyExpr = (DesignListOnlyExpr)
		// designator.getDesignAdds();
		// Struct elemType = desOnlyExpr.getExpr().struct;
		// designator.obj = new Obj(Obj.Elem, "$", elemType);
		// } else {
		designator.obj = obj;
		// }
	}

	public void visit(DesStmtActPars desstmtactpars) {
		Obj designator = desstmtactpars.getDesignator().obj;
		if (designator == Tab.noObj || designator.getKind() != Obj.Meth) {
			report_error("Greska na liniji " + desstmtactpars.getLine() + " : " + " metoda ne postoji", null);
		} else {
			ActualPars actPars = desstmtactpars.getActualPars();
			if (actPars instanceof NoActuals) {
				if (designator.getLevel() != 0) {
					report_error(
							"Greska na liniji " + desstmtactpars.getLine() + " : " + " nedostaju stvarni parametri",
							null);
				}
			}
			if (designator.getLevel() != actParsCnt) {
				report_error("Greska na liniji " + desstmtactpars.getLine() + " : " + " ne poklapa se broj parametara",
						null);
			} else {
				Object[] formPars = designator.getLocalSymbols().toArray();
				for (int i = (designator.getLevel() - 1); i >= 0; i--) {
					Struct actualParT = curAP.pop();
					Struct formalParT = ((Obj) formPars[i]).getType();
					if (actualParT.getKind() == Struct.Int||actualParT.getKind() == Struct.Enum)
						actualParT = Tab.intType;
					if (formalParT.getKind() == Struct.Int||formalParT.getKind() == Struct.Enum)
						formalParT = Tab.intType;
					if (!actualParT.assignableTo(formalParT)) {
						report_error("Greska na liniji " + desstmtactpars.getLine() + " : "
								+ " ne poklapa se tip parametara", null);
					}
				}
				actParsCnt = actParsStack.pop();
			}
		}

	}

	public void visit(DesStmtExpr desstmtexpr) {

		Obj desObj = Tab.find(desstmtexpr.getDesignator().getDesName());
		if(desObj.getType()==enumType) {
			if(desstmtexpr.getExpr().struct.getKind()!=Struct.Enum) 
				report_error("Greska: pokusaj dodele nevalidne vrednosti promenljivoj enum tipa! na liniji"+desstmtexpr.getLine(),null);
		}
		if (desstmtexpr.getDesignator().getDesignAdds() instanceof DesignListOnlyExpr
				|| desstmtexpr.getDesignator().getDesignAdds() instanceof DesignNoExprForAct) {
			if (desObj.getType().getKind() != Struct.Array) {
				report_error("Desstmtexpr nije niz! na liniji: " + desstmtexpr.getLine(), null);
			}
			if (desstmtexpr.getDesignator().getDesignAdds() instanceof DesignListOnlyExpr) {
				if (((DesignListOnlyExpr) desstmtexpr.getDesignator().getDesignAdds()).getExpr().struct
						.getKind() != Struct.Int) {
					report_error("Desstmtexpr nekompatibilni! na liniji: " + desstmtexpr.getLine(), null);
				}
			}

		}

	}

	public void visit(DestStmtDecr destStmtDecr) {
		Obj designator = destStmtDecr.getDesignator().obj;
		Struct desStruct = designator.getType();
		// if(designator.getType().getKind()==Struct.Array);
		if (designator == Tab.noObj || (designator.getKind() != Obj.Var && designator.getKind() != Obj.Elem)
				|| desStruct != Tab.intType) {
			report_error("Greska na liniji " + destStmtDecr.getLine() + " : " + " mora biti int za incr", null);
		}
	}

	public void visit(DesStmtIncr desStmtIncr) {
		Obj designator = desStmtIncr.getDesignator().obj;
		Struct desStruct = designator.getType();
		// if(designator.getType().getKind()==Struct.Array);
		if (designator == Tab.noObj || (designator.getKind() != Obj.Var && designator.getKind() != Obj.Elem)
				|| desStruct != Tab.intType) {
			report_error("Greska na liniji " + desStmtIncr.getLine() + " : " + " mora biti int za incr", null);
		}
	}

	////////////// some actpars

	public void visit(ActualParams actualParams) {
		curAP.push(actualParams.getExpr().struct);
		actParsCnt++;
	}

	public void visit(ActualParam actualParam) {
		curAP.push(actualParam.getExpr().struct);
		actParsCnt++;
	}

	public void visit(ActParsBegin actParsBegin) {
		actParsStack.push(actParsCnt);
		actParsCnt = 0;
	}

	//////////////////// DESIGNATORSEND

	//////////////////// STATEMENT START

	public void visit(ReadStmt readStmt) {
		Obj aux = readStmt.getDesignator().obj;

		if (aux.getKind() != Obj.Var) {
			report_error("Greska na liniji: " + readStmt.getLine() + " nije obj.var", null);
		}

		if (!(aux.getType() == Tab.charType || aux.getType() == boolType || aux.getType() == Tab.intType)) {
			report_error("Greska na liniji: " + readStmt.getLine() + " nekompatibilan tip(readstmt)", null);
		}
	}

	public void visit(PrintStmt printStmt) {
		printCallCount++;

		if (printStmt.getExpr().struct != Tab.charType && printStmt.getExpr().struct != Tab.intType
				&& printStmt.getExpr().struct != boolType) {
			report_error("Greska na liniji: " + printStmt.getLine() + " nekompatibilan tip(printstmt)", null);
		}
	}

	//////////////////// STATEMENT END

	//////////////////// FACTOR

	public void visit(Assignment assign) {

		if (assign.getDesignatorStatement() instanceof DesStmtActPars) {
			Obj asgn = ((DesStmtActPars) assign.getDesignatorStatement()).getDesignator().obj;

			if (Obj.Meth == asgn.getKind()) {
				report_info("Pronadjen poziv funkcije " + asgn.getName() + " na liniji " + assign.getLine(), null);
				assign.struct = asgn.getType();
			} else {
				report_error("Greska na liniji " + assign.getLine() + " : ime " + asgn.getName() + " nije funkcija!",
						null);
				assign.struct = Tab.noType;
			}
		}
	}

	public void visit(FuncCall funcCall) {
		Obj func = funcCall.getDesignator().obj;
		if (Obj.Meth == func.getKind()) {
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
			funcCall.struct = func.getType();
		} else {
			report_error("Greska na liniji " + funcCall.getLine() + " : ime " + func.getName() + " nije funkcija!",
					null);
			funcCall.struct = Tab.noType;
		}

		if (func.getKind() != Obj.Meth) {
			report_error("Pokusan poziv necega sto nije metoda na liniji: " + funcCall.getLine(), null);
		} else {
			Object[] formPars = func.getLocalSymbols().toArray();
			for (int i = (func.getLevel() - 1); i >= 0; i--) {
				Struct actualParT = curAP.pop();
				Struct formalParT = ((Obj) formPars[i]).getType();
				if (actualParT.getKind() == Struct.Int||actualParT.getKind() == Struct.Enum)
					actualParT = Tab.intType;
				if (formalParT.getKind() == Struct.Int||formalParT.getKind() == Struct.Enum)
					formalParT = Tab.intType;
				if (!actualParT.assignableTo(formalParT)) {
					report_error("Greska na liniji " + funcCall.getLine() + " : " + " ne poklapa se tip parametara",
							null);
				}
			}
			actParsCnt = actParsStack.pop();
		}

	}

	public void visit(Var var) {
		if (var.getDesignator().getDesignAdds() instanceof DesignListOnlyExpr)
			var.struct = var.getDesignator().getDesignAdds().obj.getType();
		else if (var.getDesignator().getDesignAdds() instanceof DotIdent)
			var.struct = new Struct(Struct.Enum);
		else
			var.struct = var.getDesignator().obj.getType();
	}

	public void visit(Const cnst) {
		Rhs rhs = cnst.getRhs();

		if (rhs instanceof IntInit) {
			cnst.struct = Tab.intType;
		} else if (rhs instanceof BoolInit) {
			cnst.struct = boolType;
		} else if (rhs instanceof CharInit) {
			cnst.struct = Tab.charType;
		}
	}

	public void visit(NewWExpr newWExpr) {
		newWExpr.struct = var_type;
		if (newWExpr.getExpr().struct != null) {
			newWExpr.struct = new Struct(Struct.Array, var_type);
			if (newWExpr.getExpr().struct != Tab.intType) {
				report_error("Greska u liniji " + newWExpr.getLine() + ": losa vrednost za niz(nije int)", null);
			}
		}
	}

	public void visit(NewNoExpr newNoExpr) {
		newNoExpr.struct = var_type;
	}

	public void visit(FactorOnlyExpr factorOnlyExpr) {
		factorOnlyExpr.struct = factorOnlyExpr.getExpr().struct;
	}

	public void visit(FactorListing fListing) {
		if(!((fListing.getFactor().struct==Tab.intType||fListing.getFactor().struct==enumType)&&
				(fListing.getFactorList().struct==Tab.intType||fListing.getFactorList().struct==enumType)))
		if (!fListing.getFactor().struct.assignableTo(fListing.getFactorList().struct)) {
			report_error("Greska na liniji " + fListing.getLine() + ": nekompatibilni factori(factor i factorlist)",
					null);
		}
		fListing.struct=fListing.getFactor().struct;
	}

	public void visit(FactorSolo fSolo) {
		fSolo.struct = fSolo.getFactor().struct;
	}

	//////////////////// FACTOR END

	//////////////////// TERM START

	public void visit(Term term) {
		term.struct = term.getFactorList().struct;
	}

	public void visit(AddTermListing addTermListing) {
		Struct tmp1 = addTermListing.getAddTermList().struct;
		Struct tmp2 = addTermListing.getTerm().struct;
		if (tmp1.getKind() == Struct.Int ||tmp1.getKind() == Struct.Enum)
			tmp1 = Tab.intType;
		if (tmp2.getKind() == Struct.Int||tmp2.getKind() == Struct.Enum)
			tmp2 = Tab.intType;
		if (tmp1.compatibleWith(tmp2)) {
			report_info("Greska na liniji " + addTermListing.getLine() + ": nekompatibilni termovi(term i addtermlist)",
					null);
		}
		addTermListing.struct = addTermListing.getTerm().struct;
		// ((AddTermList)addTermListing.getParent()).struct=addTermListing.getTerm().struct;
	}

	public void visit(AddTermSolo addTermSolo) {
		addTermSolo.struct = addTermSolo.getTerm().struct;
	}

	//////////////////// TERM END

	//////////////////// EXPR begin

	public void visit(TermExpr termExpr) {
		termExpr.struct = termExpr.getTerm().struct;
	}

	public void visit(MinusTerm minusTerm) {
		minusTerm.struct = minusTerm.getTerm().struct;
	}

	public void visit(MinusFrontAddExpr minFrontAddExpr) {

		if (minFrontAddExpr.getMinusTerm().struct != Tab.intType) {
			report_error("Greska na liniji " + minFrontAddExpr.getLine() + ": minFront term nije int", null);
		}

		if (!minFrontAddExpr.getMinusTerm().struct.assignableTo(minFrontAddExpr.getAddTermList().struct)) {
			report_error("Greska na liniji " + minFrontAddExpr.getLine()
					+ ": nekompatibilni addterm i term(minfrontaddexpr)", null);
		} else
			minFrontAddExpr.struct = Tab.intType;
	}

	public void visit(MinusFrontTermExpr minFrontTermExpr) {
		if (minFrontTermExpr.getMinusTerm().struct != Tab.intType) {
			report_error("Greska na liniji " + minFrontTermExpr.getLine() + ": minFront term nije int", null);
		} else
			minFrontTermExpr.struct = Tab.intType;
	}

	public void visit(AddExpr addExpr) {
		Struct tmp1 = addExpr.getAddTermList().struct;
		Struct tmp2 = addExpr.getTerm().struct;
		if (tmp1.getKind() == Struct.Int ||tmp1.getKind() == Struct.Enum)
			tmp1 = Tab.intType;
		if (tmp2.getKind() == Struct.Int||tmp2.getKind() == Struct.Enum)
			tmp2 = Tab.intType;
		// if
		// (!addExpr.getAddTermList().struct.assignableTo(addExpr.getTerm().struct))
		// {
		if (!tmp1.assignableTo(tmp2)) {
			report_error("Greska na liniji " + addExpr.getLine() + ": nekompatibilni term i addterm(addexpr)", null);
		} else
			addExpr.struct = addExpr.getTerm().struct;
	}

	//////////////////// EXPR END

	//////////////////// ENUM BEGIN

	public void visit(EnumDeclFull enumDecl) {
		String varName = enumDecl.getEnumName();
		Obj varObj = Tab.find(varName);
		if (varObj != Tab.noObj)

		{
			report_error("Greska na liniji " + enumDecl.getLine() + ": ovaj enum vec postoji", null);
		}
		Obj obj = Tab.insert(Obj.Type, varName, enumType);
		for (Obj i : enumMembers)
			obj.getType().getMembersTable().insertKey(i);

		enumCnt = 0;
		enumMembers = new ArrayList<>();

	}

	public void visit(EnumDeclNoDefPart enumDeclND) {
		String varName = enumDeclND.getGetName();
		for (Obj i : enumMembers)
			if (varName.equals(i.getName())) {
				report_error("Greska na liniji " + enumDeclND.getLine() + ": ovaj element enuma vec postoji", null);
			}
		Obj obj = new Obj(Obj.Con, varName, new Struct(Struct.Int));
		obj.setAdr(enumCnt++);
		enumMembers.add(obj);
	}

	public void visit(EnumDeclDefPart enumDeclD) {
		String varName = enumDeclD.getGetName();
		for (Obj i : enumMembers)
			if (varName.equals(i.getName())) {
				report_error("Greska na liniji " + enumDeclD.getLine() + ": ovaj element enuma vec postoji", null);
			}
		Obj obj = new Obj(Obj.Con, varName, new Struct(Struct.Int));
		enumCnt = enumDeclD.getN1();
		obj.setAdr(enumCnt++);
		enumMembers.add(obj);

	}

	/////////////// ENUM END

	/////////////// arrayshbegin

	public void visit(DesignListOnlyExpr desListOnlyExpr) {
		if (desListOnlyExpr.getExpr().struct == null)
			report_error("GRESKA", null);
		if (desListOnlyExpr.getExpr().struct.getKind() == Struct.Int)
			;
		desListOnlyExpr.getExpr().struct = Tab.intType;
		if (desListOnlyExpr.getExpr().struct != Tab.intType) {
			report_error("Indeks mora biti int : " + desListOnlyExpr.getLine(), null);
		}
		Obj desObj = Tab.find(((Designator) desListOnlyExpr.getParent()).getDesName());
		if (desObj.getType().getKind() != Struct.Array) {
			report_error("Ne postoji array sa tim nazivom : " + desListOnlyExpr.getLine(), null);
		}
		// if (!(desListOnlyExpr.getParent().getParent().getParent() instanceof
		// Var))
		desListOnlyExpr.obj = new Obj(Obj.Elem, desObj.getName(), desObj.getType().getElemType());
		report_info("neki info", null);
	}

	/////////////// arrayshend

	public boolean passed() {
		return !errorDetected;
	}

}
