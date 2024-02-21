#!/bin/bash
sudo groupadd csye6225
sudo useradd -g csye6225 -s /usr/sbin/nologin csye6225
sudo chown  csye6225:csye6225 /home/karthikvanabhojanacloud/*
sudo chmod  744 /home/karthikvanabhojanacloud/*
