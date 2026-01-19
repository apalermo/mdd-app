describe('Home Page', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should display the logo and navigation buttons', () => {
    cy.get('[data-cy=home-logo]').should('be.visible');
    cy.get('[data-cy=home-login-btn]').should('be.visible');
    cy.get('[data-cy=home-register-btn]').should('be.visible');
  });

  it('should navigate to login page via button', () => {
    cy.get('[data-cy=home-login-btn]').click();
    cy.url().should('include', '/login');
  });
});
