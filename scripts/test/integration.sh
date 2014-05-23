#!/bin/sh

set -e

if [ -n "${TEST_LOCAL}" ];
then
  echo "Testing against: local connection"
  lein test :integration
  unset TEST_LOCAL
fi

SSH_CONF_DIR=$(dirname $0)/../../.test
if [ -d $SSH_CONF_DIR ];
then
  for vm in `find $SSH_CONF_DIR/ssh-configs -type f`; do
    echo "Testing against: " $(basename $vm)
    env TEST_SSH_CONF=$vm lein test :integration
  done
fi
