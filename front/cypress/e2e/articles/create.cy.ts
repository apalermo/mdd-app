describe('Article Creation', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
    cy.visit('/articles/create');
  });

  it('should create a new article successfully', () => {
    const title = `Cypress Title ${Date.now()}`;

    cy.get('[data-cy=create-article-theme]').select('1');
    cy.get('[data-cy=create-article-title]').type(title);
    cy.get('[data-cy=create-article-content]').type(
      'Ceci est un contenu de test suffisant.',
    );

    cy.get('[data-cy=create-article-submit]').click();

    cy.url().should('include', '/articles');
    cy.contains(title).should('be.visible');
  });

  it('should show validation messages for invalid inputs', () => {
    cy.get('[data-cy=create-article-title]').type('abc').blur();
    cy.get('[data-cy=title-error]')
      .should('be.visible')
      .and('contain', 'au moins 5 caractères');

    cy.get('[data-cy=create-article-content]').type('abc').blur();
    cy.get('[data-cy=content-error]')
      .should('be.visible')
      .and('contain', 'au moins 10 caractères');

    cy.get('[data-cy=create-article-submit]').should('be.disabled');
  });

  it('should show error notification on server failure (500)', () => {
    cy.intercept('POST', '/api/articles', {
      statusCode: 500,
      body: {},
    }).as('createError');

    cy.get('[data-cy=create-article-theme]').select('1');
    cy.get('[data-cy=create-article-title]').type('Titre valide');
    cy.get('[data-cy=create-article-content]').type(
      'Contenu valide pour le test.',
    );
    cy.get('[data-cy=create-article-submit]').click();

    cy.wait('@createError');
    cy.get('.global-toast').should('be.visible');
    cy.get('.toast-content').should(
      'contain',
      'Une erreur inattendue est survenue.',
    );
  });
});
