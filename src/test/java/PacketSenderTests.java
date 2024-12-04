import TrafficGenerator.PacketSender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.io.OutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Класс PacketSenderTests содержит тесты для проверки функциональности класса PacketSender.</p>
 */
public class PacketSenderTests {
    private OutputStream mockOutputStream;
    private PacketSender packetSender;

    /**
     * <p>Настраивает тестовую среду перед каждым тестом.</p>
     * <p>Создает мок OutputStream и экземпляр PacketSender с этим моком.</p>
     */
    @BeforeEach
    public void setUp() {
        mockOutputStream = Mockito.mock(OutputStream.class);
        packetSender = new PacketSender(mockOutputStream);
    }

    /**
     * <p>Тестирует метод sendPacket класса PacketSender с корректными данными.</p>
     * <p>Проверяет, что данные корректно отправляются через OutputStream.</p>
     *
     * @throws IOException если возникает ошибка при отправке данных
     */
    @Test
    public void testSendPacket_CorrectData() throws IOException {
        byte[] data = {1, 2, 3};
        packetSender.sendPacket(data);

        verify(mockOutputStream).write(data);
        verify(mockOutputStream).flush();
    }

    /**
     * <p>Тестирует метод sendPacket класса PacketSender с null пакетом.</p>
     * <p>Ожидает, что метод не выбросит исключение, но ничего не будет отправлено.</p>
     */
    @Test
    public void testSendPacket_NullPacket() {
        packetSender.sendPacket(null);
    }

    /**
     * <p>Тестирует метод sendPacket класса PacketSender с пустым пакетом.</p>
     * <p>Проверяет, что пустой массив данных корректно отправляется через OutputStream.</p>
     *
     * @throws IOException если возникает ошибка при отправке данных
     */
    @Test
    public void testSendPacket_EmptyPacket() throws IOException {
        byte[] data = {};
        packetSender.sendPacket(data);

        verify(mockOutputStream).write(data);
        verify(mockOutputStream).flush();
    }

    /**
     * <p>Тестирует конструктор класса PacketSender с null OutputStream.</p>
     * <p>Ожидает выброс IllegalArgumentException с соответствующим сообщением.</p>
     */
    @Test
    public void testConstructor_NullOutputStream() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new PacketSender(null));
        assertEquals("OutputStream cannot be null", thrown.getMessage());
    }
}