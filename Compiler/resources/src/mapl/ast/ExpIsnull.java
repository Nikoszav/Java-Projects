package mapl.ast;

import mapl.ast.util.Visitor;

public class ExpIsnull extends Exp {

    public final Exp e;

    public ExpIsnull(Exp ae) {
        e = ae;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
