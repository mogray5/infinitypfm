# Install Flatpak

# wget https://sdk.gnome.org/keys/gnome-sdk.gpg
# flatpak remote-add --gpg-import=gnome-sdk.gpg gnome https://sdk.gnome.org/repo/
# flatpak remote-add --gpg-import=gnome-sdk.gpg gnome-apps https://sdk.gnome.org/repo-apps/
# flatpak install gnome org.gnome.Platform 3.20

# Create Bundle on Ubuntu 16.04 LTS

sudo apt-get install libibatis-java libcommons-io-java libcommons-compress-java libhsqldb-java libgettext-commons-java libcommons-httpclient-java libjson-java
sudo apt-get install libswt-gtk-3-java libswt-webkit-gtk-3-jni libswt-cairo-gtk-3-jni
sudo apt-get install bzr ant default-jdk
#flatpak install gnome org.gnome.Platform//3.24 org.gnome.Sdk//3.24
#bzr branch lp:infinitypfm

flatpak build-init infinitypfm-flatpak org.infinitypfm.App org.gnome.Sdk org.gnome.Platform 3.24

echo $PWD

ant

cp infinitypfm-ui.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/swt.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/ibatis.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/commons-io.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/commons-compress.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/gettext-commons.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/commons-httpclient.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/commons-beanutils.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/commons-logging.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/json-lib.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/commons-lang.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/ezmorph.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/commons-collections3.jar infinitypfm-flatpak/files/;
cp -L /usr/share/java/hsqldb.jar infinitypfm-flatpak/files/;
cp -L /usr/lib/jni/libswt-cairo-gtk-3836.so infinitypfm-flatpak/files/;
cp -L /usr/lib/jni/libswt-atk-gtk-3836.so infinitypfm-flatpak/files/;
cp -L /usr/lib/jni/libswt-awt-gtk-3836.so infinitypfm-flatpak/files/;
cp -L /usr/lib/jni/libswt-gtk-3836.so infinitypfm-flatpak/files/;
cp -L /usr/lib/jni/libswt-pi-gtk-3836.so infinitypfm-flatpak/files/;
cp -L /usr/lib/jni/libswt-webkit-gtk-3836.so infinitypfm-flatpak/files/;
cp -L -R /usr/lib/jvm/java-1.8.0-openjdk-amd64 infinitypfm-flatpak/files/;
mkdir infinitypfm-flatpak/files/bin/
cp flatpak/infinitypfm_fp.sh infinitypfm-flatpak/files/bin/

flatpak build-finish infinitypfm-flatpak --persist=.java --share=ipc --socket=x11 --share=network --filesystem=home --command=infinitypfm_fp.sh

# Test

# flatpak build-export repo infinitypfm-flatpak
# flatpak --user remote-add --no-gpg-verify infinitypfm-repo repo
# flatpak --user install infinitypfm-repo org.infinitypfm.App
# flatpak run org.infinitypfm.App
# flatpak --user uninstall org.infinitypfm.App
