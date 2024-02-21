#!/bin/bash

echo "Installing ..."



echo "Installing Maven..."
sudo dnf module enable  -y maven:3.8

sudo dnf install -y maven

echo "Installing Java..."
sudo yum install -y java-11-openjdk-devel

sudo update-alternatives --set java $(sudo update-alternatives --list java | grep 'java-11-openjdk' | head -n 1)

sudo update-alternatives --set javac $(sudo update-alternatives --list javac | grep 'java-11-openjdk' | head -n 1)



echo "Installing SQL Server..."
sudo yum install -y mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld


echo "Installing unzip..."
sudo yum install -y unzip


#mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'k19971998';" #constant


# Update MySQL root password
mysql -u "$DB_USERNAME" -e "ALTER USER '$DB_USERNAME'@'localhost' IDENTIFIED BY '$DB_PASSWORD';"



echo "Installation completed."