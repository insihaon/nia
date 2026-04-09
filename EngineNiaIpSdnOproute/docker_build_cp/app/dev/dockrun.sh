docker build -t ipsdn_opt_dev .
docker run -d  -p 22080:22 -p 8080:8080 -v /home/mezzo/ipsdn_opt/app:/root/ipsdn_opt/app -v /etc/localtime:/etc/localtime --name ipsdn_opt_dev_1 ipsdn_opt_dev
