package edu.gdut.engine.core;

/**
 * 用于参数注入时获取参数值
 *
 * @author Trust
 */
public interface Parameters {
    Object get(String name);
}
