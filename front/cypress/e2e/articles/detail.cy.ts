describe('Article Detail & Comments', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
  });

  it('should display article content and post a comment', () => {
    cy.visit('/articles/1');
    cy.get('[data-cy=article-title]').should(
      'contain',
      'Les nouveautés de Spring Boot 3',
    );

    const comment = 'Super article, merci pour le partage !';
    cy.get('[data-cy=comment-textarea]').type(comment);
    cy.get('[data-cy=comment-submit]').click();

    cy.get('[data-cy=comment-item]').should('contain', comment);
    cy.get('[data-cy=comment-textarea]').should('have.value', '');
  });

  it('should show validation messages for invalid comment', () => {
    cy.visit('/articles/1');
    cy.get('[data-cy=comment-textarea]').type('a').blur();
    cy.get('[data-cy=comment-error]')
      .should('be.visible')
      .and('contain', '2 caractères minimum');
    cy.get('[data-cy=comment-submit]').should('be.disabled');
  });

  it('should show error notification if loading article fails (404)', () => {
    cy.intercept('GET', '/api/articles/999', {
      statusCode: 404,
      body: { message: 'Article non trouvé' },
    }).as('getArticleError');

    cy.visit('/articles/999', { failOnStatusCode: false });
    cy.wait('@getArticleError');
    cy.get('.global-toast').should('be.visible');
    cy.get('[data-cy=toast-content]').should('contain', 'Article non trouvé');
  });

  it('should show error notification if posting comment fails (500)', () => {
    cy.visit('/articles/1');
    cy.intercept('POST', '/api/articles/1/comments', {
      statusCode: 500,
      body: {},
    }).as('postCommentError');

    cy.get('[data-cy=comment-textarea]').type('Ceci est un commentaire valide');
    cy.get('[data-cy=comment-submit]').click();

    cy.wait('@postCommentError');
    cy.get('.global-toast').should('be.visible');
    cy.get('[data-cy=toast-content]').should(
      'contain',
      'Une erreur inattendue est survenue',
    );
  });
});
