describe('commandInfoManagementByEuip Fuctionality', () => {
  /* 운용정보관리 > IP주소 라우팅 비교/점검 > 장비별 명령어 장비정보관리 */
  context('commandInfoManagementByEuip View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewListFcltCmdMst.model').as('viewListFcltCmdMst') /* 목록 */
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewInsertFcltMst.model').as('viewInsertFcltMst') /* 장비 타입 조회 */
      cy.visitPath('operInfoMng/ipAdressRoutingCompare/commandInfoManagementByEuip')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListFcltCmdMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('insert', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/insertFcltCmdMst.json').as('insertFcltCmdMst')
      cy.wait('@viewListFcltCmdMst')
      cy.get('.add-features > :nth-child(1)').click()
      /* form 입력 */
      cy.get('tbody > :nth-child(1) > td > .el-input > .el-input__inner').type('TEST')
      cy.get(':nth-child(2) > td > .el-input > .el-input__inner').type('TEST')
      cy.get(':nth-child(3) > :nth-child(2) > .el-input > .el-input__inner').type('1')
      cy.get(':nth-child(4) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.suseYn > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(3)').click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertFcltCmdMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('detail', () => {
      cy.wait('@viewListFcltCmdMst')
      cy.get('#element-table tr').eq(1).click()
      cy.get('tbody > :nth-child(1) > td > .el-input > .el-input__inner').should('be.disabled')
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/updateFcltCmdMst.json').as('updateFcltCmdMst')
      cy.wait('@viewListFcltCmdMst')
      cy.get('#element-table tr').eq(1).click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateFcltCmdMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/deleteFcltCmdMst.json').as('deleteFcltCmdMst')
      cy.wait('@viewListFcltCmdMst')
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@deleteFcltCmdMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})