# 该镜像需要依赖的基础镜像
FROM openjdk:11-jre
# 将targer目录下的jar包复制到docker容器/home/springboot目录下面目录下面
ADD ./target/demo-0.0.1-SNAPSHOT.jar /home/springboot/springboot-dockerfile.jar
# 声明服务运行在8080端口
EXPOSE 40001
# 执行命令
CMD ["java","-jar","/home/springboot/springboot-dockerfile.jar"]
# 指定维护者名称
MAINTAINER LXD yigeyanse@qq.com
