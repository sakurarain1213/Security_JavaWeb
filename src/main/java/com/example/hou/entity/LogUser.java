package com.example.hou.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor

//我们自定义一个用户信息，实现security 自带的  UserDetails
public class LogUser implements UserDetails {

    //用户信息
    private SysUser user;

    //用户权限
    private List<String> permissions;

    //存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LogUser(SysUser user,List<String> permissions){

        this.user = user;
        this.permissions = permissions;

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将权限信息封装成 SimpleGrantedAuthority
        if (authorities != null) {
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return authorities;

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountNotExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsNotExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
