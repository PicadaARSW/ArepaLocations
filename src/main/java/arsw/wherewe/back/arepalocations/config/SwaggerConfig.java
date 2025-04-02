package arsw.wherewe.back.arepalocations.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ArepaLocations API")
                        .version("1.0.0")
                        .description("API for managing locations and favorite places in the ArepaLocations service using STOMP over WebSocket")
                        .contact(new Contact()
                                .name("Picada Team")
                                .email("picadaarsw2025@outlook.com")));
    }
}
