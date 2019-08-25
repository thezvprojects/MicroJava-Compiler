// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:24


package rs.ac.bg.etf.pp1.ast;

public class MinusFrontAddExpr extends Expr {

    private MinusTerm MinusTerm;
    private AddTermList AddTermList;

    public MinusFrontAddExpr (MinusTerm MinusTerm, AddTermList AddTermList) {
        this.MinusTerm=MinusTerm;
        if(MinusTerm!=null) MinusTerm.setParent(this);
        this.AddTermList=AddTermList;
        if(AddTermList!=null) AddTermList.setParent(this);
    }

    public MinusTerm getMinusTerm() {
        return MinusTerm;
    }

    public void setMinusTerm(MinusTerm MinusTerm) {
        this.MinusTerm=MinusTerm;
    }

    public AddTermList getAddTermList() {
        return AddTermList;
    }

    public void setAddTermList(AddTermList AddTermList) {
        this.AddTermList=AddTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MinusTerm!=null) MinusTerm.accept(visitor);
        if(AddTermList!=null) AddTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MinusTerm!=null) MinusTerm.traverseTopDown(visitor);
        if(AddTermList!=null) AddTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MinusTerm!=null) MinusTerm.traverseBottomUp(visitor);
        if(AddTermList!=null) AddTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MinusFrontAddExpr(\n");

        if(MinusTerm!=null)
            buffer.append(MinusTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddTermList!=null)
            buffer.append(AddTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MinusFrontAddExpr]");
        return buffer.toString();
    }
}
