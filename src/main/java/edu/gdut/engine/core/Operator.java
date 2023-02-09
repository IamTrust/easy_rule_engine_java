package edu.gdut.engine.core;

import edu.gdut.engine.exception.RuleEvalException;

import java.util.Map;

/**
 * 用于执行语法树
 *
 * @author Trust
 */
public interface Operator {
    /**
     *
     *
     * @param root 语法树跟节点
     * @param left 语法树左节点
     * @param right 语法树右节点
     * @param parameters 参数
     * @return 返回类型和值 {type: Xxx, Value: Xxx}
     */
    Map<String, Object> operate(SyntaxNode root,
                                SyntaxNode left,
                                SyntaxNode right,
                                Parameters parameters) throws RuleEvalException;
}
