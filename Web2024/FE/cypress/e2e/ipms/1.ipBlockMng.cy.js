describe('Ip Block Management Fuctionality', () => {
  beforeEach(() => {
     /* POST 요청 정의 */
     cy.intercept('POST', '**/ipmgmt/createmgmt/viewDetailCrtIPMst.model').as('viewDetailCrtIPMst') /* 상세 */
  })
  context('IP Block Management View List', () => {
    beforeEach(() => {
      cy.visitLocal()
      cy.login()
      cy.wait(['@getkey', '@signin', '@setting'])
      cy.visit('http://localhost:4000/#/ipAssignMng/ipBlockManagement')
    })
    it('list lookup', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/createmgmt/viewListCrtIPMst.model').as('viewListCrtIPMst')

      /* assert case 1. error */
      // cy.wait('@viewListCrtIPMst').its('response.body.data.result.data').should('exist').and('not.be.empty')

      /* assert case 2. */
      cy.wait('@viewListCrtIPMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    /* 신규생성, 추가생성 테스트가 가능한 IP ? */
    // it('create', () => {
    // })

    it('detail', () => {
     
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAssignMng/ipBlockManagement')
      cy.wait(1000)
      /* 상세버튼 클릭 */
      cy.get('.add-features > :nth-child(3)').click()
      cy.wait('@viewDetailCrtIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/createmgmt/updateCrtIPMst.json').as('updateCrtIPMst')
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAssignMng/ipBlockManagement')
      cy.wait(1000)
      /* 목록 > 수정버튼 클릭 */
      cy.get('.add-features > :nth-child(4)').click()
      cy.get('.el-dialog__title').contains('수정')

      /* 수정dialog 저장버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateCrtIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })
   
    it('delete', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/createmgmt/deleteCrtIPMst.json').as('deleteCrtIPMst') /* 삭제 */
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAssignMng/ipBlockManagement')
      cy.wait(1000)
      /* 상세버튼 클릭 */
      cy.get('.add-features > :nth-child(3)').click()
      cy.wait('@viewDetailCrtIPMst')
      /* 삭제버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(2)').click()
      cy.wait(500)
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteCrtIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        /* 배정된 내역 있을 경우 삭제 불가함 */
        // const commonMsg = interception.response.body.commonMsg
        // expect(commonMsg).to.equal('SUCCESS')
      })
    })
  })
})