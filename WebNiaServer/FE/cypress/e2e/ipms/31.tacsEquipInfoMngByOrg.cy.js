describe('tacs Management equipInfoMngByOrg Fuctionality', () => {
  context('equipInfoMngByOrg View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/viewListTacsFcltMst.model').as('viewListTacsFcltMst') /* 목록 */
      cy.visitPath('operInfoMng/tacsMng/equipInfoMngByOrg')
    })
    afterEach(() => {
      cy.wait(1000)
    })

    it('list lookup', () => {
      cy.wait('@viewListTacsFcltMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    // 등록 데이터를 위한 element cy.get 불가
    // it('insert', () => {})
    it('detail', () => {
      /* 장비타입 조회 요청 */
      cy.intercept('POST', '**/opermgmt/tacsmgmt/viewInsertTacsFcltMst.model').as('viewInsertTacsFcltMst')
      cy.get('#element-table tr').eq(2).click()
      cy.wait('@viewInsertTacsFcltMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/updateTacsFcltMst.json').as('updateTacsFcltMst')
      cy.get('.el-pager > :nth-child(3)').click()
      cy.get('.add-features > :nth-child(2)').click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateTacsFcltMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/deleteTacsFcltMst.json').as('deleteTacsFcltMst')
      cy.get('.el-pager > :nth-child(3)').click()
      cy.get('.add-features > :nth-child(3)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteTacsFcltMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})