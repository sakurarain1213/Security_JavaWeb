# 工程简介
鉴权的后端模板
# 延伸阅读


部署：阿里云ECS  

管理：宝塔面板  

配置：Nginx 1.18.0  
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

页面方面  
安装nginx后，会有默认的网页，但实际中一个不够用，需要添加多网页
1）.修改Nginx服务配置，添加相关虚拟主机配置如下
[root@porxy ~]# vim /usr/local/nginx/conf/nginx.conf
server {
listen       80;
server_name  www.a.com;
        auth_basic "input Password:";  #认证提示符信息
        auth_basic_user_file "/usr/local/nginx/pass"; #认证的
密码文件
#charset koi8-r;
location / {
root   html;
index  index.html index.htm;
}
    server {
        listen       80;
        server_name  www.b.com;
 
        location / {
            root   www;
            index  index.html index.htm;
        }
    }
2）、创建网站根目录及对应的首页文件
[root@porxy ~]# mkdir /usr/local/nginx/www
[root@porxy ~]# echo "www"> /usr/local/nginx/www/index.html
3）、重新加载配置：
[root@porxy ~]# /usr/local/nginx/sbin/nginx  -s reload
4）、客户端测试：192.168.35.137   先添加域名解析
[root@client ~]# vim /etc/hosts  //本地域名解析文件，也称单机版解析文件

192.168.35.134  www.a.com  www.b.com
[root@client ~]# ping www.a.com
[root@client ~]# ping www.b.com

