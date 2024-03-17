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
variable "db_password" {}
variable "db_username" {}

source "googlecompute" "image-creation" {
  project_id          = var.project_id
  source_image_family = var.source_image_family
  ssh_username        = var.ssh_username
  zone                = var.zone
  image_name          = "${var.image_name}-${formatdate("YYYYMMDDHHmmss", timestamp())}"
}

build {
  provisioner "file" {
    source      = "webapp-1.2.0-RELEASE.zip"
    destination = "/tmp/"
  }

  provisioner "shell" {
    script = "infrastucture/packer/setup.sh"
  }

  provisioner "shell" {
    script = "infrastucture/packer/user.sh"
  }

  provisioner "shell" {
    script = "infrastucture/packer/unzip.sh"
  }

  provisioner "shell" {
    script = "infrastucture/packer/installgcp.sh"
  }

  provisioner "file" {
    source      = "systemd/system/start.service"
    destination = "/tmp/start.service"
  }

  provisioner "shell" {
    script = "infrastucture/packer/runservice.sh"
  }

  sources = ["source.googlecompute.image-creation"]
}
