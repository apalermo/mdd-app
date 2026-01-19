describe('Articles Listing', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
  });

  it('should list articles and toggle sorting direction on dates', () => {
    cy.visit('/articles');
    cy.get('[data-cy=article-card]').should('have.length', 4);

    cy.get('[data-cy=article-title]')
      .first()
      .then(($first) => {
        const newestTitle = $first.text();

        cy.get('[data-cy=sort-direction-btn]').click();

        cy.get('[data-cy=article-title]')
          .first()
          .should('not.have.text', newestTitle);
      });
  });
});
