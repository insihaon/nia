IPADDR='103.22.222.7'
PORT='22080'

JARFILENAME='app-0.0.1-SNAPSHOT.jar'

echo "1. try to copy jar file..."
sshpass -p '!mezzonice@' scp -P ${PORT} -o StrictHostKeyChecking=no root@${IPADDR}:~/ipsdn_opt/app/target/${JARFILENAME} ./ipsdn_opt_app.jar

if [ $? -eq 0 ]; then
  echo "Successfully copied"
else
  echo "failed to copy jar file!!!"
  exit 1
fi

docker stop ipsdn_opt_1
docker rm ipsdn_opt_1
docker build -t ipsdn_opt .
docker run -d -it -p8088:8080 -p22088:22 --restart always --name ipsdn_opt_1 ipsdn_opt

