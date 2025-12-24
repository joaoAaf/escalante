#!/bin/sh
set -a
. ./.env
set +a
cd apis && mvn spring-boot:run