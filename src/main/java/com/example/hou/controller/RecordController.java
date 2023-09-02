package com.example.hou.controller;


import com.example.hou.entity.DTOCountNumber;
import com.example.hou.entity.DTOUser;
import com.example.hou.entity.Record;
import com.example.hou.entity.DTOText;
import com.example.hou.result.Result;
import com.example.hou.result.ResultUtil;
import com.example.hou.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hsin
 * @since 2023-04-08
 */
/*
47.103.113.75:8080/record/add   添加记录  需要用户名和文件
{
"username":"iraina",
"mp3File":""    ？  传本地文件   具体格式待测试
}
//上述方法被替换为

47.103.113.75:8080/record/upload
{

}










47.103.113.75:8080/record/get   查询全部记录    强制需要三条信息
{
"username":"iraina",
"startTime":"2023-04-13 10:10:51",
"endTime":"2023-09-13 10:10:51"
}

{
    "username":"6",
    "startTime":"2023-04-06 23:59:59",
    "endTime":"2023-04-09 23:59:59"
}




47.103.113.75:8080/record/getCount   查询词汇统计数量   强制需要三条信息  返回三元组
{
"username":"iraina",
"startTime":"2023-04-13 10:10:51",
"endTime":"2023-09-13 10:10:51"
}

47.103.113.75:8080/record/getText   查询词汇统计数量   强制需要三条信息  返回文本
{
"username":"iraina",
"startTime":"2023-04-13 10:10:51",
"endTime":"2023-09-13 10:10:51"
}



接口0  头像功能  文件上传思路：判断合法 随机命名放入服务器  得到地址放入数据库
[添加筛选 统计的实现   包括三大词汇按天统计]  调整东八区时间

接口1  每节课（前端发送一个起止40min的时间和用户  查询）  返回三大词汇次数总计OK    1.1-1.3   返回一节课三大词汇具体时间和内容

接口2   按每天返回  用户  查询）  返回三大词汇次数总计OK     2.1-2.3   返回

接口3  教学建议板块  输出多维向量
*/
@RestController
@Slf4j
@RequestMapping("/record")
public class RecordController {

    @Autowired
    RecordService recordService;//不要忘记注入

    @RequestMapping("/add")
    public Result recordAdd(@RequestBody Record record) throws Exception {
        String msg = recordService.recordAddService(record);
        if (("SUCCESS").equals(msg)) {
            return ResultUtil.success("语音文本上传成功");
        } else {
            return ResultUtil.error(msg);
        }
    }

    //尝试另外方式上传文件
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @ModelAttribute DTOUser user) throws Exception {
        //注意这个方法 不要传json  要用form-data
        String msg = recordService.recordUpload(file, user);
        if (("SUCCESS").equals(msg)) {
            return ResultUtil.success("语音文件解析成功");
        } else {
            return ResultUtil.error(msg);
        }
    }



    @RequestMapping("/get")
    //因为返回的是一个list  所以消息需要根据新的格式自定义
    public Result recordGet(@RequestBody Record record) {
        List<Record> l = recordService.recordGetService(record);
        if (l!=null) {
            //相当于重新打开了ResultUtil的封装  自定义返回消息也在返回类的属性位置编辑
            Result r = new Result();
            r.setCode(200);
            r.setMsg("成功查询到语音记录数量：" + l.size());
            r.setData(l);
            return r;
            //return ResultUtil.success(l);//强大的result类可以自定义返回类型
        }
        else {
            return ResultUtil.error("缺少用户与时间查询条件或查询结果为空");
        }
    }


    @RequestMapping("/getCount")
    //因为返回的是一个list  所以消息需要根据新的格式自定义
    public Result recordGetCount(@RequestBody Record record) {
        DTOCountNumber c = recordService.numberGetService(record);
        if (c!=null) {
            //重新打开ResultUtil的封装
            Result r = new Result();
            r.setCode(200);
            r.setMsg("成功统计词汇");
            r.setData(c);
            return r;
            //return ResultUtil.success(l);//强大的result类可以自定义返回类型
        }
        else {
            return ResultUtil.error("缺少用户与时间查询条件或查询结果为空");
        }
    }

    @RequestMapping("/getText")
    //因为返回的是一个list  所以消息需要根据新的格式自定义
    public Result recordGetText(@RequestBody Record record) {
        List<DTOText> t = recordService.textGetService(record);
        if (t!=null) {
            //重新打开ResultUtil的封装
            Result r = new Result();
            r.setCode(200);
            r.setMsg("成功统计涉嫌违规句子："+t.size()+"条");
            r.setData(t);
            return r;
            //return ResultUtil.success(l);//强大的result类可以自定义返回类型
        }
        else {
            return ResultUtil.error("缺少用户与时间查询条件或查询结果为空");
        }
    }



}

