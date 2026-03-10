describe('authApply Fuctionality', () => {
  context('authApply View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/grantsubsmgmt/viewListUserAuthSubs.model').as('viewListUserAuthSubs') /* 목록 */
      cy.intercept('POST', '**/opermgmt/grantsubsmgmt/viewDetailUserAuthSubs.model').as('viewDetailUserAuthSubs') /* 상세 */
      cy.visitPath('board/authApply')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListUserAuthSubs').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', ()=> {
      cy.wait('@viewListUserAuthSubs')
      cy.get('#element-table tr').eq(1).dblclick()
      cy.wait('@viewDetailUserAuthSubs').then((interception)=> {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.resultListVo).to.exist
        
      })
    })
    it('insert', ()=> {
      cy.intercept('POST', '**/opermgmt/grantsubsmgmt/viewInsertUserAuthSubs.model').as('viewInsertUserAuthSubs')
      cy.intercept('POST', '**/opermgmt/grantsubsmgmt/insertUserAuthTxnSub.json').as('insertUserAuthTxnSub')
      cy.wait('@viewListUserAuthSubs')
      cy.get('.add-features > .el-button').click()
      cy.wait('@viewInsertUserAuthSubs').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
        // cy.log(interception.response.body)
      })
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertUserAuthTxnSub').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})