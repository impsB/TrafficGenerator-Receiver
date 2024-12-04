console launch:
./gradlew runReceiver
./gradlew runGenerator

console .jar launch:
cd 'D:\got a java heart attack\TrafficGenerator-Receiver-GUIVersion-TrueStructure-Gradle-FatJar-log4j'
java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficGenerator-1.0-SNAPSHOT.jar
java -Dlog4j.configurationFile=src/log4j/log4j2.xml --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficReceiver-1.0-SNAPSHOT.jar
java -Dlog4j.configurationFile=src/log4j/log4j2.xml -jar myapp.jar

compilation:
./gradlew clean build

jar creating:
./gradlew generatorJar
./gradlew receiverJar

fat jar creating:
./gradlew clean buildAll


Run/Debug configurations (VM options):
--module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml

bat:
@echo off
set JAVA_FX_PATH="D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib"
set MODULES=javafx.controls,javafx.fxml

start java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/TrafficGenerator-1.0-SNAPSHOT.jar
start  java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/TrafficReceiver-1.0-SNAPSHOT.jar

avac -cp "lib/*" src/ваш_код.java -d bin
java -cp "lib/*;bin" ваш_код

JavaDoc:
javadoc -d doc -sourcepath src/main/java/ErrorHandler/*.java src/main/java/GUI/*.java src/main/java/NetworkConnection/*.java src/main/java/Statistics/*.java src/main/java/TrafficGenerator/*.java src/main/java/TrafficReceiver/*.java

javadoc -d doc -sourcepath src/main/java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml src/main/java/ErrorHandler/*.java src/main/java/GUI/*.java src/main/java/NetworkConnection/*.java src/main/java/Statistics/*.java src/main/java/TrafficGenerator/*.java src/main/java/TrafficReceiver/*.java

AXAXAXAXAXAXAXX