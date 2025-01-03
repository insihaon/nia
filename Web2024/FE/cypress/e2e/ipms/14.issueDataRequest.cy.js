describe('issueDataRequest Fuctionality', () => {
  context('issueDataRequest View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/requiremgmt/viewListReq.model').as('viewListReq') /* 목록 */
      cy.intercept('POST', '**/opermgmt/requiremgmt/viewDetailReq.model').as('viewDetailReq') /* 상세 */
      cy.visitPath('board/issueDataRequest')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListReq').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', ()=> {
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailReq').then((interception)=> {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result).to.exist
      })
    })
    /* 2024-12-19 insert, update, delete mock data 없음 */
    it('insert', ()=> {
      cy.intercept('POST', '**/opermgmt/requiremgmt/insertReq.json').as('insertReq')
      cy.get('.add-features > .el-button').click()
      /* 내용입력 */
      cy.get('tbody > :nth-child(1) > td > .el-input > .el-input__inner').type('요구사항 등록 테스트')
      cy.get(':nth-child(2) > td > .el-input > .el-input__inner').type('테스트')
      cy.get(':nth-child(2) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.rboardDivision > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get(':nth-child(3) > :nth-child(4) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.rboardImportance > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(4)').click()
      cy.get('.el-date-editor > .el-input__inner').click()
      cy.get(':nth-child(5) > .today > div').click()
      cy.get('.el-textarea__inner').type('테스트입니다.')
      /* 등록 버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertReq').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('update', ()=> {
      cy.intercept('POST', '**/opermgmt/requiremgmt/updateReq.json').as('updateReq')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailReq')
      cy.get('.popupContentTableBottom > :nth-child(2)').click()
      cy.wait('@updateReq').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', ()=> {
      cy.intercept('POST', '**/opermgmt/requiremgmt/deleteReq.json').as('deleteReq')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailReq')
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteReq').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})