#!/bin/bash
sudo mv /tmp/java.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable java.service
