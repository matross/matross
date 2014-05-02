#!/bin/sh

set -e

here=$(dirname $0)
resources=$here/../../.test/ssh-configs

mkdir -p $resources

vagrant up

vagrant status | grep '(' | awk '{ print $1 }' |
  xargs -I {} \
    /bin/sh -c "vagrant ssh-config {} > $resources/{}"

echo generated ssh configs: $(ls $resources)
