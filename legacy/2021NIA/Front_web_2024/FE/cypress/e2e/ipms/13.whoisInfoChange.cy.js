describe('WhoisInfoChange Fuctionality', () => {
  context('WhoisInfoChange View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewListWhoisModReq.model').as('viewListWhoisModReq') /* 목록 */
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewDetailWhoisModReq.model').as('viewDetailWhoisModReq') /* 상세 */
      cy.intercept('POST', '**/opermgmt/whoismgmt/updateWhoisModReqAppr.json').as('updateWhoisModReqAppr') /* 승인, 반려 */
      cy.visitPath('board/whoisInfoChange')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListWhoisModReq').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('insert', () => { // 로컬 및 개발환경에서 연동 불가로 테스트 불가능
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewRegWhoisModReq.json').as('viewRegWhoisModReq') /* 등록 */
      cy.intercept('POST', '**/opermgmt/uploadmgmt/insertRegWhoisModReq.json').as('insertRegWhoisModReq') /* 변경신청 */
      cy.get('.add-features > .el-button').click()

      /* ip조회 */
      cy.get(':nth-child(1) > table > tbody > tr > :nth-child(2) > .el-input > .el-input__inner').type('211.107')
      cy.get(':nth-child(3) > .el-button').click()

      cy.get('tbody > :nth-child(1) > :nth-child(1) > .el-button').click()
      cy.get('.el-message-box__btns > .el-button').click()
      /* 내용입력 */
      
      // 변경신청 버튼 클릭
      cy.get('.el-dialog__body > :nth-child(1) > :nth-child(6) > :nth-child(1)').click()
      // cy.wait('insertRegWhoisModReq').then((interception) => {
      //   expect(interception.response.statusCode).to.equal(200)
      //   expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      // })
    })
    it('detail', () => {
      cy.get('#element-table tr').eq(1).click()
      /* 상세 결과 성공여부 확인 */
      cy.wait('@viewDetailWhoisModReq').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewUpdateWhoisModReqVo.json').as('viewUpdateWhoisModReqVo')
      cy.wait('@viewListWhoisModReq')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailWhoisModReq')
      /* 수정버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(4)').click()
      /* 저장버튼 클릭 */
      cy.get('.el-dialog__body > :nth-child(5) > :nth-child(4)').click()
      cy.wait('@viewUpdateWhoisModReqVo').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.tbWhoisModifyVo.commonMsg).to.equal('SUCCESS')
      })
    })
    it('alloc', () => {
      cy.wait('@viewListWhoisModReq')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailWhoisModReq')
      /* 승인 버튼 클릭 */
      cy.get('.el-dialog__body > :nth-child(5) > :nth-child(1)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@updateWhoisModReqAppr').then((interception)=> {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.tbWhoisModifyVo.commonMsg).to.equal('SUCCESS')
      })
    })
    it('reject', () => {
      cy.wait('@viewListWhoisModReq')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailWhoisModReq')
      cy.get('textarea').type('reject test')
      /* 반려 버튼 클릭 */
      cy.get('.el-dialog__body > :nth-child(5) > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@updateWhoisModReqAppr').then((interception)=> {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.tbWhoisModifyVo.commonMsg).to.equal('SUCCESS')
      })
    }) /* 반려 */
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewDeleteWhoisModReq.json').as('viewDeleteWhoisModReq')
      cy.wait('@viewListWhoisModReq')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailWhoisModReq')
      cy.get('.el-dialog__body > :nth-child(5) > :nth-child(3)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@viewDeleteWhoisModReq').then((interception)=> {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.tbWhoisModifyVo.commonMsg).to.equal('SUCCESS')
      })
    }) /* 변경신청취소 */
  })
})