package edu.gdut.web.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Expression {
    /**
     * 表达式 id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 表达式内容
     */
    private String exp;
    /**
     * 表达式创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createAt;
    /**
     * 表达式更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateAt;
}
