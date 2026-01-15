describe('Logout Flow', () => {
  beforeEach(() => {
    cy.login('test@test.com', '12345678Test!');
    cy.visit('/articles');
  });

  it('should logout successfully and clear session cache', () => {
    cy.get('nav').contains('Profil').click();
    cy.contains('Se déconnecter').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.visit('/articles', { failOnStatusCode: false });
    cy.url().should('not.include', '/articles');
    cy.url().should('include', '/login');
  });
});
