describe('uploadMng Fuctionality', () => {
  context('Upload View List', () => {
    beforeEach(() => {
      cy.visitPath('dbMng/uploadMng')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/ipuploadmgmt/viewIpUploadMst.model').as('viewIpUploadMst') /* 목록 */
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewIpUploadMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/ipuploadmgmt/viewDetailIpMst.model').as('viewDetailIpMst') /* 조회 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_13 > .cell').click()
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewDetailIpMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    /* 엑셀 다운로드 테스트는 KT-IS망에서 가능함 */
    // it('text Download', () => {
    //   /* POST 요청 정의 */
    //   cy.intercept('POST', '**/ipmgmt/ipuploadmgmt/downloadtextformat.json').as('downloadtextformat') /* 조회 */
    //   cy.get('.d-flex > div > .el-button').click()
    //   /* 업로드 pop 텍스트 양식 다운로드 버튼 클릭 */
    //   cy.get('.popupContentTableBottom > :nth-child(1)').click()
    //   cy.wait('@downloadtextformat').then((interception) => {
    //     expect(interception.response.statusCode).to.equal(200)
    //   })
    // })
    // it('excel Download', () => {
    //   /* POST 요청 정의 */
    //   cy.intercept('POST', '**/ipmgmt/ipuploadmgmt/downloadformat.json').as('downloadformat') /* 조회 */
    //   cy.get('.d-flex > div > .el-button').click()
    //   /* 계위 선택 */
    //   cy.get(':nth-child(1) > :nth-child(1) > .el-input > .el-input__inner').click()
    //   cy.get('.ssvcLineTypeCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()

    //   cy.get(':nth-child(2) > .el-input > .el-input__inner').click()
    //   cy.get('.ssvcGroupCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(8)').click()

    //   cy.get(':nth-child(3) > .el-input > .el-input__inner').click()
    //   cy.get('.ssvcObjCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(3)').click()

    //   /* 업로드 pop 엑셀 양식 다운로드 버튼 클릭 */
    //   cy.get('.popupContentTableBottom > :nth-child(2)').click()
    //   cy.wait('@downloadformat').then((interception) => {
    //     expect(interception.response.statusCode).to.equal(200)
    //   })
    // })
  })
})