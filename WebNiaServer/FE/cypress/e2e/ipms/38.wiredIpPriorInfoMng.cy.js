describe('wiredIpPriorInfoMng Fuctionality', () => {
  context('wiredIpPriorInfoMng View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewListWireMst.model').as('viewListWireMst') /* 목록 */
      cy.visitPath('operInfoMng/ipAdressRoutingCompare/wiredIpPriorInfoMng')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListWireMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    /* community 개별 등록 */
    it('insert', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/insertWireMst.json').as('insertWireMst')
      cy.wait('@viewListWireMst')
      cy.get('.add-features > :nth-child(1)').click()
      /* form 입력 */
      cy.get('td > .el-select > .el-input > .el-input__inner').click()
      cy.get('.skindCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get(':nth-child(2) > :nth-child(2) > .el-input > .el-input__inner').type('TEST')
      cy.get('.popupContentTableBottom > :nth-child(1)').click()

      cy.wait('@insertWireMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/updateWireMst.json').as('updateWireMst')
      cy.wait('@viewListWireMst')
      cy.get('#element-table tr').eq(1).click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateWireMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/deleteWireMst.json').as('deleteWireMst')
      cy.wait('@viewListWireMst')
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteWireMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})