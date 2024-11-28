describe('IpunAllocatedStatus Fuctionality', () => {
  beforeEach(() => {
    cy.visitPath('ipAssignMng/ipunAllocatedStatus')
    /* POST 요청 정의 */
    cy.intercept('POST', '**/ipmgmt/assignmgmt/viewListUnAssignIP.model').as('viewListUnAssignIP') /* 미배정 목록 */
    cy.intercept('POST', '**/ipmgmt/assignmgmt/viewDetailUnAssignIP.model').as('viewDetailUnAssignIP') /* 미배정 상세 */
  })
  context('IpunAllocated Status View List', () => {
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      cy.wait('@viewListUnAssignIP').then((interception) => {
        // cy.log('response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('unassign detail', () => {
      cy.wait('@viewListUnAssignIP')
      /* 미배정 row 클릭 */
      cy.get(':nth-child(1) > .el-table_2_column_14 > .cell > .el-button').click()
      cy.wait('@viewDetailUnAssignIP').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData.length).to.be.greaterThan(0)
      })
    })
    it('preUnassign detail', () => {
      cy.wait('@viewListUnAssignIP')
      /* 예비배정 row 클릭 */
      cy.get(':nth-child(1) > .el-table_2_column_15 > .cell > .el-button').click()
      cy.wait('@viewDetailUnAssignIP').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData.length).to.be.greaterThan(0)
      })
    })
  })
})