describe('userConnectStatus Fuctionality', () => {
  context('userConnectStatus View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/usermgmt/viewListTbUserConnHist.model').as('viewListTbUserConnHist') /* 목록 */
      cy.visitPath('operInfoMng/userMng/userConnectStatus')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListTbUserConnHist').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
  })
})