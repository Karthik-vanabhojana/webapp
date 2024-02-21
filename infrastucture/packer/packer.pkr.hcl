#packer {
#  required_plugins {
#    googlecompute = {
#      source  = "github.com/hashicorp/googlecompute"
#      version = ">= 1"
#    }
#  }
#}
#
#source "googlecompute" "image-creation" {
#  project_id          = "learn-414121"  #input
#  source_image_family = "centos-stream-8" #input
#  ssh_username        = "karthikvanabhojanacloud"
#  zone                = "us-east1-b"
#  image_name          = "image-webapp7tests9" #all input
#
#}
#build {
#  provisioner "file" {
#    source      = "../../webapp-1.2.0-RELEASE.zip"
#    destination = "/tmp/webapp-1.2.0-RELEASE.zip"
#  }
#
#
#  provisioner "shell" {
#    script       = "setup.sh"
#    pause_before = "10s"
#    timeout      = "10s"
#  }
#
#
#  provisioner "shell" {
#    script = "unzip.sh"
#
#  }
#
##
#  provisioner "file" {
#    source      = "../../systemd/system/java.service"
#    destination = "/tmp/java.service"
#  }
#
#  provisioner "shell" {
#    script = "runservice.sh"
#
#  }
#
#  sources = ["sources.googlecompute.image-creation"]
#
#
#}
#
packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = ">= 1"
    }
  }
}

variable "project_id" {}
variable "source_image_family" {}
variable "ssh_username" {}
variable "zone" {}
variable "image_name" {}
variable "db_password"{}
variable "db_username"{}


source "googlecompute" "image-creation" {
  project_id          = var.project_id
  source_image_family = var.source_image_family
  ssh_username        = var.ssh_username
  zone                = var.zone
  image_name          = var.image_name
}

build {
  provisioner "file" {
    source      = "webapp-1.2.0-RELEASE.zip"
    destination = "/tmp/"
  }

  provisioner "shell" {
    script       = "infrastucture/packer/setup.sh"

    environment_vars = [
      "DB_PASSWORD=${var.db_password }",
      "DB_USERNAME=${var.db_username }",

    ]
  }

  provisioner "shell" {
    script = "infrastucture/packer/unzip.sh"
  }

  provisioner "shell" {
    script = "infrastucture/packer/user.sh"
  }
  provisioner "file" {
    source      = "systemd/system/java.service"
    destination = "/tmp/java.service"
  }

  provisioner "shell" {
    script = "infrastucture/packer/runservice.sh"
  }

  sources = ["source.googlecompute.image-creation"]
}
