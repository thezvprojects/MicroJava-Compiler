// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:24


package rs.ac.bg.etf.pp1.ast;

public class FormalParamDeclAr extends FormalParamDecl {

    private Type Type;
    private String fName;

    public FormalParamDeclAr (Type Type, String fName) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.fName=fName;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName=fName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParamDeclAr(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+fName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParamDeclAr]");
        return buffer.toString();
    }
}
