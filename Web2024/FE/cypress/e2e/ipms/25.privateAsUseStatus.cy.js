describe('privateAsUseStatus Fuctionality', () => {
  context('privateAsUseStatus View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/asmgmt/viewListAsHist.model').as('viewListAsHist') /* 목록 */
      cy.visitPath('operInfoMng/privateAsMng/privateAsUseStatus')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListAsHist').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('detail', () => {
      cy.intercept('POST', '**/opermgmt/asmgmt/viewDetailAsHist.model').as('viewDetailAsHist') /* 상세 */
      cy.wait('@viewListAsHist')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailAsHist').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
      
    })
  })
})