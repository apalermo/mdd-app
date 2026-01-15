describe('User Profile', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
  });

  it('should update user information', () => {
    cy.visit('/me');
    cy.get('input#profile-name').clear().type('Anthony Updated');
    cy.get('button.btn-save').click();

    cy.contains('Vos modifications ont été enregistrées !').should(
      'be.visible'
    );
  });

  it('should display subscriptions and allow unsubscription', () => {
    cy.visit('/themes');
    cy.contains("S'abonner").first().click();

    cy.visit('/me');
    cy.get('app-theme-card').should('have.length', 1);

    cy.contains('Se désabonner').click();
    cy.get('app-theme-card').should('not.exist');
  });
});
