package edu.gdut.engine.core;

import edu.gdut.engine.constant.Kind;
import edu.gdut.engine.constant.KindConstant;
import edu.gdut.engine.exception.CompileException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 表达式扫描器
 *
 * @author Trust
 */
public class Scan {
    private byte[] source; // 规则表达式字符串
    private int position;  // 当前扫描位置
    private final int length;    // 表达式长度
    private byte ch;       // 当前扫描位置的字符

    public Scan(String exp) {
        source = exp.getBytes(StandardCharsets.UTF_8);

        if (source.length == 0) {
            // 终结符
            source = new byte[]{KindConstant.EOF_BYTE};
        }

        ch = this.source[0];
        length = source.length;
    }

    /**
     * <p>
     * 扫描完整个表达式, 将得到的所有 Token 保存在 List 中返回。
     * 例如对于表达式 price > 100 && goodsNum <= 10 ,
     * 扫描完成后应该得到一个保存有所有 Token 的 List , List 中的每个 Token 大致如下:
     * Token{Kind=变量, Value=price, Position=...},
     * Token{Kind=大于, Value=">", Position=...},
     * Token{Kind=整型数值, Value=100, Position=...},
     * Token{Kind=逻辑与, Value="&&", Position=...},
     * Token{Kind=变量, Value="goodsNum", Position=...},
     * Token{Kind=小于等于, Value="<=", Position=...},
     * Token{Kind=整型数值, Value=10, Position=...}
     * </p>
     *
     * @return List
     */
    public List<Token> lexer() throws CompileException {
        List<Token> tokens = new ArrayList<>();

        for (; ; ) {
            Token tok = this.scan();
            tokens.add(tok);
            if (tok.getKind() == Kind.EOF) {
                // 扫描到结束符或者 scan() 方法抛出异常就停止循环
                break;
            }
        }

        return tokens;
    }

    /**
     * 一趟扫描, 生成一个 Token (语法单元)
     *
     * @return 语法单元
     */
    private Token scan() throws CompileException {
        Token tok = new Token();

        this.skipWhitespace();
        tok.setPosition(this.position);

        if (Lexical.isEof(ch)) { // 终结符
            tok.setKind(Kind.EOF);
        } else if (Lexical.isLetter(ch)) { // 字母
            // 如果是字母, 则可能是变量, 也可能是布尔字面量 true 或 false
            String literal = this.scanIdentifier();
            tok.setKind(Token.lookup(literal));
            tok.setValue(literal);
            if (tok.getKind() == Kind.BoolLiteral) {
                tok.setValue(Boolean.parseBoolean((String) tok.getValue()));
            }
        } else if (Lexical.isDecimal(ch) || Lexical.isDot(ch)) { // 数字或小数点
            String literal = this.scanNumber();
            try {
                if (literal.contains(".")) { // float
                    tok.setValue(Float.parseFloat(literal));
                    tok.setKind(Kind.FloatLiteral);
                } else { // int
                    tok.setValue(Integer.parseInt(literal));
                    tok.setKind(Kind.IntegerLiteral);
                }
            } catch (NumberFormatException e) { // 转换不成功抛出异常
                throw new CompileException("编译表达式失败, 数字格式有误, 请检查: " + e.getMessage());
            }
        } else {
            // 运算符
            switch (ch) {
                case '+', '-', '*', '/', '%', '(', ')' -> { // 确定的单一运算符
                    tok.setKind(Token.lookupOperator((char) ch + ""));
                    tok.setValue(this.read());
                }
                case '"', '\'' -> { // 双引号或单引号, 字符串类型
                    tok.setKind(Kind.StringLiteral);
                    tok.setValue(this.scanString());
                }
                case '`' -> { // 也支持 ` 这个符号表达字符串
                    tok.setKind(Kind.StringLiteral);
                    tok.setValue(this.scanRawString());
                }
                case '<' ->
                    // 判断是 < 还是 <=
                        this.scanSwitch2(tok, Kind.LessThan, '=', Kind.LessEqual);
                case '>' ->
                    // > or >=
                        this.scanSwitch2(tok, Kind.GreaterThan, '=', Kind.GreaterEqual);
                case '!' ->
                    // ! or !=
                        this.scanSwitch2(tok, Kind.Not, '=', Kind.NotEqual);
                case '=' ->
                    // =(不支持) or ==
                        this.scanSwitch2(tok, Kind.Illegal, '=', Kind.Equal);
                case '&' ->
                    // &(不支持) or &&
                        this.scanSwitch2(tok, Kind.Illegal, '&', Kind.And);
                case '|' ->
                    // |(不支持) or ||
                        this.scanSwitch2(tok, Kind.Illegal, '|', Kind.Or);
                default -> {
                    // 扫描到不支持的内容, 抛出异常
                    tok.setKind(Kind.Illegal);
                    tok.setValue(ch);
                }
            }

            if (tok.getKind() == Kind.Illegal) {
                throw new CompileException("表达式中包含不支持的字符: " + tok.getValue().toString());
            }

        }

        return tok;
    }

    /**
     * <p>
     * 扫描连续字符, 诸如 >= <= != == && || 这样的单个字符和连续两个字符时语义不同的语法
     * </p>
     *
     * @param tok     Token
     * @param curKind 当前字符类型, 例如 >
     * @param nxt     下一个字符应该是什么, 例如 curKind 为 > 的话, 下一个应该为 =
     * @param nxtKind 实际的下一个是什么
     */
    private void scanSwitch2(Token tok, Kind curKind, char nxt, Kind nxtKind) {
        StringBuilder sb = new StringBuilder();
        sb.append((char) read());
        if (ch == nxt) {
            sb.append((char) read());
            tok.setKind(nxtKind);
        } else {
            tok.setKind(curKind);
        }
        tok.setValue(new String(sb));
    }

