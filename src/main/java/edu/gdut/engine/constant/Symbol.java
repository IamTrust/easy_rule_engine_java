package edu.gdut.engine.constant;

import edu.gdut.engine.core.OperatorImpl;
import edu.gdut.engine.core.Operator;
import edu.gdut.engine.core.TypeChecker;
import edu.gdut.engine.core.TypeCheckerImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * 运算符定义
 *
 * @author Trust
 */
public enum Symbol {
    VALUE,
    LITERAL,        // var string int float bool
    NOOP,           // noop
    EQ,             // ==
    NEQ,            // !=
    GT,             // >
    LT,             // <
    GTE,            // >=
    LTE,            // <=
    AND,            // &&
    OR,             // ||
    PLUS,           // +
    MINUS,          // -
    MULTIPLY,       // *
    DIVIDE,         // /
    MODULUS,        // %
    INVERT,         // ！
    POSITIVE,       // +
    NEGATIVE;       // -

    public static final Map<Kind, Symbol> MultiKindsToSymbol = new HashMap<>() {
        {
            put(Kind.Multiply, MULTIPLY);
            put(Kind.Divide, DIVIDE);
            put(Kind.Modulus, MODULUS);
        }
    };

    public static final Map<Kind, Symbol> AddKindsToSymbol = new HashMap<>() {
        {
            put(Kind.Addition, PLUS);
            put(Kind.Subtraction, MINUS);
        }
    };

    public static final Map<Kind, Symbol> CompareKindsToSymbol = new HashMap<>() {
        {
            put(Kind.GreaterThan, GT);
            put(Kind.GreaterEqual, GTE);
            put(Kind.LessThan, LT);
            put(Kind.LessEqual, LTE);
            put(Kind.Equal, EQ);
            put(Kind.NotEqual, NEQ);
        }
    };

    public static final Map<Kind, Symbol> OrKindsToSymbol = new HashMap<>() {
        {
            put(Kind.Or, OR);
        }
    };

    public static final Map<Kind, Symbol> AndKindsToSymbol = new HashMap<>() {
        {
            put(Kind.And, AND);
        }
    };

    public static final Map<Kind, Symbol> NotKindsToSymbol = new HashMap<>() {
        {
            put(Kind.Not, INVERT);
        }
    };

    public static final Map<Symbol, Boolean> NeedFixedSymbol = new HashMap<>() {
        {
            put(POSITIVE, true);
            put(NEGATIVE, true);
        }
    };

    public static final Map<Symbol, Operator> SymbolToOperator = new HashMap<>() {
        {
            put(VALUE, OperatorImpl.parameterOperator);
            put(LITERAL, OperatorImpl.literalOperator);
            put(NOOP, OperatorImpl.noopOperator);
            put(EQ, OperatorImpl.equalOperator);
            put(NEQ, OperatorImpl.notEqualOperator);
            put(GT, OperatorImpl.gtOperator);
            put(LT, OperatorImpl.ltOperator);
            put(GTE, OperatorImpl.gteOperator);
            put(LTE, OperatorImpl.lteOperator);
            put(AND, OperatorImpl.andOperator);
            put(OR, OperatorImpl.orOperator);
            put(PLUS, OperatorImpl.addOperator);
            put(MINUS, OperatorImpl.subtractOperator);
            put(MULTIPLY, OperatorImpl.multiplyOperator);
            put(DIVIDE, OperatorImpl.divideOperator);
            put(MODULUS, OperatorImpl.modulusOperator);
            put(INVERT, OperatorImpl.invertOperator);
            put(NEGATIVE, OperatorImpl.negateOperator);
            put(POSITIVE, OperatorImpl.noopOperator);
        }
    };

    public static final Map<Symbol, TypeChecker> SymbolToTypeChecker = new HashMap<>() {
        {
            put(VALUE, null);
            put(LITERAL, null);
            put(NOOP, null);

            put(EQ, TypeCheckerImpl.matchChecker);
            put(NEQ, TypeCheckerImpl.matchChecker);

            put(GT, TypeCheckerImpl.numberOrStringChecker);
            put(LT, TypeCheckerImpl.numberOrStringChecker);
            put(GTE, TypeCheckerImpl.numberOrStringChecker);
            put(LTE, TypeCheckerImpl.numberOrStringChecker);

            put(AND, TypeCheckerImpl.doubleBoolChecker);
            put(OR, TypeCheckerImpl.doubleBoolChecker);

            put(PLUS, TypeCheckerImpl.numberOrStringChecker);

            put(MINUS, TypeCheckerImpl.doubleNumberChecker);
            put(MULTIPLY, TypeCheckerImpl.doubleNumberChecker);
            put(DIVIDE, TypeCheckerImpl.doubleNumberChecker);
            put(MODULUS, TypeCheckerImpl.doubleNumberChecker);

            put(INVERT, TypeCheckerImpl.singleBoolChecker);

            put(NEGATIVE, TypeCheckerImpl.singleNumberChecker);
            put(POSITIVE, TypeCheckerImpl.singleNumberChecker);
        }
    };

    public Operator getOperator() {
        return SymbolToOperator.get(this);
    }

    public TypeChecker getTypeChecker() {
        return SymbolToTypeChecker.get(this);
    }
}
