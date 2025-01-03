describe('routingLinkHistStatus Fuctionality', () => {
  context('routingLinkHistStatus View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewListRoutHistMst.model').as('viewListRoutHistMst') /* 목록 */
      cy.visitPath('operInfoMng/ipAdressRoutingCompare/routingLinkHistStatus')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListRoutHistMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
  })
})