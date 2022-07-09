if [ ! $1 ] || [ ! $2 ];then
    echo "usage:  make-release.sh <RELEASE_VERSION> <NEXT_SNAPSHOT_VERSION>"
    exit 0
fi

echo "READY TO RELEASE eazybuilder-CI VERSION:$1 , NEXT DEV VERSION:$2"
#quit if any error
set -e
#dry run to check if any error
mvn -B -DreleaseVersion=$1 -DdevelopmentVersion=$2 release:prepare -Dresume=false

# mvn -B -DreleaseVersion=1.0.7 -Dresume=false release:prepare
#rollback dryrun
#mvn release:rollback
#tag scm and change versions
#mvn -B -DreleaseVersion=$1 release:prepare
#deploy to repository 
#mvn -B release:perform -Darguments="-Dsnapshot.repo=http://0.0.0.0:8181/repository/cpf-snapshot  -Drelease.repo=http://0.0.0.0:8181/repository/cpf-release -Dmaven.javadoc.failOnError=false"