<p align="center">
<img src="https://github.com/luiscajl/VideoTranscoding-Backend/blob/developing/src/main/resources/static/images/VideoTranscoding_logo.png"></img>
</p>

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6b9cd649a14a4431882a51dd2c779e85)](https://www.codacy.com/app/luiscajl/VideoTranscoding?utm_source=github.com&utm_medium=referral&utm_content=luiscajl/VideoTranscoding&utm_campaign=badger)
[![Maintainability](https://api.codeclimate.com/v1/badges/a3de0e21cd574e78341e/maintainability)](https://codeclimate.com/github/luiscajl/VideoTranscoding/maintainability)
[![Build Status](https://travis-ci.org/luiscajl/VideoTranscoding-Backend.svg?branch=developing)](https://travis-ci.org/luiscajl/VideoTranscoding-Backend)
![Version](https://img.shields.io/badge/version-1.0-brightgreen.svg?style=flat)
[![License badge](https://img.shields.io/badge/license-Apache2-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0)

This application transcode a video that you send on all formats what you want and diferent resolutions. It´s build on a docker container to simplify the installation.
## Test it:
1. Visit [Api VideoTranscoding](https://api.videotranscoding.es) user/pass for the user authentication.
2. You can see the methods on [Api Documentation](https://api.videotranscoding.es/swagger-ui.html)
## Run it:
1. Install [docker](https://docs.docker.com/engine/installation/) for your machine.
2. Download [the docker-compose.yml](https://raw.githubusercontent.com/luiscajl/VideoTranscoding/master/docker-compose.yml).
3. Run this command on your terminal on the path of docker-compose.
```sh
sudo docker-compose up 
```
4. Visit [https://localhost:8443](https://localhost:8443) on your web browser.
5. You can navigate to the api documentation and throw the methods. user/pass are the users for loggin by basic auth
6. Stop docker compose when you finished
```sh
ctrl+c
```

## Develop it:
1. Clone respository:
```sh
git clone https://github.com/luiscajl/VideoTranscoding-Backend.git 
```
2. Run this script to install ffmpeg on your mac.
```sh
sh /scripts/install_ffmpeg_macosx.sh
```
or this for linux
```sh
sh /scripts/install_ffmpeg_linux.sh
```
3. Download and install [MySQL-CE](https://dev.mysql.com/downloads/).
4. Now you can import the project on your ide and start SpringBoot Application or develop the project after change on the application.properties (src/main/resources) the user and password for your mysql configuration.


The project is finished and ready to deliver to my tutor.

## Screenshots:
<p align="center">
  <img src="https://github.com/luiscajl/VideoTranscoding/blob/master/screens/screenIndexF.png"/>
</p>
<p align="center">
  <img src="https://github.com/luiscajl/VideoTranscoding/blob/master/screens/screenSucessfully.png"/>
</p>
<p align="center">
  <img src="https://github.com/luiscajl/VideoTranscoding/blob/master/screens/screenStatus.png"/>
</p>
<p align="center">
  <img src="https://github.com/luiscajl/VideoTranscoding/blob/master/screens/screenMedia.png"/>
</p>
<p align="center">
  <img src="https://github.com/luiscajl/VideoTranscoding/blob/master/screens/screenUser.png"/>
</p>
<p align="center">
  <img src="https://github.com/luiscajl/VideoTranscoding/blob/master/screens/screenConversion.png"/>
</p>





