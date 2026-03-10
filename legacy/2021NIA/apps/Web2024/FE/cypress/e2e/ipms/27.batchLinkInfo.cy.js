describe('batchLinkInfo Fuctionality', () => {
  context('batchLinkInfo View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/linkmgmt/batchmgmt/viewListTbBatchSvcBas.model').as('viewListTbBatchSvcBas') /* 목록 */
      cy.visitPath('operInfoMng/linkMng/batchLinkInfo')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListTbBatchSvcBas').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('update', () => {
      
      cy.intercept('POST', '**/linkmgmt/batchmgmt/updateTbBatchSvcBas.json').as('updateTbBatchSvcBas')
      cy.wait('@viewListTbBatchSvcBas')
      cy.get('#element-table tr').eq(1).click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()

      cy.get('.popupContentTable > table > tbody > :nth-child(4) > td').find('.el-select').should('have.length', 3)
      
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateTbBatchSvcBas').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})