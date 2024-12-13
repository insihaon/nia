describe('IP AdressNodeApplyTransfer Fuctionality', () => {
  context('IP AdressNodeApplyTransfer View List', () => {
    beforeEach(() => {
      cy.visitPath('board/ipAdressNodeApplyTransfer')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/nodemgmt/viewListNode.model').as('viewListNode') /* 목록 */
      cy.intercept('POST', '**/opermgmt/nodemgmt/viewDetailNode.model').as('viewDetailNode') /* 상세 조회 */
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListNode').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('insert', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/nodemgmt/selectListIpAssignMst.model').as('selectListIpAssignMst') /* IP주소 조회 */
      cy.intercept('POST', '**/opermgmt/nodemgmt/insertNode.json').as('insertNode') /* 등록 */
      cy.get('.add-features > .el-button').click()
      /* IP주소 조회 */
      cy.get(':nth-child(2) > .el-input__inner').type('1.100')
      cy.get('tr > :nth-child(3) > .el-button').click()
      /* IP조회 성공여부 확인 */
      cy.wait('@selectListIpAssignMst').then((interception) => {
        // cy.log('selectListIpAssignMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
      /*IP주소 선택 */
      /* 변경 후 계위 정보 입력 */
      cy.get('.flex > .el-select > .el-input > .el-input__inner').click()
      cy.get('.ssvcLineTypeCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()

      cy.get(':nth-child(4) > table > tr > :nth-child(4) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.ssvcGroupCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()

      cy.get(':nth-child(4) > table > tr > :nth-child(6) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.ssvcObjCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      /* 변경 사유 입력 */
      cy.get('tbody > :nth-child(1) > td > .el-select > .el-input > .el-input__inner').click()
      cy.get('.reason > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get('textarea').type('test')

      /* 등록 버튼 선택 */
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      /* 등록 결과 성공여부 확인 */
      cy.wait('@insertNode').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('detail', () => {
      cy.get('tbody > :nth-child(1) > .el-table_2_column_13').click()
      /* 상세 결과 성공여부 확인 */
      cy.wait('@viewDetailNode').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('delete', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/nodemgmt/viewDeleteNode.json').as('viewDeleteNode') /* 신청취소 */
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
    // it('cencel', () => {}) /* 반려 */
    // it('confirm', () => {}) /* 승인 */
  })
})