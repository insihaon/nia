docker stop ipsdn_opt_probe_dev_1
docker rm ipsdn_opt_probe_dev_1
docker rmi ipsdn_opt_probe_dev
docker build -t ipsdn_opt_probe_dev .
docker run -d  --privileged -p22081:22 -p8081:8080 -v /home/mezzo/ipsdn_opt/probe:/root/ipsdn_opt/probe --name ipsdn_opt_probe_dev_1 ipsdn_opt_probe_dev
