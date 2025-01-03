describe('menuManagement Fuctionality', () => {
  /* 메뉴관리 하위 화면 미개발로 테스트코드 작성 X */
  context('menuManagement View List', () => {
    beforeEach(() => {
      
      cy.intercept('POST', '**/opermgmt/menumgmt/viewListMenuAuth.model').as('viewListMenuAuth') /* 목록 */
      cy.visitPath('operInfoMng/menuMng/menuAuthManagement')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListMenuAuth').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
  })
})