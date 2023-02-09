# easy_rule_engine_java

本项目基于 Java 实现了一个简单的 Web 规则引擎。

- 引擎自定义了一套词法、语法。
- 在自定义词法语法的基础上实现了一个典型的编译器前端，能够生成表达式对应的抽象语法树。
- 基于编译构建的抽象语法树实现了Java版本的虚拟机。通过注入参数可以获得执行结果。
- 引擎采用 Web 形式，提供了下列接口：
    - 提供规则表达式和参数，运行一条规则，返回执行结果，不保存规则到数据库。
    - 添加一条规则到数据库。
    - 删除指定 ID 的规则。
    - 提供规则表达式的 ID 和参数，执行规则，返回执行结果。

## 词法
引擎支持指定的运算符和数据类型。

**运算符**
- 一元计算符 : `!` `-` `+`
- 二元计算符 : `+` `-` `/` `*` `%`
- 二元比较符 : `>` `>=` `<` `<=`  `==` `!=`
- 逻辑操作符 : `||` `&&`
- 括号 : `(` `)`

**数据类型**
- 字符串 `"abc"` `'def'`
- 十进制int `123`
- 十进制float `123.4`
- bool `true`
- 变量 `id(自定义变量名)`

**表达式词法**
- 表达式以换行结束、不支持多行表达式。形如`a + 7 > 100`
- 支持字面量 (上述数据类型的常量)、变量和运算符(上述运算符)
- 变量：由字母数字下划线构成且必须以字母或下划线开头，形如：`_id`、`foo`
- 关键字：系统内置部分关键字(2个)
    - `true`: bool类型常量
    - `false`: bool类型常量

## 语法
支持简单的表达式语法
- 一元运算: `!true`
- 二元运算: `a + b > c`
- 逻辑运算: `a || b == 100`
- 括号: `(a + b) * c`

运算符的优先级

| 优先级 | 运算符                         |
|-----|-----------------------------|
| 0   | `或运算(两个竖线,与Markdown制表符冲突)`  |
| 1   | `&&`                        |
| 2   | `!` `-` `+`                 |
| 3   | `>` `>=` `<` `<=` `==` `!=` |
| 4   | `+` `-`                     |
| 5   | `*` `/`                     |

## 项目结构
``` shell
.
├── edu.gdut
│   ├── engine
│       ├── core        # 引擎核心逻辑代码
│       ├── constant    # 类型、符号等常量定义
│       ├── exception   # 异常
│   ├── web            # web部分(还没做)
```

## 项目功能

规则引擎直接使用：
```java
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

    // 引擎也支持简单计算
    String exp2 = "a * (b - c) + 20";
    Map<String, Object> params2 = new HashMap<>();
    params2.put("a", 10);
    params2.put("b", 10);
    params2.put("c", 30);
    SyntaxNode root2 = Compiler.compile(exp2);
    root2.eval(params2);
    long res2 = (long)root2.getValue();  // getVal 可以得到 Object 类型的结果
    System.out.println("result: " + res2); // -180
}
```

### /api/engine/run 接口

在线执行输入的规则和参数，返回执行结果，不保存规则。

请求方式：POST

- Request：

  ```json
  {
      "exp": "a == 12345 || b > 0",
      "params": {
          "a": 123456,
          "b": 1
      }
  }
  ```

  `exp`：规则表达式

  `params`：表达式中参数的值

- Response：

  ```json
  {
      "code": 0,
      "message": "success",
      "data": true
  }
  ```

  `code`：响应码，0 表示成功

  `message`：响应消息，成功则为`success`，失败则为失败原因

  `data`：响应数据，即规则执行结果，这里为`true`，即参数(`a、b`)的值符合规则

### /api/engine/exp/new 接口

保存一条规则到数据库。

请求方式：POST

- Request：

  ```json
  {
      "exp": "age >= 20 && userLevel >= 5"
  }
  ```

- Response：

  数据库中不存在则添加到数据库，并返回`ID`；数据库中存在则直接返回`ID`。

  ```json
  {
      "code": 0,
      "message": "success",
      "data": {
          "id": 1,
          "exp": "age >= 20 && userLevel >= 5"
      }
  }
  ```

### /api/engine/exp/list 接口

查询所有规则。

请求方式：GET

- Response：

  ```json
  {
      "code": 0,
      "message": "success",
      "data": [
          {
              "id": 1,
              "exp": "age >= 20 && userLevel >= 5"
          },
          {
              "id": 2,
              "exp": "a > 10 && b > 20"
          }
      ]
  }
  ```

### /api/engine/exp/:id 接口

删除对应 `ID` 的规则，若 `ID` 不存在返回 `ID` 不存在，存在则返回被删除的规则。

请求方式：DELETE

- Response：

  `ID` 不存在

  ```json
  {
      "code": 20002,
      "message": "exp id 10 not exist",
      "data": null
  }
  ```
  删除成功
  ```json
  {
      "code": 0,
      "message": "success",
      "data": {
          "id": 1,
          "exp": "age >= 20 && userLevel >= 5"
      }
  }
  ```

### /api/engine/exp/run 接口

执行对应 `ID` 的规则。若规则不存在则提示。

请求方式：POST

- Request：

  ```json
  {
      "expId": 2,
      "params": {
        "a": 20, 
        "b": 10
      }
  }
  ```

- Response：

  规则` a > 10 && b > 20`的执行结果`false`

  ```json
  {
      "code": 0,
      "message": "success",
      "data": false	
  }
  ```

  规则不存在

  ```json
  {
      "code": 20002,
      "message": "exp id 71 does not exists",
      "data": null
  }
  ```
  