describe('Ip Allocation Fuctionality', () => {
  beforeEach(() => {
    cy.intercept('POST', '**/ipmgmt/allocmgmt/viewListIpAllocMst.model').as('viewListIpAllocMst') /* 할당 목록 */
    cy.intercept('POST', '**/ipmgmt/allocmgmt/viewDetailAlcIPMst.model').as('viewDetailAlcIPMst') /* 할당 목록 */
  })
  context('IP Allocation View List', () => {
    beforeEach(() => {
      cy.visitLocal()
      cy.login()
      cy.wait(['@getkey', '@signin', '@setting'])
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAllocationMng/ipAllocation')
      cy.wait(1000)
    })
    it('list lookup', () => {
      /* 조회 버튼 클릭 */ 
      cy.get('.searchBtnWrap > .el-button--primary').click()
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListIpAllocMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
      cy.wait('@viewListIpAllocMst')
      /* 상세버튼 클릭 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_2 > .cell').dblclick()
      // cy.get('tbody > :nth-child(1) > .el-table_2_column_7')
      cy.wait('@viewDetailAlcIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/updateScommentAsgnIPMst.json').as('updateScommentAsgnIPMst')
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
      cy.wait('@viewListIpAllocMst')
      /* 상세버튼 클릭 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_2 > .cell').dblclick()
      cy.wait('@viewDetailAlcIPMst')
      /* 수정버튼 클릭 */
      cy.get('.popupContentTable > .popupContentTableBottom > .el-button').click()
      cy.wait('@updateScommentAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })

    // it('alloc', () => { }) /* 할당 */
    // it('division', () => {}) /* 분할 */
    // it('CheckTacsIpBlock', () => {}) /* IP블럭 중복체크 - 로컬에서 확인불가 */
  })
})