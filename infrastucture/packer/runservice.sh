#!/bin/bash

sudo setenforce 0

sudo mv /tmp/start.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable start.service