describe('notice Fuctionality', () => {
  context('notice View List', () => {
    beforeEach(() => {
      cy.visitPath('board/notice')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/boardmgmt/viewListNotice.model').as('viewListNotice') /* 목록 */
      cy.intercept('POST', '**/opermgmt/boardmgmt/viewDetailNotice.model').as('viewDetailNotice') /* 상세 조회 */
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListNotice').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('insert', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/boardmgmt/insertNotice.json').as('insertNotice') /* 등록 */
      cy.get('.add-features > .el-button').click()
      /* 등록 pop 내용 입력 */
      cy.get(':nth-child(2) > td > .el-input > .el-input__inner').type('test')
      cy.get(':nth-child(3) > .textflex > .el-select > .el-input > .el-input__inner').click()
      cy.get('.noticeType > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(1)').click()
      cy.get('textarea').type('content test')
      /* 등록 btn 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      /* 등록 결과 성공여부 확인 */
      cy.wait('@insertNotice').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('detail', () => {
      cy.get('tbody > :nth-child(1) > .el-table_2_column_14').click()
      /* 상세 결과 성공여부 확인 */
      cy.wait('@viewDetailNotice').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/boardmgmt/viewUpdateNotice.model').as('viewUpdateNotice') /* 수정 정보 조회 */
      cy.intercept('POST', '**/opermgmt/boardmgmt/updateNotice.json').as('updateNotice') /* 수정 */
      /* row 클릭 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_14').click()
      cy.wait('@viewDetailNotice')
      /* 수정 버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@viewUpdateNotice').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
      /* 수정 결과 성공여부 확인 */
      cy.wait('@updateNotice').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/boardmgmt/deleteNotice.json').as('deleteNotice') /* 삭제 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_14').click()
      cy.wait('@viewDetailNotice')
      /* 삭제 버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      /* 수정 결과 성공여부 확인 */
      cy.wait('@deleteNotice').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})