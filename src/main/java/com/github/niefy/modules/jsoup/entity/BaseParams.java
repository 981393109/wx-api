package com.github.niefy.modules.jsoup.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseParams {
    @TableField(exist = false)
    private  Integer currpage;
    @TableField(exist = false)
    private  Integer pagesize;

}
