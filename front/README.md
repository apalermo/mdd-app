# MDD App - Documentation Frontend

## 🛠 Spécifications Techniques

- **Framework** : Angular 20
- **Gestion d'état** : NGRX & Signals
- **Performance** : Lazy-loading implémenté sur 100% des routes
- **Style** : SCSS avec architecture modulaire et variables centralisées

---

## 📂 Organisation du Code

L'application est structurée pour favoriser la réutilisabilité (principes DRY) :

- **`src/app/core`** : Contient les éléments transversaux (Services d'authentification, Guards, Interceptors JWT).
- **`src/app/pages`** : Regroupe les composants de vue principaux (Articles, Thèmes, Profil utilisateur).
- **`src/app/shared`** : Centralise les composants réutilisables, comme la `theme-card` utilisée sur plusieurs écrans pour éviter la duplication de logique.

---

## 🏃 Lancement et Build

### 1. Installation

```bash
npm install
```

### 2. Développement

```bash
npm start
```

L'application est servie sur `http://localhost:4200/`.

### 3. Production

```bash
npm run build
```

---

## 🧪 Qualité et Tests

Le projet affiche une couverture de **91%** sur la partie interface.

### Tests Unitaires (Vitest)

Nous avons remplacé Karma par **Vitest** pour gagner en performance et en fiabilité lors des tests de composants.

- **Lancer les tests** : `npm test`
- **Rapport de couverture** : `npm run test:coverage` (Génère le dossier `/coverage`).

### Tests E2E (Cypress)

Utilisé pour valider les parcours utilisateurs critiques (Connexion, Publication, Abonnement).

- **Mode console** : `npx cypress run`
- **Interface visuelle** : `npx cypress open`
