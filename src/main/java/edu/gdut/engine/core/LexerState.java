package edu.gdut.engine.core;

import edu.gdut.engine.constant.Kind;

/**
 * 状态转换
 * 记录一个语法单元可以跟哪些语法单元
 *
 * @author Trust
 */
public class LexerState {
    private final boolean isEOF;
    private final Kind[] validNextKinds;

    public LexerState(boolean isEOF, Kind[] validNextKinds) {
        this.isEOF = isEOF;
        this.validNextKinds = validNextKinds;
    }

    public boolean canTransitionTo(Kind k) {
        for (Kind validKind : validNextKinds) {
            if (validKind == k) return true;
        }
        return false;
    }

    public boolean isEOF() {
        return isEOF;
    }
}
