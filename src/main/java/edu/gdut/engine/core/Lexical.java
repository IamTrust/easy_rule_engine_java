package edu.gdut.engine.core;

import edu.gdut.engine.constant.KindConstant;

/**
 * 字符判断工具类
 *
 * @author Trust
 */
public class Lexical {
    public static boolean isEof(byte ch) {
        return ch == KindConstant.EOF_BYTE;
    }

    public static boolean isLetter(byte ch) {
        return 'a' <= ch && ch <= 'z' ||
                'A' <= ch && ch <= 'Z' ||
                ch == '_' ||
                Character.isLetter(ch);
    }

    public static boolean isDecimal(byte ch) {
        return '0' <= ch && ch <= '9';
    }

    public static boolean isDot(byte ch) {
        return ch == '.';
    }

    public static boolean isDigit(byte ch) {
        return isDecimal(ch) || Character.isDigit(ch);
    }

    public static int digitVal(byte ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        } else if ('a' <= lower(ch) && lower(ch) <= 'f') {
            return lower(ch) - 'a' + 10;
        }
        return 16;
    }

    private static int lower(byte ch) {
        return ('a' - 'A') | ch;
    }
}
