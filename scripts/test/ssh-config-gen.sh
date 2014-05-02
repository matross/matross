#!/bin/sh

set -e

BOX_NAME=$1

here=$(dirname $0)
resources=$here/../../.test/ssh-configs

mkdir -p $resources

vagrant up $BOX_NAME

vagrant status \
    | grep '(' \
    | grep 'running' \
    | awk '{ print $1 }' \
    | xargs -I {} \
    /bin/sh -c "vagrant ssh-config {} > $resources/{}"

echo generated ssh configs: $(ls $resources)
