#!/bin/sh
SQL="";
echo "Enter your SUPERADMIN email"
read -r email;
SQL=$SQL"use wbot;
         INSERT INTO users(user_id, email, user_role) values(100, '${email}', 'SUPERADMIN');"
echo  "${SQL}" > addSuperAdmin.sql

docker cp ./addSuperAdmin.sql wbotcontainer:/
docker exec -it wbotcontainer bash -c 'mysql -u user -p < /addSuperAdmin.sql'
