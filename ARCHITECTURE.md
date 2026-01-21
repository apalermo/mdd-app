# Architecture et Choix Techniques

Ce document justifie la conception logicielle du projet **MDD** pour l'entreprise **ORION**.

---

## 🏗 Vision Globale

Le projet adopte une structure **Monorepo** découplée, séparant strictement la logique métier (Backend) de l'expérience utilisateur (Frontend) pour faciliter la maintenance et l'évolution indépendante des services.

## 🔙 Conception Backend (Java / Spring Boot)

Le backend repose sur une **Architecture en Couches** :

- **Séparation des responsabilités** : Utilisation systématique de services pour la logique métier, laissant les contrôleurs gérer uniquement les requêtes HTTP.
- **Pattern DTO** : Les entités JPA ne sont jamais exposées directement. Des mappers assurent la transition vers des DTOs, protégeant ainsi l'intégrité de la base de données.
- **Sécurité Stateless** : Implémentation de JWT pour une authentification sécurisée sans stockage de session côté serveur.

## 🔜 Conception Frontend (Angular)

L'interface est conçue pour la réactivité et la performance :

- **Patterns modernes** : Combinaison de **NGRX** pour le state global et des **Signals** pour une détection de changement ultra-fine au niveau des composants.
- **Optimisation** : Découpage en modules avec **Lazy-loading** pour garantir un temps de chargement initial minimal, même en cas de croissance de l'application.
- **DRY** : Mutualisation des styles SCSS et des composants transversaux (Boutons, Cards).

## 🗄️ Modèle de Données (MySQL)

Le schéma relationnel garantit la cohérence des interactions métier :

- **Relations fortes** : Gestion des abonnements via une table de jointure pour assurer l'intégrité entre utilisateurs et thèmes.
- **Validation** : Hibernate est configuré en mode `validate` pour s'assurer que le code est toujours en phase avec les scripts SQL de production.

## 📈 Évolutions Futures (Dette Technique)

Pour les itérations futures, les chantiers suivants sont préconisés :

1. **Cookies HttpOnly** : Migration du stockage JWT pour supprimer les risques de vol de token via XSS.
2. **Pagination Serveur** : Intégration de `Pageable` dans les contrôleurs pour gérer de gros volumes d'articles.
3. **Tri Déporté** : Déplacer la logique de tri complexe vers les requêtes SQL pour alléger le processeur du client.
