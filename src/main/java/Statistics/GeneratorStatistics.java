package Statistics;

import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>GeneratorStatistics - класс для генерации и отображения статистики передачи данных.</p>
 */
public class GeneratorStatistics {
    private static final Logger logger = LogManager.getLogger(GeneratorStatistics.class);
    private TextArea outputArea;

    /**
     * <p>Конструктор без параметров. Необходим т.к. создается конструктор
     * класса-наследника в @see ReceiverStatistics.</p>
     */
    public GeneratorStatistics() {
    }

    /**
     * <p>Конструктор с параметром для инициализации области вывода.</p>
     *
     * @param outputArea область текста для отображения статистики
     */
    public GeneratorStatistics(TextArea outputArea) {
        this.outputArea = outputArea;
    }

    /**
     * <p>Метод для отображения статистики передачи данных.</p>
     *
     * @param packetCount общее количество отправленных пакетов
     * @param totalBytesRead общее количество байт, созданных для передачи
     * @param startTime время начала передачи в наносекундах
     */
    public void displayStatistics(int packetCount, int totalBytesRead, long startTime) {
        long endTime = System.nanoTime();
        long transferTime = endTime - startTime;

        if (transferTime <= 0) {
            outputArea.appendText("Transfer time is too short to measure.\n");
            logger.warn("Transfer time is too short to measure");
            return;
        }

        double timeInSeconds = transferTime / 1_000_000_000.0;

        outputArea.appendText(String.format("Sent %d packets in %.2f seconds.\n", packetCount, timeInSeconds));
        outputArea.appendText(String.format("Transfer speed: %.2f KB/s.\n", (totalBytesRead) / (1024.0 * timeInSeconds)));

        logger.info("Sent {} packets in {}  seconds", packetCount, timeInSeconds);
        logger.info("Transfer speed: {} KB/s", (totalBytesRead) / (1024.0 * timeInSeconds));
    }
}
