# Create Archive Bundle on Ubuntu 20.04 LTS

echo -e "\n
Enter release version to use when naming the archive: \n"
read response

rm infinitypfm-$response.zip

chmod a+x infinitypfm.archive/bin;

mv infinitypfm.archive infinitypfm-$response
zip  -r infinitypfm-$response.zip infinitypfm-$response/bin infinitypfm-$response/lib

