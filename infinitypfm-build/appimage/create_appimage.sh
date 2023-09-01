# Create Bundle on Ubuntu 20.04 LTS

echo -e "\n
Enter release version to use when naming the app image: \n"
read response

chmod a+x infinitypfm.AppDir/AppRun;
cp -L -R /usr/lib/jvm/java-17-openjdk-amd64 infinitypfm.AppDir/usr/lib/;

#cp infinitypfm.run infinitypfm.AppDir/usr/bin/infinitypfm

~/bin/appimagetool-x86_64.AppImage infinitypfm.AppDir infinitypfm-"$response".AppImage generate
