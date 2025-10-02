#!/bin/bash
cd ..
user=$(whoami)
sudo mvn clean validate compile assembly:assembly -DdescriptorId=jar-with-dependencies &&
sudo chown -R $user ./target &&
cp ./target/autodownload-1-jar-with-dependencies.jar ./autodownload.jar