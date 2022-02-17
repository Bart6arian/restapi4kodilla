#!/usr/bin/env bash

export CATALINA_HOME=/Users/bart/Desktop/apache-tomcat-8.5.73

stop_tomcat()
{
   $CATALINA_HOME/bin/shutdown.sh
}

start_tomcat()
{
   $CATALINA_HOME/bin/catalina.sh start
   end
}

copy_file() {
   if cp build/libs/crud.war $CATALINA_HOME/webapps; then
      start_tomcat
   else
      fail
   fi
}

fail() {
   echo "There were errors"
}

end() {
   echo "Work is finished"
}

if ./gradlew build; then
   copy_file
else
   stop_tomcat
   fail
fi