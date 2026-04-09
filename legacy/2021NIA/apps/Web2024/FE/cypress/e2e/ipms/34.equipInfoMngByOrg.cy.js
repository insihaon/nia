describe('equipInfoMngByOrg Fuctionality', () => {
  /* 운용정보관리 > IP주소 라우팅 비교/점검 > 조직별 장비정보관리 */
  context('equipInfoMngByOrg View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewListFcltMst.model').as('viewListFcltMst') /* 목록 */
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewInsertFcltMst.model').as('viewInsertFcltMst') /* 장비 타입 조회 */
      cy.visitPath('operInfoMng/ipAdressRoutingCompare/equipInfoMngByOrg')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListFcltMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    // 등록 데이터를 위한 element cy.get 불가
    // it('insert', () => { })
    it('detail', () => {
      cy.wait('@viewListFcltMst')
      cy.get('.el-pager > :nth-child(3)').click()
      cy.get('#element-table tr').eq(3).click()
      cy.wait('@viewInsertFcltMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/updateFcltMst.json').as('updateFcltMst')
      // table row 클릭 후 수정 버튼 클릭
      cy.wait('@viewListFcltMst')
      cy.get('.el-pager > :nth-child(3)').click()
      cy.get('#element-table tr').eq(3).click()
      cy.wait('@viewInsertFcltMst')
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateFcltMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/deleteFcltMst.json').as('deleteFcltMst')
      cy.wait('@viewListFcltMst')
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteFcltMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})