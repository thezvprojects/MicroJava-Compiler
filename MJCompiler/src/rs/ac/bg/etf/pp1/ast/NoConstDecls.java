// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:25


package rs.ac.bg.etf.pp1.ast;

public class NoConstDecls extends ConstDeclList {

    private ConstDeclPart ConstDeclPart;

    public NoConstDecls (ConstDeclPart ConstDeclPart) {
        this.ConstDeclPart=ConstDeclPart;
        if(ConstDeclPart!=null) ConstDeclPart.setParent(this);
    }

    public ConstDeclPart getConstDeclPart() {
        return ConstDeclPart;
    }

    public void setConstDeclPart(ConstDeclPart ConstDeclPart) {
        this.ConstDeclPart=ConstDeclPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclPart!=null) ConstDeclPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclPart!=null) ConstDeclPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclPart!=null) ConstDeclPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoConstDecls(\n");

        if(ConstDeclPart!=null)
            buffer.append(ConstDeclPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NoConstDecls]");
        return buffer.toString();
    }
}
