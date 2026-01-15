describe('Login Flow', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.visit('/login');
  });

  it('should log in successfully with valid credentials', () => {
    cy.get('input#identifier').type('test@test.com');
    cy.get('input#password').type('12345678Test!');

    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/articles');
    cy.get('.article-card').should('have.length', 4);
  });

  it('should show validation errors for empty fields', () => {
    cy.get('input#identifier').focus().blur();
    cy.get('input#password').focus().blur();

    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input#identifier').should('have.attr', 'aria-invalid', 'true');
  });

  it('should navigate to the register page', () => {
    cy.get('a[routerLink="/register"]').click();
    cy.url().should('include', '/register');
    cy.get('h1').should('contain', 'Inscription');
  });

  it('should navigate back home when clicking the logo in the header', () => {
    cy.get('header').find('img, a').first().click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');
  });
});
