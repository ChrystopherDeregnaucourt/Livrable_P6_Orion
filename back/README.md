# MDD API - Back-end

API REST Spring Boot pour la plateforme MDD (Monde de Dév), permettant la gestion des articles, thèmes et commentaires avec authentification sécurisée.

## Technologies

- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - Spring Actuator
- **MySQL** - Base de données relationnelle
- **JWT (jjwt 0.12.3)** - Authentification stateless
- **Lombok** - Réduction du code boilerplate
- **Maven** - Gestion des dépendances

## Prérequis

- **JDK 17** ou supérieur
- **Maven 3.8+**
- **MySQL 8.0+**
- Un fichier `.env` configuré (voir section Configuration)

## Architecture

L'application suit une architecture en couches classique Spring Boot :

```
src/main/java/com/openclassrooms/mddapi/
config/          # Configuration Spring (CORS, Sécurité, etc.)
controller/      # REST Controllers (endpoints API)
dto/             # Data Transfer Objects (Request/Response)
entity/          # Entités JPA (modèle de données)
exception/       # Gestion des erreurs (@ControllerAdvice)
repository/      # Interfaces Spring Data JPA
security/        # Configuration JWT et filtres de sécurité
service/         # Logique métier
```

### Principes respectés

**Séparation des responsabilités** : Controller → Service → Repository  
**Sécurité JWT** : Toutes les routes sont protégées sauf `/auth/login` et `/auth/register`  
**Validation** : Bean Validation sur tous les DTO d'entrée  
**Gestion d'erreurs centralisée** : `@ControllerAdvice` pour des réponses d'erreur cohérentes  
**Pas d'exposition directe des entités** : Utilisation systématique de DTO

##  Configuration

### 1. Base de données

Créez une base de données MySQL :

```sql
CREATE DATABASE mdd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Le schéma sera automatiquement créé au démarrage grâce à `spring.jpa.hibernate.ddl-auto=update`.

### 2. Variables d'environnement

Créez un fichier `.env` à la racine du projet `back/` :

```bash
cp .env.example .env
```

Puis configurez les variables :

```properties
# Base de données MySQL
DB_URL=jdbc:mysql://localhost:3306/mdd?serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=votre_mot_de_passe

# Serveur
SERVER_PORT=9000

# JWT - IMPORTANT : Changez cette clé en production !
JWT_SECRET=votre_cle_secrete_complexe_minimum_256_bits
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200
```

**Sécurité** : Ne committez JAMAIS le fichier `.env` ! Il contient des informations sensibles.

Pour générer une clé JWT sécurisée :

```bash
# PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))

# Linux/Mac
openssl rand -base64 64
```

### 3. Profils Spring

L'application supporte plusieurs profils :

- **dev** (par défaut) : Développement local
- **prod** : Production (variables d'environnement système)

Activation d'un profil :

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Voir `ENV_CONFIG.md` pour plus de détails.

## Installation et lancement

### Avec Maven Wrapper (recommandé)

```bash
# Installation des dépendances
./mvnw clean install

# Lancement de l'application
./mvnw spring-boot:run
```

### Avec Maven local

```bash
# Installation des dépendances
mvn clean install

# Lancement de l'application
mvn spring-boot:run
```

L'API sera accessible sur **http://localhost:9000**

## Endpoints API

### Authentification (publics)

- `POST /api/auth/register` - Inscription d'un nouvel utilisateur
- `POST /api/auth/login` - Connexion (retourne un JWT)

### Utilisateurs (protégés)

- `GET /api/auth/me` - Profil de l'utilisateur connecté
- `PUT /api/auth/me` - Mise à jour du profil

### Thèmes (protégés)

- `GET /api/themes` - Liste des thèmes disponibles
- `POST /api/themes/{id}/subscribe` - S'abonner à un thème
- `DELETE /api/themes/{id}/unsubscribe` - Se désabonner d'un thème

### Articles (protégés)

- `GET /api/articles` - Liste des articles (flux de l'utilisateur)
- `GET /api/articles/{id}` - Détail d'un article
- `POST /api/articles` - Créer un article

### Commentaires (protégés)

- `POST /api/articles/{id}/comments` - Ajouter un commentaire
- `GET /api/articles/{id}/comments` - Liste des commentaires d'un article

## Sécurité

### Authentification JWT

Toutes les requêtes vers des endpoints protégés doivent inclure un en-tête `Authorization` :

```
Authorization: Bearer <votre_token_jwt>
```

Le token est obtenu lors de la connexion via `/api/auth/login`.

### Points de sécurité

- **Mots de passe** : Hachés avec BCrypt
- **JWT** : Signé avec HMAC-SHA512
- **CORS** : Liste blanche d'origines autorisées (configurable via `.env`)
- **Validation** : Tous les DTO d'entrée sont validés
- **Erreurs** : Pas d'exposition de stacktrace en production

## Tests

### Lancer les tests

```bash
# Tous les tests
./mvnw test

# Tests avec couverture
./mvnw test jacoco:report
```

Les tests incluent :
- Tests unitaires des services
- Tests d'intégration des controllers (MockMvc)
- Tests de sécurité

## Monitoring

L'application expose des endpoints Actuator pour le monitoring :

- `GET /actuator/health` - État de santé de l'application
- `GET /actuator/info` - Informations sur l'application

**Note** : Les endpoints Actuator sont protégés en production.

## Outils de développement

### Postman

Une collection Postman est disponible à la racine du projet : `postman-config.json`

Importez-la dans Postman pour tester facilement tous les endpoints avec des environnements pré-configurés.

### Mockoon

Un fichier de configuration Mockoon (`mockoon-config.json`) est disponible pour simuler l'API pendant le développement front-end.

## Docker

Un fichier `docker-compose.yml` est disponible à la racine du projet pour lancer MySQL facilement :

```bash
docker-compose up -d
```

## Bonnes pratiques

### Structure du code

- **Controllers** : Endpoints REST uniquement, pas de logique métier
- **Services** : Toute la logique métier et les règles de validation
- **Repositories** : Accès aux données uniquement (JPA)
- **DTO** : Séparation stricte entre entités JPA et objets exposés

### Conventions de nommage

- **Endpoints** : Noms au pluriel (`/articles`, `/themes`)
- **Méthodes** : Verbes HTTP standards (GET, POST, PUT, DELETE)
- **Réponses** : Codes HTTP appropriés (200, 201, 204, 400, 401, 403, 404, 500)

### Logs

Utilisez les niveaux de log appropriés :
- `INFO` : Flux applicatif
- `DEBUG` : Diagnostic détaillé
- `ERROR` : Erreurs nécessitant une attention


## Déploiement en production

1. **Variables d'environnement** : Définissez-les au niveau système (pas de fichier `.env`)
2. **Profil** : Activez le profil `prod`
3. **Base de données** : Utilisez une base MySQL distante sécurisée
4. **JWT Secret** : Générez une nouvelle clé forte
5. **CORS** : Configurez précisément les origines autorisées (pas de `*`)
6. **Actuator** : Restreignez l'accès aux endpoints de monitoring

## Documentation complémentaire

- `ENV_CONFIG.md` - Configuration détaillée des variables d'environnement
- `CHOIX_TECHNIQUES.md` - Justifications des choix techniques

---

**Auteur** : Chrystopher Deregnaucourt  
**Version** : 0.0.1  
**Date** : Novembre 2025
