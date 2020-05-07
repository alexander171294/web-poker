cd ../exchange
echo [LIB] INSTALLING EXCHANGE PROTOCOL
mvn clean install
cd ../persistence
echo [LIB] INSTALLING PERSISTENCE LIBRARY
mvn clean install
cd ../room-int
echo [LIB] INSTALLING ROOMs INTERFACE
mvn clean install
cd ../room-poker/room-poker
echo [LIB] INSTALLING ROOM IMPLEMENTATION SUBSYSTEM
mvn clean install
echo [OK] all tasks, [LIB-EXCHANGE] [LIB-PERSISTENCE] [LIB-ROOM-INT] [LIB-ROOM]
cd ../../ApiServer
./build.sh
cd ../orchestrator
./build.sh
cd ../room
./build-poker.sh
echo [OK] all docker images builded.