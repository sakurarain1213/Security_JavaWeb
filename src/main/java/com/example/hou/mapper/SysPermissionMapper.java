package com.example.hou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hou.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /*
     通过用户id查询用户的权限数据
     */
  //重大debug   回到这个join表  找permission为空的地方  这种注解似乎对mybatisplus无效  改变此类
    //select 语句验证是可以的
    //复杂sql还是建议用mybatis 的 @Select  而不是mybatisplus的querywrapper
    @Select({"<script>"+
            " SELECT p.* FROM"+
            " sys_user u"+
            " LEFT JOIN sys_user_permission_relation r ON u.user_id = r.user_id"+
            " LEFT JOIN sys_permission p ON r.permission_id = p.permission_id"+
            " WHERE u.user_id = #{userId} "+
            "</script>"
    })   //这样会选出来四列  其实只需要一列 Columns: permission_id, permission_code, permission_name, url
    //@Results({
    //        @Result(property = "permissionCode", column = "permission_code")
   // })

    List<SysPermission> selectPermissionList(@Param("userId") Integer userId);

}

