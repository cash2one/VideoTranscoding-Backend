FROM centos
MAINTAINER luisca_jl@hotmail.com
	
ENV JAVA_VERSION 8u144
ENV BUILD_VERSION b01
 
# Upgrading system
RUN yum -y upgrade
RUN yum -y install wget
 
# Downloading & Config Java 8
RUN yum -y install java-1.8.0-openjdk
#Download FFMPEG
RUN yum -y install epel-release 
RUN yum -y update 
RUN rpm --import http://li.nux.ro/download/nux/RPM-GPG-KEY-nux.ro
RUN rpm -Uvh http://li.nux.ro/download/nux/dextop/el7/x86_64/nux-dextop-release-0-5.el7.nux.noarch.rpm
RUN yum -y install ffmpeg ffmpeg-devel -y

#Config LOGS
RUN mkdir -p /tmp/logs/VideoTranscoding
RUN chmod -R 777 /tmp/logs/VideoTranscoding
#Config PATH to save Videos
RUN mkdir -p /tmp/videos/transcoded
RUN chmod -R 777 /tmp/videos/transcoded

#Config APP
COPY target/VideoTranscoding--BETA-0.1.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
