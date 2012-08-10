#!/bin/bash

mvn install:install-file -DgroupId=com.stanford -DartifactId=corenlp-edu.stanford.models -Dpackaging=jar -Dversion=4.2 -Dfile=stanford-corenlp-2012-07-06-edu.stanford.models.jar

mvn install:install-file -DgroupId=com.stanford -DartifactId=corenlp -Dpackaging=jar -Dversion=4.2 -Dfile=stanford-corenlp-2012-07-09.jar 