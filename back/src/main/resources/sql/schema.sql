CREATE DATABASE IF NOT EXISTS mdd_db;
USE mdd_db;

CREATE TABLE IF NOT EXISTS themes (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      title VARCHAR(50) NOT NULL,
    description VARCHAR(2000),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT UK_THEMES_TITLE UNIQUE (title)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT UK_USERS_EMAIL UNIQUE (email),
    CONSTRAINT UK_USERS_NAME UNIQUE (name)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS articles (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        author_id BIGINT NOT NULL,
                                        theme_id BIGINT NOT NULL,
                                        title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT FK_ARTICLES_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT FK_ARTICLES_THEME FOREIGN KEY (theme_id) REFERENCES themes (id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        article_id BIGINT NOT NULL,
                                        author_id BIGINT NOT NULL,
                                        content TEXT NOT NULL,
                                        created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_COMMENTS_ARTICLE FOREIGN KEY (article_id) REFERENCES articles (id),
    CONSTRAINT FK_COMMENTS_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS subscriptions (
                                             theme_id BIGINT NOT NULL,
                                             user_id BIGINT NOT NULL,
                                             created_at DATETIME(6),
    PRIMARY KEY (theme_id, user_id),
    CONSTRAINT FK_SUBSCRIPTIONS_THEME FOREIGN KEY (theme_id) REFERENCES themes (id),
    CONSTRAINT FK_SUBSCRIPTIONS_USER FOREIGN KEY (user_id) REFERENCES users (id)
    ) ENGINE=InnoDB;