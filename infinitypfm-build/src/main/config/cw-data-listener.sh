#!/bin/bash

set -x

echo %1

rm cargowatch.properties
ln -s $1/cargowatch.properties

java -cp "lib/*:$1/*:lib-swt-lin/*" com.orbcomm.demo.client.Application

set +x
