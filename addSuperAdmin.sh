#!/bin/sh

docker cp ./addSuperAdmin.sql wbotcontainer:/
docker exec -it wbotcontainer bash -c 'mysql -u user -p < /addSuperAdmin.sql'
