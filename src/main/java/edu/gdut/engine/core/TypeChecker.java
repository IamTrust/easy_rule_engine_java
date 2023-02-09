package edu.gdut.engine.core;

/**
 * 函数式接口, 类型检查
 */
public interface TypeChecker {
    boolean check(SyntaxNode left, SyntaxNode right);
}
