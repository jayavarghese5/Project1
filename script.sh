#!/bin/bash

git clone https://github.com/jayavarghese5/Project1.git
cd Project1
mvn clean install
java -jar target/upload-csv-mysql.jar