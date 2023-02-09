package edu.gdut.engine.exception;

/**
 * 编译规则表达式时异常
 * @author Trust
 */
public class CompileException extends Exception {
    public CompileException() {
    }

    public CompileException(String message) {
        super(message);
    }
}
