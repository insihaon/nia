#!/bin/bash

sleep 5
/usr/lib/frr/frrinit.sh start
/usr/sbin/sshd -D
tail -f /dev/null
