import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;

/**
 * <p>Абстрактный класс JavaFXTest предназначен для инициализации JavaFX среды перед выполнением тестов.</p>
 * <p>Этот класс использует JFXPanel для запуска JavaFX в тестах, что позволяет использовать компоненты JavaFX в тестах JUnit.</p>
 */
public abstract class JavaFXTest {

    /**
     * <p>Инициализирует JavaFX среду перед выполнением всех тестов.</p>
     * <p>Этот метод вызывается один раз перед всеми тестами в классе, что позволяет избежать повторной инициализации.</p>
     */
    @BeforeAll
    public static void initJavaFX() {
        new JFXPanel(); // Создание JFXPanel для инициализации JavaFX
    }
}