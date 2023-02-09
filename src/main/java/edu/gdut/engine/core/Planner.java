package edu.gdut.engine.core;

import edu.gdut.engine.exception.RuleEvalException;

/**
 * 函数式接口
 */
public interface Planner {
    SyntaxNode planValue(SyntaxTreeBuilder builder, Precedence curPre) throws RuleEvalException;
}
