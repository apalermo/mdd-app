describe('Themes Subscriptions', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
    cy.visit('/themes');
  });

  it('should allow subscribing to a specific theme', () => {
    cy.get('app-theme-card').should('have.length.at.least', 1);

    cy.get('app-theme-card')
      .first()
      .within(() => {
        cy.contains("S'abonner").click();
        cy.contains("S'abonner").should('not.exist');
      });
  });
});
