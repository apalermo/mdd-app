describe('Home Page', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should display the logo and navigation buttons', () => {
    cy.get('img.logo').should('be.visible');
    cy.contains('button', 'Se connecter').should('be.visible');
    cy.contains('button', "S'inscrire").should('be.visible');
  });

  it('should navigate to login page via button', () => {
    cy.contains('button', 'Se connecter').click();
    cy.url().should('include', '/login');
  });
});
