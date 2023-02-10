package edu.gdut.web.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MybatisPlusConfig implements MetaObjectHandler {
    /**
     * 配置 MybatisPlus 插入时自动填充策略
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createAt", new Date(), metaObject);
        this.setFieldValByName("updateAt", new Date(), metaObject);
    }
    /**
     * 配置 MybatisPlus 更新时自动填充策略
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateAt", new Date(), metaObject);
    }
}
