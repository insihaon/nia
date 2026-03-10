describe('organizationalStandardsManagement Fuctionality', () => {
  context('organizationalStandardsManagement View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/orgmgmt/viewListOrgBas.model').as('viewListOrgBas') /* 목록 */
      cy.visitPath('board/organizationalStandardsManagement')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListOrgBas').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
  })
})