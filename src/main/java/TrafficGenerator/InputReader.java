package TrafficGenerator;

import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>InputReader - класс для чтения и обработки пользовательского ввода из текстовых полей.</p>
 */
public class InputReader {
    private static final Logger logger = LogManager.getLogger(InputReader.class);
    private int packetCount;
    private int packetSize;
    private int frequency;

    /**
     * <p>Конструктор без параметров.</p>
     */
    public InputReader() {
    }

    /**
     * <p>Возвращает количество пакетов.</p>
     *
     * @return количество пакетов
     */
    public int getPacketCount() {
        return packetCount;
    }

    /**
     * <p>Возвращает размер пакета.</p>
     *
     * @return размер пакета
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * <p>Возвращает частоту.</p>
     *
     * @return частота
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * <p>Считывает количество пакетов из текстового поля.</p>
     *
     * @param packetCountField текстовое поле для ввода количества пакетов
     * @return количество пакетов
     */
    public int scanPacketCount(TextField packetCountField) {
        this.packetCount = parseInput(packetCountField);
        return this.packetCount;
    }

    /**
     * <p>Считывает размер пакета из текстового поля.</p>
     *
     * @param packetSizeField текстовое поле для ввода размера пакета
     * @return размер пакета
     */
    public int scanPacketSize(TextField packetSizeField) {
        this.packetSize = parseInput(packetSizeField);
        return this.packetSize;
    }

    /**
     * <p>Считывает частоту отправления пакетов из текстового поля.</p>
     *
     * @param frequencyField текстовое поле для ввода частоты
     * @return частота
     */
    public int scanFrequency(TextField frequencyField) {
        this.frequency = parseInput(frequencyField);
        return this.frequency;
    }

    /**
     * <p>Проверяет ввод из текстового поля и возвращает целочисленное значение.</p>
     *
     * @param field текстовое поле для парсинга
     * @return целочисленное значение или -1 в случае ошибки
     */
    private int parseInput(TextField field) {
        try {
            int value = Integer.parseInt(field.getText());
            logger.info("Parsed input from field {}: {}", field, field.getText());
            return value;
        } catch (NumberFormatException e) {
            logger.error("Invalid input in field {}: {}", field, field.getText());
            return -1;
        }
    }
}
