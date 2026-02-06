#! /bin/bash

sleep 5
service ssh start
#java -jar -Dspring.profiles.active=prod /root/ipsdn_opt/app.jar
tail -f /dev/null
