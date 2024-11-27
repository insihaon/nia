describe('IpunAllocatedStatus Fuctionality', () => {
  beforeEach(() => {
     /* POST 요청 정의 */
     cy.intercept('POST', '**/ipmgmt/assignmgmt/viewListUnAssignIP.model').as('viewListUnAssignIP') /* 미배정 목록 */
     cy.intercept('POST', '**/ipmgmt/assignmgmt/viewDetailUnAssignIP.model').as('viewDetailUnAssignIP') /* 미배정 상세 */
  })  
  context('IpunAllocated Status View List', () => {
    beforeEach(() => {
      cy.visitLocal()
      cy.login()
      cy.wait(['@getkey', '@signin', '@setting'])
    })
    it('list lookup', () => {
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAssignMng/ipunAllocatedStatus')
      cy.wait(1000)
      cy.wait('@viewListUnAssignIP').then((interception) => {
        // cy.log('response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('unassign detail', () => {
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAssignMng/ipunAllocatedStatus')
      cy.wait('@viewListUnAssignIP')
      /* 미배정 row 클릭 */
      cy.get(':nth-child(1) > .el-table_2_column_3 > .cell > .el-button').click()
      cy.wait('@viewDetailUnAssignIP').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData.length).to.be.greaterThan(0)
      })
    })
    it('preUnassign detail', () => {
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAssignMng/ipunAllocatedStatus')
      cy.wait('@viewListUnAssignIP')
      /* 예비배정 row 클릭 */
      cy.get(':nth-child(1) > .el-table_2_column_4 > .cell > .el-button').click()
      cy.wait('@viewDetailUnAssignIP').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData.length).to.be.greaterThan(0)
      })
    })
  })
})