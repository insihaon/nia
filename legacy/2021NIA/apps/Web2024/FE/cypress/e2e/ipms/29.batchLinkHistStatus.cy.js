describe('batchLinkHistStatus Fuctionality', () => {
  context('batchLinkHistStatus View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/linkmgmt/batchmgmt/viewListBatchHistMst.model').as('viewListBatchHistMst') /* 목록 */
      cy.visitPath('operInfoMng/linkMng/batchLinkHistStatus')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListBatchHistMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
  })
})