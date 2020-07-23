#!/bin/bash
set -m

host=${CLAMD_HOST:-127.0.0.1}
port=${CLAMD_PORT:-3310}
filesize=${MAXSIZE:-20MB}
timeout=${TIMEOUT:-120000}

echo "using clamd server: $host:$port"
echo "using limits: "
echo "- clamd.maxfilesize: $filesize"
echo "- clamd.maxrequestsize: $filesize"
echo "- clamd.timeout: $timeout"
echo "- spring.servlet.multipart.max-request-size: $filesize"
echo "- spring.servlet.multipart.max-file-size: $filesize"

java -jar /var/clamav-rest/clamav-rest.jar --clamd.host=$host --clamd.port=$port \
--clamd.maxfilesize=$filesize --clamd.maxrequestsize=$filesize --clamd.timeout=$timeout \
--spring.servlet.multipart.max-file-size=$filesize --spring.servlet.multipart.max-request-size=$filesize