FROM centos
MAINTAINER luisca_jl@hotmail.com
	
ENV JAVA_VERSION 9~b149
ENV BUILD_VERSION b01
 
# Upgrading system
RUN yum -y upgrade
RUN yum -y install wget
 
# Downloading & Config Java 9
RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/9.0.4+11/c2514751926b4512b076cc82f959763f/jdk-9.0.4_linux-x64_bin.rpm
RUN yum -y install jdk-9.0.4_linux-x64_bin.rpm
RUN rm jdk-9.0.4_linux-x64_bin.rpm
#Download FFMPEG
RUN yum -y install epel-release 
RUN yum -y update 
RUN rpm --import http://li.nux.ro/download/nux/RPM-GPG-KEY-nux.ro
RUN rpm -Uvh http://li.nux.ro/download/nux/dextop/el7/x86_64/nux-dextop-release-0-5.el7.nux.noarch.rpm
RUN yum -y install ffmpeg ffmpeg-devel -y

#Config LOGS
RUN mkdir -p /tmp/VideoTranscoding/logs/
RUN chmod -R 777 /tmp/VideoTranscoding/logs/
RUN touch /tmp/VideoTranscoding/logs/FFMPEG_Spring.log && \
    ln -sf /dev/stdout /tmp/VideoTranscoding/logs/FFMPEG_Spring.log
RUN touch /tmp/VideoTranscoding/logs/FFMPEG_Core.log && \
    ln -sf /dev/stdout /tmp/VideoTranscoding/logs/FFMPEG_Core.log
RUN touch /tmp/VideoTranscoding/logs/FFMPEG_Hibernate.log && \
    ln -sf /dev/stdout /tmp/VideoTranscoding/logs/FFMPEG_Hibernate.log    
#Config PATH to save Videos
RUN mkdir -p /tmp/VideoTranscoding/videos/transcoded/
RUN mkdir -p /tmp/VideoTranscoding/videos/original/
RUN chmod -R 777 /tmp/VideoTranscoding/videos/original/
RUN chmod -R 777 /tmp/VideoTranscoding/videos/transcoded/


#Config APP
COPY VideoTranscoding--BETA-0.2.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
