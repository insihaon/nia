describe('tacs Management tacsLinkHistStatus Fuctionality', () => {
  context('tacsLinkHistStatus View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/viewListTacsConnHist.model').as('viewListTacsConnHist') /* 목록 */
      cy.visitPath('operInfoMng/tacsMng/tacsLinkHistStatus')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListTacsConnHist').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
  })
})