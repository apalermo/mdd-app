describe('Article Detail & Comments', () => {
  beforeEach(() => {
    cy.task('resetDb');
    cy.login('test@test.com', '12345678Test!');
    cy.visit('/articles/1');
  });

  it('should display article content and post a comment', () => {
    cy.get('h1').should('contain', 'Les nouveautés de Spring Boot 3');

    const comment = 'Super article, merci pour le partage !';
    cy.get('textarea#comment-content').type(comment);
    cy.get('button.send-btn').click();

    cy.get('.comment-item').should('contain', comment);
    cy.get('textarea#comment-content').should('have.value', '');
  });
});
