// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:24


package rs.ac.bg.etf.pp1.ast;

public class VarId extends VarDeclPart {

    private VarName VarName;

    public VarId (VarName VarName) {
        this.VarName=VarName;
        if(VarName!=null) VarName.setParent(this);
    }

    public VarName getVarName() {
        return VarName;
    }

    public void setVarName(VarName VarName) {
        this.VarName=VarName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarName!=null) VarName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarName!=null) VarName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarName!=null) VarName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarId(\n");

        if(VarName!=null)
            buffer.append(VarName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarId]");
        return buffer.toString();
    }
}
