//NIkolaos Zavitsanos for language processing

package mapl.compiler;

import jdk.jfr.Label;
import mapl.Compile;
import mapl.ast.*;
import mapl.ast.util.VisitorAdapter;
import ir.ast.*;
import mapl.ast.util.Visitor;

import static mapl.compiler.FreshNameGenerator.makeName;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler {

    private final StmCompiler stmCompiler;
    private final ExpCompiler expCompiler;

    public Compiler() {
        stmCompiler = new StmCompiler(); // mapl statments to IR exp
        expCompiler = new ExpCompiler(); // map expressions to IR exo
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
            return new IRStmMoveTemp(((IRExpTemp) el).name, er);
        } else if (el instanceof IRExpMem) {
            return new IRStmMoveMem(((IRExpMem) el).e, er);
        } else {
            throw new Error("Left-expression of MOVE must be either a TEMP or a MEM, not: " + el);
        }
    }

    private static IRStmNoop NOOP = new IRStmNoop();

    private static IRStmJump JUMP(IRExp e) {
        return new IRStmJump(e);
    }

    private static IRStmCJump CJUMP(IRExp e1, IROp op, IRExp e2, String trueLabel, String falseLabel) {
        return new IRStmCJump(e1, op, e2, trueLabel, falseLabel);
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
        IRStm stm = stms[n - 1];
        for (int i = n - 2; i >= 0; --i) {
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
        //create an empty list of IRExp called args
        List<IRExp> args = new ArrayList<>();
        HashMap<String, String> errors = new HashMap<String,String>();
        errors.put("outB", "IndexOutOfBounds");
        errors.put("null", "NUllPointerExeption");
        for (int i = 1; i <= n.pd.fs.size(); i++) {
            args.add(MEM(BINOP(TEMP("FP"), IROp.SUB, CONST(i))));
        }

        stms.add(EXP(CALL(NAME(n.pd.id), args)));
        stms.add(JUMP(NAME("_END")));

        // TODO: add code generation for all the method declarations

        stms.addAll(n.pd.accept(stmCompiler));
        for (MethodDecl methodDecl : n.mds) {
            stms.addAll(methodDecl.accept(stmCompiler));
        }
        return new IRProgram(errors,stms);
    }

    // TODO: add visit methods for all the Stm classes
    // TODO: add visit methods for method declarations
    private class StmCompiler extends VisitorAdapter<List<IRStm>> {

        @Override
        public List<IRStm> visit(StmOutchar s) { // s = outchar 72;
            List<IRStm> stms = new ArrayList<>();
            stms.add(EXP(CALL(NAME("_printchar"), s.e.accept(expCompiler)))); // outchar 72; example so we use a predefined printchar method follow by an exp
            return stms;
        }

        @Override
        public List<IRStm> visit(StmOutput n) {
            List<IRStm> stms = new ArrayList<>();
            stms.add(EXP(CALL(NAME("_printint"), n.e.accept(expCompiler))));
            return stms;
        }

        @Override
        public List<IRStm> visit(StmVarDecl n) {
            return new ArrayList<>();
        }

        @Override
        public List<IRStm> visit(StmBlock n) {

            List<IRStm> stms = new ArrayList<>();
            for (Stm stm : n.ss) {
                stms.addAll(stm.accept(stmCompiler));
            }

            return stms;
        }

        @Override
        public List<IRStm> visit(StmAssign n) {
            List<IRStm> stms = new ArrayList<>();
            stms.add(MOVE(MEM(BINOP(TEMP("FP"), IROp.ADD, CONST(n.v.offset))), n.e.accept(expCompiler)));
            //stms.add(MOVE(TEMP(n.v.id), n.e.accept(expCompiler)));
            return stms;
        }

        @Override
        public List<IRStm> visit(ProcDecl n) {
            //n.fs.size(); number of parameters
            //n.stackAllocation number of local variables
            List<IRStm> stms = new ArrayList<>();
            // add a label base on the method name n.id
            stms.add(LABEL(n.id));
            // add prologue with parameters n.fs and n.stackAllocation
            stms.add(PROLOGUE(n.fs.size(), n.stackAllocation));
            // add for each loop to add each statement (list)
            //compile to IR to stms
            for (Stm stm : n.ss) {
                stms.addAll(stm.accept(stmCompiler));
            }
            //add Epilogue
            stms.add(EPILOGUE(n.fs.size(), n.stackAllocation));
            return stms;
        }

        @Override
        public List<IRStm> visit(FunDecl n) {
            List<IRStm> stms = new ArrayList<>();
            //n.fs.size(); number of parameters
            //n.stackAllocation number of local variables
            // add a label base on the method name n.id
            stms.add(LABEL(n.id));
            // add prologue with parameters n.fs and n.stackAllocation
            stms.add(PROLOGUE(n.fs.size(), n.stackAllocation));
            // add for each loop to add each statement (list)
            //compile to IR to stms
            for (Stm stm : n.ss) {
                stms.addAll(stm.accept(stmCompiler));
            }
            //add moving expression n.e to the register TEMP RV
            stms.add(MOVE(TEMP("RV"), n.e.accept(expCompiler)));
            //add Epilogue
            stms.add(EPILOGUE(n.fs.size(), n.stackAllocation));
            return stms;
        }

        @Override
        public List<IRStm> visit(StmCall n) {
            List<IRStm> stms = new ArrayList<>();
            List<IRExp> args = new ArrayList<>();
            for (Exp arg : n.es) {
                args.add(arg.accept(expCompiler));
            }

            stms.add(EXP(CALL(NAME(n.id), args)));
            return stms;
        }

        @Override
        public List<IRStm> visit(StmIf n) {
            String trueL = FreshNameGenerator.makeName();
            String falseL = FreshNameGenerator.makeName();
            String endL = FreshNameGenerator.makeName();

            List<IRStm> stms = new ArrayList<>();
            stms.add(CJUMP(n.e.accept(expCompiler), IROp.EQ, CONST(1), trueL, falseL));
            stms.add(LABEL(trueL));
            stms.addAll(n.st.accept(stmCompiler));
            stms.add(JUMP(NAME(endL)));
            stms.add(LABEL(falseL));
            stms.addAll(n.sf.accept(stmCompiler));
            stms.add(LABEL(endL));

            return stms;
        }

        @Override
        public List<IRStm> visit(StmWhile n) {
            String start = FreshNameGenerator.makeName();
            String main1 = FreshNameGenerator.makeName();
            String main2 = FreshNameGenerator.makeName();

            List<IRStm> stms = new ArrayList<>();
            stms.add(LABEL(start));
            stms.add(CJUMP(n.e.accept(expCompiler), IROp.EQ, CONST(1), main1, main2));
            stms.add(LABEL(main1));
            stms.addAll(n.body.accept(stmCompiler));
            stms.add(JUMP(NAME(start)));
            stms.add(LABEL(main2));
            return stms;
        }


        //array implematation
        @Override
        public List<IRStm> visit(StmArrayAssign n) {
            List<IRStm> stms = new ArrayList<>();
            String maxSize = FreshNameGenerator.makeName();
            String trueBranch = FreshNameGenerator.makeName();
            String falseBranch = FreshNameGenerator.makeName();
            String check0 = FreshNameGenerator.makeName();
            String endL = FreshNameGenerator.makeName();


            stms.add(MOVE(TEMP(maxSize),MEM(BINOP(n.e1.accept(expCompiler), IROp.SUB, CONST(1)))));
            stms.add(CJUMP(n.e2.accept(expCompiler), IROp.LT,TEMP(maxSize) ,trueBranch, falseBranch));
            stms.add(LABEL(trueBranch));
            stms.add(CJUMP(n.e2.accept(expCompiler), IROp.LT,CONST(0) , falseBranch, check0));
            stms.add(LABEL(check0));
            stms.add(MOVE(MEM(BINOP(n.e1.accept(expCompiler), IROp.ADD, n.e2.accept(expCompiler))), n.e3.accept(expCompiler)));
            stms.add(JUMP(NAME(endL)));
            stms.add(LABEL(falseBranch));
            stms.add(EXP(CALL(NAME("_printstr"),NAME( "outB"))));
            stms.add(JUMP(NAME("_END")));
            stms.add(LABEL(endL));

            return stms;
        }
    }


    // TODO: add visit methods for all the Exp classes
    // Note: no need to define visit methods for any other AST types
    private class ExpCompiler extends VisitorAdapter<IRExp> {

        @Override
        public IRExp visit(ExpInteger e) { return CONST(e.i);}

        @Override
        public IRExp visit(ExpTrue n) { return CONST(1);}

        @Override
        public IRExp visit(ExpFalse n) { return CONST(0);}

        @Override
        public IRExp visit(ExpVar n) { return MEM(BINOP(TEMP("FP"), IROp.ADD, CONST(n.v.offset)));}

        @Override
        public IRExp visit(ExpCall n) {
            List<IRExp> args = new ArrayList<>();
            for (Exp arg : n.es) {
                args.add(arg.accept(expCompiler));
            }
            return CALL(NAME(n.id), args);
        }

        @Override
        public IRExp visit(ExpNot n) { return BINOP(n.e.accept(expCompiler),IROp.EQ,CONST(0));}

        @Override
        public IRExp visit(ExpOp n) {
            if (n.op == ExpOp.Op.AND) {
                String trueBranch = FreshNameGenerator.makeName();
                String falseBranch = FreshNameGenerator.makeName();
                String labelEnd = FreshNameGenerator.makeName();
                String outPutVar = FreshNameGenerator.makeName();

                IRStm stmIf = CJUMP(n.e1.accept(expCompiler), IROp.EQ, CONST(1), trueBranch, falseBranch);
                IRStm stmLabelTrue = LABEL(trueBranch);
                IRStm moveTrueVar = MOVE(TEMP(outPutVar), n.e2.accept(expCompiler));
                IRStm jumpEnd = JUMP(NAME(labelEnd));
                IRStm stmLabelFalse = LABEL(falseBranch);
                IRStm moveFalseVar = MOVE(TEMP(outPutVar), CONST(0));
                IRStm stmLabelEnd = LABEL(labelEnd);

                IRStm sequence = SEQ(stmIf, stmLabelTrue, moveTrueVar, jumpEnd, stmLabelFalse, moveFalseVar, stmLabelEnd);

                return ESEQ(sequence, TEMP(outPutVar));
            }
            return BINOP(n.e1.accept(expCompiler), change(n.op), n.e2.accept(expCompiler));
        }

        public IROp change (ExpOp.Op var){
            if (var.equals(ExpOp.Op.PLUS)) {
                return IROp.ADD;
            } else if (var.equals(ExpOp.Op.DIV)) {
                return IROp.DIV;
            } else if (var.equals(ExpOp.Op.EQUALS)) {
                return IROp.EQ;
            } else if (var.equals(ExpOp.Op.LESSTHAN)) {
                return IROp.LT;
            } else if (var.equals(ExpOp.Op.TIMES)) {
                return IROp.MUL;
            } else if (var.equals(ExpOp.Op.MINUS)) {
                return IROp.SUB;
            } else
                return IROp.MUL;
        }


        //array implemetation
        @Override
        public IRExp visit(ExpIsnull n) { return BINOP(n.e.accept(expCompiler),IROp.EQ,CONST(0));}


        @Override
        public IRExp visit(ExpArrayLength n) {

            String trueBranch = FreshNameGenerator.makeName();
            String falseBranch = FreshNameGenerator.makeName();
            String end = FreshNameGenerator.makeName();
            String returnVar = FreshNameGenerator.makeName();

            IRStm checkNull = CJUMP(n.e.accept(expCompiler), IROp.EQ, CONST(0), trueBranch, falseBranch);
            IRStm trueBranchLabel = LABEL(trueBranch);
            IRStm printErrorStm =  EXP(CALL(NAME("_printstr"),NAME( "null"))); // change error
            IRStm jumpToTerminate = JUMP(NAME("_END"));
            IRStm falseBrunchLabel = LABEL(falseBranch);
            IRStm returnVarStm = MOVE(TEMP(returnVar), MEM(BINOP(n.e.accept(expCompiler), IROp.SUB, CONST(1))));
            IRStm endBrunchl = JUMP(NAME(end));
            IRStm endLAbel = LABEL(end);
            IRStm sequence = SEQ(checkNull, trueBranchLabel,printErrorStm,jumpToTerminate, falseBrunchLabel, returnVarStm,endBrunchl,endLAbel);
            return ESEQ(sequence,TEMP(returnVar));





            //return MEM(BINOP(n.e.accept(expCompiler), IROp.SUB, CONST(1)));
        }


        @Override
        public IRExp visit(ExpArrayLookup n) {
            String maxSize = FreshNameGenerator.makeName();
            String trueBranch = FreshNameGenerator.makeName();
            String falseBranch = FreshNameGenerator.makeName();
            String endL = FreshNameGenerator.makeName();
            String returnVar = FreshNameGenerator.makeName();

            IRStm maxSizeStm = MOVE(TEMP(maxSize),MEM(BINOP(n.e1.accept(expCompiler), IROp.SUB, CONST(1))));
            IRStm ifStm = CJUMP(n.e2.accept(expCompiler), IROp.LT,TEMP(maxSize) ,trueBranch, falseBranch);
            IRStm trueBranchStm = LABEL(trueBranch);
            IRStm returnVarStm = MOVE(TEMP(returnVar), MEM(BINOP(n.e1.accept(expCompiler), IROp.ADD, n.e2.accept(expCompiler))));
            IRStm GoEndStm = JUMP(NAME(endL));
            IRStm falseBranchStm = LABEL(falseBranch);
            IRStm printErrorStm = EXP(CALL(NAME("_printstr"),NAME( "outB")));
            IRStm jumpToTerminate = JUMP(NAME("_END"));
            IRStm endLStm = LABEL(endL);

            IRStm sequence = SEQ(maxSizeStm,ifStm,trueBranchStm,returnVarStm,GoEndStm, falseBranchStm,printErrorStm, jumpToTerminate ,endLStm);
            return ESEQ(sequence, TEMP(returnVar));


          //  return MEM(BINOP(n.e1.accept(expCompiler), IROp.ADD, n.e2.accept(expCompiler)));
            }



        @Override
        public IRExp visit(ExpNewArray n) {
            //initialize sting variables
            String arrStart = FreshNameGenerator.makeName();
            String arrSize = FreshNameGenerator.makeName();

            //start of the method
            IRStm StmArrSize = MOVE(TEMP(arrSize), n.e.accept(expCompiler));
            IRStm StmArrayStart = MOVE(TEMP(arrStart), CALL(NAME("_malloc"), BINOP(TEMP(arrSize), IROp.ADD, CONST(1))));

            //initialization of an array with zeroes.
            String count = FreshNameGenerator.makeName();
            String main1 = FreshNameGenerator.makeName();
            String main2 = FreshNameGenerator.makeName();
            String start = FreshNameGenerator.makeName();

            //start of the loop
            IRStm setCount = MOVE(TEMP(count), CONST(1));
            IRStm labelStart = LABEL(start);
            IRStm startLoop = CJUMP(BINOP(TEMP(count), IROp.EQ, BINOP(TEMP(arrSize),IROp.ADD, CONST(1))), IROp.EQ, CONST(0), main1, main2);
            IRStm Lmain1 = LABEL(main1);
            IRStm make0 = MOVE(MEM(BINOP(TEMP(arrStart), IROp.ADD, TEMP(count))), CONST(0));
            IRStm increaseCount = MOVE(TEMP(count), BINOP(TEMP(count), IROp.ADD, CONST(1)));
            IRStm goStart = JUMP(NAME(start));
            IRStm Lmain2 = LABEL(main2);

            // end of the method
            IRStm executeLoop = SEQ(setCount, labelStart, startLoop, Lmain1, make0, increaseCount, goStart, Lmain2);
            IRStm newLength = MOVE(MEM(TEMP(arrStart)), TEMP(arrSize));
            IRStm sequence = SEQ(StmArrSize, StmArrayStart, executeLoop, newLength);
            return ESEQ(sequence, BINOP(TEMP(arrStart), IROp.ADD, CONST(1)));

        }
    }


}
