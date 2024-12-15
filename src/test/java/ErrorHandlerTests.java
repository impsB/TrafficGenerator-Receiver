import ErrorHandler.ErrorHandler;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.BindException;
import java.net.SocketException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * <p>Класс ErrorHandlerTests содержит тесты для проверки функциональности класса ErrorHandler.</p>
 */
class ErrorHandlerTests {
    private Logger logger;

    /**
     * <p>Настраивает тестовую среду перед каждым тестом.</p>
     * <p>Создает мок логгера и устанавливает его в ErrorHandler.</p>
     */
    @BeforeEach
    void setUp() {
        logger = Mockito.mock(Logger.class);
        ErrorHandler.setLogger(logger);
    }

    /**
     * <p>Тестирует метод handleError класса ErrorHandler.</p>
     * <p>Проверяет, что сообщение об ошибке и исключение корректно логируются.</p>
     */
    @Test
    void testHandleError() {
        String message = "ERROR";
        Exception exception = new Exception("Test exception");

        ErrorHandler.handleError(message, exception);

        verify(logger).error(eq(message), eq(exception));
    }

    /**
     * <p>Тестирует метод handleSocketError класса ErrorHandler.</p>
     * <p>Проверяет, что сообщение об ошибке сокета корректно логируется.</p>
     */
    @Test
    void testHandleSocketError() {
        String message = "SOCKET ERROR";
        SocketException socketException = new SocketException("Socket test exception");

        ErrorHandler.handleSocketError(message, socketException);

        verify(logger).error(startsWith("Socket Error: "), eq(message), eq(socketException));
    }

    /**
     * <p>Тестирует метод handleBindError класса ErrorHandler.</p>
     * <p>Проверяет, что сообщение об ошибке привязки корректно логируется.</p>
     */
    @Test
    void testHandleBindError() {
        String message = "BIND ERROR";
        BindException bindException = new BindException("Bind test exception");

        ErrorHandler.handleBindError(message, bindException);

        verify(logger).error(startsWith("Bind Error: "), eq(message), eq(bindException));
    }

    /**
     * <p>Тестирует метод handleIOException класса ErrorHandler.</p>
     * <p>Проверяет, что сообщение об ошибке ввода-вывода корректно логируется.</p>
     */
    @Test
    void testHandleIOException() {
        String message = "IO ERROR";
        IOException ioException = new IOException("IO test exception");

        ErrorHandler.handleIOException(message, ioException);

        verify(logger).error(startsWith("IO Error: "), eq(message), eq(ioException));
    }

    /**
     * <p>Тестирует метод handleInputError класса ErrorHandler с null сообщением.</p>
     * <p>Ожидает выброс IllegalArgumentException.</p>
     */
    @Test
    void testHandleInputError_NullMessage() {
        assertThrows(IllegalArgumentException.class, () -> ErrorHandler.handleInputError(null));
    }

    /**
     * <p>Тестирует метод handleInputError класса ErrorHandler с пустым сообщением.</p>
     * <p>Ожидает выброс IllegalArgumentException.</p>
     */
    @Test
    void testHandleInputError_EmptyMessage() {
        assertThrows(IllegalArgumentException.class, () -> ErrorHandler.handleInputError(""));
    }

    /**
     * <p>Тестирует метод handleInputError класса ErrorHandler с корректным сообщением.</p>
     * <p>Ожидает, что метод не выбросит исключение и логирует предупреждение.</p>
     */
    @Test
    void testHandleInputError_ValidMessage() {
        String message = "Valid input error message";

        assertDoesNotThrow(() -> ErrorHandler.handleInputError(message));
        verify(logger).warn("Input Error: {}", message);
    }
}