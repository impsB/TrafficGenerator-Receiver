package GUI;

import NetworkConnection.NetworkConfig;
import TrafficReceiver.TrafficReceiver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.Socket;

/**
 * <p>Класс ReceiverGUI представляет собой JavaFX приложение, которое подключается к серверу и отображает полученные данные.</p>
 */
public class ReceiverGUI extends Application {
    private static final Logger logger = LogManager.getLogger(ReceiverGUI.class);
    private TrafficReceiver receiver;
    private TextArea outputArea;

    /**
     * <p>Конструктор по умолчанию.</p>
     */
    public ReceiverGUI() {
        logger.info("ReceiverGUI initialized");
    }

    /**
     * <p>Главный входной пункт для приложения JavaFX.</p>
     * <p>Инициализирует интерфейс приложения и запускает поток подключения.</p>
     *
     * @param primaryStage основной этап для этого приложения
     */
    @Override
    public void start(Stage primaryStage) {
        outputArea = new TextArea();
        outputArea.setEditable(false);
        logger.info ("TextArea outputArea created; parameters: editable = false, pref Width = {}, prefHeight = {}",outputArea.getPrefWidth(), outputArea.getPrefHeight());

        new Thread(() -> {
            while (true) {
                try (Socket socket = new Socket(NetworkConfig.SERVER_ADDRESS, NetworkConfig.PORT)) {
                    appendText("The connection is established");
                    logger.info("The connection is established with the server at {}:{}", NetworkConfig.SERVER_ADDRESS, NetworkConfig.PORT);

                    receiver = new TrafficReceiver(socket, outputArea);
                    logger.info ("TrafficReceiver receiver created");
                    receiver.handle();
                    break;
                } catch (IOException ex) {
                    appendText("Error when starting the server: " + ex.getMessage());
                    logger.error("Error when starting the server: {}", ex.getMessage());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        appendText("The stream is interrupted: " + e.getMessage());
                        logger.warn("The stream is interrupted: {}", e.getMessage());
                        break;
                    }
                }
            }
        }).start();

        VBox layout = new VBox(10, outputArea);
        layout.setPadding(new Insets(5));
        VBox.setVgrow(outputArea, Priority.ALWAYS);

        Scene scene = new Scene(layout, 400, 400);
        logger.info("New Scene scene created");
        primaryStage.setScene(scene);
        logger.info("Scene scene installed");
        primaryStage.setTitle("Traffic Receiver");
        logger.info("primaryStage title installed");
        primaryStage.show();
        logger.info("Stage primaryStage showed");

        primaryStage.setOnCloseRequest(event -> {
            if (receiver != null) {
                receiver.close();
                logger.info("TrafficReceiver receiver is closed");
            }
            Platform.exit();
            logger.info("The application is shutting down.");
            System.exit(0);
        });
    }

    /**
     * <p>Добавляет текст в область вывода в потокобезопасном режиме.</p>
     *
     * @param text текст для добавления
     */
    private void appendText(String text) {
        Platform.runLater(() -> outputArea.appendText(text + "\n"));
        logger.info("Adding text to outputArea: {}", text);
    }

    /**
     * <p>Запускает приложение.</p>
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
        logger.info("The traffic receiver is running");
    }
}
