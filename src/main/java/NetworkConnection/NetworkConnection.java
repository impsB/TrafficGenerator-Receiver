package NetworkConnection;

import ErrorHandler.ErrorHandler;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * <p>NetworkConnection - класс для управления сетевыми соединениями.
 * Создает серверный сокет и обрабатывает входящие соединения от клиентов.</p>
 */
public class NetworkConnection {
    private static final Logger logger = LogManager.getLogger(NetworkConnection.class);
    private volatile boolean listening = true;

    /**
     * <p>Серверный сокет для ожидания входящих соединений.</p>
     */

    protected ServerSocket serverSocket;

    /**
     * <p>Сокет для установления соединения с клиентом.</p>
     */
    protected Socket socket;

    /**
     * <p>Конструктор, создающий серверный сокет на заданном порту.</p>
     *
     * @param port порт, на котором будет создан серверный сокет
     * @throws IOException если возникла ошибка при создании сокета
     */
    public NetworkConnection(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("The server socket is created on the port {}", port);
        } catch (BindException e) {
            ErrorHandler.handleBindError("Port " + port + " is already in use", e);
            logger.error("Port {} is already in use", port);
            throw e;
        } catch (IOException e) {
            ErrorHandler.handleIOException("An error occurred when creating the server socket", e);
            logger.error("An error occurred when creating the server socket.: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * <p>Конструктор, принимающий существующий сокет.</p>
     *
     * @param socket сокет для взаимодействия с клиентом
     */
    public NetworkConnection(Socket socket) {
        this.socket = socket;
    }

    /**
     * <p>Метод, который прослушивает входящие соединения и обновляет интерфейс.</p>
     *
     * @param outputArea область текста для отображения информации о клиентах
     */
    public void listenForConnections(TextArea outputArea) {
        while (listening) {
            try {
                socket = serverSocket.accept();
                String clientIP = socket.getInetAddress().getHostAddress();
                logger.info("The client is connected: {}", clientIP);
                Platform.runLater(() -> {
                    outputArea.appendText("The client is connected: " + clientIP + "\n");
                });
            } catch (SocketException e) {
                ErrorHandler.handleSocketError("A socket error has occurred", e);
            } catch (IOException e) {
                ErrorHandler.handleError("An error occurred while accepting the connection", e);
            }
        }
    }

    /**
     * <p>Метод для обработки соединений. Переопределяется в @see TrafficGenerator и @see TrafficReceiver.</p>
     *
     * @throws IOException если возникла ошибка при обработке соединения
     */
    public void handle() throws IOException {
    }

    /**
     * <p>Метод для закрытия серверного сокета и прекращения прослушивания (для тестирования).</p>
     */
    public void stopListening() {
        listening = false;
        try {
            serverSocket.close(); // Закрываем серверный сокет
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Закрывает сокеты, освобождая ресурсы.</p>
     */
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();

                logger.info("The socket is closed");
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logger.info("The server socket is closed");
            }
        } catch (IOException e) {
            ErrorHandler.handleError("Error closing the connection", e);
        }
    }
}
