package com.example.hou.entity;

import lombok.Data;

import java.util.Date;



@Data//临时类也需要注解
/**
 *
 临时类  用于 文本内容统计返回
 */
public class DTOText {

    private String txtFile;

    private Date startTime;

    private String word;

    public void initialize(){
        this.txtFile="";
        this.word="";
        this.startTime=new Date();
    }

}
