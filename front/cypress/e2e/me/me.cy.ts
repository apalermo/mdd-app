describe('User Profile', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
    cy.visit('/me');
  });

  it('should update user information successfully', () => {
    const newName = 'Anthony Updated';
    cy.get('[data-cy=profile-name]').clear().type(newName);
    cy.get('[data-cy=profile-submit]').click();

    cy.get('.toast-content')
      .should('be.visible')
      .and('contain', 'Vos modifications ont été enregistrées !');
  });

  it('should show validation errors for invalid profile data', () => {
    cy.get('[data-cy=profile-name]').clear().blur();
    cy.get('[data-cy=name-error]').should('be.visible');

    cy.get('[data-cy=profile-email]').clear().type('not-an-email').blur();
    cy.get('[data-cy=email-error]').should('be.visible');

    cy.get('[data-cy=profile-submit]').should('be.disabled');
  });

  it('should display subscriptions and allow unsubscription', () => {
    cy.visit('/themes');
    cy.get('[data-cy=subscribe-btn]').first().click();

    cy.visit('/me');
    cy.get('[data-cy=subscription-item]').should('have.length', 1);

    cy.get('[data-cy=unsubscribe-btn]').click();
    cy.get('[data-cy=subscription-item]').should('not.exist');
    cy.get('[data-cy=no-subscriptions]').should('be.visible');
  });
});
