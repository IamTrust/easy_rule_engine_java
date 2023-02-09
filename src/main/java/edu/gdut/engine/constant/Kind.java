package edu.gdut.engine.constant;

import edu.gdut.engine.core.LexerState;
import edu.gdut.engine.exception.CompileException;

/**
 * 定义语法类型
 *
 * @author Trust
 */
public enum Kind {
    EOF,            // 终结符

    Identifier,     // 变量, 例如 a b c uid userLevel ...
    BoolLiteral,    // 布尔关键字, true false
    IntegerLiteral, // 整形数值, 1 2 3 19 201 1234 ...
    FloatLiteral,   // 浮点数值, 1.23 3.14 ...
    StringLiteral,  // 字符串数值, "abc" ...

    OpenParen,      // 左括号 (
    CloseParen,     // 右括号 )

    /* 算数运算符 */

    Addition,       // +
    Subtraction,    // -
    Multiply,       // *
    Divide,         // /
    Modulus,        // %

    /* 比较运算符 */
    GreaterThan,    // >
    GreaterEqual,   // >=
    LessThan,       // <
    LessEqual,      // <=
    Equal,          // ==
    NotEqual,       // !=

    /* 逻辑运算符 */
    And,            // &&
    Or,             // ||
    Not,            // !

    /* 非法字符 */
    Illegal;

    /**
     * 获取转换状态机
     * 当前语法单元可以跟哪些语法单元
     */
    public LexerState getLexerState() throws CompileException {
        LexerState state = KindConstant.VALID_LEXER_STATES.get(this);
        if (state != null) return state;
        throw new CompileException("No lexer state found for token kind: '" + this.name() + "'");
    }
}
