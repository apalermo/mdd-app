# MDD - Monde de Dév

## 📝 Présentation du projet

L'application **MDD** (Monde de Dév) est une plateforme de réseau social conçue pour les développeurs de l'entreprise **ORION**. Elle permet de centraliser la veille technologique grâce à un système d'abonnement à des thèmes, de publication d'articles et d'échanges par commentaires.

## 🛠 Stack Technique

| Composant    | Technologie          | Version / Détails                  |
| :----------- | :------------------- | :--------------------------------- |
| **Backend**  | Java / Spring Boot 3 | API RESTful, Spring Security (JWT) |
| **Frontend** | Angular 20           | Signals, NGRX, Lazy-loading (100%) |
| **Database** | MySQL 8              | Architecture relationnelle         |

---

## 🚀 Installation et Configuration

### 1. Prérequis

- **Java 21** installé.
- **Node.js** (version LTS) et **Angular CLI**.
- Instance **MySQL 8** opérationnelle.

### 2. Base de données

Initialisez votre base de données en exécutant les commandes suivantes :

```sql
CREATE DATABASE mdd_db;
CREATE USER 'mdd_user'@'localhost' IDENTIFIED BY 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON mdd_db.* TO 'mdd_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Variables d'environnement

Configurez les clés suivantes dans votre environnement ou votre IDE :

- `DB_HOST`: localhost
- `DB_PORT`: 3306
- `DB_NAME`: mdd_db
- `DB_USERNAME`: mdd_user
- `DB_PASSWORD`: votre_mot_de_passe
- `TOKEN_SECRET`: [votre_cle_secrete_pour_jwt]

---

## 🏃 Lancement de l'application

### Backend

```bash
cd back
./mvnw clean install
./mvnw spring-boot:run
```

- **Swagger UI** : Accédez à la documentation interactive de l'API sur `http://localhost:8080/swagger-ui.html`.

### Frontend

```bash
cd front
npm install
npm start
```

_L'interface est accessible sur `http://localhost:4200`._

---

## 🧪 Tests et Rapports de Couverture

Le projet respecte un haut standard de qualité avec une couverture bien supérieure au minimum requis de 70%.

### Génération des rapports (Global)

- **Backend** : **92%** de couverture (JUnit 5 / JaCoCo). Exécutez `./mvnw clean verify` pour générer le rapport dans `target/site/jacoco/index.html`.
- **Frontend** : **91%** de couverture (**Vitest** / Cypress).
  - Tests unitaires : `npm run test:coverage` (Rapport dans `coverage/index.html`).
  - Tests E2E : `npx cypress run` (Cypress).

---

## 📂 Liens et Documentation

- [**Architecture & Justifications**](./ARCHITECTURE.md) : Détails sur les patterns (SOLID, DRY) et choix techniques.
- [**Documentation Frontend**](./front/README.md) : Spécifications de l'application Angular et guides de développement
- [**Documentation API (Backend)**](./back/README.md) : Liste exhaustive des endpoints et spécifications techniques.
