
#!/bin/bash
sshpass -p $SSH_PASSWORD ssh $SSH_USER@lavandadelpatio.es
sudo docker-compose -f /home/luisca/TFG2017_2018/docker-compose.yml stop
echo $SSH_PASSWORD 
sudo docker-compose -f /home/luisca/TFG2017_2018/docker-compose.yml pull
sudo docker-compose -f /home/luisca/TFG2017_2018/docker-compose.yml start
