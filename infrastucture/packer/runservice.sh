#!/bin/bash

sudo setenforce 0

sudo mv /tmp/start.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable start.service

sudo mv /tmp/config.yaml /etc/google-cloud-ops-agent/
sudo systemctl restart google-cloud-ops-agent