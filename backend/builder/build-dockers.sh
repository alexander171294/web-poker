#!/bin/bash
echo Building dockers images with version $1
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
./build.sh $1
cd ../orchestrator
./build.sh $1
cd ../room
./build-poker.sh $1
echo [OK] all docker images builded.