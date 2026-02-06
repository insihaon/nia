FROM frrouting/frr
LABEL maintainer="myidkyk@gmail.com"

RUN apk update && apk add nginx
RUN apk add openssh
RUN apk add vim

WORKDIR /root
USER root

ADD ./sshd_config /etc/ssh/sshd_config
ADD ./entrypoint.sh /root/entrypoint.sh
RUN  chmod 777 /root/entrypoint.sh
RUN ssh-keygen -P "" -t rsa -f /etc/ssh/ssh_host_rsa_key
RUN echo 'root:ipsdnpw1004' | chpasswd
COPY ./frr /etc/frr

EXPOSE 179
EXPOSE 22
CMD ["/root/entrypoint.sh"]
#CMD ["/usr/lib/frr/frrinit.sh", "start"]
#CMD ["tail", "-f", "/dev/null"]
