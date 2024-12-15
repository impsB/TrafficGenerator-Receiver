import TrafficGenerator.TrafficGenerator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки метода {@link TrafficGenerator#generatePackets(int)}.
 *
 * Тест проверяет, что сгенерированные пакеты
 * имеют ожидаемый размер.
 */
public class TrafficGeneratorTests {

    /**
     * Тестирует метод {@link TrafficGenerator#generatePackets(int)}.
     */
    @Test
    public void testGeneratePackets() {
        int packetSize = 1024;
        byte[] generatedPackets = TrafficGenerator.generatePackets(packetSize);

        assertNotNull(generatedPackets, "Сгенерированные пакеты не должны быть null.");
        assertEquals(packetSize, generatedPackets.length, "Размер сгенерированного пакета должен соответствовать запрашиваемому размеру.");
    }
}