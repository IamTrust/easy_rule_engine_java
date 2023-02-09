package edu.gdut.engine.constant;

import edu.gdut.engine.core.LexerState;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量定义
 *
 * @author Trust
 */
public interface KindConstant {
    byte EOF_BYTE = -1;

    /* 字符串和对应类型的枚举的映射 */

    Map<String, Kind> KEYWORDS = new HashMap<>() {
        {
            put("true", Kind.BoolLiteral);
            put("false", Kind.BoolLiteral);
        }
    };

    Map<String, Kind> OPERATOR_KIND = new HashMap<>() {
        {
            put("(", Kind.OpenParen);
            put(")", Kind.CloseParen);

            put("+", Kind.Addition);
            put("-", Kind.Subtraction);
            put("*", Kind.Multiply);
            put("/", Kind.Divide);
            put("%", Kind.Modulus);

            put(">", Kind.GreaterThan);
            put(">=", Kind.GreaterEqual);
            put("<", Kind.LessThan);
            put("<=", Kind.LessEqual);
            put("==", Kind.Equal);
            put("!=", Kind.NotEqual);

            put("&&", Kind.And);
            put("||", Kind.Or);
            put("!", Kind.Not);
        }
    };

    /* 有限自动机, 定义状态转移 */

    Map<Kind, LexerState> VALID_LEXER_STATES = new HashMap<>() {
        {
            // 终结符可以跟哪些字符
            put(Kind.EOF, new LexerState(true, new Kind[]{}));
            // 非法字符可以跟哪些字符
            put(Kind.Illegal, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.BoolLiteral,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.StringLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction,
                    Kind.Not
            }));
            // 变量可以跟哪些字符
            put(Kind.Identifier, new LexerState(true, new Kind[]{
                    Kind.CloseParen,    // .... a )

                    Kind.Addition,      // a +
                    Kind.Subtraction,   // a -
                    Kind.Multiply,      // a *
                    Kind.Divide,        // a /
                    Kind.Modulus,       // a %

                    Kind.GreaterThan,   // a >
                    Kind.LessThan,      // a <
                    Kind.GreaterEqual,  // a >=
                    Kind.LessEqual,     // a <=
                    Kind.Equal,         // a ==
                    Kind.NotEqual,      // a !=

                    Kind.And,           // a &&
                    Kind.Or,            // a ||
                    Kind.EOF
            }));
            // 布尔可以跟哪些字符
            put(Kind.BoolLiteral, new LexerState(true, new Kind[]{
                    Kind.CloseParen,
                    Kind.Equal,
                    Kind.NotEqual,

                    Kind.And,
                    Kind.Or,
                    Kind.EOF
            }));
            // int
            put(Kind.IntegerLiteral, new LexerState(true, new Kind[]{
                    Kind.CloseParen,

                    Kind.Addition,
                    Kind.Subtraction,
                    Kind.Multiply,
                    Kind.Divide,
                    Kind.Modulus,

                    Kind.GreaterThan,
                    Kind.LessThan,
                    Kind.GreaterEqual,
                    Kind.LessEqual,
                    Kind.Equal,
                    Kind.NotEqual,
                    Kind.EOF,

                    Kind.And,
                    Kind.Or
            }));
            // float
            put(Kind.FloatLiteral, new LexerState(true, new Kind[]{
                    Kind.CloseParen,

                    Kind.Addition,
                    Kind.Subtraction,
                    Kind.Multiply,
                    Kind.Divide,
                    Kind.Modulus,

                    Kind.GreaterThan,
                    Kind.LessThan,
                    Kind.GreaterEqual,
                    Kind.LessEqual,
                    Kind.Equal,
                    Kind.NotEqual,
                    Kind.EOF,

                    Kind.And,
                    Kind.Or
            }));
            // string
            put(Kind.StringLiteral, new LexerState(true, new Kind[]{
                    Kind.CloseParen,
                    Kind.Addition,
                    Kind.Equal,
                    Kind.NotEqual,

                    Kind.EOF,

                    Kind.And,
                    Kind.Or
            }));
            // (
            put(Kind.OpenParen, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.BoolLiteral,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.StringLiteral,
                    Kind.Addition,
                    Kind.Subtraction,
                    Kind.Not
            }));
            // )
            put(Kind.CloseParen, new LexerState(true, new Kind[]{
                    Kind.Addition,
                    Kind.Subtraction,
                    Kind.Multiply,
                    Kind.Divide,
                    Kind.Modulus,

                    Kind.GreaterThan,
                    Kind.LessThan,
                    Kind.GreaterEqual,
                    Kind.LessEqual,
                    Kind.Equal,
                    Kind.NotEqual,

                    Kind.And,
                    Kind.Or,

                    Kind.EOF
            }));
            // +
            put(Kind.Addition, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.StringLiteral,
                    Kind.OpenParen,
                    Kind.Subtraction,
                    Kind.Addition
            }));
            // -
            put(Kind.Subtraction, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // *
            put(Kind.Multiply, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // /
            put(Kind.Divide, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // %
            put(Kind.Modulus, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen
            }));
            // >
            put(Kind.GreaterThan, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // >=
            put(Kind.GreaterEqual, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // <
            put(Kind.LessThan, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // <=
            put(Kind.LessEqual, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // ==
            put(Kind.Equal, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.StringLiteral,
                    Kind.BoolLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // !=
            put(Kind.NotEqual, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.IntegerLiteral,
                    Kind.FloatLiteral,
                    Kind.StringLiteral,
                    Kind.BoolLiteral,
                    Kind.OpenParen,
                    Kind.Addition,
                    Kind.Subtraction
            }));
            // &&
            put(Kind.And, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.BoolLiteral,
                    Kind.OpenParen,
                    Kind.Subtraction,
                    Kind.Addition,
                    Kind.FloatLiteral,
                    Kind.IntegerLiteral,
                    Kind.Not
            }));
            // ||
            put(Kind.Or, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.BoolLiteral,
                    Kind.OpenParen,
                    Kind.Subtraction,
                    Kind.Addition,
                    Kind.FloatLiteral,
                    Kind.IntegerLiteral,
                    Kind.Not
            }));
            // !
            put(Kind.Not, new LexerState(false, new Kind[]{
                    Kind.Identifier,
                    Kind.BoolLiteral,
                    Kind.OpenParen,
                    Kind.Not
            }));
        }
    };
}
