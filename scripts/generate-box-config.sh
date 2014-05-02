#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TARGET="${DIR}/../.build"

BOX_NAME=$1

if [ ! -d $TARGET ]
then
    mkdir $TARGET
fi

if [ ! -f $BOX_NAME ]
then
    vagrant ssh-config $1 > $TARGET/$BOX_NAME.conf
fi
