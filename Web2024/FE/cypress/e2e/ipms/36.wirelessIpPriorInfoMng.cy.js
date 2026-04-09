describe('wirelessIpPriorInfoMng Fuctionality', () => {
  context('wirelessIpPriorInfoMng View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewListMobileMst.model').as('viewListMobileMst') /* 목록 */
      cy.visitPath('operInfoMng/ipAdressRoutingCompare/wirelessIpPriorInfoMng')
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListMobileMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    /* 무선전체라우팅수집 */
    it('Wireless Routing Collection', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/insertListRoutChkMst.json').as('insertListRoutChkMst')
      cy.wait('@viewListMobileMst')
      cy.get('.add-features > :nth-child(1)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@insertListRoutChkMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        // expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* summary 관리 */
    it('Summary Management', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/viewPopSummaryMst.model').as('viewPopSummaryMst')
      cy.intercept('POST', '**/opermgmt/intgrmgmt/insertMobileSummMst.json').as('insertMobileSummMst')
      cy.intercept('POST', '**/opermgmt/intgrmgmt/deleteMobileSummMst.json').as('deleteMobileSummMst')
      cy.wait('@viewListMobileMst')
      cy.get('.add-features > :nth-child(2)').click()
      /* 조회결과 */
      cy.wait('@viewPopSummaryMst').then((interception) => {
        expect(interception.response.body.result.data).to.exist
      })
      /* 등록 */
      cy.get(':nth-child(2) > table > tbody > tr > :nth-child(4) > .el-input > .el-input__inner').type('1.1.1.1')
      cy.get(':nth-child(2) > table > tbody > tr > :nth-child(5) > .el-button').click()
      cy.wait('@insertMobileSummMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
      cy.get('.el-message-box__btns > .el-button').click()
      /* 등록된 데이터 확인 */
      cy.get(':nth-child(1) > table > tbody > tr > :nth-child(4) > .el-input > .el-input__inner').type('1.1.1.1')
      cy.get(':nth-child(1) > table > tbody > tr > :nth-child(5) > .el-button').click()
      cy.wait('@viewPopSummaryMst')
      cy.get(':nth-child(3) > .compTable tr').eq(1).find('td').eq(2).should('contain.text', '1.1.1.1')
      /* 삭제 */
      cy.get(':nth-child(3) > .compTable tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteMobileSummMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    // 텍스트 파일 업로드
    // community 개별 등록
    it('insert', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/insertMobileMst.json').as('insertMobileMst')
      cy.wait('@viewListMobileMst')
      cy.get('.add-features > :nth-child(4)').click()
      /* form 입력 */
      cy.get('td > .el-select > .el-input > .el-input__inner').click()
      cy.get('.skindCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get(':nth-child(2) > :nth-child(2) > .el-input > .el-input__inner').type('TEST')
      cy.get('.txt > .el-input__inner').type('TEST')
      cy.get('.popupContentTableBottom > :nth-child(1)').click()

      cy.wait('@insertMobileMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/updateMobileMst.json').as('updateMobileMst')
      cy.wait('@viewListMobileMst')
      cy.get('#element-table tr').eq(1).click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateMobileMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/intgrmgmt/deleteMobileMst.json').as('deleteMobileMst')
      cy.wait('@viewListMobileMst')
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(5)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteMobileMst').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})