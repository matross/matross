#!/bin/sh

set -e

BOX_NAME=$1

HERE=$(dirname $0)
RESOURCES=$HERE/../../.test/ssh-configs

mkdir -p $RESOURCES

if [ "${BOX_NAME}" = "local" ]
then

  cat > $RESOURCES/local <<EOF
Host local
  HostName 127.0.0.1
EOF

else

  vagrant up $BOX_NAME

  vagrant status \
      | grep '(' \
      | grep 'running' \
      | awk '{ print $1 }' \
      | xargs -I {} \
      /bin/sh -c "vagrant ssh-config {} > $RESOURCES/{}"

fi

echo generated ssh config: $BOX_NAME
