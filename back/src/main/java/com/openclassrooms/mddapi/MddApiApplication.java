package com.openclassrooms.mddapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot MDD API.
 * <p>
 * Point d'entrée de l'application. Annotée avec {@code @SpringBootApplication}
 * pour activer l'auto-configuration, le scan des composants et la configuration Spring.
 * </p>
 * <p>
 * L'application expose une API REST sécurisée par JWT permettant :
 * </p>
 * <ul>
 *   <li>L'authentification et la gestion des utilisateurs</li>
 *   <li>La gestion des topics (thèmes)</li>
 *   <li>La création et consultation de posts (articles)</li>
 *   <li>L'ajout de commentaires sur les posts</li>
 *   <li>Les abonnements aux topics</li>
 * </ul>
 *
 */
@SpringBootApplication
public class MddApiApplication {

	/**
	 * Méthode principale pour lancer l'application Spring Boot.
	 *
	 * @param args arguments de ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication.run(MddApiApplication.class, args);
	}

}
