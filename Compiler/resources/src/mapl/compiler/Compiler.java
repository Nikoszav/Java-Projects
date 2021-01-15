package mapl.compiler;

import mapl.ast.*;
import mapl.ast.util.VisitorAdapter;
import ir.ast.*;
import static mapl.compiler.FreshNameGenerator.makeName;

import java.util.ArrayList;
import java.util.List;

public class Compiler {
    
    private final StmCompiler stmCompiler;
    private final ExpCompiler expCompiler;

    public Compiler() {
        stmCompiler = new StmCompiler();
        expCompiler = new ExpCompiler();
    }
    
    /**************************************************************************/
    /*                                                                        */
    /* The following factory methods methods are not strictly necessary but   */
    /* they greatly simplify the code that you have to write. For example,    */
    /* instead of:                                                            */
    /*                                                                        */
    /*    new IRStmMoveTemp("x",                                              */
    /*                      new IRExpBinOp(new IRExpTemp("y"),                */
    /*                                     IROp.EQ,                           */
    /*                                     new IRExpConst(7)                  */
    /*                                    )                                   */
    /*                     )                                                  */
    /*                                                                        */
    /* you can write:                                                         */
    /*                                                                        */
    /*    MOVE(TEMP("x"), BINOP(TEMP("y"), IROp.EQ, CONST(7)))                */
    /*                                                                        */
    /**************************************************************************/
    
    /****************************************************/
    /* Convenience factory methods for building IRStms. */
    /****************************************************/
    
    private static IRStm MOVE(IRExp el, IRExp er) {
        if (el instanceof IRExpTemp) {
            return new IRStmMoveTemp(((IRExpTemp)el).name, er);
        } else if (el instanceof IRExpMem) {
            return new IRStmMoveMem(((IRExpMem)el).e, er);
        } else {
            throw new Error("Left-expression of MOVE must be either a TEMP or a MEM, not: " + el);
        }
    }
    
    private static IRStmNoop NOOP = new IRStmNoop();
    
    private static IRStmJump JUMP(IRExp e) {
        return new IRStmJump(e);
    }
    
    private static IRStmCJump CJUMP(IRExp e1, IROp op, IRExp e2, String trueLabel, String falseLabel) {
        return new IRStmCJump(e1 , op, e2, trueLabel, falseLabel);
    }
    
    private static IRStmExp EXP(IRExp e) {
        return new IRStmExp(e);
    }
    
    private static IRStmLabel LABEL(String name) {
        return new IRStmLabel(name);
    }
    
    private static IRStm SEQ(IRStm... stms) {
        int n = stms.length;
        if (n == 0) return NOOP;
        IRStm stm = stms[n-1];
        for (int i = n-2; i >= 0; --i) {
            stm = new IRStmSeq(stms[i], stm);
        }
        return stm;
    }
    
    private static IRStm SEQ(List<IRStm> stms) {
        return SEQ(stms.toArray(new IRStm[0]));
    }
    
    private static IRStmPrologue PROLOGUE(int params, int locals) {
        return new IRStmPrologue(params, locals);
    }
    
    private static IRStmEpilogue EPILOGUE(int params, int locals) {
        return new IRStmEpilogue(params, locals);
    }
    
    /****************************************************/
    /* Convenience factory methods for building IRExps. */
    /****************************************************/
    
    private static IRExpBinOp BINOP(IRExp e1, IROp op, IRExp e2) {
        return new IRExpBinOp(e1, op, e2);
    }
    
    private static IRExpCall CALL(IRExp f, IRExp... args) {
        return new IRExpCall(f, args);
    }
    
    private static IRExpCall CALL(IRExp f, List<IRExp> args) {
        return new IRExpCall(f, args);
    }
    
    private static IRExpConst CONST(int n) {
        return new IRExpConst(n);
    }
    
    private static IRExpMem MEM(IRExp e) {
        return new IRExpMem(e);
    }
    
    private static IRExpTemp TEMP(String name) {
        return new IRExpTemp(name);
    }
    
    private static IRExpName NAME(String labelName) {
        return new IRExpName(labelName);
    }

    private static IRExpESeq ESEQ(IRStm s, IRExp e) {
        return new IRExpESeq(s, e);
    }
    
    // TODO: extend this to handle more complex programs
    // The initial prototype assumes just a single top-level proc with zero
    // parameters. The extended version will retrieve the command-line
    // parameters from the stack and then CALL the top-level proc.
    public IRProgram compile(Program n) {
        List<IRStm> stms = new ArrayList<>();
        // compile the top-level proc body
        // in the extended version this won't be done here (it will be
        // part of the code generation for method declarations)
        for (Stm stm: n.pd.ss) {
            stms.addAll(stm.accept(stmCompiler));
        }
        // this jump to _END is redundant in the prototype
        stms.add(JUMP(NAME("_END")));
        // TODO: add code generation for all the method declarations
        
        return new IRProgram(stms);
    }
    
    // TODO: add visit methods for all the Stm classes
    // TODO: add visit methods for method declarations
    // Note: no need to define visit methods for any other AST types
    private class StmCompiler extends VisitorAdapter<List<IRStm>> {
        
        @Override
        public List<IRStm> visit(StmOutchar s) {
            List<IRStm> stms = new ArrayList<>();
            stms.add(EXP(CALL(NAME("_printchar"), s.e.accept(expCompiler))));
            return stms;
        }
        
        @Override
        public List<IRStm> visit(StmVarDecl n) {
            return new ArrayList<>();
        }
        
    }
    
    // TODO: add visit methods for all the Exp classes
    // Note: no need to define visit methods for any other AST types
    private class ExpCompiler extends VisitorAdapter<IRExp> {
        
        @Override
        public IRExp visit(ExpInteger e) {
            return CONST(e.i);
        }
        
    }
}
