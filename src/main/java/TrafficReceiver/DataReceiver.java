package TrafficReceiver;

import Statistics.ReceiverStatistics;

import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * <p>DataReceiver - класс для приема и обработки трафика.</p>
 */
public class DataReceiver {
    private static final Logger logger = LogManager.getLogger(DataReceiver.class);
    private static final int BUFFER_SIZE = 1024;
    private static final String SESSION_END_MARKER = "END";
    private TextArea outputArea;
    private final ReceiverStatistics statistics;

    /**
     * <p>Конструктор класса DataReceiver.</p>
     *
     * @param outputArea текстовая область для вывода информации
     */
    public DataReceiver(TextArea outputArea) {
        this.outputArea = outputArea;
        this.statistics = new ReceiverStatistics(outputArea);
        logger.info("DataReceiver initialized");
    }

    /**
     * <p>Обрабатывает входящие данные из потока.</p>
     *
     * @param inputStream входной поток для чтения данных
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public void processIncomingData(BufferedInputStream inputStream) throws IOException {
        int currentSessionCount = readSessionCount(inputStream);
        int expectedPacketCount = readPacketCount(inputStream);
        if (currentSessionCount == -1) {
            return;
        }

        outputArea.appendText("Receiving session # " + currentSessionCount + '\n');
        logger.info("Receiving session # {}", currentSessionCount);

        int totalPackets = 0;
        int totalBytesRead = 0;
        long startTime = System.nanoTime();

        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                break;
            }

            if (isSessionEnd(buffer, bytesRead)) {
                logger.info("Session end marker received");
                break;
            }

            totalPackets++;
            totalBytesRead += bytesRead;
        }

        if (totalPackets > 0) {
            statistics.displayStatistics(totalPackets, totalBytesRead, startTime);
            statistics.displayPacketLoss(expectedPacketCount, totalPackets);
            logger.info("Total packets received: {}", totalPackets);
        } else {
            outputArea.appendText("No packets received." + '\n');
            logger.warn("No packets received in session # {}", currentSessionCount);
        }
    }

    /**
     * <p>Читает номер текущей сессии из входного потока.</p>
     *
     * @param inputStream входной поток для чтения данных
     * @return номер сессии или -1 в случае ошибки
     * @throws IOException если возникает ошибка ввода-вывода
     */
    private int readSessionCount(BufferedInputStream inputStream) throws IOException {
        byte[] currentSessionCountBytes = new byte[4];
        int bytesRead = inputStream.read(currentSessionCountBytes);
        if (bytesRead == -1) {
            logger.error("Failed to read session count from input stream");
            return -1;
        }
        int sessionCount = ByteBuffer.wrap(currentSessionCountBytes).getInt();
        logger.info("Session count read: {}", sessionCount);
        return sessionCount;
    }

    /**
     * <p>Читает ожидаемое количество пакетов из входного потока.</p>
     *
     * @param inputStream входной поток для чтения данных
     * @return ожидаемое количество пакетов или -1 в случае ошибки
     * @throws IOException если возникает ошибка ввода-вывода
     */
    private int readPacketCount(BufferedInputStream inputStream) throws IOException {
        byte[] expectedPacketCountBytes = new byte[4];
        int bytesRead = inputStream.read(expectedPacketCountBytes);
        if (bytesRead == -1) {
            logger.error("Failed to read expected packet count from input stream");
            return -1;
        }
        int packetCount = ByteBuffer.wrap(expectedPacketCountBytes).getInt();
        logger.info("Expected packet count read: {}", packetCount);
        return packetCount;
    }

    /**
     * <p>Проверяет, является ли полученный буфер маркером конца сессии.</p>
     *
     * @param buffer массив байтов, содержащий данные
     * @param bytesRead количество прочитанных байтов
     * @return true, если получен маркер конца сессии, иначе false
     */
    private boolean isSessionEnd(byte[] buffer, int bytesRead) {
        byte[] sessionEndBytes = SESSION_END_MARKER.getBytes();
        if (bytesRead < sessionEndBytes.length) {
            return false;
        }
        boolean endMarkerReceived = Arrays.equals(Arrays.copyOfRange(buffer, bytesRead - sessionEndBytes.length, bytesRead), sessionEndBytes);
        if (endMarkerReceived) {
            logger.info("Detected session end marker in buffer");
        }
        return endMarkerReceived;
    }
}
