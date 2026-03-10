describe('tacsLinkInfoMng Fuctionality', () => {
  context('tacsLinkInfoMng View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/tacsmgmt/viewTacsConnBas.model').as('viewTacsConnBas') /* 목록 */
      cy.visitPath('operInfoMng/tacsMng/tacsLinkInfoMng')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    /*
      mock에서는 테스트 가능함
      local 환경에서 확인불가(개발DB)
      화면 초기 개발시 데이터 확인했으나 환경이 바뀐 것인지 24.12월경부터 컨트롤러 오류
      null StackTrace: AESCryptoUtil.java:83
    */
    it('list lookup', () => {
      cy.wait('@viewTacsConnBas').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const connId = interception.response.body.result.data.connId
        expect(connId).to.exist
      })
    })
  })
})