// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:25


package rs.ac.bg.etf.pp1.ast;

public class ConstPart extends ConstDeclPart {

    private String constName;
    private Rhs Rhs;

    public ConstPart (String constName, Rhs Rhs) {
        this.constName=constName;
        this.Rhs=Rhs;
        if(Rhs!=null) Rhs.setParent(this);
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
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
        buffer.append("ConstPart(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(Rhs!=null)
            buffer.append(Rhs.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstPart]");
        return buffer.toString();
    }
}
