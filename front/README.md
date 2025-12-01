# MDD Front-end

Application web Angular pour la plateforme MDD (Monde de Dév), permettant aux développeurs de consulter des articles, de s'abonner à des thèmes et de participer aux discussions.

## Technologies

- **Angular 20.3.1**
- **Angular Material 20.2.4** - Composants UI
- **RxJS 7.8** - Programmation réactive
- **Chart.js 4.4.4** - Graphiques et visualisations
- **TypeScript 5.8**
- **Karma & Jasmine** - Tests unitaires

## Prérequis

- **Node.js 18+** (LTS recommandé)
- **npm 9+**
- API back-end MDD lancée sur `http://localhost:9000` (voir `../back/README.md`)

## Architecture

L'application suit les bonnes pratiques Angular avec une architecture modulaire :

```
src/
app/
  guards/            # Gardes de navigation (authentification)
  interceptors/      # Intercepteur JWT
  models/            # Interfaces TypeScript (Article, Theme, User, etc.)
  pages/             # Composants de pages
    home/            # Page d'accueil
    login/           # Connexion
    register/        # Inscription
    articles/        # Liste des articles
    article-detail/  # Détail d'un article
    create-article/  # Création d'article
    themes/          # Gestion des thèmes
    profile/         # Profil utilisateur
  services/          # Services métier
    api.service.ts   # Communication avec l'API
    auth.service.ts  # Gestion de l'authentification
  validators/        # Validateurs personnalisés
  assets/            # Ressources statiques
  environments/      # Configuration d'environnement
  styles/            # Styles SCSS globaux et partiels
```

### Principes respectés

**Services par domaine** : `AuthService`, `ApiService` pour la communication API  
**Intercepteur JWT** : Ajout automatique du token `Authorization: Bearer <token>`  
**Gardes de route** : Protection des pages nécessitant une authentification  
**Modèles typés** : Interfaces TypeScript pour tous les objets métier  
**Environnements** : URLs d'API configurables par environnement  
**Gestion des erreurs** : Traitement uniforme via intercepteur

## Configuration

### Environnements

Les URLs de l'API sont configurées dans les fichiers d'environnement :

**`src/environments/environment.ts`** (développement) :
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:9000/api'
};
```

**`src/environments/environment.prod.ts`** (production) :
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://votre-api-prod.com/api'
};
```

**Important** : Ne jamais hardcoder d'URL dans les services, toujours utiliser `environment.apiUrl`.

## Installation et lancement

### Installation des dépendances

```bash
npm install
```

### Lancement en mode développement

```bash
npm start
# ou
ng serve
```

L'application sera accessible sur **http://localhost:4200**

Le serveur de développement recharge automatiquement l'application à chaque modification de fichier.

### Build de production

```bash
npm run build
# ou
ng build --configuration production
```

Les fichiers de production seront générés dans `dist/`.

### Lancement des tests

```bash
# Tests unitaires (Karma + Jasmine)
npm test
# ou
ng test

# Tests avec couverture
ng test --code-coverage
```

## Fonctionnalités

### Authentification

- **Inscription** : Création d'un compte utilisateur
- **Connexion** : Authentification par email/mot de passe (JWT)
- **Déconnexion** : Suppression du token et redirection
- **Persistence** : Token stocké en `localStorage` (session persistante)

### Gestion des articles

- **Flux d'articles** : Affichage des articles des thèmes suivis
- **Détail d'article** : Visualisation complète avec commentaires
- **Création d'article** : Publication sur un thème
- **Commentaires** : Ajout de commentaires sur les articles

### Gestion des thèmes

- **Liste des thèmes** : Affichage de tous les thèmes disponibles
- **Abonnement/Désabonnement** : Gestion des abonnements aux thèmes

### Profil utilisateur

- **Consultation** : Visualisation des informations personnelles
- **Modification** : Mise à jour du nom, email, etc.
- **Abonnements** : Liste des thèmes suivis

## Sécurité

### Intercepteur JWT

L'intercepteur `AuthInterceptor` ajoute automatiquement le token JWT à toutes les requêtes vers l'API :

```typescript
Authorization: Bearer <token>
```

### Gardes de navigation

- **`AuthGuard`** : Protège les routes nécessitant une authentification
- **`UnauthGuard`** : Empêche l'accès aux pages publiques (login/register) si déjà connecté

### Stockage du token

Le token JWT est stocké dans `localStorage`. Pour un niveau de sécurité supérieur, considérez :
- L'utilisation de cookies `HttpOnly` (nécessite un support back-end)
- La mise en place d'un refresh token

### Gestion des erreurs

