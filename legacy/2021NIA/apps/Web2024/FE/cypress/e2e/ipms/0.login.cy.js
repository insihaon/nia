describe('User Login Fuctionality', () => {
  beforeEach(() => {
    cy.visitLocal()
  })
  context('Test the login functionality', () => {
    it('should succesfully log in with valid credentials', () => {
      cy.get(':nth-child(1) > .el-form-item__content > input').type('10150810');
      cy.get(':nth-child(2) > .el-form-item__content > input').type('1');
      cy.get('button').click();

      cy.wait(['@getkey', '@signin', '@setting']).then((interception) => {
        interception.forEach((interception) => {
          expect(interception.response.statusCode).to.equal(200)
          cy.saveToken()
        })
      })
      cy.url().should('include', '/ipms/index')
    })
  })
})
