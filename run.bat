@echo off
set JAVA_FX_PATH="D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib"
set MODULES=javafx.controls,javafx.fxml

start java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficGenerator-1.0-SNAPSHOT.jar
start  java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficReceiver-1.0-SNAPSHOT.jar