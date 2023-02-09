package edu.gdut.engine.core;

import edu.gdut.engine.constant.Symbol;
import edu.gdut.engine.constant.SymbolConstant;
import edu.gdut.engine.constant.TypeFlags;
import edu.gdut.engine.exception.RuleEvalException;

import java.util.Map;

/**
 * 抽象语法树节点
 */
public class SyntaxNode {
    private final Symbol symbol;
    private Object value;
    private TypeFlags tp;
    private SyntaxNode leftNode, rightNode;
    private Operator operator;
    private TypeChecker typeChecker;

    public SyntaxNode(SyntaxNode leftNode, SyntaxNode rightNode, Symbol symbol, Object value) {
        this(leftNode, rightNode, symbol, value, TypeFlags.TypeNull);
    }

    public SyntaxNode(SyntaxNode leftNode, SyntaxNode rightNode, Symbol symbol, Object value, TypeFlags tp) {
        this.symbol = symbol;
        this.value = value;
        this.tp = tp;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.operator = symbol.getOperator();
        this.typeChecker = symbol.getTypeChecker();
    }

    public static SyntaxNode NewNodeWithPrefixFix(SyntaxNode right, Symbol symbol, Object value) throws RuleEvalException {
        Boolean needFixed = Symbol.NeedFixedSymbol.get(symbol);
        if (!needFixed)
            throw new RuleEvalException("should not use this new node function for current symbol");
        if (right != null && right.getRightNode() != null && right.getSymbol() != Symbol.NEGATIVE && right.getSymbol() != Symbol.POSITIVE) {
            right.setLeftNode(new SyntaxNode(null, right.getLeftNode(), symbol, value));
            return right;
        } else {
            return new SyntaxNode(null, right, symbol, value);
        }
    }

    /**
     * 注入参数到语法树并执行语法树
     * @param params 待注入的参数
     * @throws RuleEvalException 执行语法树异常, 例如待注入的参数类型和语法树中对应的参数类型不匹配,
     * 或者参数名和语法树中不一致等.
     */
    public void eval(Map<String, Object> params) throws RuleEvalException {
        if (this.leftNode != null)
            this.leftNode.eval(params);

        if (this.rightNode != null)
            this.rightNode.eval(params);

        if (this.typeChecker != null)
            if (!this.typeChecker.check(this.leftNode, this.rightNode))
                throw new RuleEvalException("params type error");

        Map<String, Object> ret = this.operator.operate(this, this.leftNode, this.rightNode, new MapParameters(params));

        this.value = ret.get(SymbolConstant.VALUE);
        this.tp = (TypeFlags) ret.get(SymbolConstant.TYPE);
    }

    /**
     * 执行结果为 bool
     */
    public boolean getBoolVal() {
        return (boolean) value;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Object getValue() {
        return value;
    }

    public TypeFlags getTp() {
        return tp;
    }

    public SyntaxNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(SyntaxNode leftNode) {
        this.leftNode = leftNode;
    }

    public SyntaxNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(SyntaxNode rightNode) {
        this.rightNode = rightNode;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public TypeChecker getTypeChecker() {
        return typeChecker;
    }

    public void setTypeChecker(TypeChecker typeChecker) {
        this.typeChecker = typeChecker;
    }
}
