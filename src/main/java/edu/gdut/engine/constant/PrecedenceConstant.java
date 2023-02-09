package edu.gdut.engine.constant;

import edu.gdut.engine.core.Precedence;
import edu.gdut.engine.core.SyntaxNode;
import edu.gdut.engine.exception.RuleEvalException;

/**
 * 运算符优先级常量
 *
 * @author Trust
 */
public interface PrecedenceConstant {
    Precedence Multiplicative = new Precedence(Symbol.MultiKindsToSymbol, null, (builder, curPre) -> {
        if (!builder.getParser().hasNext()) return null;

        var tok = builder.getParser().next();
        if (tok.getKind() == Kind.OpenParen) {
            // 最高优先级
            SyntaxNode ret = builder.build();
            builder.getParser().next();
            return new SyntaxNode(null, ret, Symbol.NOOP, null);
        } else if (tok.getKind() == Kind.Identifier)
            return new SyntaxNode(null, null, Symbol.VALUE, tok.getValue());
        else if (tok.getKind() == Kind.IntegerLiteral)
            return new SyntaxNode(null, null, Symbol.LITERAL, tok.getValue(), TypeFlags.TypeInteger);
        else if (tok.getKind() == Kind.FloatLiteral)
            return new SyntaxNode(null, null, Symbol.LITERAL, tok.getValue(), TypeFlags.TypeFloat);
        else if (tok.getKind() == Kind.BoolLiteral)
            return new SyntaxNode(null, null, Symbol.LITERAL, tok.getValue(), TypeFlags.TypeBool);
        else if (tok.getKind() == Kind.StringLiteral)
            return new SyntaxNode(null, null, Symbol.LITERAL, tok.getValue(), TypeFlags.TypeString);
        else if (tok.getKind() == Kind.Subtraction) {
            SyntaxNode ret = curPre.plan(builder);
            return SyntaxNode.NewNodeWithPrefixFix(ret, Symbol.NEGATIVE, null);
        } else if (tok.getKind() == Kind.Addition) {
            SyntaxNode ret = curPre.plan(builder);
            return SyntaxNode.NewNodeWithPrefixFix(ret, Symbol.POSITIVE, null);
        } else if (tok.getKind() == Kind.Not) {
            builder.getParser().rewind();
            return null;
        } else
            throw new RuleEvalException("Unable to plan token kind: '" + tok.getKind() + "', value: '" + tok.getValue() + "'");
    });

    Precedence Additive = new Precedence(Symbol.AddKindsToSymbol, Multiplicative, null);

    Precedence Comparator = new Precedence(Symbol.CompareKindsToSymbol, Additive, null);

    Precedence LogicalNot = new Precedence(Symbol.NotKindsToSymbol, Comparator, null);

    Precedence LogicAnd = new Precedence(Symbol.AndKindsToSymbol, LogicalNot, null);

    Precedence LogicalOr = new Precedence(Symbol.OrKindsToSymbol, LogicAnd, null);

    Precedence LowestPrecedence = LogicalOr;
}
