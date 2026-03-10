docker stop ipsdn_opt_ui_1
docker rm ipsdn_opt_ui_1
docker rmi ipsdn_opt_ui

cd /home/mezzo/ipsdn_opt/ui/manager/dist
tar cvf manager.tar .
cd /home/mezzo/ipsdn_opt/docker_build/ui/opr

mv /home/mezzo/ipsdn_opt/ui/manager/dist/manager.tar .
docker build -t ipsdn_opt_ui .
docker run -d -it -p8288:80 -p22288:22 --restart always --name ipsdn_opt_ui_1 ipsdn_opt_ui
