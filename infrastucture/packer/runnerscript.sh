#!/bin/bash
sudo yum install -y unzip

sudo yum install -y java-11-openjdk-devel

# Enabling a specific Maven module version
sudo dnf module enable -y maven:3.8

# Installing Maven
sudo dnf install -y maven


#installing mysql



sudo yum -y install mysql-server

sudo systemctl start mysqld

sleep 1s

echo "Installing SQL Server..."
sudo yum install -y mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld





mysql -u "$DB_USERNAME" -e "ALTER USER '$DB_USERNAME'@'localhost' IDENTIFIED BY '$DB_PASSWORD';"
sudo yum install -y unzip

sudo groupadd csye6225
sudo useradd -r -s /usr/sbin/nologin -g csye6225 csye6225
sudo mkdir -p /opt/csye6225/

sudo cp /tmp/webapp-1.2.0-RELEASE.zip /opt/csye6225/
sudo yum install -y unzip

ZIP_FILE="/opt/csye6225/webapp-1.2.0-RELEASE.zip"
DESTINATION="/home/karthikvanabhojanacloud/"

sudo chown -R csye6225:csye6225 /opt/csye6225/target/webapp-1.2.0-RELEASE.jar


sudo cp /tmp/webapp-1.2.0-RELEASE.jar /opt/csye6225/
sudo chown -R csye6225:csye6225 /opt/csye6225/target/webapp-1.2.0-RELEASE.jar

cat <<EOF | sudo tee /etc/systemd/system/webapp.service
[Unit]
Description=webapp
After=network.target

[Service]
User=csye6225
Group=csye6225
ExecStart=/usr/bin/java -jar /opt/csye6225/target/webapp-1.2.0-RELEASE.jar
WorkingDirectory=/opt/csye6225
Restart=always


[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload

sudo systemctl enable webapp

sudo systemctl start webapp

sudo systemctl status webapp