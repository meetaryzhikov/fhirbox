#!/bin/bash

case $1 in
start)
    clojure -M -m fhirbox.core &
    echo "Server started on http://127.0.0.1:3001"
    ;;
stop)
    ps aux | grep clojure | grep -v grep | awk '{print $2}' | xargs kill
    echo "Server stopped"
    ;;
*)
    echo "Usage: $0 {start|stop}"
    ;;
esac