L'intercepteur gère automatiquement :
- **401 Unauthorized** : Déconnexion et redirection vers `/login`
- **403 Forbidden** : Accès interdit
- **500 Server Error** : Erreur serveur

## Interface utilisateur

### Angular Material

L'application utilise Angular Material pour une interface moderne et cohérente :

- Boutons, formulaires, cartes
- Navigation (toolbar, sidenav)
- Feedback utilisateur (snackbar)

### SCSS modulaire

Les styles sont organisés en partials SCSS :

```
src/styles/
  _cards.scss         # Styles des cartes
  _form-pages.scss    # Styles des formulaires
  _nav-pages.scss     # Styles de navigation
  styles.scss         # Point d'entrée global
```

## Communication avec l'API

### ApiService

Service centralisé pour toutes les requêtes HTTP :

```typescript
// Exemples d'utilisation
this.apiService.getArticles().subscribe(articles => { ... });
this.apiService.getThemes().subscribe(themes => { ... });
this.apiService.createArticle(articleData).subscribe(article => { ... });
```

### AuthService

Gestion de l'authentification :

```typescript
// Connexion
this.authService.login(credentials).subscribe(response => {
  // Token automatiquement stocké
});

// Déconnexion
this.authService.logout();

// Vérifier si connecté
this.authService.isLoggedIn()
```

## Tests

### Structure des tests

Chaque composant dispose d'un fichier `.spec.ts` pour les tests unitaires :

```typescript
describe('ArticlesComponent', () => {
  let component: ArticlesComponent;
  let fixture: ComponentFixture<ArticlesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArticlesComponent ],
      imports: [ HttpClientTestingModule, RouterTestingModule ]
    }).compileComponents();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
```

### Bonnes pratiques de tests

- Mocker les services avec `HttpClientTestingModule`
- Tester les interactions utilisateur
- Vérifier les cas d'erreur
- Maintenir une couverture > 80%

## Déploiement

### Build optimisé

```bash
ng build --configuration production
```

Options de build :
- Minification du code
- Optimisation des bundles
- AOT compilation
- Tree-shaking

### Serveur web (exemple avec nginx)

Un fichier `nginx.conf` est fourni pour le déploiement :

```nginx
server {
  listen 80;
  server_name localhost;
  root /usr/share/nginx/html;
  index index.html;

  location / {
    try_files $uri $uri/ /index.html;
  }
}
```

### Variables d'environnement

Pour la production, mettez à jour `environment.prod.ts` avec l'URL de votre API :

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.votre-domaine.com/api'
};
```

## Outils de développement

### Angular DevTools

Extension Chrome/Firefox pour debugger les applications Angular :
- Inspection des composants
- Profilage des performances
- Visualisation de la structure

### Commandes utiles

```bash
# Générer un nouveau composant
ng generate component pages/ma-page

# Générer un service
ng generate service services/mon-service

# Générer un guard
ng generate guard guards/mon-guard

# Analyser la taille des bundles
ng build --stats-json
npx webpack-bundle-analyzer dist/front/stats.json
```

## Bonnes pratiques

### Code

- **Typage strict** : Utiliser TypeScript rigoureusement
- **Observables** : Toujours se désabonner (async pipe ou unsubscribe)
- **Immutabilité** : Éviter les mutations directes
- **Fonctions pures** : Favoriser les fonctions sans effets de bord

### Performances

- **Lazy loading** : Charger les modules à la demande
- **OnPush strategy** : Pour les composants à faible fréquence de changement
- **TrackBy** : Dans les `*ngFor` pour optimiser le rendu
- **Pagination** : Pour les grandes listes

### Accessibilité

- Labels sur tous les champs de formulaire
- Navigation au clavier
- Contrastes de couleurs suffisants
- ARIA attributes si nécessaire

## Débogage

### Erreurs courantes

**CORS Error** :
- Vérifiez que l'API autorise l'origine `http://localhost:4200`
- Voir la configuration CORS dans `back/.env` (`CORS_ALLOWED_ORIGINS`)

**401 Unauthorized** :
- Token expiré ou invalide
- Vérifiez le stockage du token dans `localStorage`
- Reconnectez-vous

**Cannot find module** :
- Relancez `npm install`
- Vérifiez les imports dans les fichiers TypeScript

## Documentation complémentaire

- [Angular Documentation](https://angular.dev/)
- [Angular Material](https://material.angular.io/)
- [RxJS](https://rxjs.dev/)
- [TypeScript](https://www.typescriptlang.org/)

---

**Auteur** : Chrystopher Deregnaucourt  
**Version** : 0.0.1  
**Date** : Novembre 2025
