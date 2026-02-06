FROM node:latest
LABEL maintainer="england@koren.kr"

RUN apt-get update
RUN apt-get install -y openssh-server openssh-client
RUN apt-get install -y vim
RUN apt-get install -y sysstat
RUN npm install -g @vue/cli

WORKDIR /root
USER root

ADD ./sshd_config /etc/ssh/sshd_config
RUN echo 'root:!mezzonice@' | chpasswd

ADD ./profile ./.profile
ADD entrypoint.sh /root/entrypoint/entrypoint.sh
RUN chmod +x /root/entrypoint/entrypoint.sh

WORKDIR /root/ipsdn_opt

EXPOSE 8080
EXPOSE 22

ENTRYPOINT ["/root/entrypoint/entrypoint.sh"]
