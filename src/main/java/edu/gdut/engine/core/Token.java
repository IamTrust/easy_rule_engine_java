package edu.gdut.engine.core;

import edu.gdut.engine.constant.Kind;
import edu.gdut.engine.constant.KindConstant;

/**
 * <h1>
 * 语法单元
 * </h1>
 * 例如对于表达式: a >= 10 && b < 5 ,
 * 其中 a 是一个语法单元, >= 又是一个语法单元, 10 又是一个语法单元......
 *
 * @author Trust
 */
public class Token {
    private Kind kind;      // 类型, 例如是 变量 或 布尔字面量 或 整型字面量 或 大于 或 大于等于 ...
    private Object value;   // 具体值
    private int position;   // 在表达式中的位置(发生错误的时候用于提醒表达式哪里写错了)

    public Token() {
        kind = Kind.Illegal;
    }

    /**
     * 查看一个字符串是布尔字面量(true/false)还是变量名(a、b、userLevel...)
     * @param ident 字符串
     * @return 根据结果返回枚举 Kind 的对应值
     */
    public static Kind lookup(String ident) {
        Kind res = KindConstant.KEYWORDS.get(ident);
        if (res != null) return res;
        return Kind.Identifier;
    }

    /**
     * 查看一个操作符是什么操作符
     * @param op 操作符
     * @return 返回操作符类型
     */
    public static Kind lookupOperator(String op) {
        Kind res = KindConstant.OPERATOR_KIND.get(op);
        if (res != null) return res;
        return Kind.Illegal;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
