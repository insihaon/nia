describe('rankCodeManagement Fuctionality', () => {
  context('rankCodeManagement View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/orgmgmt/viewListTbLvlCdVo.model').as('viewListTbLvlCdVo') /* 목록 */
      cy.visitPath('organizationalMng/rankCodeManagement')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListTbLvlCdVo').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('insert', () => {
      cy.intercept('POST', '**/opermgmt/orgmgmt/insertTbLvlCdVo.json').as('insertTbLvlCdVo')
      
      cy.wait('@viewListTbLvlCdVo')
      cy.get('.add-features > .el-button').click()
      cy.get(':nth-child(2) > td > .el-input > .el-input__inner').type('test')
      cy.get(':nth-child(3) > td > .el-select > .el-input > .el-input__inner').click()
      cy.get('.sorgOfficeFlagYn > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(1)').click()
      cy.get('.last > td > .el-input > .el-input__inner').type('test')
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertTbLvlCdVo').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})