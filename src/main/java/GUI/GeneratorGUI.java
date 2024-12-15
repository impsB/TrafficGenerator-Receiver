package GUI;

import ErrorHandler.ErrorHandler;
import TrafficGenerator.InputReader;
import TrafficGenerator.TrafficGenerator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

/**
 * <p>Класс GeneratorGUI представляет собой JavaFX приложение для генерации сетевого трафика.</p>
 */
public class GeneratorGUI extends Application {
    private static final Logger logger = LogManager.getLogger(GeneratorGUI.class);

    private TrafficGenerator generator;
    private TextArea outputArea;
    private Button generateButton;
    private int sessionCount = 0;
    private TextField packetCountField;
    private TextField packetSizeField;
    private TextField frequencyField;
    private InputReader inputReader;
    private boolean isTrafficGenerating = false;

    /**
     * <p>Конструктор по умолчанию.</p>
     */
    public GeneratorGUI() {
        logger.info("GeneratorGUI initialized");
    }

    /**
     * <p>Инициализирует интерфейс приложения и запускает сервер для прослушивания входящих соединений.</p>
     *
     * @param primaryStage Основная сцена приложения.
     */
    @Override
    public void start(Stage primaryStage) {
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefWidth(400);
        logger.info ("TextArea outputArea created; parameters: editable = false, pref Width = {}, prefHeight = {}",outputArea.getPrefWidth(), outputArea.getPrefHeight());
        packetCountField = createTextField("PACKET COUNT");
        packetSizeField = createTextField("PACKET SIZE [BYTES]");
        frequencyField = createTextField("FREQUENCY [MS]");

        inputReader = new InputReader();
        logger.info ("InputReader inputReader created");

        try {
            generator = new TrafficGenerator(outputArea, inputReader);
            logger.info ("TrafficGenerator generator created");
            appendText("Server started. Waiting for client connection...");
            logger.info("Server started");

            new Thread(() -> {
                logger.info("Listening for connections");
                generator.listenForConnections(outputArea);
            }).start();
        } catch (IOException ex) {
            ErrorHandler.handleError("Error starting server", ex);
            appendText("Error starting server: " + ex.getMessage());
        }

        generateButton = new Button("GENERATE TRAFFIC");
        generateButton.setDisable(true);
        generateButton.setPrefWidth(200);
        generateButton.setStyle("-fx-background-radius: 0;");
        logger.info ("Button generateButton created; parameters: disable = true, pref Width = {}, prefHeight = {}", generateButton.getPrefWidth(), generateButton.getPrefHeight());

        packetCountField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        packetSizeField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        frequencyField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());

        generateButton.setOnAction(event -> {
            if (!generator.isClientConnected()) {
                showErrorDialog("Client is not connected. Please connect the client first.");
                isTrafficGenerating = false;
                generateButton.setDisable(false);
                logger.warn("Activating the button: {}; Client is not connected", generateButton);
                return;
            }

            if (isTrafficGenerating) {
                showErrorDialog("Traffic generation is already in progress. Please wait for it to finish.");
                logger.warn("Activating the button: {}; Traffic generation is already in progress", generateButton);
                return;
            }

            logger.info("Activating the button: {}", generateButton);
            isTrafficGenerating = true;
            generateButton.setDisable(true);

            int packetCount = inputReader.scanPacketCount(packetCountField);
            int packetSize = inputReader.scanPacketSize(packetSizeField);
            int frequency = inputReader.scanFrequency(frequencyField);

            if (packetCount < 0 || packetSize < 0 || frequency < 0) {
                showErrorDialog("Enter the correct data: positive int");
                isTrafficGenerating = false;
                generateButton.setDisable(false);
                logger.warn("Invalid input data: packetCount = {}, packetSize = {}, frequency = {}", packetCount, packetSize, frequency);
                return;
            }

            sessionCount++;
            logger.info("Increasing the sessionCount: sessionCount = {}", sessionCount);

            new Thread(() -> {
                if (generator != null) {
                    generator.handle(sessionCount);
                } else {
                    appendText("Generator is not initialized");
                    logger.error("Generator is not initialized");
                }
                isTrafficGenerating = false;
                Platform.runLater(() -> generateButton.setDisable(false));
            }).start();
        });

        VBox inputLayout = new VBox(10, packetCountField, packetSizeField, frequencyField, generateButton);
        inputLayout.setPadding(new Insets(0));
        logger.info("New VBox inputLayout created");

        HBox layout = new HBox(10, inputLayout, outputArea);
        layout.setPadding(new Insets(5));
        logger.info("New HBox layout created");

        Scene scene = new Scene(layout, 600, 400);
        logger.info("New Scene scene created");
        primaryStage.setScene(scene);
        logger.info("Scene scene installed");
        primaryStage.setTitle("Traffic Generator");
        logger.info("primaryStage title installed");
        primaryStage.show();
        logger.info("Stage primaryStage showed");

        primaryStage.setOnCloseRequest(event -> {
            if (generator != null) {
                generator.close();
                logger.info ("TrafficGenerator generator is closed");
            }
            Platform.exit();
            logger.info("The application is shutting down.");
            System.exit(0);
        });
    }

    /**
     * <p>Создает и настраивает поле ввода с заданным текстом на заднем поле.</p>
     *
     * @param promptText Текст на заднем фоне для поля ввода.
     * @return Созданное поле ввода.
     */
    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-radius: 0;");
        logger.info("TextField {} created; parameters: prompt text = {}", textField, promptText);
        return textField;
    }

    /**
     * <p>Добавляет текст в область вывода.</p>
     *
     * @param text Текст для добавления.
     */
    private void appendText(String text) {
        Platform.runLater(() -> outputArea.appendText(text + "\n"));
        logger.info("Adding text to outputArea: {}", text);
    }

    /**
     * <p>Проверяет, заполнены ли все поля ввода. Активирует или деактивирует кнопку генерации трафика.</p>
     */
    private void checkFields() {
        boolean isAllFieldsFilled = !packetCountField.getText().isEmpty() &&
                !packetSizeField.getText().isEmpty() &&
                !frequencyField.getText().isEmpty();
        generateButton.setDisable(!isAllFieldsFilled);
        logger.info("Checking fields: All fields are filled in: {}", isAllFieldsFilled);
    }

    /**
     * <p>Отображает диалоговое окно с сообщением об ошибке.</p>
     *
     * @param message Сообщение об ошибке.
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        logger.error("Input error: {}", message);
    }

    /**
     * <p>Запускает приложение.</p>
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
        logger.info("The traffic generator is running");
    }
}
