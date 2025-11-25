package com.openclassrooms.mddapi.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration pour charger les variables d'environnement depuis le fichier .env
 * Les variables sont chargées AVANT le démarrage de Spring Boot via EnvironmentPostProcessor
 * Cela permet à Spring d'utiliser ces variables dès le début (datasource, etc.)
 */
public class DotenvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            // Charge le fichier .env depuis le répertoire de travail
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()  // Ne plante pas si .env n'existe pas
                    .load();

            // Convertit les entrées dotenv en Map pour Spring
            Map<String, Object> dotenvProperties = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                dotenvProperties.put(entry.getKey(), entry.getValue());
            });

            // Ajoute les propriétés à l'environnement Spring avec une priorité élevée
            environment.getPropertySources()
                    .addFirst(new MapPropertySource("dotenvProperties", dotenvProperties));

            System.out.println("Fichier .env chargé avec succès (" + dotenvProperties.size() + " variables)");
        } catch (Exception e) {
            System.out.println("Fichier .env non trouvé - utilisation des variables d'environnement système");
        }
    }
}
