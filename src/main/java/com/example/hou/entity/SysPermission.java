package com.example.hou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("sys_permission")
public class SysPermission implements Serializable {
    @TableId(value = "permission_id", type = IdType.ID_WORKER)
    private Integer permissionId;

    private String permissionCode;

    private String permissionName;

    private String url;
}

