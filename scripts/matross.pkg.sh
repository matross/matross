#!/bin/sh

HERE=$(dirname $0)
(cd $HERE/..; exec java $JAVA_OPTS -jar $HERE/../lib/matross.jar $@)
