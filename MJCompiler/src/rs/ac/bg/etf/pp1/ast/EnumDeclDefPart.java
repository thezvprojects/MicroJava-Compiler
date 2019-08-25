// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:25


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclDefPart extends EnumDeclPart {

    private String getName;
    private Integer N1;

    public EnumDeclDefPart (String getName, Integer N1) {
        this.getName=getName;
        this.N1=N1;
    }

    public String getGetName() {
        return getName;
    }

    public void setGetName(String getName) {
        this.getName=getName;
    }

    public Integer getN1() {
        return N1;
    }

    public void setN1(Integer N1) {
        this.N1=N1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDeclDefPart(\n");

        buffer.append(" "+tab+getName);
        buffer.append("\n");

        buffer.append(" "+tab+N1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclDefPart]");
        return buffer.toString();
    }
}
