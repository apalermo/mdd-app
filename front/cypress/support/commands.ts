export {};

declare global {
  namespace Cypress {
    interface Chainable {
      login(identifier: string, password: string): Chainable<void>;
    }
  }
}

Cypress.Commands.add('login', (identifier: string, password: string) => {
  cy.session(identifier, () => {
    cy.visit('/login');

    cy.get('input#identifier').type(identifier);
    cy.get('input#password').type(password);

    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/articles');
  });
});
