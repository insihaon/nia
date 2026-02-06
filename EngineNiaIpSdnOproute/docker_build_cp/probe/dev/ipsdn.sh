#! /bin/bash
sleep 5
service ssh start
java -jar -Dspring.profiles.active=prod /root/ipsdn_opt/probe-0.0.1-SNAPSHOT.jar -Dprobe.number=6
