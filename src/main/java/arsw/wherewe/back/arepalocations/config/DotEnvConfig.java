package arsw.wherewe.back.arepalocations.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {
    public DotEnvConfig() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./") // Looks for .env in the project root
                .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}