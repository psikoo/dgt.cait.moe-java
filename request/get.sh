#!/bin/bash

api=$1

#* Get cameraId
curl -s -k --location $api'/cameras' > ./request/cams.json
