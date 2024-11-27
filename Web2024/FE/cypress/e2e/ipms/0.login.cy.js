describe('User Login Fuctionality', () => {
  beforeEach(() => {
    cy.visitLocal()
  })
  context('Test the login functionality', () => {
    it('should succesfully log in with valid credentials', () => {
      cy.login()

      cy.wait(['@getkey', '@signin', '@setting']).then((interception)=> {
        interception.forEach((interception)=> {
          expect(interception.response.statusCode).to.equal(200)
        })
      })
      cy.url().should('include', '/ipms/index')
    })
  })
})