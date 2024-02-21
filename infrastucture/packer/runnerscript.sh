sudo yum install -y unzip

sudo yum install -y java-11-openjdk-devel

# Enabling a specific Maven module version
sudo dnf module enable -y maven:3.8

# Installing Maven
sudo dnf install -y maven


#installing mysql


#!/bin/bash

sudo yum -y install mysql-server

# Start the MySQL service
sudo systemctl start mysqld

# Wait for a second to ensure MySQL service is started
sleep 1s

echo "Installing SQL Server..."
sudo yum install -y mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld







mysql -u "$DB_USERNAME" -e "ALTER USER '$DB_USERNAME'@'localhost' IDENTIFIED BY '$DB_PASSWORD';"
sudo yum install -y unzip

ZIP_FILE="/tmp/webapp-1.2.0-RELEASE.zip"
DESTINATION="/home/karthikvanabhojanacloud/"

unzip "$ZIP_FILE" -d "$DESTINATION"

sudo groupadd csye6225
sudo useradd -r -s /usr/sbin/nologin -g csye6225 csye6225
sudo mkdir -p /opt/csye6225/

sudo cp /tmp/application.jar /opt/csye6225/
sudo chown -R csye6225:csye6225 /opt/csye6225/application.jar


sudo cp /tmp/application.jar /opt/csye6225/
sudo chown -R csye6225:csye6225 /opt/csye6225/application.jar

cat <<EOF | sudo tee /etc/systemd/system/webapp.service
[Unit]
Description=webapp
After=network.target

[Service]
User=csye6225
Group=csye6225
ExecStart=/usr/bin/java -jar /opt/csye6225/application.jar
WorkingDirectory=/opt/csye6225
Restart=always


[Install]
WantedBy=multi-user.target
EOF

# Reload systemd to recognize the new service
sudo systemctl daemon-reload

# Enable your service to start at boot
sudo systemctl enable webapp

# Optional: Start the service now
sudo systemctl start webapp

# Check the status of your service (optional)
sudo systemctl status webapp