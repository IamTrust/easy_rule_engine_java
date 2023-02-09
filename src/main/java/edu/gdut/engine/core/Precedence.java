package edu.gdut.engine.core;

import edu.gdut.engine.constant.Kind;
import edu.gdut.engine.constant.Symbol;
import edu.gdut.engine.exception.RuleEvalException;

import java.util.Map;

/**
 * 运算符优先级
 *
 * @author Trust
 */
public class Precedence {
    /*
     * 当前优先级的 Token 类型
     */
    private final Map<Kind, Symbol> validKindsToSymbols;
    /*
     * 更高优先级的 Token 类型
     */
    private final Precedence nextPrecedence;
    private final Planner planner;

    public Precedence(Map<Kind, Symbol> validKindsToSymbols, Precedence nextPrecedence, Planner planner) {
        this.validKindsToSymbols = validKindsToSymbols;
        this.nextPrecedence = nextPrecedence;
        this.planner = planner;
    }

    /**
     * 递归下降算法构造抽象语法树
     */
    public SyntaxNode plan(SyntaxTreeBuilder builder) throws RuleEvalException {
        SyntaxNode leftNode = null, rightNode;
        if (nextPrecedence != null) {
            leftNode = nextPrecedence.plan(builder);
        } else if (planner != null) {
            leftNode = planner.planValue(builder, this);
        }

        while (builder.getParser().hasNext()) {
            var tok = builder.getParser().next();
            if (tok.getKind() == Kind.EOF) break;

            Symbol symbol = validKindsToSymbols.get(tok.getKind());
            if (symbol == null) break;

            rightNode = plan(builder);

            return new SyntaxNode(leftNode, rightNode, symbol, null);
        }
        builder.getParser().rewind();
        return leftNode;
    }
}
