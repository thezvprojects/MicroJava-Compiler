// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:25


package rs.ac.bg.etf.pp1.ast;

public class EnumDecls extends EnumDeclList {

    private EnumDeclList EnumDeclList;
    private EnumDeclPart EnumDeclPart;

    public EnumDecls (EnumDeclList EnumDeclList, EnumDeclPart EnumDeclPart) {
        this.EnumDeclList=EnumDeclList;
        if(EnumDeclList!=null) EnumDeclList.setParent(this);
        this.EnumDeclPart=EnumDeclPart;
        if(EnumDeclPart!=null) EnumDeclPart.setParent(this);
    }

    public EnumDeclList getEnumDeclList() {
        return EnumDeclList;
    }

    public void setEnumDeclList(EnumDeclList EnumDeclList) {
        this.EnumDeclList=EnumDeclList;
    }

    public EnumDeclPart getEnumDeclPart() {
        return EnumDeclPart;
    }

    public void setEnumDeclPart(EnumDeclPart EnumDeclPart) {
        this.EnumDeclPart=EnumDeclPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumDeclList!=null) EnumDeclList.accept(visitor);
        if(EnumDeclPart!=null) EnumDeclPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumDeclList!=null) EnumDeclList.traverseTopDown(visitor);
        if(EnumDeclPart!=null) EnumDeclPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumDeclList!=null) EnumDeclList.traverseBottomUp(visitor);
        if(EnumDeclPart!=null) EnumDeclPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDecls(\n");

        if(EnumDeclList!=null)
            buffer.append(EnumDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumDeclPart!=null)
            buffer.append(EnumDeclPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDecls]");
        return buffer.toString();
    }
}
