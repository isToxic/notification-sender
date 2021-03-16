#!/bin/bash
port=9998 # application port
platform=postgres # database platform
db_address=172.17.0.4 # database ip
db_port=5432 # database port
db_name=notification-service # database name
db_user=postgres # database user
db_pass="" # database password
sn_366_sms=dfgdfg # service number for 366 sms
sn_366_push=dfgdfgdf #service number for 366 push
sn_gorzdrav_sms=dfgdfg #service number for gorzdrav sms
sn_gorzdrav_push=dfgdfgdf #service number for gorzdrav push
send_url=http://www.send.to # url for sending notifications

export port
export platform
export db_address
export db_port
export db_name
export db_user
export db_pass
export sn_366_sms
export sn_366_push
export sn_366_push
export sn_gorzdrav_sms
export sn_gorzdrav_push
export send_url

chmox +x gradlew
./gradlew migrate
docker build -t notification-sender .
docker rm -f notification-sender || true
docker run -d -p $port:$port --name notification-sender -e platform=$platform \
-e port=$port -e db_address=$db_address -e db_port=$db_port \
-e db_name=$db_name -e db_user=$db_user -e db_pass=$db_pass \
-e sn_366_sms=$sn_366_sms -e sn_366_push=$sn_366_push \
-e sn_gorzdrav_sms=$sn_gorzdrav_sms -e sn_gorzdrav_push=$sn_gorzdrav_push \
-e send_url=$send_url \
notification-sender

exit 0