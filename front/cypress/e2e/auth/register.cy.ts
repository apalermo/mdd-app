describe('Register Flow', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.visit('/register');
  });

  it('should register successfully and redirect to articles', () => {
    const email = `dev_${Date.now()}@test.com`;

    cy.get('input#name').type('Anthony Dev');
    cy.get('input#email').type(email);
    cy.get('input#password').type('12345678Test!');

    cy.get('button.btn-submit').click();

    cy.url().should('include', '/articles');
    cy.get('.articles-grid').should('be.visible');
  });
});
