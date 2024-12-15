package TrafficGenerator;

import ErrorHandler.ErrorHandler;
import NetworkConnection.NetworkConfig;
import NetworkConnection.NetworkConnection;
import Statistics.GeneratorStatistics;

import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.util.stream.IntStream;

/**
 * <p>TrafficGenerator - класс для генерации и отправки трафика через сетевое соединение.</p>
 */
public class TrafficGenerator extends NetworkConnection {
    private static final Logger logger = LogManager.getLogger(TrafficGenerator.class);
    private TextArea outputArea;
    private InputReader inputReader;

    /**
     * <p>Конструктор класса TrafficGenerator.</p>
     *
     * @param outputArea текстовая область для вывода информации
     * @param inputReader класс для чтения пользовательского ввода
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public TrafficGenerator(TextArea outputArea, InputReader inputReader) throws IOException {
        super(NetworkConfig.PORT);
        this.outputArea = outputArea;
        this.inputReader = inputReader;
        logger.info("TrafficGenerator initialized with outputArea and inputReader.");
    }

    /**
     * <p>Обрабатывает сессию генерации трафика.</p>
     *
     * @param sessionCount номер сессии
     */
    public void handle(int sessionCount) {
        try {
            OutputStream outputStream = socket.getOutputStream();

            outputArea.appendText("Generation session # " + sessionCount + '\n');
            logger.info("Starting generation session # {}", sessionCount);

            int numberOfPackets = inputReader.getPacketCount();
            int packetSize = inputReader.getPacketSize();
            int frequency = inputReader.getFrequency();

            sendSessionInfo(outputStream, sessionCount, numberOfPackets);
            sendPackets(outputStream, numberOfPackets, packetSize, frequency);
        } catch (IOException e) {
            ErrorHandler.handleError("Error sending data", e);
            outputArea.appendText("Error sending data: " + e.getMessage());
            logger.error("IOException in handle method: {}", e.getMessage());
        }
    }

    /**
     * <p>Отправляет информацию о сессии.</p>
     *
     * @param outputStream выходной поток для отправки данных
     * @param sessionCount номер сессии
     * @param numberOfPackets количество пакетов
     * @throws IOException если возникает ошибка ввода-вывода
     */
    private void sendSessionInfo(OutputStream outputStream, int sessionCount, int numberOfPackets) throws IOException {
        byte[] byteSessionCount = ByteBuffer.allocate(4).putInt(sessionCount).array();
        outputStream.write(byteSessionCount);
        outputStream.flush();

        byte[] bytePacketCount = ByteBuffer.allocate(4).putInt(numberOfPackets).array();
        outputStream.write(bytePacketCount);
        outputStream.flush();
        logger.info("Session info sent: sessionCount={}, numberOfPackets={}", sessionCount, numberOfPackets);
    }

    /**
     * <p>Отправляет пакеты данных.</p>
     *
     * @param outputStream выходной поток для отправки данных
     * @param numberOfPackets количество пакетов
     * @param packetSize размер пакета
     * @param frequency частота отправки пакетов
     * @throws IOException если возникает ошибка ввода-вывода
     */
    private void sendPackets(OutputStream outputStream, int numberOfPackets, int packetSize, int frequency) throws IOException {
        PacketSender packetSender = new PacketSender(outputStream);
        GeneratorStatistics statistics = new GeneratorStatistics(outputArea);

        long startTime = System.nanoTime();
        for (int i = 0; i < numberOfPackets; i++) {
            byte[] data = generatePackets(packetSize);
            packetSender.sendPacket(data);
            logger.info("Packet {} sent with size {}", i + 1, packetSize);

            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ErrorHandler.handleError("Error during sleep", e);
                logger.error("InterruptedException during sleep: {}", e.getMessage());
            }
        }

        outputStream.write("END".getBytes());
        outputStream.flush();
        statistics.displayStatistics(numberOfPackets, packetSize, startTime);
        logger.info("Finished sending packets. Total packets sent: {}", numberOfPackets);
    }

    /**
     * <p>Генерирует пакеты данных.</p>
     *
     * @param packetSize размер пакета
     * @return byte[] data массив байтов
     */
    public static byte[] generatePackets(int packetSize) {
        byte[] data = IntStream.range(0, packetSize)
                .mapToObj(j -> (byte) (Math.random() * 256))
                .collect(ByteArrayOutputStream::new, ByteArrayOutputStream::write, (a, b) -> {
                    try {
                        b.writeTo(a);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }).toByteArray();
        return data;
    }

    /**
     * <p>Закрывает сокет.</p>
     */
    public void close() {
        if (socket != null) {
            try {
                serverSocket.close();
                socket.close();
                logger.info("Socket closed successfully.");
            } catch (IOException e) {
                ErrorHandler.handleIOException("Error closing socket", e);
                logger.error("IOException while closing socket: {}", e.getMessage());
            }
        }
    }

    /**
     * <p> Проверяет закрыт ли сокет</p>
     *
     * @return 0, если сокет пуст или закрыт или 1, если сокет не пуст и не закрыт
     */
    public boolean isClientConnected() {
        return socket != null && !socket.isClosed();
    }

    /**
     * Главный метод приложения.
     * <p> Он оставлен для соответствия стандартной структуре Java-приложения.</p>
     *
     * @param args аргументы командной строки, переданные при запуске программы
     */
    public static void main(String[] args) {
    }
}
