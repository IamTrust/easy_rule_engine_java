package edu.gdut.engine.core;

import edu.gdut.engine.constant.Symbol;
import edu.gdut.engine.constant.SymbolConstant;
import edu.gdut.engine.constant.TypeFlags;
import edu.gdut.engine.exception.RuleEvalException;

import java.util.HashMap;
import java.util.Map;

/**
 * 各种运算操作的实现
 *
 * @author Trust
 */
public class OperatorImpl {
    /**
     * 参数
     */
    public static Operator parameterOperator = (root, left, right, parameters) -> {
        Map<String, Object> ret = new HashMap<>();
        Object value = parameters.get((String) root.getValue());
        TypeFlags tp = getType(value);
        if (tp.isNull()) {
            throw new RuleEvalException("unsupported type");
        }
        ret.put(SymbolConstant.VALUE, value);
        ret.put(SymbolConstant.TYPE, tp);
        return ret;
    };
    /**
     * 字面值
     */
    public static Operator literalOperator = ((root, left, right, parameters) -> new HashMap<>() {
        {
            put(SymbolConstant.VALUE, root.getValue());
            put(SymbolConstant.TYPE, root.getTp());
        }
    });
    /**
     * 空操作
     */
    public static Operator noopOperator = (root, left, right, parameters) -> new HashMap<>() {
        {
            put(SymbolConstant.VALUE, right.getValue());
            put(SymbolConstant.TYPE, right.getTp());
        }
    };
    /**
     * a == b
     */
    public static Operator equalOperator = (root, left, right, parameters) -> {
        if (left.getTp().isNumber() && right.getTp().isNumber()) {
            return execNumberBinOp(left, right, Symbol.EQ);
        } else if (left.getTp().isString() && right.getTp().isString()) {
            return new HashMap<>() {
                {
                    put(SymbolConstant.VALUE, left.getValue().equals(right.getValue()));
                    put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                }
            };
        } else {
            // 肯定是 bool 类型
            return new HashMap<>() {
                {
                    put(SymbolConstant.VALUE, left.getValue() == right.getValue());
                    put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                }
            };
        }
    };
    /**
     * a != b
     */
    public static Operator notEqualOperator = (root, left, right, parameters) -> {
        {
            if (left.getTp().isNumber() && right.getTp().isNumber())
                return execNumberBinOp(left, right, Symbol.NEQ);
            else if (left.getTp().isString() && right.getTp().isString()) {
                return new HashMap<>() {
                    {
                        put(SymbolConstant.VALUE, !left.getValue().equals(right.getValue()));
                        put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                    }
                };
            } else
                return new HashMap<>() {
                    {
                        put(SymbolConstant.VALUE, left.getValue() != right.getValue());
                        put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                    }
                };
        }
    };
    /**
     * a > b
     */
    public static Operator gtOperator = (root, left, right, parameters) -> {
        if (left.getTp().isNumber() && right.getTp().isNumber())
            return execNumberBinOp(left, right, Symbol.GT);
        else {
            // left > right -----> ret > 0
            boolean val = ((String) left.getValue()).compareTo((String) right.getValue()) > 0;
            return new HashMap<>() {
                {
                    put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                    put(SymbolConstant.VALUE, val);
                }
            };
        }
    };
    /**
     * a < b
     */
    public static Operator ltOperator = (root, left, right, parameters) -> {
        if (left.getTp().isNumber() && right.getTp().isNumber())
            return execNumberBinOp(left, right, Symbol.LT);
        else {
            // left < right -----> ret < 0
            boolean val = ((String) left.getValue()).compareTo((String) right.getValue()) < 0;
            return new HashMap<>() {
                {
                    put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                    put(SymbolConstant.VALUE, val);
                }
            };
        }
    };
    /**
     * a >= b
     */
    public static Operator gteOperator = (root, left, right, parameters) -> {
        if (left.getTp().isNumber() && right.getTp().isNumber())
            return execNumberBinOp(left, right, Symbol.GTE);
        else {
            // left >= right -----> ret >= 0
            boolean val = ((String) left.getValue()).compareTo((String) right.getValue()) >= 0;
            return new HashMap<>() {
                {
                    put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                    put(SymbolConstant.VALUE, val);
                }
            };
        }
    };
    /**
     * a <= b
     */
    public static Operator lteOperator = (root, left, right, parameters) -> {
        if (left.getTp().isNumber() && right.getTp().isNumber())
            return execNumberBinOp(left, right, Symbol.GTE);
        else {
            // left <= right -----> ret <= 0
            boolean val = ((String) left.getValue()).compareTo((String) right.getValue()) <= 0;
            return new HashMap<>() {
                {
                    put(SymbolConstant.TYPE, TypeFlags.TypeBool);
                    put(SymbolConstant.VALUE, val);
                }
            };
        }
    };
    /**
     * a && b
     */
    public static Operator andOperator = (root, left, right, parameters) -> new HashMap<>() {
        {
            put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            put(SymbolConstant.VALUE, (boolean) left.getValue() && (boolean) right.getValue());
        }
    };
    /**
     * a || b
     */
    public static Operator orOperator = (root, left, right, parameters) -> new HashMap<>() {
        {
            put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            put(SymbolConstant.VALUE, (boolean) left.getValue() || (boolean) right.getValue());
        }
    };
    /**
     * a + b
     */
    public static Operator addOperator = (root, left, right, parameters) -> {
        if (left.getTp().isString() && right.getTp().isString()) {
            String val = left.getValue() + (String)right.getValue();
            return new HashMap<>() {
                {
                    put(SymbolConstant.TYPE, TypeFlags.TypeString);
                    put(SymbolConstant.VALUE, val);
                }
            };
        } else
            return execNumberBinOp(left, right, Symbol.PLUS);
    };
    /**
     * -
     * 减, a - b
     */
    public static Operator subtractOperator = (root, left, right, parameters) -> execNumberBinOp(left, right, Symbol.MINUS);

