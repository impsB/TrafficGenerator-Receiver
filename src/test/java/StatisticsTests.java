import Statistics.GeneratorStatistics;
import Statistics.ReceiverStatistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.control.TextArea;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

/**
 * Тесты для классов GeneratorStatistics и ReceiverStatistics.
 * <p>
 * Данный класс содержит тесты, которые проверяют функциональность методов,
 * отвечающих за отображение статистики генератора и приемника трафика.
 * </p>
 */
public class StatisticsTests extends  JavaFXTest {
    private TextArea mockOutputArea;
    private GeneratorStatistics generatorStatistics;
    private ReceiverStatistics receiverStatistics;

    /**
     * Инициализирует моки для TextArea и создает экземпляры GeneratorStatistics и ReceiverStatistics.
     * <p>
     * Этот метод выполняется перед каждым тестом и настраивает необходимые зависимости.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        mockOutputArea = Mockito.mock(TextArea.class);
        generatorStatistics = new GeneratorStatistics(mockOutputArea);
        receiverStatistics = new ReceiverStatistics(mockOutputArea);
    }

    /**
     * Тест для метода displayStatistics в классе GeneratorStatistics.
     * <p>
     * Проверяет, что метод корректно выводит информацию об отправленных пакетах и скорости передачи
     * при времени передачи пакетов большем, чем 0.
     * </p>
     */
    @Test
    public void testDisplayStatistics_Generator_NoZeroTime() {
        long startTime = System.nanoTime() - 100_000_000;

        generatorStatistics.displayStatistics(10, 2048, startTime);

        verify(mockOutputArea).appendText(Mockito.contains("Sent 10 packets in"));
        verify(mockOutputArea).appendText(Mockito.contains("Transfer speed:"));
    }

    /**
     * Тест для метода displayStatistics в классе GeneratorStatistics.
     * <p>
     * Проверяет, что метод выводит сообщение о слишком малом времени передачи пакетов при времени передачи, равном 0.
     * </p>
     */
    @Test
    public void testDisplayStatistics_Generator_ZeroTime() {
        long startTime = System.nanoTime() + 100_000_000;
        generatorStatistics.displayStatistics(10, 2048, startTime);

        verify(mockOutputArea).appendText("Transfer time is too short to measure.\n");
    }

    /**
     * Тест для метода displayStatistics в классе ReceiverStatistics.
     * <p>
     * Проверяет, что метод корректно выводит информацию о полученных пакетах, когда пакеты были получены
     * и время передачи пакетов больше 0.
     * </p>
     */
    @Test
    public void testDisplayStatistics_Receiver_WithPackets_NoZeroTime() {
        long startTime = System.nanoTime() - 100_000_000;

        receiverStatistics.displayStatistics(5, 1024, startTime);

        verify(mockOutputArea).appendText(Mockito.contains("Received 5 packets in"));
        verify(mockOutputArea).appendText(Mockito.contains("Average delay per packet:"));
        verify(mockOutputArea).appendText(Mockito.contains("Receiving speed:"));
    }

    /**
     * Тест для метода displayStatistics в классе ReceiverStatistics.
     * <p>
     * Проверяет, что метод корректно выводит информацию о полученных пакетах, когда пакеты были получены
     * и время передачи пакетов равно 0.
     * </p>
     */
    @Test
    public void testDisplayStatistics_Receiver_WithPackets_ZeroTime() {
        long startTime = System.nanoTime() + 100_000_000;

        receiverStatistics.displayStatistics(5, 1024, startTime);

        verify(mockOutputArea).appendText(Mockito.contains("Received 5 packets in"));
        verify(mockOutputArea).appendText(Mockito.contains("Average delay per packet:"));
        verify(mockOutputArea).appendText(Mockito.contains("Transfer time is too short to measure speed.\n"));
    }

    /**
     * Тест для метода displayStatistics в классе ReceiverStatistics.
     * <p>
     * Проверяет, что метод корректно выводит сообщение, когда не было получено ни одного пакета.
     * </p>
     */
    @Test
    public void testDisplayStatistics_Receiver_NoPackets() {
        long startTime = System.nanoTime();
        receiverStatistics.displayStatistics(0, 0, startTime);

        verify(mockOutputArea).appendText("No packets received.\n");
    }

    /**
     * Тест для метода displayPacketLoss в классе ReceiverStatistics.
     * <p>
     * Проверяет, что метод корректно выводит информацию о потерях пакетов.
     * </p>
     */
    @Test
    public void testDisplayPacketLoss() {
        receiverStatistics.displayPacketLoss(3, 5);

        verify(mockOutputArea).appendText(Mockito.contains("Packet loss:"));
    }

    /**
     * Тест для метода displayPacketLoss в классе ReceiverStatistics.
     * <p>
     * Проверяет, что метод не пытается выводить информацию, когда ожидаемое количество пакетов равно нулю.
     * </p>
     */
    @Test
    public void testDisplayPacketLoss_ZeroExpectedPackets() {
        receiverStatistics.displayPacketLoss(3, 0);

        Mockito.verify(mockOutputArea, Mockito.never()).appendText(Mockito.anyString());
    }
}
