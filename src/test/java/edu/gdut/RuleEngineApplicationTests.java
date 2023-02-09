package edu.gdut;

import edu.gdut.engine.core.*;
import edu.gdut.engine.core.Compiler;
import edu.gdut.engine.exception.CompileException;
import edu.gdut.engine.exception.RuleEvalException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class RuleEngineApplicationTests {

    @Test
    void contextLoads() {
    }

    // 整个引擎功能测试
    @Test
    void testRunEngine() throws CompileException, RuleEvalException {
        String exp = "price > 500 && (userLevel > 5 || isNewUser)";
        Map<String, Object> params = new HashMap<>();
        params.put("price", 1200);
        params.put("userLevel", 1);
        params.put("isNewUser", true);
        SyntaxNode root = Compiler.compile(exp); // 编译表达式, 得到语法树根节点
        root.eval(params); // 参数注入, 执行规则
        boolean res = root.getBoolVal();  // 得到语法树执行结果
        // 本测试应该为 true , 因为 price > 500 为 true 且 (userLevel > 5 || isNewUser) 为 true
        System.out.println("规则执行结果:" + res);
    }

    @Test
    void testEngine() throws CompileException, RuleEvalException {
        // 不仅支持结果为 boolean , 也支持运算
        String exp = "a + b";
        Map<String, Object> params = new HashMap<>();
        params.put("a", 1200);
        params.put("b", 1);
        System.out.println("result: " + runEngine(exp, params)); // 1201

        String exp2 = "a * (b - c) + 20";
        Map<String, Object> params2 = new HashMap<>();
        params2.put("a", 10);
        params2.put("b", 10);
        params2.put("c", 30);
        System.out.println("result: " + runEngine(exp2, params2)); // -180
    }

    Object runEngine(String exp, Map<String, Object> params) throws RuleEvalException, CompileException {
        SyntaxNode root = Compiler.compile(exp);
        root.eval(params);
        return root.getValue();
    }

    // 测试扫描表达式模块
    @Test
    void testScan() throws CompileException {
        // 基本测试
        String exp1 = "price > 200 && (userLevel > 10 || isNewUser)";
        Scan scan = new Scan(exp1);
        List<Token> tokens = scan.lexer();
        System.out.println(tokens); // 请 debug 查看 tokens 的内容是否正确

        // 测试运算符和整型
        String exp2 = "a > 1 && b >= 2 || c < 3 || d <= 10 && e == 20 && f !=50";
        Scan scan1 = new Scan(exp2);
        List<Token> tokens1 = scan1.lexer();
        System.out.println(tokens1);

        // 测试浮点型
        String exp3 = "a == 3.14";
        Scan scan2 = new Scan(exp3);
        List<Token> tokens2 = scan2.lexer();
        System.out.println(tokens2);

        // 测试字符串类型
        String exp4 = "a == \"car\"";
        Scan scan3 = new Scan(exp4);
        List<Token> tokens3 = scan3.lexer();
        System.out.println(tokens3);

        // 测试包含特殊字符的字符串
        String exp5 = "a == \"I am a student!\t hello!\"";
        Scan scan4 = new Scan(exp5);
        List<Token> tokens4 = scan4.lexer();
        System.out.println(tokens4);
    }

    // 测试语法检查
    @Test
    void testParser() throws CompileException {
        //String exp1 = "a > true"; // 不合法表达式
        String exp1 = "a > 3";  // 合法
        Scan scan = new Scan(exp1);
        List<Token> tokens = scan.lexer();
        Parser parser = new Parser(tokens);
        parser.ParseSyntax(); // 如果不合法应该抛出异常
    }
}
