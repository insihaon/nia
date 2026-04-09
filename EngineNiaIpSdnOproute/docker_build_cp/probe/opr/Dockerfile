FROM maven:3.8.2-jdk-11
LABEL maintainer="myidkyk@gmail.com"

ENV JAVA_HOME=/usr/local/openjdk-11
ENV PATH=/usr/local/openjdk-11/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

RUN apt-get upgrade
RUN apt-get update
#RUN apt-get install -y gcc
#RUN apt-get install -y make
RUN apt-get install -y openssh-server openssh-client
RUN apt-get install -y vim
RUN apt-get install -y snmp
ENV TZ Asia/Seoul
#RUN apt-get install -y chrony
#ADD ./chrony.conf /etc/chrony/chrony.conf
#ADD ./hosts /etc/hosts
#RUN mkdir /run/chrony
#RUN apt-get install -y autoconf
#WORKDIR /root
#RUN git clone https://github.com/perfsonar/owamp.git
#WORKDIR /root/owamp
#RUN git submodule update --init

WORKDIR /root
USER root

ADD ./sshd_config /etc/ssh/sshd_config
RUN echo 'root:ipsdnpw1004' | chpasswd

ADD ./profile ./.profile
ADD entrypoint.sh /root/entrypoint/entrypoint.sh
RUN chmod +x /root/entrypoint/entrypoint.sh

#VOLUME ["/home/koren/ipsdn_opt/app", "/root/ipsdn_opt"]
# install IPSDN-OPT running image
WORKDIR /root/ipsdn_opt

EXPOSE 8080
EXPOSE 22
#EXPOSE 123/udp
#EXPOSE 861
#EXPOSE 861/udp
#EXPOSE 862
#EXPOSE 862/udp
#EXPOSE 863
#EXPOSE 863/udp
#EXPOSE 8888-8904

ENTRYPOINT ["/root/entrypoint/entrypoint.sh"]
