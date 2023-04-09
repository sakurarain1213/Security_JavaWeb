package com.example.hou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hou.entity.Record;
import com.example.hou.entity.UserInfo;
import com.example.hou.mapper.RecordMapper;
import com.example.hou.mapper.UserInfoMapper;
import com.example.hou.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anyic.Wenbenchuli;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hsin
 * @since 2023-04-08
 */
@Service
public class RecordServiceImpl /*extends ServiceImpl<RecordMapper, Record> */implements RecordService {

    @Autowired
    RecordMapper recordMapper;//不要忘记注入


    @Override
    public String recordAddService(Record record){

        //username   txt   start +end  time
        //add的时候不用判断已存在的教师记录 但是要判断非空
        //疯狂debug


        if (record.getTxtFile()==null) {
            //System.out.println(record.getTxtFile()+"?????????");//测试语句
             return "缺少文本信息";
        } else if (record.getEndTime()==null) {
            return "缺少时间信息";
        }
        else{
            /***********************************************************
             开始连接自定义评分标准类
             */

            String test=record.getTxtFile();
            //时间差要以分钟为单位的float   gettime方法返回ms
            float deltaTime=(record.getEndTime().getTime()-record.getStartTime().getTime());
            deltaTime=deltaTime/1000/60;//ms转minute

            Wenbenchuli W=new Wenbenchuli();
            W.GetString_analyse(test,deltaTime);

            record.setWuruCount(W.wuru_Count);
            record.setGuliCount(W.guli_Count);
            record.setTiwenCount(W.wenda_Count);
            record.setYusu(W.yusu);

            //由于高频词返回一个list string  直接拼成一个大string即可
            String gao=String.join(",", W.Get_gaopinci(5));
            record.setGaopin(gao);//默认返回前五大高频词
      /*******
       * 结束调用
       * ***/
            recordMapper.insert(record);
            return "SUCCESS";
        }





     // record.setTxtFile("测试嗷嗷");
      // record.setUsername("3");
      //  recordMapper.insert(record);
      //  return "yes";
}


    @Override//通过时间范围和username拿语音记录
    //查询 应该返回对象List 而不再是string
    public List<Record> recordGetService(Record record) {//传入的前端请求对象
           Date time1=record.getStartTime(); // 考虑要不要tostring
           Date time2=record.getEndTime(); // 考虑要不要tostring
           String user=record.getUsername();
       //因为有可能为空 所以要返回临时的量
       // List<Record> t;
       // Record temp = new Record();

        //准备查询
        //temp.setUsername(user);
        // temp.setStartTime(time1);
        //temp.setEndTime(time2);
        //plus的条件构造器 查询条件
        //有可能出错 注意列名不能改


           if(time1==null ||time2 ==null ||user ==null)
           {
             //"缺少用户或时间范围的筛选条件";
               return null;
           }
        //尝试用wrapper 实现SQL的等于 介于 大 小  筛选 合并 查询
        QueryWrapper<Record> qw = new QueryWrapper<>();
        qw
                .eq("username",user)
                .between("end_time",time1,time2)
                .orderByDesc("end_time")//asc desc 升降序
        ;
        //然后得到记录行
        List<Record> l = recordMapper.selectList(qw);
            //l==null 表示查询结果为空
            //test
            return l;
            //l.forEach(System.out::println);
            //return "SUCCESS";

        }
//再写一个查询全部  以及平均分！！！


}