#!/bin/sh

vagrant status | grep '(' | awk '{ print $1 }'
