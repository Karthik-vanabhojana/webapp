#!/bin/bash

echo "Installing ..."
echo "Installing Java..."
sudo yum install -y java-11-openjdk-devel

sudo update-alternatives --set java $(sudo update-alternatives --list java | grep 'java-11-openjdk' | head -n 1)

sudo update-alternatives --set javac $(sudo update-alternatives --list javac | grep 'java-11-openjdk' | head -n 1)

echo "Installing Maven..."
sudo dnf module enable  -y maven:3.8

sudo dnf install -y maven

echo "Installing unzip..."
sudo yum install -y unzip



echo "Installation completed."