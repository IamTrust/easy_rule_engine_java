package edu.gdut.engine.constant;

/**
 * 类型定义
 *
 * @author Trust
 */
public enum TypeFlags {
    TypeNull,
    TypeBool,
    TypeInteger,
    TypeFloat,
    TypeString;

    public boolean isNull() {
        return this == TypeNull;
    }

    public boolean isNumber() {
        return this == TypeInteger || this == TypeFloat;
    }

    public boolean isString() {
        return this == TypeString;
    }

    public boolean isBool() {
        return this == TypeBool;
    }
}
