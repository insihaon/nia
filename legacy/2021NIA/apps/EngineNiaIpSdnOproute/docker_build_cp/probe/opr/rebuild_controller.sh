IPADDR='103.255.249.31'
PORT='2222'

JARFILENAME='probe-0.0.1-SNAPSHOT.jar'

#echo ${JARFILENAME}
#echo "sshpass -p 'ipsdnpw1004' ssh -p ${PORT} -o StrictHostKeyChecking=no root@${IPADDR} "
#echo "pkill -9 -f java && sleep 3 && nohup java -jar -Dspring.profiles.active=prod ~/${JARFILENAME} &"


#exit 1
echo "1. try to copy jar file..."
sshpass -p 'ipsdnpw1004' scp -P ${PORT} -o StrictHostKeyChecking=no root@${IPADDR}:~/ipsdn_app/target/${JARFILENAME} .
#scp -P ${PORT} root@${IPADDR}:~/ipsdn_app/target/${JARFILENAME} .

if [ $? -eq 0 ]; then
  echo "Successfully copied"
else
  echo "failed to copy jar file!!!"
  exit 1
fi

docker stop ipsdn_controller_1
docker rm ipsdn_controller_1
docker build -t ipsdn_controller .
docker run -d -it -p8088:8080 -p22222:22 --restart always --name ipsdn_controller_1 ipsdn_controller

