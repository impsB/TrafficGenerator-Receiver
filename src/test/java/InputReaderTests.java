import TrafficGenerator.InputReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javafx.scene.control.TextField;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>Класс InputReaderTests содержит тесты для проверки функциональности класса InputReader.</p>
 */
public class InputReaderTests extends JavaFXTest {
    private TextField mockPacketCountField;
    private TextField mockPacketSizeField;
    private TextField mockFrequencyField;
    private InputReader inputReader;

    /**
     * <p>Настраивает тестовую среду перед каждым тестом.</p>
     * <p>Создает моки текстовых полей и экземпляр InputReader.</p>
     */
    @BeforeEach
    public void setUp() {
        mockPacketCountField = Mockito.mock(TextField.class);
        mockPacketSizeField = Mockito.mock(TextField.class);
        mockFrequencyField = Mockito.mock(TextField.class);
        inputReader = new InputReader();
    }

    /**
     * <p>Тестирует метод scanPacketCount класса InputReader с валидным вводом.</p>
     * <p>Ожидает, что метод вернет корректное значение и установит его в InputReader.</p>
     */
    @Test
    public void testScanPacketCount_ValidInput() {
        Mockito.when(mockPacketCountField.getText()).thenReturn("10");
        int packetCount = inputReader.scanPacketCount(mockPacketCountField);
        assertEquals(10, packetCount);
        assertEquals(10, inputReader.getPacketCount());
    }

    /**
     * <p>Тестирует метод scanPacketCount класса InputReader с невалидным вводом.</p>
     * <p>Ожидает, что метод вернет -1 и установит его в InputReader.</p>
     */
    @Test
    public void testScanPacketCount_InvalidInput() {
        Mockito.when(mockPacketCountField.getText()).thenReturn("abc");
        int packetCount = inputReader.scanPacketCount(mockPacketCountField);
        assertEquals(-1, packetCount);
        assertEquals(-1, inputReader.getPacketCount());
    }

    /**
     * <p>Тестирует метод scanPacketSize класса InputReader с валидным вводом.</p>
     * <p>Ожидает, что метод вернет корректное значение и установит его в InputReader.</p>
     */
    @Test
    public void testScanPacketSize_ValidInput() {
        Mockito.when(mockPacketSizeField.getText()).thenReturn("2048");
        int packetSize = inputReader.scanPacketSize(mockPacketSizeField);
        assertEquals(2048, packetSize);
        assertEquals(2048, inputReader.getPacketSize());
    }

    /**
     * <p>Тестирует метод scanPacketSize класса InputReader с невалидным вводом.</p>
     * <p>Ожидает, что метод вернет -1 и установит его в InputReader.</p>
     */
    @Test
    public void testScanPacketSize_InvalidInput() {
        Mockito.when(mockPacketSizeField.getText()).thenReturn("xyz");
        int packetSize = inputReader.scanPacketSize(mockPacketSizeField);
        assertEquals(-1, packetSize);
        assertEquals(-1, inputReader.getPacketSize());
    }

    /**
     * <p>Тестирует метод scanFrequency класса InputReader с валидным вводом.</p>
     * <p>Ожидает, что метод вернет корректное значение и установит его в InputReader.</p>
     */
    @Test
    public void testScanFrequency_ValidInput() {
        Mockito.when(mockFrequencyField.getText()).thenReturn("5");
        int frequency = inputReader.scanFrequency(mockFrequencyField);
        assertEquals(5, frequency);
        assertEquals(5, inputReader.getFrequency());
    }

    /**
     * <p>Тестирует метод scanFrequency класса InputReader с невалидным вводом.</p>
     * <p>Ожидает, что метод вернет -1 и установит его в InputReader.</p>
     */
    @Test
    public void testScanFrequency_InvalidInput() {
        Mockito.when(mockFrequencyField.getText()).thenReturn("invalid");
        int frequency = inputReader.scanFrequency(mockFrequencyField);
        assertEquals(-1, frequency);
        assertEquals(-1, inputReader.getFrequency());
    }
}