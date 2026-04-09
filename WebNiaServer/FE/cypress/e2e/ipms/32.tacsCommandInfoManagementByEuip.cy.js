describe('tacs Management commandInfoManagementByEuip Fuctionality', () => {
  context('commandInfoManagementByEuip View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/viewListTacsFcltCmdMst.model').as('viewListTacsFcltCmdMst') /* 목록 */
      cy.visitPath('operInfoMng/tacsMng/commandInfoManagementByEuip')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListTacsFcltCmdMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('insert', () => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/insertTacsFcltCmdMst.json').as('insertTacsFcltCmdMst')
      cy.get('.add-features > :nth-child(1)').click()
      cy.get('tbody > :nth-child(1) > td > .el-input > .el-input__inner').type('ACCESS-UBIQUOSS_TEST')
      cy.get(':nth-child(2) > td > .el-input > .el-input__inner').type('TEST')
      cy.get(':nth-child(3) > :nth-child(2) > .el-input > .el-input__inner').type('TEST')
      cy.get(':nth-child(4) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.savailYn > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
    })
    it('detail', () => {
      cy.wait('@viewListTacsFcltCmdMst')
      cy.get('#element-table tr').eq(1).click()
      cy.get('tbody > :nth-child(1) > td > .el-input > .el-input__inner').should('be.disabled')
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/updateTacsFcltCmdMst.model').as('updateTacsFcltCmdMst')
      cy.wait('@viewListTacsFcltCmdMst')
      cy.get('#element-table tr').eq(1).click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateTacsFcltCmdMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})