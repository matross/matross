#!/bin/sh

set -e

version=0.1.0-SNAPSHOT
here=$(dirname $0)

target=$here/../target
matross=$target/matross-$version

lein do clean, uberjar

mkdir -p $matross/bin $matross/lib $matross/docs

cp -r $here/../plugins $matross
cp $target/matross-$version-standalone.jar $matross/lib/matross.jar
cp $here/matross.pkg.sh $matross/bin/matross

cp $here/../LICENSE $matross/docs/LICENSE
echo $version > $matross/docs/VERSION
cp $here/../README.md $matross/docs/README

tar -czf $target/matross-$version.tar.gz -C $target matross-$version
echo Created: target/matross-$version.tar.gz
