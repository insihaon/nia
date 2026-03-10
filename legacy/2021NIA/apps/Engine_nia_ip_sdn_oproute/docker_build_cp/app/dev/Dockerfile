FROM maven:3.8.2-jdk-11
LABEL maintainer="myidkyk@gmail.com"

ENV JAVA_HOME=/usr/local/openjdk-11
ENV PATH=/usr/local/openjdk-11/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

RUN apt-get update
RUN apt-get install -y openssh-server openssh-client
RUN apt-get install -y vim
RUN apt-get install -y sysstat

WORKDIR /root
USER root

ADD ./sshd_config /etc/ssh/sshd_config
RUN echo 'root:!mezzonice@' | chpasswd

ADD ./profile ./.profile
ADD entrypoint.sh /root/entrypoint/entrypoint.sh
RUN chmod +x /root/entrypoint/entrypoint.sh

#VOLUME ["/home/koren/ipsdn_opt/app", "/root/ipsdn_opt"]
# install IPSDN-OPT running image
WORKDIR /root/ipsdn_opt

EXPOSE 8080
EXPOSE 22

ENTRYPOINT ["/root/entrypoint/entrypoint.sh"]
