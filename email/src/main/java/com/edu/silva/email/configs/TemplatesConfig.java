package com.edu.silva.email.configs;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class TemplatesConfig {

    @Bean
    public Handlebars handlebars() {
        ClassPathTemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates/");
        loader.setSuffix(".hbs");
        loader.setCharset(StandardCharsets.UTF_8);

        return new Handlebars(loader);
    }
}