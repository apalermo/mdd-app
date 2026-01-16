
INSERT IGNORE INTO themes (id, title, description, created_at, updated_at) VALUES
(1, 'Java', 'Explorez l''écosystème Java, du langage de base aux frameworks comme Spring Boot.', NOW(), NOW()),
(2, 'Angular', 'Apprenez à construire des interfaces modernes et performantes avec le framework de Google.', NOW(), NOW()),
(3, 'Node.js', 'Maîtrisez JavaScript côté serveur pour construire des APIs scalables et rapides.', NOW(), NOW()),
(4, 'TypeScript', 'Sécurisez votre code JavaScript avec le typage statique et les fonctionnalités modernes.', NOW(), NOW()),
(5, 'DevOps', 'Découvrez l''intégration continue, le déploiement et la gestion d''infrastructure moderne.', NOW(), NOW());


INSERT IGNORE INTO users (id, email, name, password, created_at, updated_at) VALUES
(1, 'test@test.com', 'Anthony Palermo', '$2a$12$rnNkZpTZGUUFNFS4xB3oNegnUE.Li7HxHYldUn2hCT1aw4WnUIqRK', NOW(), NOW());

INSERT IGNORE INTO articles (id, theme_id, author_id, title, content, created_at, updated_at) VALUES
(1, 1, 1, 'Les nouveautés de Spring Boot 3', 'Découvrez les dernières fonctionnalités de Spring Boot 3 pour vos microservices.', NOW(), NOW()),
(2, 2, 1, 'Passer aux Signals avec Angular', 'Les Signals révolutionnent la gestion de la réactivité dans vos applications Angular.', NOW(), NOW()),
(3, 3, 1, 'Express vs NestJS en 2026', 'Comparatif complet pour choisir votre prochain framework backend Node.js.', NOW(),NOW()),
(4, 5, 1, 'Pourquoi Docker est indispensable', 'Apprenez à conteneuriser vos applications pour un déploiement simplifié.', NOW(),NOW());

INSERT IGNORE INTO comments (id, article_id, author_id, content, created_at) VALUES
(1, 2, 1, 'Super article, les Signals simplifient vraiment la gestion du state !', NOW());