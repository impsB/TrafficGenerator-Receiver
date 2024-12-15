package TrafficGenerator;

import ErrorHandler.ErrorHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>PacketSender - класс для отправки пакетов данных через заданный поток вывода OutputStream.</p>
 */
public class PacketSender {
    private static final Logger logger = LogManager.getLogger(PacketSender.class);
    private final OutputStream outputStream;

    /**
     * <p>Конструктор класса PacketSender.</p>
     * <p>Инициализирует PacketSender с указанным потоком вывода OutputStream.</p>
     *
     * @param outputStream выходной поток для отправки данных
     * @throws IllegalArgumentException если outputStream равен null
     */
    public PacketSender(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("OutputStream cannot be null");
        }
        this.outputStream = outputStream;
        logger.info("PacketSender initialized with OutputStream: {}", outputStream);
    }

    /**
     * <p>Отправляет пакет данных.</p>
     *
     * @param data массив байтов для отправки
     */
    public void sendPacket(byte[] data) {
        if (data == null) {
            ErrorHandler.handleInputError("Cannot send null data packet");
            return;
        }

        try {
            outputStream.write(data);
            outputStream.flush();
            logger.info("Packet sent successfully: {} bytes", data.length);
        } catch (IOException e) {
            ErrorHandler.handleIOException("Error sending packet", e);
        }
    }
}
