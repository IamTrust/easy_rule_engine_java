package edu.gdut.engine.exception;

/**
 * 规则执行时异常
 * @author Trust
 */
public class RuleEvalException extends Exception {
    public RuleEvalException() {
    }

    public RuleEvalException(String message) {
        super(message);
    }
}
