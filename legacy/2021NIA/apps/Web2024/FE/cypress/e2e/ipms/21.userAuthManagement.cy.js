describe('userAuthManagement Fuctionality', () => {
  context('userAuthManagement View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/grantmgmt/viewListUserAuth.model').as('viewListUserAuth') /* 목록 */
      cy.intercept('POST', '**/opermgmt/grantmgmt/viewInsertUserAuth.model').as('viewInsertUserAuth') /* 상세 */
      cy.intercept('POST', '**/opermgmt/grantmgmt/insertUserAuthTxn.json').as('insertUserAuthTxn') /* 등록 */
      cy.visitPath('operInfoMng/userMng/userAuthManagement')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListUserAuth').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      cy.wait('@viewListUserAuth')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewInsertUserAuth').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data.tbUserAuthTxnVos).to.exist
      })
    })
    it('update', () => {
      cy.wait('@viewListUserAuth')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewInsertUserAuth')

      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertUserAuthTxn').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('insert', () => {

      cy.intercept('POST', '**/opermgmt/usermgmt/selectSearchTbUserBas.json').as('selectSearchTbUserBas')
      
      cy.wait('@viewListUserAuth')
      cy.get('.add-features > :nth-child(1)').click()
      cy.get(':nth-child(1) > .el-input > .el-input__suffix > .el-input__suffix-inner > .el-button').click()
      cy.get('.popupContentTable > table > tr > :nth-child(2) > .el-input > .el-input__inner').type('정회창')
      cy.get('table > tr > :nth-child(3) > .el-button').click()
      cy.wait('@selectSearchTbUserBas').then((interception) => {
        expect(interception.response.body.tbUserBasVos).to.exist
      })
      cy.get('.el-dialog__body > .compTable tr').eq(1).dblclick()
      cy.get(':nth-child(3) > .el-button').click()

      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertUserAuthTxn').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/grantmgmt/deleteUserAuthTxn.json').as('deleteUserAuthTxn')
      
      cy.wait('@viewListUserAuth')
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(2)').click()
      cy.wait('@deleteUserAuthTxn').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })

    })
  })
})