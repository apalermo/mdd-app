describe('Login Flow', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.visit('/login');
  });

  it('should log in successfully with valid credentials', () => {
    cy.get('[data-cy=login-identifier]').type('test@test.com');
    cy.get('[data-cy=login-password]').type('12345678Test!');

    cy.get('[data-cy=login-submit]').click();

    cy.url().should('include', '/articles');
    cy.get('.article-card').should('exist');
  });

  it('should show validation errors for empty fields', () => {
    cy.get('[data-cy=login-identifier]').focus().blur();
    cy.get('[data-cy=login-password]').focus().blur();

    cy.get('[data-cy=login-submit]').should('be.disabled');
    cy.get('[data-cy=login-identifier]').should(
      'have.attr',
      'aria-invalid',
      'true',
    );
    cy.get('.error-msg').should('contain', 'Ce champ est obligatoire');
  });

  it('should show a notification for invalid credentials (401)', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Identifiants invalides' },
    }).as('loginRequest');

    cy.get('[data-cy=login-identifier]').type('wrong@test.com');
    cy.get('[data-cy=login-password]').type('WrongPassword123!');
    cy.get('[data-cy=login-submit]').click();

    cy.wait('@loginRequest');
    cy.get('.global-toast').should('be.visible');
    cy.get('[data-cy=toast-content]').should(
      'contain',
      'Identifiants invalides',
    );
  });

  it('should show a generic notification for server error (500)', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 500,
      body: {},
    }).as('serverError');

    cy.get('[data-cy=login-identifier]').type('test@test.com');
    cy.get('[data-cy=login-password]').type('Password123!');
    cy.get('[data-cy=login-submit]').click();

    cy.wait('@serverError');
    cy.get('.global-toast').should('be.visible');
    cy.get('[data-cy=toast-content]').should(
      'contain',
      'Une erreur inattendue est survenue',
    );
  });

  it('should navigate to the register page', () => {
    cy.get('a[routerLink="/register"]').click();
    cy.url().should('include', '/register');
  });
});
