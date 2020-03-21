# Pre-Step - Download SWT 3.8.2 for Windows 64bit.
#http://archive.eclipse.org/eclipse/downloads/drops/R-3.8.2-201301310800/download.php?dropFile=swt-3.8.2-win32-win32-x86_64.zip

WIN_SWT_LOCAL=/tmp/swt-3.8.2-win32-win32-x86_64

# Create Archive Bundle on Ubuntu 18.04 LTS

echo -e "\n
Enter release version to use when naming the archive: \n"
read response

echo -e "\n
Install required libraries? (y|n): \n"
read doinstall

rm -rf infinitypfm.archive
rm -rf infinitypfm-$response
rm infinitypfm-$response.zip

if [[ "${doinstall}" == *"y"* ]]
then
	sudo apt install libibatis-java libcommons-io-java libcommons-compress-java libhsqldb-java libgettext-commons-java libcommons-httpclient-java libjson-java libmail-java libcommons-csv-java libfreemarker-java liblog4j1.2-java
	sudo apt install libswt-gtk-3-java libswt-webkit-gtk-3-jni libswt-cairo-gtk-3-jni	
	sudo apt install ant default-jdk
fi

mkdir -p infinitypfm.archive/bin
mkdir -p infinitypfm.archive/lib

ant

cp archive/infinitypfm.bat infinitypfm.archive/bin;
cp archive/infinitypfm.sh infinitypfm.archive/bin;
chmod a+x infinitypfm.archive/bin;
#cp infinitypfm-ui/infinitypfm.desktop infinitypfm.archive/;
#cp infinitypfm-ui/infinitypfm.xpm infinitypfm.archive/;
cp *.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/swt.jar infinitypfm.archive/lib/;
cp -L $WIN_SWT_LOCAL/swt.jar infinitypfm.archive/lib/swt_win.jar;
cp -L /usr/share/java/ibatis.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-io.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-compress.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-codec.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/gettext-commons.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-httpclient.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-beanutils.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-configuration.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-logging.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/json-lib.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-lang.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/ezmorph.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-collections3.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/hsqldb.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/junit.jar infinitypfm.archive/lib/;
cp -L /usr/lib/jni/libswt-cairo-gtk-3836.so infinitypfm.archive/lib/;
cp -L /usr/lib/jni/libswt-atk-gtk-3836.so infinitypfm.archive/lib/;
cp -L /usr/lib/jni/libswt-awt-gtk-3836.so infinitypfm.archive/lib/;
cp -L /usr/lib/jni/libswt-gtk-3836.so infinitypfm.archive/lib/;
cp -L /usr/lib/jni/libswt-pi-gtk-3836.so infinitypfm.archive/lib/;
cp -L /usr/lib/jni/libswt-webkit-gtk-3836.so infinitypfm.archive/lib/;
cp -L /usr/share/java/mailapi.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/commons-csv.jar infinitypfm.archive/lib/;
cp -L /usr/share/java/freemarker.jar infinitypfm.archive/usr/lib/;
cp -L /usr/share/java/log4j-1.2.jar infinitypfm.archive/usr/lib/;
#cp -L -R /usr/lib/jvm/java-1.8.0-openjdk-amd64 infinitypfm.archive/lib/;

#cp infinitypfm.run infinitypfm.archive/usr/bin/infinitypfm

# appimagetool-x86_64.AppImage infinitypfm.archive infinitypfm-"$response".AppImage generate

mv infinitypfm.archive infinitypfm-$response
zip  -r infinitypfm-$response.zip infinitypfm-$response/bin infinitypfm-$response/lib

