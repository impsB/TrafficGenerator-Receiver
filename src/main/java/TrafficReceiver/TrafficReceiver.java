package TrafficReceiver;

import ErrorHandler.ErrorHandler;
import NetworkConnection.NetworkConnection;

import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * <p>TrafficReceiver - класс для приема сетевого трафика через сокет.</p>
 */
public class TrafficReceiver extends NetworkConnection {
    private static final Logger logger = LogManager.getLogger(TrafficReceiver.class);
    private TextArea outputArea;

    /**
     * <p>Конструктор класса TrafficReceiver.</p>
     *
     * @param socket сокет для связи
     * @param outputArea текстовая область для вывода информации
     */
    public TrafficReceiver(Socket socket, TextArea outputArea) {
        super(socket);
        this.outputArea = outputArea;
        logger.info("TrafficReceiver initialized with socket: {}", socket);
    }

    /**
     * <p>Обрабатывает входящие данные из сокета.</p>
     */
    @Override
    public void handle() {
        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream())) {
            outputArea.appendText("Waiting for data...\n");
            logger.info("Waiting for data");

            DataReceiver dataReceiver = new DataReceiver(getOutputArea());

            while (true) {
                if (inputStream.available() > 0) {
                    logger.info("Data is available; processing incoming data");
                    dataReceiver.processIncomingData(inputStream);
                } else {
                    if (socket.isClosed() || !socket.isConnected()) {
                        logger.warn("The connection is broken.");
                        throw new SocketException("The connection is broken");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        handleInterrupt(e);
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            ErrorHandler.handleSocketError("Error receiving data", e);
        } catch (IOException e) {
            ErrorHandler.handleIOException("Error receiving data", e);
        } catch (Exception e) {
            ErrorHandler.handleError("Unexpected error", e);
        }
    }

    /**
     * <p>Метод - геттер.</p>
     * <p>Возвращает текстовую область для вывода информации.</p>
     *
     * @return текстовая область
     */
    public TextArea getOutputArea() {
        return outputArea;
    }

    /**
     * <p>Обрабатывает прерывание потока.</p>
     *
     * @param e исключение InterruptedException
     */
    private void handleInterrupt(InterruptedException e) {
        outputArea.appendText("The stream was interrupted: " + e.getMessage() + '\n');
        logger.warn("The stream was interrupted: {}", e.getMessage());
        Thread.currentThread().interrupt();
    }

    /**
     * Главный метод приложения.
     * <p>
     * Он оставлен для соответствия стандартной структуре Java-приложения.
     * </p>
     *
     * @param args аргументы командной строки, переданные при запуске программы
     */
    public static void main(String[] args) {
    }
}
