package Statistics;

import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>ReceiverStatistics - класс для отображения статистики приема данных,
 * наследующий функциональность @see GeneratorStatistics.</p>
 */
public class ReceiverStatistics extends GeneratorStatistics {
    private static final Logger logger = LogManager.getLogger(ReceiverStatistics.class);
    private TextArea outputArea;

    /**
     * <p>Конструктор для инициализации @see ReceiverStatistics с областью текста.</p>
     *
     * @param outputArea область текста для отображения статистики
     */
    public ReceiverStatistics(TextArea outputArea) {
        this.outputArea = outputArea;
    }

    /**
     * <p>Метод для отображения статистики приема данных.</p>
     *
     * @param packetCount общее количество полученных пакетов
     * @param totalBytesReceived общее количество байт, полученных за передачу
     * @param startTime время начала передачи в наносекундах
     */
    @Override
    public void displayStatistics(int packetCount, int totalBytesReceived, long startTime) {
        long endTime = System.nanoTime();
        long transferTime = endTime - startTime;

        if (packetCount == 0) {
            outputArea.appendText("No packets received.\n");
            logger.warn("No packets received");
            return;
        }

        double timeInMilliseconds = transferTime / 1_000_000.0;
        double timeInSeconds = transferTime / 1_000_000_000.0;

        outputArea.appendText(String.format("Received %d packets in %.2f ms.\n", packetCount, timeInMilliseconds));
        outputArea.appendText(String.format("Average delay per packet: %.2f ms.\n", timeInMilliseconds / packetCount));
        logger.info("Received {} packets in {} ms", packetCount, timeInMilliseconds);
        logger.info("Average delay per packet: {} ms", timeInMilliseconds / packetCount);

        if (timeInSeconds > 0) {
            outputArea.appendText(String.format("Receiving speed: %.2f KB/s\n", (totalBytesReceived) / (1024.0 * timeInSeconds)));
            logger.info("Receiving speed: {} KB/s", (totalBytesReceived) / (1024.0 * timeInSeconds));
        } else {
            outputArea.appendText("Transfer time is too short to measure speed.\n");
            logger.warn("Transfer time is too short to measure speed");
        }
    }

    /**
     * <p>Метод для отображения потерь пакетов.</p>
     *
     * @param expectedPacketCount общее количество полученных пакетов
     * @param packetCount ожидаемое количество пакетов
     */
    public void displayPacketLoss(int packetCount, int expectedPacketCount) {
        if (expectedPacketCount == 0) {
            logger.warn("Expected packet count is zero, cannot calculate packet loss");
            return;
        }
        int lostPackets = packetCount - expectedPacketCount;
        double packetLoss = (lostPackets / (double) packetCount) * 100;

        outputArea.appendText(String.format("Packet loss: %.2f%%.\n", packetLoss));
        logger.info("Packet loss: {}%", packetLoss);
    }
}
