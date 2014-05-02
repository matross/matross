#!/bin/sh

set -e

for vm in `find $(dirname $0)/../../.test/ssh-configs -type f`; do
  echo "Testing against: " $(basename $vm)
  env TEST_SSH_CONF=$vm lein test :integration
done
