describe('IP AdressNodeApplyTransfer Fuctionality', () => {
  context('IP AdressNodeApplyTransfer View List', () => {
    beforeEach(() => {
      cy.visitPath('board/ipResourceAssignmentApply')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/assignmgmt/viewListAssignApyTxn.model').as('viewListAssignApyTxn') /* 목록 */
      cy.intercept('POST', '**/opermgmt/assignmgmt/viewDetailAssignApyTxn.model').as('viewDetailAssignApyTxn') /* 상세 조회 */
      cy.intercept('POST', '**/opermgmt/assignmgmt/updateAssignApyTxn.json').as('updateAssignApyTxn') /* 배정, 승인, 반송, 처리내용 수정 */
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    // it('list lookup', () => {
    //   /* 조회 결과 성공여부 확인 */
    //   cy.wait('@viewListAssignApyTxn').then((interception) => {
    //     // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
    //     expect(interception.response.statusCode).to.equal(200)
    //     const responseData = interception.response.body.result.data
    //     expect(responseData).to.exist
    //   })
    // })
    // it('insert', () => {
    //   /* POST 요청 정의 */
    //   cy.intercept('POST', '**/opermgmt/assignmgmt/insertAssignApyTxn.json').as('insertAssignApyTxn') /* 등록 */
    //   cy.get('.add-features > .el-button').click()


    //   /* 내용 입력 */
    //   cy.get('tbody > :nth-child(1) > td > .el-input > .el-input__inner').type('test')

    //   cy.get(':nth-child(2) > .el-select > .el-input > .el-input__inner').click()
    //   cy.get('.ssvcLineTypeCdOp > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()

    //   cy.get(':nth-child(2) > :nth-child(4) > .el-select > .el-input > .el-input__inner').click()
    //   cy.get('.ssvcGroupCdOp > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()

    //   cy.get(':nth-child(6) > .el-select > .el-input > .el-input__inner').click()
    //   cy.get('.ssvcObjCdOp > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()

    //   cy.get('.textflex > .el-input > .el-input__inner').type(1)
    //   cy.get('textarea').type('test')

    //   /* 등록 버튼 선택 */
    //   cy.get('.popupContentTableBottom > :nth-child(1)').click()
    //   /* 등록 결과 성공여부 확인 */
    //   cy.wait('@insertAssignApyTxn').then((interception) => {
    //     expect(interception.response.statusCode).to.equal(200)
    //     expect(interception.response.body.commonMsg).to.equal('SUCCESS')
    //   })
    // })
    // it('detail', () => {
    //   cy.get('tbody > :nth-child(1) > .el-table_2_column_13').click()
    //   /* 상세 결과 성공여부 확인 */
    //   cy.wait('@viewDetailAssignApyTxn').then((interception) => {
    //     // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
    //     expect(interception.response.statusCode).to.equal(200)
    //     const responseData = interception.response.body.result.data
    //     expect(responseData).to.exist
    //   })
    // })
    // it('update', () => {
    //   cy.get('tbody > :nth-child(1) > .el-table_2_column_13').click()
    //   cy.get('#txtAssignIpCnt').clear().type('update test')
    //   cy.get('.popupContentTableBottom > :nth-child(1)').click()

    //   /* 수정 결과 성공여부 확인 */
    //   cy.wait('@updateAssignApyTxn').then((interception) => {
    //     expect(interception.response.statusCode).to.equal(200)
    //     expect(interception.response.body.commonMsg).to.equal('SUCCESS')
    //   })
    // })
    // it('assign', () => {}) /* 배정 */
    // it('confirm', () => {}) /* 승인 */
    // it('cancel', () => {}) /* 반송 */
  })
})