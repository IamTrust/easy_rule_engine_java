package edu.gdut.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.gdut.engine.core.Compiler;
import edu.gdut.engine.core.SyntaxNode;
import edu.gdut.engine.exception.CompileException;
import edu.gdut.engine.exception.RuleEvalException;
import edu.gdut.web.dao.ExpressionDao;
import edu.gdut.web.dto.EngineRunDto;
import edu.gdut.web.dto.ExpRunDto;
import edu.gdut.web.pojo.Expression;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/engine/")
public class EngineController {
    @Resource
    private ExpressionDao expressionDao;

    @GetMapping("/ping")
    public ResponseDate ping() {
        return ResponseDate.success().data("ping", "pong");
    }

    /**
     * 执行规则, 不保存规则到数据库
     */
    @PostMapping("/run")
    public ResponseDate run(@RequestBody EngineRunDto req) {
        try {
            SyntaxNode root = Compiler.compile(req.getExp());
            root.eval(req.getParams());

            return ResponseDate.success().data("result", root.getValue());
        } catch (CompileException | RuleEvalException e) {
            return ResponseDate.failure().message(e.getMessage());
        }
    }

    /**
     * 保存规则到数据库，然后返回规则 id
     * 如果已存在，直接返回规则 id
     */
    @PostMapping("/exp/new")
    public ResponseDate expNew(@RequestBody Expression expression) {
        String exp = expression.getExp();
        QueryWrapper<Expression> wrapper = new QueryWrapper<>();
        wrapper.eq("exp", exp);
        Expression ret = expressionDao.selectOne(wrapper);
        if (ret != null)
            return ResponseDate.success().data("exp", ret);
        int cnt = expressionDao.insert(expression);
        return cnt == 1 ? ResponseDate.success().data("exp", expression) : ResponseDate.failure();
    }

    /**
     * 查询所有规则
     */
    @GetMapping("/exp/list")
    public ResponseDate expList() {
        return ResponseDate.success().data("expList", expressionDao.selectList(null));
    }

    /**
     * 删除对应 id 的规则，返回被删除的规则记录
     * 如果 id 不存在则删除失败
     */
    @DeleteMapping("/exp/{id}")
    public ResponseDate delete(@PathVariable Long id) {
        // 根据 id 查询
        Expression expression = expressionDao.selectById(id);
        if (expression == null)
            return ResponseDate.failure().message("exp id " + id + " not exist");
        int cnt = expressionDao.deleteById(id);
        return cnt == 1 ? ResponseDate.success().data("exp", expression) : ResponseDate.failure();
    }

    /**
     * 执行对应 id 的规则
     * id 不存在则返回失败
     */
    @PostMapping("/exp/run")
    public ResponseDate expRun(@RequestBody ExpRunDto expRunDto) {
        Long id = expRunDto.getExpId();
        Expression expression = expressionDao.selectById(id);
        if (expression == null)
            return ResponseDate.failure().message("exp id " + id + " not exist");

        try {
            SyntaxNode root = Compiler.compile(expression.getExp());
            root.eval(expRunDto.getParams());
            return ResponseDate.success().data("result", root.getValue());
        } catch (CompileException | RuleEvalException e) {
            return ResponseDate.failure().message(e.getMessage());
        }
    }
}
