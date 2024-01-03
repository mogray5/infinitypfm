# Create Bundle on Ubuntu 20.04 LTS

echo -e "\n
Enter release version to use when naming the app image: \n"
read response

chmod a+x infinitypfm.AppDir/AppRun;
#cp -L -R /usr/lib/jvm/java-17-openjdk-amd64 infinitypfm.AppDir/usr/lib/;
jlink --add-modules java.base,java.naming,java.desktop,java.sql,java.datatransfer,java.logging --output infinitypfm.AppDir/usr/lib/jre --compress=2 --no-header-files --no-man-pages --strip-debug

#cp infinitypfm.run infinitypfm.AppDir/usr/bin/infinitypfm

~/bin/appimagetool-x86_64.AppImage infinitypfm.AppDir infinitypfm-"$response".AppImage generate
