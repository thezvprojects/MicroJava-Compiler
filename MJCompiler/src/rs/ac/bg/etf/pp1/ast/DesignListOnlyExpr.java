// generated with ast extension for cup
// version 0.8
// 24/7/2019 14:59:25


package rs.ac.bg.etf.pp1.ast;

public class DesignListOnlyExpr extends DesignAdds {

    private LBrack LBrack;
    private Expr Expr;
    private RBrack RBrack;

    public DesignListOnlyExpr (LBrack LBrack, Expr Expr, RBrack RBrack) {
        this.LBrack=LBrack;
        if(LBrack!=null) LBrack.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.RBrack=RBrack;
        if(RBrack!=null) RBrack.setParent(this);
    }

    public LBrack getLBrack() {
        return LBrack;
    }

    public void setLBrack(LBrack LBrack) {
        this.LBrack=LBrack;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public RBrack getRBrack() {
        return RBrack;
    }

    public void setRBrack(RBrack RBrack) {
        this.RBrack=RBrack;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LBrack!=null) LBrack.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(RBrack!=null) RBrack.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LBrack!=null) LBrack.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(RBrack!=null) RBrack.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LBrack!=null) LBrack.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(RBrack!=null) RBrack.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignListOnlyExpr(\n");

        if(LBrack!=null)
            buffer.append(LBrack.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RBrack!=null)
            buffer.append(RBrack.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignListOnlyExpr]");
        return buffer.toString();
    }
}
