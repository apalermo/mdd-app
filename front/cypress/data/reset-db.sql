SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE comments;
TRUNCATE TABLE subscriptions;
TRUNCATE TABLE articles;
TRUNCATE TABLE themes;
TRUNCATE TABLE users;

INSERT INTO themes (id, title, description) VALUES 
(1, 'Java', 'Explorez l''écosystème Java, du langage de base aux frameworks comme Spring Boot.'),
(2, 'Angular', 'Apprenez à construire des interfaces modernes et performantes avec le framework de Google.'),
(3, 'Node.js', 'Maîtrisez JavaScript côté serveur pour construire des APIs scalables et rapides.'),
(4, 'TypeScript', 'Sécurisez votre code JavaScript avec le typage statique et les fonctionnalités modernes.'),
(5, 'DevOps', 'Découvrez l''intégration continue, le déploiement et la gestion d''infrastructure moderne.');

INSERT INTO users (id, email, name, password, created_at) VALUES 
(1, 'test@test.com', 'Anthony Palermo', '$2a$10$9v1P3O6Wl.K9vQG5u1Wn9NueC4H6X7Y9Z.V9Q8W7Qz7L1T8S', NOW());

INSERT INTO articles (id, theme_id, user_id, title, content, created_at) VALUES 
(1, 1, 1, 'Les nouveautés de Spring Boot 3', 'Découvrez les dernières fonctionnalités de Spring Boot 3 pour vos microservices.', NOW()),
(2, 2, 1, 'Passer aux Signals avec Angular', 'Les Signals révolutionnent la gestion de la réactivité dans vos applications Angular.', NOW()),
(3, 3, 1, 'Express vs NestJS en 2026', 'Comparatif complet pour choisir votre prochain framework backend Node.js.', NOW()),
(4, 5, 1, 'Pourquoi Docker est indispensable', 'Apprenez à conteneuriser vos applications pour un déploiement simplifié.', NOW());

SET FOREIGN_KEY_CHECKS = 1;