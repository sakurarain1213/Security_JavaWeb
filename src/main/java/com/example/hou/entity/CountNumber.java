package com.example.hou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
临时类  用于计数结果返回
 */
@Data//临时类也需要注解

public class CountNumber {

    Integer wuru;
    Integer guli;
    Integer tiwen;

   // boolean flag;  //查询状态码   用于异常信息返回前端   null 即可

    public void initialize(){
         this.guli=0;
         this.wuru=0;
         this.tiwen=0;
    }
    public void set(Integer a,Integer b,Integer c){
        this.wuru=a;
        this.guli=b;
        this.tiwen=c;
    }
}
