# 工程简介
鉴权的后端模板
# 延伸阅读


部署：阿里云ECS  

管理：宝塔面板  

配置：Nginx 1.22.0  
MySQL 5.7.43  
PHP-7.4.33  
Redis 7.2.3  
Java项目一键部署 3.5  
phpMyAdmin 5.0  
  

模块：  
见pom配置文件  
  
  
本地管理：  
navicat  
Another Redis Desktop Manager  
Xshell  
Xftp  


公网ip  47.103.113.75

jdk目录
/usr/java/jdk1.8.0_371/bin/java  



常见问题：
mysql异常没有root或者密码错误   不要重装数据库
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123';

页面方面  
nginx问题导致界面无法访问  直接下载高版本 1.22


数据库攻击问题：
安全组把3306端口的ip指定为本地公网  或者只在需要时开启全部ip访问
警惕“”
