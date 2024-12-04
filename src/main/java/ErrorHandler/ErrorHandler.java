package ErrorHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.BindException;
import java.net.SocketException;

/**
 * <p>Класс для обработки ошибок, возникающих в приложении.</p>
 *
 * <p>Этот класс предоставляет методы для вывода сообщений об ошибках,
 * связанных с исключениями: {@link SocketException},
 * {@link BindException} и {@link IOException} и логирования.</p>
 */
public class ErrorHandler {
    private static Logger logger = LogManager.getLogger(ErrorHandler.class);

    /**
     * <p>Конструктор по умолчанию.</p>
     */
    public ErrorHandler() {
    }

    /**
     * <p>Метод для установки логгера (для тестирования).</p>
     */
    public static void setLogger(Logger logger) {
        ErrorHandler.logger = logger;
    }

    /**
     * <p>Обрабатывает общее исключение и выводит сообщение об ошибке.</p>
     *
     * @param message сообщение, описывающее ошибку
     * @param e исключение, которое произошло
     * Exception если произошло общее исключение
     */
    public static void handleError(String message, Exception e) {
        logger.error(message, e);
    }

    /**
     * <p>Обрабатывает ошибку сокета и выводит сообщение об ошибке.</p>
     *
     * @param message сообщение, описывающее ошибку
     * @param e исключение типа {@link SocketException}
     * SocketException если произошла ошибка сокета
     */
    public static void handleSocketError(String message, SocketException e) {
        logger.error("Socket Error: {}", message, e);
    }

    /**
     * <p>Обрабатывает ошибку привязки и выводит сообщение об ошибке.</p>
     *
     * @param message сообщение, описывающее ошибку
     * @param e исключение типа {@link BindException}
     * BindException если произошла ошибка привязки
     */
    public static void handleBindError(String message, BindException e) {
        logger.error("Bind Error: {}", message, e);
    }

    /**
     * <p>Обрабатывает ошибку ввода-вывода и выводит сообщение об ошибке.</p>
     *
     * @param message сообщение, описывающее ошибку
     * @param e исключение типа {@link IOException}
     * IOException если произошла ошибка ввода-вывода
     */
    public static void handleIOException(String message, IOException e) {
        logger.error("IO Error: {}", message, e);
    }

    /**
     * <p>Обрабатывает ошибку ввода и выводит предупреждающее сообщение.</p>
     *
     * @param message сообщение, описывающее ошибку
     * @throws IllegalArgumentException если сообщение является пустым или null
     */
    public static void handleInputError(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        logger.warn("Input Error: {}", message);
    }
}
