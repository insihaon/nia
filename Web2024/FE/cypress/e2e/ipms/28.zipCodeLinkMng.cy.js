describe('zipCodeLinkMng Fuctionality', () => {
  context('zipCodeLinkMng View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/uploadmgmt/uploadView.model').as('uploadView') /* 목록 */
      cy.visitPath('operInfoMng/linkMng/zipCodeLinkMng')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@uploadView').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
  })
})