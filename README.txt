console .jar launch:
java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficGenerator-1.0-SNAPSHOT.jar
java --module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficReceiver-1.0-SNAPSHOT.jar
("D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" заменить на путь к папке с .jar файлами javafx)

compilation:
./gradlew clean build

fat jar creating:
./gradlew clean buildAll

VM options:
--module-path "D:\Coding\openjfx 23.0.1\javafx sdk 23.0.1\lib" --add-modules javafx.controls,javafx.fxml

JavaDoc creating:
./gradlew JavaDoc