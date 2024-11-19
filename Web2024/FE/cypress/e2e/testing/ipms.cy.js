/* ==== Test Created with Cypress Studio ==== */
it('ipms', function() {
  /* ==== Generated with Cypress Studio ==== */
  cy.visit('http://localhost:4000');
  cy.get('.is-required > .el-form-item__content > input').clear('di');
  cy.get('.is-required > .el-form-item__content > input').type('dimmby');
  cy.get(':nth-child(2) > .el-form-item__content > input').clear();
  cy.get(':nth-child(2) > .el-form-item__content > input').type('dimmby');
  cy.get('button').click();
  /* ==== End Cypress Studio ==== */
});