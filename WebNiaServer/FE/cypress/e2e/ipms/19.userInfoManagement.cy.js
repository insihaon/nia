describe('userInfoManagement Fuctionality', () => {
  context('userInfoManagement View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/usermgmt/viewListTbUserBas.model').as('viewListTbUserBas') /* 목록 */
      cy.visitPath('operInfoMng/userMng/userInfoManagement')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListTbUserBas').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', ()=> {
      cy.intercept('POST', '**/opermgmt/usermgmt/updateTbUserBas.json').as('updateTbUserBas')
      cy.intercept('POST', '**/opermgmt/usermgmt/viewListUserHndSetTxn.model').as('viewListUserHndSetTxn')
      cy.intercept('POST', '**/opermgmt/usermgmt/viewInsertUserHndSetTxn.model').as('viewInsertUserHndSetTxn')
      cy.intercept('POST', '**/opermgmt/usermgmt/viewDeleteUserHndSetTxn.model').as('viewDeleteUserHndSetTxn')
      

      cy.wait('@viewListTbUserBas')
      cy.get('#element-table tr').eq(1).find('td').eq(4).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      /* 비밀번호 실패횟수 초기화 */
      cy.get('.popupContentTableBottom > div > :nth-child(1)').click()
      cy.wait('@updateTbUserBas').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
      cy.get('.el-message-box__btns > .el-button').click()
      /* 사용자 접속 IP변경(사용자 단말 관리) */
      cy.get('.popupContentTableBottom > div > :nth-child(2)').click()
      /* IP승인 현황 조회 */
      cy.wait('@viewListUserHndSetTxn').then((interception) => {
        expect(interception.response.body.result.data).to.exist
      })
      /* 사용자 접속 IP변경 > IP 등록 */
      cy.get(':nth-child(1) > td > .w-100 > .el-input__inner').type('1.1.1.1')
      cy.get(':nth-child(2) > td > .w-100 > .el-input__inner').type('test')
      cy.get('.ModalUpdateUserConIp > .el-dialog > .el-dialog__body > .popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@viewInsertUserHndSetTxn').then((interception) => {
        expect(interception.response.body.result.data.commonMsg).to.equal('SUCCESS')
      })
      cy.get('.el-message-box__btns > .el-button').click()
      /* 사용자 접속 IP변경 > IP 삭제 */
      cy.get(':nth-child(1) > :nth-child(5) > .el-button').click()
      cy.wait('@viewDeleteUserHndSetTxn').then((interception) => {
        expect(interception.response.body.result.data.commonMsg).to.equal('SUCCESS')
      })
    })

  })
})