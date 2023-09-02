package com.example.hou.service.impl;

import com.anyic.Wenbenchuli;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.hou.entity.DTOCountNumber;
import com.example.hou.entity.DTOUser;
import com.example.hou.entity.Record;
import com.example.hou.entity.DTOText;
import com.example.hou.mapper.RecordMapper;
import com.example.hou.service.RecordService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anyic.Wenbenchuli.Sentence;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.anyic.WebIATWS;
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
    public String recordAddService(Record record) throws Exception {
        //username   txt   start +end  time
        //add的时候不用判断已存在的教师记录 但是要判断非空
        //疯狂debug
        if (record.getMp3File()==null) {//现在拿的是整体
            //System.out.println(record.getTxtFile()+"?????????");//测试语句
             return "缺少语音文件";
        } //else if (record.getEndTime()==null) {
          //  return "缺少时间信息";//时间交给每句话处理
        //}
        else if(record.getUsername()==null){
            return "缺少用户信息";
        }
        else {
            /****
             开始分割文本   注意放回到txtFile    需要时间
             */
            Date date = new Date();
            //注意这个时间是格林尼治标准时间 东八区+8小时
            //long time = date.getTime() + 8 * 3600000;
            long time = date.getTime() ;
            date.setTime(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss "); //"yyyy-MM-dd HH:mm:ss"
            String format = dateFormat.format(date);

            //得到时分秒  format


            MultipartFile myFile = record.getMp3File();
            String newFileName = null;
            try {
                //1.获取源文件的输入流
                InputStream is = myFile.getInputStream();
                //2.获取源文件类型，文件后缀名
                String originalFileName = myFile.getOriginalFilename();
                //3.定义上传后的目标文件名(为了避免文件名称重复，此时使用UUID)
                newFileName = UUID.randomUUID().toString() + "." + originalFileName;
                //4.通过上传路径得到上传的文件夹
                File file = new File("resource\\iat");
                //4.1.若目标文件夹不存在，则创建
                if (!file.exists()) { //判断目标文件夹是否存在
                    file.mkdirs();//4.2.不存在，则创建文件夹
                }
                //5.根据目标文件夹和目标文件名新建目标文件（上传后的文件）
                File newFile = new File("resource\\iat", newFileName);  //空的目标文件
                //6.根据目标文件的新建其输出流对象
                FileOutputStream os = new FileOutputStream(newFile);
                //7.完成输入流到输出流的复制
                IOUtils.copy(is, os);
                //8.关闭流(先开后关)
                os.close();
                is.close();
                //return "SUCCESS";    上传完毕
            } catch (IOException e) {
                e.printStackTrace();
                //return "ERROR";   有异常
            }


            String Pcmfile = "resource\\iat\\" + newFileName;//保存到路径   .pcm 格式名已经有了
            WebIATWS pcm = new WebIATWS(Pcmfile);

            String ans = pcm.getWenben(pcm);
            //ans前面加  三段时间  后面加st
            //以上进行文件文本化
            String test =format +ans+"st";

            //String test = record.getTxtFile();


            //时间差要以分钟为单位的float   gettime方法返回ms
            // float deltaTime=(record.getEndTime().getTime()-record.getStartTime().getTime());
            //deltaTime=deltaTime/1000/60;//ms转minute

            Wenbenchuli W = new Wenbenchuli();
            W.GetString_analyse2(test);//改一下对应的文本分析
            ArrayList<Sentence> s = W.Get_AllSentences();

            Record r = new Record();//临时插入变量

            //时间格式转化
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //进行遍历

            for (Sentence each : s) {

                //临时词listt   用下标返回侮辱词
                String word = each.words.get(each.wuru_pos);
                r.setIswuru((each.iswuru) ? 1 : 0);//boolean转int
                r.setIstiwen((each.istiwen) ? 1 : 0);
                r.setIsguli((each.isguli) ? 1 : 0);

                //先判是不是侮辱
                if (r.getIswuru() == 1) {
                    r.setWuru(word);
                }//一个侮辱词
                else {
                    r.setWuru(null);
                }
                try {

                    Date d = simpleDateFormat.parse(each.Get_Sentence_time());
                    //d.setTime(d.getTime() + (1000 * 60 * 60 * 8)); //调整一下东八区时间 加八小时
                    d.setTime(d.getTime() );
                    r.setStartTime(d);//时间格式转化 强制要求异常提醒
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                //将list words拼成句子再存进表
                String ju = "";
                for (String fenci : each.words) {
                    ju = ju + fenci;
                }
                r.setTxtFile(ju);

                //别忘记用户名
                r.setUsername(record.getUsername());
                recordMapper.insert(r);

            }

            // record.setYusu(W.yusu);  没有语速
            // W.
            //由于高频词返回一个list string  直接拼成一个大string即可
            //String gao=String.join(",", W.Get_gaopinci(5));
            // record.setGaopin(gao);//默认返回前五大高频词
            /*******
             * 结束调用
             * ***/
            return "SUCCESS";
        }


     // record.setTxtFile("测试嗷嗷");
      // record.setUsername("3");
      //  recordMapper.insert(record);
      //  return "yes";
}


    @Override
    public String recordUpload(MultipartFile file, DTOUser user) throws Exception{

        if (file == null || file.isEmpty()) {
            return "请上传文件";
        }
        // 检查后缀
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith(".pcm")) {
            return "请上传pcm格式的文件";
        }
        if (user == null || user.getUsername() == null) {
            return "缺少用户名信息";
        }

        String filePath;

          // 本地保存测试通过   接下来 放到服务器的目录上
        try {
            // 生成新的文件名：UUID + 系统时间 + 用户名 + 原始文件后缀
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String formattedDate = dateFormat.format(new Date());
            String newFileName = UUID.randomUUID().toString() + "_" + formattedDate + "_"
                    + user.getUsername() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构建目标文件路径
            filePath = "resource\\iat\\" + newFileName;
            File destFile = new File(filePath);

            // 将上传的文件保存到本地
            try (InputStream inputStream = file.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败";
        }

       /* //以下是服务器版本
        try {
            // 生成新的文件名：UUID + 系统时间 + 用户名 + 原始文件后缀
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String formattedDate = dateFormat.format(new Date());
            String newFileName = UUID.randomUUID().toString() + "_" + formattedDate + "_"
                    + user.getUsername() + originalFilename.substring(originalFilename.lastIndexOf("."));


            // 如果目录不存在，创建目录
            File directory = new File("\\www\\wwwroot\\iat");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //  构建目标文件路径  保存文件到目标路径
            filePath = "\\www\\wwwroot\\iat\\" + newFileName;
            File destFile = new File(filePath);

            // 将上传的文件保存到本地
            try (InputStream inputStream = file.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败";
        }

 */






        //开始解析文件文本  并保存到数据库


        Date date = new Date();
        long time = date.getTime();//+ 8 * 3600000;
        date.setTime(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss "); //"yyyy-MM-dd HH:mm:ss"
        String format = dateFormat.format(date);
        //得到时分秒  format

        String Pcmfile = filePath;//保存到路径   .pcm 格式名已经有了
        WebIATWS pcm = new WebIATWS(Pcmfile);
        String ans = pcm.getWenben(pcm);
        String test = format + ans + "st";

        Wenbenchuli W = new Wenbenchuli();
        W.GetString_analyse2(test);//改一下对应的文本分析
        ArrayList<Sentence> s = W.Get_AllSentences();

        Record r = new Record();//临时插入变量

        //时间格式转化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //进行遍历
        for (Sentence each : s) {
            //临时词listt   用下标返回侮辱词
            String word = each.words.get(each.wuru_pos);
            r.setIswuru((each.iswuru) ? 1 : 0);//boolean转int
            r.setIstiwen((each.istiwen) ? 1 : 0);
            r.setIsguli((each.isguli) ? 1 : 0);
            //先判是不是侮辱
            if (r.getIswuru() == 1) {
                r.setWuru(word);
            }//一个侮辱词
            else {
                r.setWuru(null);
            }
            try {
                Date d = simpleDateFormat.parse(each.Get_Sentence_time());
                d.setTime(d.getTime() + (1000 * 60 * 60 * 8)); //调整一下东八区时间 加八小时
                r.setStartTime(d);//时间格式转化 强制要求异常提醒
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            //将list words拼成句子再存进表
            String ju = "";
            for (String fenci : each.words) {
                ju = ju + fenci;
            }
            r.setTxtFile(ju);
            r.setUsername(user.getUsername());
            recordMapper.insert(r);
        }

        /*
         结束调用
         */
        return "SUCCESS";

    }

    @Override//通过时间范围和username拿语音记录
    //查询 应该返回对象List 而不再是string
    public List<Record> recordGetService(Record record) {//传入的前端请求对象
           Date time1=record.getStartTime(); // 考虑要不要tostring
           Date time2=record.getEndTime();
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
                .eq("iswuru",1)  //加一个筛选侮辱词的接口
                .between("start_time",time1,time2)
                .orderByDesc("start_time")//asc desc 升降序
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


    @Override
    public DTOCountNumber numberGetService(Record record) {
        DTOCountNumber c=new DTOCountNumber();
        c.initialize();

        Date time1=record.getStartTime();
        Date time2=record.getEndTime();
        String user=record.getUsername();

        if(time1==null ||time2 ==null ||user ==null)
        {
            return null;
        }

        QueryWrapper<Record> qw1 = new QueryWrapper<>();
        qw1
                .eq("username",user)
                .eq("iswuru",1)  //性能显然可以提高  后续自行写SQL
                .between("start_time",time1,time2);
        QueryWrapper<Record> qw2 = new QueryWrapper<>();
        qw2
                .eq("username",user)
                .eq("isguli",1)
                .between("start_time",time1,time2);
        QueryWrapper<Record> qw3 = new QueryWrapper<>();
        qw3
                .eq("username",user)
                .eq("istiwen",1)
                .between("start_time",time1,time2);
       //  q1= qw.eq("iswuru",1);尝试分阶段查询 提高性能   但是似乎不行

        c.set(recordMapper.selectCount(qw1),
              recordMapper.selectCount(qw2),
              recordMapper.selectCount(qw3));
        //recordMapper.selectCount(q1)用于返回查询数量

        return c;
    }


    @Override
    public List<DTOText> textGetService(Record record){
        Date time1=record.getStartTime();
        Date time2=record.getEndTime();
        String user=record.getUsername();
        if(time1==null ||time2 ==null ||user ==null)
        {
            return null;
        }
        QueryWrapper<Record> qw = new QueryWrapper<>();
        qw
                .eq("username",user)
                .eq("iswuru",1)  //加一个筛选侮辱词的接口
                .between("start_time",time1,time2)
                .orderByAsc("start_time")//asc desc 升降序
        ;
        //然后得到记录行
        List<Record> l = recordMapper.selectList(qw);
        List<DTOText> lText = new ArrayList<>();
        for(Record each : l ){
                DTOText temp = new DTOText();
                temp.setStartTime(each.getStartTime());
                temp.setWord(each.getWuru());
                temp.setTxtFile(each.getTxtFile());
                lText.add(temp);
        }

        return lText;
    }

}
