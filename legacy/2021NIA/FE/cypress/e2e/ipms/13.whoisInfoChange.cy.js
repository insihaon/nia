describe('WhoisInfoChange Fuctionality', () => {
  context('WhoisInfoChange View List', () => {
    beforeEach(() => {
      cy.visitPath('board/whoisInfoChange')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/asmgmt/viewListPrivateAs.model').as('viewListPrivateAs') /* 목록 */
      cy.intercept('POST', '**/opermgmt/asmgmt/viewDetailPrivateAs.model').as('viewDetailPrivateAs') /* 상세 */
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    // it('list lookup', () => {
    //   /* 조회 결과 성공여부 확인 */
    //   cy.wait('@viewListPrivateAs').then((interception) => {
    //     // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
    //     expect(interception.response.statusCode).to.equal(200)
    //     const responseData = interception.response.body.result.data
    //     expect(responseData).to.exist
    //   })
    // })
    it('insert', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/asmgmt/insertPrivateAs.json').as('insertPrivateAs') /* 등록 */
      cy.get('.add-features > .el-button').click()

      /* 내용입력 */
      cy.get(':nth-child(1) > table > tbody > tr > td > .el-input > .el-input__inner').type('테스트')
      cy.get(':nth-child(2) > table > tbody > :nth-child(1) > :nth-child(2) > .el-input > .el-input__inner').type('0')
      cy.get(':nth-child(2) > table > tbody > :nth-child(1) > :nth-child(3) > .el-input > .el-input__inner').type('1')
      cy.get(':nth-child(2) > table > tbody > :nth-child(2) > :nth-child(2) > .el-input > .el-input__inner').type('2')
      cy.get(':nth-child(2) > table > tbody > :nth-child(2) > :nth-child(3) > .el-input > .el-input__inner').type('3')

      cy.get(':nth-child(2) > .el-date-editor > .el-input__inner').click()
      cy.get(':nth-child(5) > :nth-child(4) > div').click()

      cy.get(':nth-child(3) > .el-date-editor > .el-input__inner').click()
      cy.get('.el-zoom-in-top-enter-active > .el-picker-panel__body-wrapper > .el-picker-panel__body > .el-picker-panel__content > .el-date-table > tbody > :nth-child(5) > :nth-child(4) > div').click()

      cy.get(':nth-child(4) > :nth-child(2) > .el-input > .el-input__inner').type('4')
      cy.get(':nth-child(4) > :nth-child(3) > .el-input > .el-input__inner').type('5')

      cy.get('textarea').type('test')

      cy.get(':nth-child(3) > table > tbody > tr > :nth-child(2) > .el-input > .el-input__inner').type('6')
      cy.get(':nth-child(4) > .el-input > .el-input__inner').type('7')
      cy.get(':nth-child(4) > table > tbody > :nth-child(1) > :nth-child(2) > .el-input > .el-input__inner').type('8')
      cy.get(':nth-child(4) > table > tbody > :nth-child(1) > :nth-child(3) > .el-input > .el-input__inner').type('9')
      cy.get(':nth-child(4) > table > tbody > :nth-child(2) > :nth-child(2) > .el-input > .el-input__inner').type('10')
      cy.get(':nth-child(4) > table > tbody > :nth-child(2) > :nth-child(3) > .el-input > .el-input__inner').type('11')

      // 등록 버튼 클릭
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('insertPrivateAs').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    // it('detail', () => {
    //   cy.get('tbody > :nth-child(1) > .el-table_2_column_13').click()
    //   /* 상세 결과 성공여부 확인 */
    //   cy.wait('@viewDetailPrivateAs').then((interception) => {
    //     // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
    //     expect(interception.response.statusCode).to.equal(200)
    //     const responseData = interception.response.body.result.data
    //     expect(responseData).to.exist
    //   })
    // })
    // it('update', () => {}) /* 수정 */
    // it('return', () => {}) /* 반납 */
    // it('delete', () => {}) /* 신청취소 */
    // it('alloc', () => {}) /* 할당 */
    // it('reject', () => {}) /* 반려 */
  })
})