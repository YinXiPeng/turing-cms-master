FROM docker.registry:5000/jdk:1.8.0_171
ADD target/cms.jar /home/bonc/cms.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime 
RUN echo 'Asia/Shanghai' >/etc/timezone
COPY run.sh /home/bonc/
COPY myhosts /home/bonc/
RUN chmod +x /home/bonc/run.sh
ENV HADOOP_USER_NAME hadoop
EXPOSE 8083
#ENTRYPOINT ["java","-jar","/salon.jar"]
ENTRYPOINT /bin/sh -c /home/bonc/run.sh

