package com.openclassrooms.mddapi.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration pour charger les variables d'environnement depuis un fichier .env.
 * <p>
 * Implémente {@link EnvironmentPostProcessor} pour charger le fichier .env
 * AVANT le démarrage de Spring Boot, permettant ainsi à Spring d'utiliser
 * ces variables dès l'initialisation (datasource, JWT secret, etc.).
 * </p>
 * <p>
 * Le fichier .env doit être placé à la racine du projet.
 * Si le fichier n'existe pas, aucune erreur n'est levée (mode ignoreIfMissing).
 * </p>
 * <p>
 * Configuration requise dans META-INF/spring.factories pour activer ce processor.
 * </p>
 *
 */
public class DotenvConfig implements EnvironmentPostProcessor {

    /**
     * Charge le fichier .env et injecte les variables dans l'environnement Spring.
     * <p>
     * Méthode appelée automatiquement par Spring Boot avant le démarrage de l'application.
     * </p>
     *
     * @param environment l'environnement Spring configurable
     * @param application l'application Spring Boot
     */
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
