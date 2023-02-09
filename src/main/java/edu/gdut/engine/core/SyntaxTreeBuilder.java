package edu.gdut.engine.core;

import edu.gdut.engine.constant.PrecedenceConstant;
import edu.gdut.engine.exception.RuleEvalException;

/**
 * 语法树构造器
 */
public class SyntaxTreeBuilder {
    private final Precedence rootPlanner;
    private final Parser parser;

    public Parser getParser() {
        return parser;
    }

    public SyntaxTreeBuilder(Parser parser) {
        this.rootPlanner = PrecedenceConstant.LowestPrecedence;
        this.parser = parser;
    }

    public SyntaxNode build() throws RuleEvalException {
        if (parser == null)
            throw new RuleEvalException("parse is null");

        if (rootPlanner != null) {
            return rootPlanner.plan(this);
        }

        throw new RuleEvalException("build failed");
    }
}
