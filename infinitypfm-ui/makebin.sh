#!/bin/bash

HOME_DIR="infinitypfm_bin/infinitypfm"
LIB_DIR="$HOME_DIR/lib"
CLASSPTH="infinitypfm-ui.jar"

rm -rf infinitypfm_bin

mkdir -p $LIB_DIR

arr=$(echo $1 | tr ":" "\n")

for x in $arr
do
	filename=$(basename "$x")
	CLASSPTH=$CLASSPTH":lib/$filename"
	echo "Copying dependancy $x"
	cp -H $x "$LIB_DIR/$filename"
done

cp infinitypfm-ui.jar "$HOME_DIR/"

echo "#!/bin/bash" > "$HOME_DIR/infinitypfm.sh"
echo "java -cp $CLASSPTH org.infinitypfm.client.InfinityPfm" >>  "$HOME_DIR/infinitypfm.sh"
chmod  744  "$HOME_DIR/infinitypfm.sh"

echo "#!/bin/bash" > "$HOME_DIR/startup.sh"
echo echo "Infinitypfm extracted.  Run infinitypfm.sh to launch the application" >> "$HOME_DIR/startup.sh"
chmod  744  "$HOME_DIR/startup.sh"

cd infinitypfm_bin
makeself --gzip --current infinitypfm infinitypfm.bin "Infinitypfm Self Extractor" ./startup.sh
