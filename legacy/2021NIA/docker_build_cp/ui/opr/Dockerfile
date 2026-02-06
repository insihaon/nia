FROM ubuntu:18.04
LABEL maintainer="england@koren.kr"

RUN apt-get update
RUN apt-get install -y apache2
ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2
ENV APACHE_PID_FILE /var/run/apache2/apache2.pid

RUN apt-get install -y openssh-server openssh-client
RUN apt-get install -y vim
ADD ./apache2.conf /etc/apache2/apache2.conf
#RUN apt-get install -y a2enmod


ADD ./sshd_config /etc/ssh/sshd_config
#ADD ./httpd.conf /usr/local/apache2/conf/httpd.conf
RUN echo 'root:!mezzonice@' | chpasswd

WORKDIR /root
ADD ./profile ./.profile
ADD entrypoint.sh /root/entrypoint/entrypoint.sh
RUN chmod +x /root/entrypoint/entrypoint.sh

ADD ./manager.tar /var/www/html
WORKDIR /var/www/html

EXPOSE 80
EXPOSE 22

ENTRYPOINT ["/root/entrypoint/entrypoint.sh"]