    /**
     * 扫描字符串, 被 `` 包裹起来的字符串, 里面没有诸如 \n 这样的特殊字符
     *
     * @return String
     * @throws CompileException 出现语法错误时(例如双引号没有结束)抛出异常
     */
    private String scanRawString() throws CompileException {
        byte quote = read();
        int startPos = position;
        int endPos = position;
        for (; ; ) {
            byte c = read();
            if (Lexical.isEof(c)) {
                throw new CompileException("raw string literal not terminated (原始字符串没有终止) : " + new String(source, startPos, endPos));
            }
            if (c == quote) {
                endPos = position - 1;
                break;
            }
        }
        return new String(source).substring(startPos, endPos);
    }

    /**
     * 扫描字符串, 被 "" 包裹的字符串, 里面可能包含 \n \v 等特殊字符
     *
     * @return String
     * @throws CompileException 出现错误的语法时抛出异常
     */
    private String scanString() throws CompileException {
        byte quote = read();
        var startPos = position;
        var endPos = position;
        for (; ; ) {
            var c = read();
            if (Lexical.isEof(c)) {
                throw new CompileException("string literal not terminated (字符串未终止) : " + new String(source, startPos, endPos));
            }
            if (c == quote) {
                endPos = position - 1;
                break;
            }
            if (c == '\\') { // \x
                scanEscape(quote); // 可能抛出异常, 终止循环
            }
        }
        return new String(source).substring(startPos, endPos);
        //return new String(source, startPos, position); 不能使用这种方式创建
    }

    /**
     * 诸如 \n \t 等特殊字符处理
     * @param quote 特殊字符开头
     * @throws CompileException 处理失败抛出异常
     */
    private void scanEscape(byte quote) throws CompileException {
        int n = 0;
        int base = 0, max = 0;
        if (peek() == 'a' || peek() == 'b' ||
            peek() == 'f' || peek() == 'n' ||
            peek() == 'r' || peek() == 't' ||
            peek() == 'v' || peek() == '\\' ||
            peek() == '\'' || peek() == '"' ||
            peek() == quote) {
            read();
        } else if (peek() == '0' || peek() == '1' ||
                   peek() == '2' || peek() == '3' ||
                   peek() == '4' || peek() == '5' ||
                   peek() == '6' || peek() == '7') {
            n = 3;
            base = 8;
            max = 255;
        } else if (peek() == 'x') {
            read();
            n = 2;
            base = 16;
            max = 255;
        } else if (peek() == 'u') {
            read();
            n = 4;
            base = 16;
            max = Character.MAX_CODE_POINT;
        } else if (peek() == 'U') {
            read();
            n = 8;
            base = 16;
            max = Character.MAX_CODE_POINT;
        } else {
            String msg = "unknown escape sequence";
            if (peek() < 0) {
                msg = "escape sequence not terminated";
            }
            throw new CompileException(msg);
        }

        int x = 0;
        while (n > 0) {
            int d = Lexical.digitVal(peek());
            if (d >= base) {
                String msg = "illegal character " + peek() + " in escape sequence";
                if (peek() < 0) {
                    msg = "escape sequence not terminated";
                }
                throw new CompileException(msg);
            }
            x = x * base + d;
            read();
            n--;
        }

        if (x > max || 0xD800 <= x && x < 0xE000) {
            throw new CompileException("escape sequence is invalid Unicode code point");
        }
    }

    /**
     * 扫描数字, 包括整数和小数
     *
     * @return String
     */
    private String scanNumber() {
        var startPos = position;
        while (Lexical.isDigit(peek()) || Lexical.isDot(peek())) {
            read();
        }
        read();
        return new String(source).substring(startPos, position);
    }

    /**
     * 扫描变量, 例如 userLevel233
     * 变量可以由数字、字母、下划线组成, 但必须以字母或下划线开头
     * 也可能扫描到的是 true/false 布尔字面量
     *
     * @return String
     */
    private String scanIdentifier() {
        var startPos = position;
        while (Lexical.isLetter(peek()) || Lexical.isDigit(peek())) {
            read();
        }
        read();
        return new String(source).substring(startPos, position);
    }

    /**
     * 读取 position 位置的字符, 之后将 position 右移
     *
     * @return 返回 position 位置的字符, 如果已经读到末尾返回终结符
     */
    private byte read() {
        byte ch;

        if (!this.canRead()) {
            return KindConstant.EOF_BYTE;
        }

        ch = this.source[this.position];

        this.ch = this.peek();

        this.position += 1;

        return ch;
    }

    /**
     * 查看当前位置的下一个位置的字符, 不移动 position 指针
     *
     * @return 返回当前位置的下一个位置的字符, 如果已经到末尾则返回终结符
     */
    private byte peek() {
        if (position < length - 1) {
            return source[position + 1];
        }
        return KindConstant.EOF_BYTE;
    }

    /**
     * 跳过空格
     */
    private void skipWhitespace() {
        while (this.canRead() && Character.isSpaceChar(this.ch)) {
            this.read();
        }
    }

    /**
     * 判断是否已经读到末尾
     *
     * @return 已经读到末尾返回 false , 还可以读返回 true
     */
    private boolean canRead() {
        return this.position < this.length;
    }
}