    public static Operator multiplyOperator = (root, left, right, parameters) -> execNumberBinOp(left, right, Symbol.MULTIPLY);
    /**
     * a / b
     */
    public static Operator divideOperator = (root, left, right, parameters) -> execNumberBinOp(left, right, Symbol.DIVIDE);
    /**
     * a % b
     */
    public static Operator modulusOperator = (root, left, right, parameters) -> execNumberBinOp(left, right, Symbol.MODULUS);
    /**
     * !a
     */
    public static Operator invertOperator = (root, left, right, parameters) -> new HashMap<>() {
        {
            put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            put(SymbolConstant.VALUE, !(boolean)right.getValue());
        }
    };
    /**
     * -
     * 负, -a
     */
    public static Operator negateOperator = (root, left, right, parameters) -> {
        Map<String, Object> ret = new HashMap<>();
        if (right.getTp() == TypeFlags.TypeFloat)
            ret.put(SymbolConstant.VALUE, -(double)right.getValue());
        else
            ret.put(SymbolConstant.VALUE, -(long)right.getValue());
        ret.put(SymbolConstant.TYPE, right.getTp());
        return ret;
    };

    /**
     * 语法树左节点和右节点都是数值类型的统一处理
     */
    private static Map<String, Object> execNumberBinOp(SyntaxNode left, SyntaxNode right, Symbol op) throws RuleEvalException {
        Object value1 = left.getValue();
        Object value2 = right.getValue();

        long v1 = 0, v2 = 0;
        double v3 = 0, v4 = 0;

        boolean isInt;
        if ((value1 instanceof Integer || value1 instanceof Long) && (value2 instanceof Integer || value2 instanceof Long)) {
            isInt = true;
            if (value1 instanceof Integer)
                v1 = (long) (int) value1;
            else
                v1 = (long) value1;
            if (value2 instanceof Integer)
                v2 = (long) (int) value2;
            else
                v2 = (long) value2;
        }
        else if ((value1 instanceof Double || value1 instanceof Float) && (value2 instanceof Double || value2 instanceof Float)) {
            isInt = false;
            if (value1 instanceof Double)
                v3 = (double) value1;
            else
                v3 = (double) (float) value1;
            if (value2 instanceof Double)
                v4 = (double) value2;
            else
                v4 = (double) (float) value2;
        }
        else throw new RuleEvalException("unsupported type: " + value1 + ", " + value2);

        Map<String, Object> ret = new HashMap<>();
        if (op == Symbol.PLUS) { // +
            if (isInt) {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeInteger);
                ret.put(SymbolConstant.VALUE, v1 + v2);
            } else {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeFloat);
                ret.put(SymbolConstant.VALUE, v3 + v4);
            }
        } else if (op == Symbol.MINUS) { // -
            if (isInt) {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeInteger);
                ret.put(SymbolConstant.VALUE, v1 - v2);
            } else {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeFloat);
                ret.put(SymbolConstant.VALUE, v3 - v4);
            }
        } else if (op == Symbol.MULTIPLY) { // *
            if (isInt) {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeInteger);
                ret.put(SymbolConstant.VALUE, v1 * v2);
            } else {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeFloat);
                ret.put(SymbolConstant.VALUE, v3 * v4);
            }
        } else if (op == Symbol.DIVIDE) { // /
            if (isInt) {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeInteger);
                ret.put(SymbolConstant.VALUE, v1 / v2);
            } else {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeFloat);
                ret.put(SymbolConstant.VALUE, v3 / v4);
            }
        } else if (op == Symbol.MODULUS) { // %
            if (isInt) {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeInteger);
                ret.put(SymbolConstant.VALUE, v1 % v2);
            } else {
                ret.put(SymbolConstant.TYPE, TypeFlags.TypeFloat);
                ret.put(SymbolConstant.VALUE, v3 % v4);
            }
        } else if (op == Symbol.GT) { // >
            ret.put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            if (isInt) {
                ret.put(SymbolConstant.VALUE, v1 > v2);
            } else {
                ret.put(SymbolConstant.VALUE, v3 > v4);
            }
        } else if (op == Symbol.GTE) { // >=
            ret.put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            if (isInt) {
                ret.put(SymbolConstant.VALUE, v1 >= v2);
            } else {
                ret.put(SymbolConstant.VALUE, v3 >= v4);
            }
        } else if (op == Symbol.LT) { // <
            ret.put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            if (isInt) {
                ret.put(SymbolConstant.VALUE, v1 < v2);
            } else {
                ret.put(SymbolConstant.VALUE, v3 < v4);
            }
        } else if (op == Symbol.LTE) { // <=
            ret.put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            if (isInt) {
                ret.put(SymbolConstant.VALUE, v1 <= v2);
            } else {
                ret.put(SymbolConstant.VALUE, v3 <= v4);
            }
        } else if (op == Symbol.EQ) { // ==
            ret.put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            if (isInt) {
                ret.put(SymbolConstant.VALUE, v1 == v2);
            } else {
                ret.put(SymbolConstant.VALUE, v3 == v4);
            }
        } else if (op == Symbol.NEQ) { // !=
            ret.put(SymbolConstant.TYPE, TypeFlags.TypeBool);
            if (isInt) {
                ret.put(SymbolConstant.VALUE, v1 != v2);
            } else {
                ret.put(SymbolConstant.VALUE, v3 != v4);
            }
        } else
            throw new RuleEvalException("engine: unreachable code");

        return ret;
    }

    private static TypeFlags getType(Object value) {
        if (value instanceof Integer) return TypeFlags.TypeInteger;
        else if (value instanceof Long) return TypeFlags.TypeInteger;
        else if (value instanceof Float) return TypeFlags.TypeFloat;
        else if (value instanceof Double) return TypeFlags.TypeFloat;
        else if (value instanceof String) return TypeFlags.TypeString;
        else if (value instanceof Boolean) return TypeFlags.TypeBool;
        else return TypeFlags.TypeNull;
    }
}
