describe('Themes Subscriptions', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
  });

  it('should allow subscribing to a specific theme', () => {
    cy.visit('/themes');
    cy.get('[data-cy=theme-card]').should('have.length.at.least', 1);

    cy.get('[data-cy=theme-card]')
      .first()
      .within(() => {
        cy.get('[data-cy=subscribe-btn]').click();
        cy.get('[data-cy=subscribed-btn]')
          .should('be.disabled')
          .and('contain', 'Déjà abonné');
      });
  });

  it('should show error notification if loading themes fails', () => {
    cy.intercept('GET', '/api/themes', {
      statusCode: 500,
      body: { message: 'Erreur lors du chargement des thèmes' },
    }).as('getThemesError');

    cy.visit('/themes');
    cy.wait('@getThemesError');

    cy.get('.toast-content')
      .should('be.visible')
      .and('contain', 'Erreur lors du chargement des thèmes');
  });
});
