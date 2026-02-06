docker build -t ipsdn_opt .
docker run -d  -p 22088:22 -p 8088:8080 -v /etc/localtime:/etc/localtime --name ipsdn_opt_1 ipsdn_opt

#docker run -d -it -p 22311:22 -p 8182:8080 --name ipsdn_opt_1 ipsdn_opt
