describe('Register Flow', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.visit('/register');
  });

  it('should register successfully and redirect to articles', () => {
    const email = `dev_${Date.now()}@test.com`;

    cy.get('[data-cy=register-name]').type('Anthony Dev');
    cy.get('[data-cy=register-email]').type(email);
    cy.get('[data-cy=register-password]').type('12345678Test!');

    cy.get('[data-cy=register-submit]').click();

    cy.url().should('include', '/articles');
    cy.get('.articles-grid').should('be.visible');
  });

  it('should show validation errors and disable button for empty fields', () => {
    cy.get('[data-cy=register-name]').focus().blur();
    cy.get('[data-cy=register-email]').focus().blur();
    cy.get('[data-cy=register-password]').focus().blur();

    cy.get('[data-cy=register-submit]').should('be.disabled');
    cy.get('[data-cy=register-email]').should(
      'have.attr',
      'aria-invalid',
      'true',
    );
    cy.get('[data-cy=register-password]').should(
      'have.attr',
      'aria-invalid',
      'true',
    );
  });

  it('should show error message for invalid email format', () => {
    cy.get('[data-cy=register-email]').type('invalid-email-format').blur();
    cy.get('.error-msg').should(
      'contain',
      'Veuillez saisir une adresse e-mail valide',
    );
    cy.get('[data-cy=register-submit]').should('be.disabled');
  });

  it('should show error messages for weak password complexity', () => {
    cy.get('[data-cy=register-password]').type('123').blur();
    cy.get('.error-msg').should('contain', '8 caractères minimum');
    cy.get('[data-cy=register-submit]').should('be.disabled');

    cy.get('[data-cy=register-password]')
      .clear()
      .type('SimplePassword123')
      .blur();
    cy.get('.error-msg').should('be.visible');
  });

  it('should show an error notification if email is already taken (409)', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 409,
      body: { message: 'Cet email est déjà utilisé !' },
    }).as('registerConflict');

    cy.get('[data-cy=register-name]').type('User Test');
    cy.get('[data-cy=register-email]').type('already@taken.com');
    cy.get('[data-cy=register-password]').type('Password123!');
    cy.get('[data-cy=register-submit]').click();

    cy.wait('@registerConflict');
    cy.get('.global-toast').should('be.visible');
    cy.get('.toast-content').should('contain', 'Cet email est déjà utilisé !');
  });

  it('should show a generic notification for server error (500)', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500,
      body: {},
    }).as('serverError');

    cy.get('[data-cy=register-name]').type('User Test');
    cy.get('[data-cy=register-email]').type('test@test.com');
    cy.get('[data-cy=register-password]').type('Password123!');
    cy.get('[data-cy=register-submit]').click();

    cy.wait('@serverError');
    cy.get('.global-toast').should('be.visible');
    cy.get('.toast-content').should(
      'contain',
      'Une erreur inattendue est survenue',
    );
  });

  it('should navigate to the login page', () => {
    cy.get('[data-cy=register-to-login]').click();
    cy.url().should('include', '/login');
    cy.get('h1').should('contain', 'Connexion');
  });
});
