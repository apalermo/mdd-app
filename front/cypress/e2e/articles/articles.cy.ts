describe('Articles Management', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
  });

  it('should list articles and toggle sorting', () => {
    cy.visit('/articles');
    cy.get('.article-card').should('have.length', 4);

    cy.get('.sort-toggle-btn').should('contain', 'Date');
    cy.get('.sort-toggle-btn').click();
    cy.get('.sort-toggle-btn').should('contain', 'Titre');
  });

  it('should create a new article successfully', () => {
    cy.visit('/articles/create');
    const title = `Cypress Title ${Date.now()}`;

    cy.get('select#theme-select').select('1');
    cy.get('input#article-title').type(title);
    cy.get('textarea#article-content').type(
      'Contenu de test pour la validation E2E.'
    );

    cy.get('button.submit-btn').click();

    cy.url().should('eq', Cypress.config().baseUrl + '/articles');
    cy.contains(title).should('be.visible');
  });
});
