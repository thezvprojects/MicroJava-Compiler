// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:25


package rs.ac.bg.etf.pp1.ast;

public class Designator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String DesName;
    private DesignAdds DesignAdds;

    public Designator (String DesName, DesignAdds DesignAdds) {
        this.DesName=DesName;
        this.DesignAdds=DesignAdds;
        if(DesignAdds!=null) DesignAdds.setParent(this);
    }

    public String getDesName() {
        return DesName;
    }

    public void setDesName(String DesName) {
        this.DesName=DesName;
    }

    public DesignAdds getDesignAdds() {
        return DesignAdds;
    }

    public void setDesignAdds(DesignAdds DesignAdds) {
        this.DesignAdds=DesignAdds;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignAdds!=null) DesignAdds.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignAdds!=null) DesignAdds.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignAdds!=null) DesignAdds.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator(\n");

        buffer.append(" "+tab+DesName);
        buffer.append("\n");

        if(DesignAdds!=null)
            buffer.append(DesignAdds.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator]");
        return buffer.toString();
    }
}
