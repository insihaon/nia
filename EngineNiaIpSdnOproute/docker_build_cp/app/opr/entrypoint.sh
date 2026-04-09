#! /bin/bash

sleep 5
service ssh start
java -jar -Dspring.profiles.active=prod /root/ipsdn_opt/ipsdn_opt_app.jar
#tail -f /dev/null
