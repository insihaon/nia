describe('whoisInfoOpenMng Fuctionality', () => {
  context('whoisInfoOpenMng View List', () => {
    beforeEach(() => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewListWhois.model').as('viewListWhois') /* 목록 */
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewRegWhoisNew.model').as('viewRegWhoisNew') /* 상세 */
      cy.visitPath('operInfoMng/whoIs/whoisInfoOpenMng')
      cy.get('.searchBtnWrap > .el-button--primary').click()
    })
    afterEach(() => {
      cy.wait(1000)
    })
    it('list lookup', () => {
      cy.wait('@viewListWhois').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('detail', () => {
      cy.wait('@viewListWhois')
      cy.get('#element-table tr').eq(1).dblclick()
      cy.wait('@viewRegWhoisNew').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.resultVo).to.exist
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/updateWhoisComplexNew.json').as('updateWhoisComplexNew') /* 반영(whois전송) */
      cy.wait('@viewListWhois')
      cy.get('#element-table tr').eq(1).dblclick()
      cy.wait('@viewRegWhoisNew') 
      cy.get('.el-dialog__body > :nth-child(1) > :nth-child(8) > :nth-child(1)').click()
      /* 
      04 :성공
      03: 반송
      else: 실패
      */
      cy.wait('@updateWhoisComplexNew').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.tbWhoisVo.swhoisresultCd).to.equal('04')
      })
    })
    /* 이용기관관리 */
    it('user org management', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewListWhoisKeywordMst.model').as('viewListWhoisKeywordMst')
      cy.intercept('POST', '**/opermgmt/whoismgmt/insertWhoisKeyword.json').as('insertWhoisKeyword')
      cy.wait('@viewListWhois')
      cy.get('.add-features > :nth-child(1)').click()
      cy.wait('@viewListWhoisKeywordMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.get('.ModalInsertKeyword > .el-dialog > .el-dialog__body > :nth-child(1) > .popupContentTable > table > tr > :nth-child(2) > :nth-child(1) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.suserorggb > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(1)').click()
      cy.get('.ModalInsertKeyword > .el-dialog > .el-dialog__body > :nth-child(1) > .popupContentTable > table > tr > :nth-child(4) > :nth-child(1) > .el-input > .el-input__inner').type('TEST')
      cy.get('.popupContentTableBottom > .el-icon-check').click()
      cy.wait('@insertWhoisKeyword').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* 대체 키워드 관리 */
    it('keyword management', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewListWhoisKeywordMstNew.model').as('viewListWhoisKeywordMstNew')
      cy.intercept('POST', '**/opermgmt/whoismgmt/insertWhoisKeywordNew.json').as('insertWhoisKeywordNew')
      cy.intercept('POST', '**/opermgmt/whoismgmt/deleteWhoisKeywordNew.json').as('deleteWhoisKeywordNew')
      cy.wait('@viewListWhois')

      cy.get('.add-features > :nth-child(2)').click()
      cy.wait('@viewListWhoisKeywordMstNew').then((interception) => {
        expect(interception.response.body.result.data).to.exist
      })
      /* 등록 */
      cy.get(':nth-child(3) > tr > :nth-child(2) > .el-input > .el-input__inner').type('TEST')
      // cy.get(':nth-child(4) > .el-select > .el-input > .el-input__inner').click()
      cy.get(':nth-child(5) > .el-button').click()
      cy.wait('@insertWhoisKeywordNew').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
      cy.get('.el-message-box__btns > .el-button').click()
      /* 삭제 */
      cy.get(':nth-child(1) > tr > :nth-child(2) > .el-input > .el-input__inner').type('TEST')
      cy.get('tr > :nth-child(3) > .el-button').click()
      cy.get('.el-dialog__body > .el-col > .compTable tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@deleteWhoisKeywordNew').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* KT대체 정보 관리 */
    it('kt info management', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewUpdateKtInfo.model').as('viewUpdateKtInfo')
      cy.intercept('POST', '**/opermgmt/whoismgmt/updateKtInfo.json').as('updateKtInfo')
      cy.get('.add-features > :nth-child(3)').click()
      cy.wait('@viewUpdateKtInfo').then((interception) => {
        expect(interception.response.body.result.data).to.exist
      })
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateKtInfo').then((interception) => {
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* DB 현행화 전송 */
    it('db transmit current currency', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/viewListWhoisDbMatchMst.model').as('viewListWhoisDbMatchMst')
      cy.intercept('POST', '**/opermgmt/whoismgmt/dbMatchListTbWhoisVo.json').as('dbMatchListTbWhoisVo')
      cy.get('.add-features > :nth-child(4)').click()
      cy.get(':nth-child(11) > .el-button').click()
      cy.wait('@viewListWhoisDbMatchMst').then((interception) => {
        expect(interception.response.body.result.data).to.exist
      })
      cy.get('.el-dialog__body > .el-col > .compTable tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@dbMatchListTbWhoisVo').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        // expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/whoismgmt/deleteTbWhoisVo.json').as('deleteTbWhoisVo')
      cy.wait('@viewListWhois')
      cy.get('.searchOptionWrap > table').find('td').find('.ApplyStatus').then(($el) => {
        if($el.length > 0) {
          cy.wrap($el[0]).click()
        }
       })
       cy.get('.RegistrationStatus > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(4)').click()
       cy.get('.searchBtnWrap > .el-button--primary').click()
       
       cy.get('#element-table tr').eq(4).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
        cy.get('.add-features > :nth-child(5)').click()
        cy.wait('@deleteTbWhoisVo').then((interception) => {
          expect(interception.response.body.commonMsg).to.equal('SUCCESS')
        })
      })
    })
  })
})