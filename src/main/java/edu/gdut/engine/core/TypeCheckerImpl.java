package edu.gdut.engine.core;

/**
 * <h1>
 * 类型检查
 * </h1>
 * 主要用于判断一个运算符左右两边的类型是否一致
 *
 * @author Trust
 */
public class TypeCheckerImpl {

    public static TypeChecker matchChecker = (left, right) -> left.getTp() == right.getTp();
    public static TypeChecker numberOrStringChecker = (left, right) -> (left.getTp().isString() && right.getTp().isString()) || (left.getTp().isNumber() && right.getTp().isNumber());
    public static TypeChecker doubleBoolChecker = (left, right) -> left.getTp().isBool() && right.getTp().isBool();
    public static TypeChecker doubleNumberChecker = (left, right) -> left.getTp().isNumber() && right.getTp().isNumber();
    public static TypeChecker singleBoolChecker = (left, right) -> right.getTp().isBool();
    public static TypeChecker singleNumberChecker = (left, right) -> right.getTp().isNumber();
}
