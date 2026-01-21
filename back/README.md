# MDD API - Documentation Backend

## 🛠 Spécifications Techniques

- **Framework** : Spring Boot 3.5.8
- **Langage** : Java 21
- **Sécurité** : Spring Security avec **JWT** (Stateless, expiration 24h)
- **Mapping** : Utilisation de **Mappers** pour le découplage Entités/DTOs

---

## 🗄️ Base de Données et Scripts SQL

Le projet utilise **Spring SQL Initialization** pour automatiser la création du schéma et l'insertion des données de test à chaque démarrage.

### 1. Emplacement des scripts

Les fichiers se trouvent dans `src/main/resources/sql/` :

- **`schema.sql`** : Définit la structure des tables (`users`, `themes`, `articles`, `comments`, `subscriptions`).
- **`data.sql`** : Insère les thématiques par défaut (Java, Angular, Node.js, TypeScript, DevOps) et un jeu de données de test.

### 2. Configuration Hibernate

La propriété `hibernate.ddl-auto` est définie sur **`validate`**. Cela garantit que l'application ne modifie pas la structure de la base dynamiquement et s'appuie exclusivement sur les scripts SQL fournis pour assurer l'intégrité du schéma.

---

## 📡 Liste des Endpoints (API REST)

### Authentification (`/api/auth`)

| Méthode | Endpoint    | Description                                      | Accès       |
| :------ | :---------- | :----------------------------------------------- | :---------- |
| POST    | `/register` | Inscription d'un nouvel utilisateur              | Public      |
| POST    | `/login`    | Connexion et récupération du token JWT           | Public      |
| GET     | `/me`       | Récupération du profil de l'utilisateur connecté | Authentifié |

### Articles & Commentaires (`/api/article`)

| Méthode | Endpoint        | Description                                  | Accès       |
| :------ | :-------------- | :------------------------------------------- | :---------- |
| GET     | `/`             | Récupère la liste de tous les articles       | Authentifié |
| GET     | `/{id}`         | Récupère les détails d'un article spécifique | Authentifié |
| POST    | `/`             | Publie un nouvel article                     | Authentifié |
| POST    | `/{id}/comment` | Ajoute un commentaire à un article           | Authentifié |

### Thèmes (`/api/theme`)

| Méthode | Endpoint          | Description                       | Accès       |
| :------ | :---------------- | :-------------------------------- | :---------- |
| GET     | `/`               | Liste tous les thèmes disponibles | Authentifié |
| POST    | `/subscribe/{id}` | S'abonner à un thème spécifique   | Authentifié |
| DELETE  | `/subscribe/{id}` | Se désabonner d'un thème          | Authentifié |

### Utilisateur (`/api/user`)

| Méthode | Endpoint | Description                                           | Accès       |
| :------ | :------- | :---------------------------------------------------- | :---------- |
| PUT     | `/`      | Met à jour les informations du profil (email, pseudo) | Authentifié |

---

## 🧪 Qualité et Tests

- **Couverture** : **92%** de couverture globale.
- **Génération du rapport** : `./mvnw clean verify` (Rapport dans `target/site/jacoco/index.html`).
