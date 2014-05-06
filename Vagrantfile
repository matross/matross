# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  boxes = [
    ["centos-6.5", "https://github.com/2creatives/vagrant-centos/releases/download/v6.5.1/centos65-x86_64-20131205.box"],
    ["ubuntu-12.04", "http://cloud-images.ubuntu.com/vagrant/precise/current/precise-server-cloudimg-amd64-vagrant-disk1.box"],
    ["ubuntu-14.04", "http://cloud-images.ubuntu.com/vagrant/trusty/current/trusty-server-cloudimg-amd64-vagrant-disk1.box"],
    ["openbsd-5.0", "https://github.com/downloads/stefancocora/openbsd_i386-vagrant/openbsd50_i386.box"],
    ["freebsd-10.0", "http://files.wunki.org/freebsd-10.0-amd64-wunki.box"]
  ]

  def create_box(config, name, url)
    config.vm.define name do |box|
      box.vm.synced_folder "./", "/vagrant", disabled: true
      box.vm.box = name
      box.vm.box_url = url
    end
  end

  for name, url in boxes do
    create_box(config, name, url)
  end
end
