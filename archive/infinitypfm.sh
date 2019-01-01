#!/bin/sh

set -x

rm lib/swt_win.jar
java -Djava.library.path="lib" -cp "lib/*" org.infinitypfm.client.InfinityPfm

set +x
