// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:25


package rs.ac.bg.etf.pp1.ast;

public class Const extends Factor {

    private Rhs Rhs;

    public Const (Rhs Rhs) {
        this.Rhs=Rhs;
        if(Rhs!=null) Rhs.setParent(this);
    }

    public Rhs getRhs() {
        return Rhs;
    }

    public void setRhs(Rhs Rhs) {
        this.Rhs=Rhs;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Rhs!=null) Rhs.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Rhs!=null) Rhs.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Rhs!=null) Rhs.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Const(\n");

        if(Rhs!=null)
            buffer.append(Rhs.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Const]");
        return buffer.toString();
    }
}
