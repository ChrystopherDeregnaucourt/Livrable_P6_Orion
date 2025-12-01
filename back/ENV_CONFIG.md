# Configuration avec fichier .env

## Mise en place

Le projet utilise un fichier `.env` pour gérer les variables d'environnement de façon sécurisée.

### 1. Créer votre fichier .env

Copiez le fichier exemple et adaptez les valeurs :

```bash
cp .env.example .env
```

### 2. Configurer les variables

Éditez le fichier `.env` avec vos valeurs :

```properties
# Base de données MySQL
DB_URL=jdbc:mysql://localhost:3306/mdd?serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=votre_mot_de_passe

# Serveur
SERVER_PORT=9000

# JWT - IMPORTANT : Changez cette clé en production !
JWT_SECRET=votre_cle_secrete_complexe
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200
```

### 3. Générer une clé JWT sécurisée

**Pour la production, générez une nouvelle clé JWT :**

```bash
# Linux/Mac
openssl rand -base64 64

# PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

## Lancement de l'application

### Avec Maven

```bash
mvn spring-boot:run
```

### Avec votre IDE

L'application charge automatiquement le fichier `.env` au démarrage grâce à la classe `DotenvConfig`.

## Sécurité

**IMPORTANT** :
- Le fichier `.env` contient des informations sensibles
- Il est automatiquement ignoré par Git (voir `.gitignore`)
- Ne le commitez JAMAIS
- Utilisez `.env.example` comme modèle pour votre équipe
- Changez TOUTES les valeurs par défaut en production

## Variables d'environnement

| Variable | Description | Valeur par défaut (dev) |
|----------|-------------|------------------------|
| `DB_URL` | URL de connexion MySQL | `jdbc:mysql://localhost:3306/mdd?serverTimezone=UTC` |
| `DB_USERNAME` | Utilisateur MySQL | `root` |
| `DB_PASSWORD` | Mot de passe MySQL | `root` |
| `SERVER_PORT` | Port du serveur | `9000` |
| `JWT_SECRET` | Clé secrète JWT | À changer ! |
| `JWT_EXPIRATION` | Durée validité token (ms) | `86400000` (24h) |
| `CORS_ALLOWED_ORIGINS` | Origines CORS autorisées | `http://localhost:4200` |

## Déploiement en production

En production, définissez les variables d'environnement directement sur votre serveur/conteneur :

```bash
export DB_URL="jdbc:mysql://prod-server:3306/mdd"
export DB_USERNAME="mdd_user"
export DB_PASSWORD="mot_de_passe_fort"
export JWT_SECRET="cle_secrete_generee_avec_openssl"
export CORS_ALLOWED_ORIGINS="https://votre-domaine.com"
```

Le fichier `.env` est ignoré si les variables système sont définies.
