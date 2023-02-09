package edu.gdut.engine.core;

import edu.gdut.engine.exception.CompileException;
import edu.gdut.engine.exception.RuleEvalException;

import java.util.List;

/**
 * 编译器
 * @author Trust
 */
public class Compiler {
    /**
     * <h1>编译规则</h1>
     * 输入规则表达式, 返回生成的抽象语法树的根节点
     * @param exp 规则表达式, 例如 "price > 500 && (userLevel > 5 || isNewUser)"
     * @return 抽象语法树根节点
     * @throws CompileException 编译表达式时发生异常, 例如出现了不支持的字符、语法错误等
     */
    public static SyntaxNode compile(String exp) throws CompileException, RuleEvalException {
        // 编译阶段之扫描阶段
        Scan scan = new Scan(exp); // 扫描器
        List<Token> tokens = scan.lexer(); // 扫描得到所有语法单元(Token)
        Parser parser = new Parser(tokens); // 语法检查
        parser.ParseSyntax();   // 进行检查, 语法有误抛出异常
        // 编译阶段之构建语法树
        SyntaxTreeBuilder builder = new SyntaxTreeBuilder(parser); // 抽象语法树构造器
        return builder.build(); // 构造抽象语法树, 返回根节点
    }
}
