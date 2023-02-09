package edu.gdut.engine.core;

import edu.gdut.engine.constant.Kind;
import edu.gdut.engine.exception.CompileException;

import java.util.List;

/**
 * 语法分析
 *
 * @author Trust
 */
public class Parser {
    private final List<Token> tokens;
    private int index;
    private final int tokenLength;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        tokenLength = tokens.size();
    }

    /**
     * 检查 tokens 中的每个语法单元 Token 的语法是否正确。
     * 例如 (a + (b > c) 这样的表达式生成的 tokens 是有误的(少一个右括号)、
     * a + 100 b < 200 这样的表达式也是有误的(100 b 非法)。
     * @throws CompileException 语法有误抛出异常。
     */
    public void ParseSyntax() throws CompileException {
        checkBalance(); // 检查括号是否有误

        // 有限自动机检查表达式是否非法
        Token lastTok = new Token(); // 默认创建的是非法字符 Token
        LexerState lexerState = lastTok.getKind().getLexerState();
        while (hasNext()) {
            Token tok = next();
            if (!lexerState.canTransitionTo(tok.getKind()))
                throw new CompileException("cannot transition token types from " + lastTok.getKind() + " to " + tok.getKind() + ".");
            lexerState = tok.getKind().getLexerState();
            lastTok = tok;
        }

        // a + b +
        if (!lexerState.isEOF())
            throw new CompileException("unexpected end of expression.");
        reset();
    }

    public Token next() {
        Token tok = tokens.get(index);
        index++;
        return tok;
    }

    public boolean hasNext() {
        return index < tokenLength;
    }

    private void checkBalance() throws CompileException {
        int parens = 0;
        while (hasNext()) {
            Token tok = next();
            if (tok.getKind() == Kind.OpenParen) {
                parens++;
                continue;
            }
            if (tok.getKind() == Kind.CloseParen) {
                parens--;
                //continue;
            }
        }
        if (parens != 0) {
            throw new CompileException("unbalanced parenthesis");
        }
        reset();
    }

    private void reset() {
        index = 0;
    }

    public void rewind() {
        index -= 1;
    }
}
