docker stop ipsdn_opt_ui_dev_1
docker rm ipsdn_opt_ui_dev_1
docker rmi ipsdn_opt_ui_dev
docker build -t ipsdn_opt_ui_dev .
docker run -d  -p 22280:22 -p 8280:8080 -v /home/mezzo/ipsdn_opt/ui:/root/ipsdn_opt/ui -v /etc/localtime:/etc/localtime --name ipsdn_opt_ui_dev_1 ipsdn_opt_ui_dev
