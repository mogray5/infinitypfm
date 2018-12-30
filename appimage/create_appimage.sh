rm -rf infinitypfm.AppDir
# Create Bundle on Ubuntu 16.04 LTS

echo -e "\n
Enter release version to use when naming the app image: \n"
read response

echo -e "\n
Install required libraries? (y|n): \n"
read doinstall

if [[ "${doinstall}" == *"y"* ]]
then
	sudo apt-get install libibatis-java libcommons-io-java libcommons-compress-java libhsqldb-java libgettext-commons-java libcommons-httpclient-java libjson-java libmail-java libcommons-csv-java
	sudo apt-get install libswt-gtk-3-java libswt-webkit-gtk-3-jni libswt-cairo-gtk-3-jni	
	sudo apt-get install ant default-jdk
fi

mkdir -p infinitypfm.AppDir/usr/bin
mkdir -p infinitypfm.AppDir/usr/lib

ant

cp appimage/AppRun infinitypfm.AppDir/;
chmod a+x infinitypfm.AppDir/AppRun;
cp infinitypfm-ui/infinitypfm.desktop infinitypfm.AppDir/;
cp infinitypfm-ui/infinitypfm.xpm infinitypfm.AppDir/;
cp *.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/swt.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/ibatis.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-io.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-compress.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-codec.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/gettext-commons.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-httpclient.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-beanutils.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-configuration.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-logging.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/json-lib.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-lang.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/ezmorph.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-collections3.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/hsqldb.jar infinitypfm.AppDir/usr/lib/;
cp -L /usrshare/java/junit.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/lib/jni/libswt-cairo-gtk-3836.so infinitypfm.AppDir/usr/lib/;
cp -L /usr/lib/jni/libswt-atk-gtk-3836.so infinitypfm.AppDir/usr/lib/;
cp -L /usr/lib/jni/libswt-awt-gtk-3836.so infinitypfm.AppDir/usr/lib/;
cp -L /usr/lib/jni/libswt-gtk-3836.so infinitypfm.AppDir/usr/lib/;
cp -L /usr/lib/jni/libswt-pi-gtk-3836.so infinitypfm.AppDir/usr/lib/;
cp -L /usr/lib/jni/libswt-webkit-gtk-3836.so infinitypfm.AppDir/usr/lib/;
cp -L /usr/shre/java/mailapi.jar infinitypfm.AppDir/usr/lib/;
cp -L /usr/share/java/commons-csv.jar infinitypfm.AppDir/usr/lib/;
cp -L -R /usr/lib/jvm/java-1.8.0-openjdk-amd64 infinitypfm.AppDir/usr/lib/;

#cp infinitypfm.run infinitypfm.AppDir/usr/bin/infinitypfm

appimagetool-x86_64.AppImage infinitypfm.AppDir infinitypfm-"$response".AppImage generate
