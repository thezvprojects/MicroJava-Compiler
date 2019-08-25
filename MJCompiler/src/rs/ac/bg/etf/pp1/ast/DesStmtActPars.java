// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:24


package rs.ac.bg.etf.pp1.ast;

public class DesStmtActPars extends DesignatorStatement {

    private Designator Designator;
    private ActParsBegin ActParsBegin;
    private ActualPars ActualPars;
    private ActParsEnd ActParsEnd;

    public DesStmtActPars (Designator Designator, ActParsBegin ActParsBegin, ActualPars ActualPars, ActParsEnd ActParsEnd) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ActParsBegin=ActParsBegin;
        if(ActParsBegin!=null) ActParsBegin.setParent(this);
        this.ActualPars=ActualPars;
        if(ActualPars!=null) ActualPars.setParent(this);
        this.ActParsEnd=ActParsEnd;
        if(ActParsEnd!=null) ActParsEnd.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ActParsBegin getActParsBegin() {
        return ActParsBegin;
    }

    public void setActParsBegin(ActParsBegin ActParsBegin) {
        this.ActParsBegin=ActParsBegin;
    }

    public ActualPars getActualPars() {
        return ActualPars;
    }

    public void setActualPars(ActualPars ActualPars) {
        this.ActualPars=ActualPars;
    }

    public ActParsEnd getActParsEnd() {
        return ActParsEnd;
    }

    public void setActParsEnd(ActParsEnd ActParsEnd) {
        this.ActParsEnd=ActParsEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ActParsBegin!=null) ActParsBegin.accept(visitor);
        if(ActualPars!=null) ActualPars.accept(visitor);
        if(ActParsEnd!=null) ActParsEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ActParsBegin!=null) ActParsBegin.traverseTopDown(visitor);
        if(ActualPars!=null) ActualPars.traverseTopDown(visitor);
        if(ActParsEnd!=null) ActParsEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ActParsBegin!=null) ActParsBegin.traverseBottomUp(visitor);
        if(ActualPars!=null) ActualPars.traverseBottomUp(visitor);
        if(ActParsEnd!=null) ActParsEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesStmtActPars(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsBegin!=null)
            buffer.append(ActParsBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualPars!=null)
            buffer.append(ActualPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsEnd!=null)
            buffer.append(ActParsEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesStmtActPars]");
        return buffer.toString();
    }
}
