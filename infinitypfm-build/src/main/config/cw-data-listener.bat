
echo %1

del cargowatch.properties
mklink cargowatch.properties "%1\cargowatch.properties"

java -cp "lib/*;%1/*;lib-swt-win/*" com.orbcomm.demo.client.Application