describe('menuManagement Fuctionality', () => {
  /* 메뉴관리 하위 화면 미개발로 테스트코드 작성 X */
  context('menuManagement View List', () => {
    beforeEach(() => {
      cy.visitPath('operInfoMng/menuMng/menuManagement')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
    })
  })
})