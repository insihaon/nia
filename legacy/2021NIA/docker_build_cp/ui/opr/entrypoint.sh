#! /bin/bash

sleep 5
service ssh start
a2enmod rewrite
/usr/sbin/apache2ctl -D FOREGROUND
