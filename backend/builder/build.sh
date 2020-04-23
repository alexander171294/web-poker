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
cd ../../room
echo [PCKG] Building Generic Room
mvn clean package
cd ../orchestrator
echo [PCKG] Building Orchestrator System
mvn clean package
cd ../ApiServer
echo [PCKG] Building ApiServer Service.
mvn clean package
echo Done all tasks, [LIB-EXCHANGE] [LIB-PERSISTENCE] [LIB-ROOM-INT] [PCKG-POKER-ROOM] [LIB-ROOM] [PCKG-ORCHESTRATOR] [PCKG-API-SERVER]

