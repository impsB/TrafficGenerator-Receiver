Программный комплекс из 2 приложений.

Для сборки jar файлов необходимо ввести в консоль команду ./gradlew clean buildAll.
После этого в директории /build/libs будут созданы 2 .jar файла:
1. fatTrafficGenerator-1.0-SNAPSHOT-all.jar
2. fatTrafficReceiver-1.0-SNAPSHOT-all.jar

Для запуска 2 приложений в среде разработки IntellIJ IDEA в меню Run/Debug Configurations
необходимо добавить VM Options(Modify Options -> Add VM options) с значением
--module-path "*" --add-modules javafx.controls,javafx.fxml, где * - путь к .jar файлам JavaFX
(можно скачать в https://gluonhq.com/products/javafx/). Необходим JavaFx 23.0.1.

Для запуска 2 приложений из консоли необходимо открыть 2 консоли, в которых прописать команды
java --module-path "*" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficGenerator-1.0-SNAPSHOT-all.jar
java --module-path "*" --add-modules javafx.controls,javafx.fxml -jar build/libs/fatTrafficReceiver-1.0-SNAPSHOT-all.jar
, где * - путь к .jar файлам JavaFX
(можно скачать в https://gluonhq.com/products/javafx/). Необходим JavaFx 23.0.1.

Tests:
./gradlew Test

JavaDoc:
./gradlew JavaDoc

Программа создавалась в среде разработки IntellIJ IDEA на Windows 10