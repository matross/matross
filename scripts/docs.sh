#!/bin/sh

lein run -m matross.docs

if [ -n "$1" ]; then
  (cd docs; make $1)
fi
