package com.example.hou.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//在controller层控制用户能访问的接口权限


/*测试接口

要先登录得到token


只传token

localhost:8080/testPreAuthorize/hello

localhost:8080/testPreAuthorize/hello2

*/


//authority  自定义鉴权依然无效   暂时忽略   后续开发注意完善

@RestController
@RequestMapping("/testPreAuthorize")
public class TestPreAuthorizeController {


    @PostMapping("/hello")
    // 只有sys:queryUser 权限才能访问
    //重大debug  仍未解决      放行失败
    // @PreAuthorize 注解的异常，抛出AccessDeniedException异常，
    // 不会被accessDeniedHandler捕获，而是会被全局异常捕获。
    //@PreAuthorize("hasAuthority('sys:queryUser')") //这是没有自定义权限校验方法的默认写法
    @PreAuthorize("@sysex.hasTheAuthority('sys:queryUser')" )  //这个是自定义表里的值  有此权限才可以访问
    public String hello(){

        return "hello";
    }

    @PostMapping("/hello2")
    // 只有sys:queryUser2 权限才能访问
    @PreAuthorize("@sysex.hasTheAuthority('sys:queryUser2')")
    public String hello2(){

        return "hello2";
    }
}
