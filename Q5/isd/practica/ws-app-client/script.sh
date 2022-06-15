#! /bin/bash

#Usage: ./script.sh "-a 'A Coruña' 'Ciudad Vieja' '2022-08-15T11:00' 20 10"               

mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" \
-Dexec.args="$1"
