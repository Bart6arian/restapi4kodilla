#!/usr/bin/env bash

if ./runcrud.sh; then
    sleep 3
    open http://localhost:8080/crud/v1/task/getTasks
else
   echo "Failed running ./runcrud.sh command"
fi