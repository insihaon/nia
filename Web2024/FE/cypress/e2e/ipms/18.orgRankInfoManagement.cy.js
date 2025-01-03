describe('orgRankInfoManagement Fuctionality', () => {
  context('orgRankInfoManagement View List', () => {
    beforeEach(() => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/orgmgmt/viewListLvlBas.model').as('viewListLvlBas') /* 목록 */
      cy.intercept('POST', '**/opermgmt/orgmgmt/selectSearchLvlCd.json').as('selectSearchLvlCd') /* 계위관리 > 국사 조회 */
      cy.visitPath('organizationalMng/orgRankInfoManagement')
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListLvlBas').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('insert', () => {
      cy.intercept('POST', '**/opermgmt/orgmgmt/insertTbLvlBas.json').as('insertTbLvlBas')
      cy.wait('@viewListLvlBas')
      cy.get('.add-features > .el-button').click()
      /* 센터/지역본부 */
      cy.get(':nth-child(2) > td > .el-input > .el-input__suffix > .el-input__suffix-inner > .el-button').click()
      cy.get('table > :nth-child(3) > .el-button').click()
      cy.get('.el-dialog__body > .compTable tr').eq(1).dblclick()
      // cy.get('#element-table tr').eq(1).dblclick()
      /* 노드 */
      cy.get(':nth-child(3) > td > .el-input > .el-input__suffix > .el-input__suffix-inner > .el-button').click()
      cy.get('table > :nth-child(3) > .el-button').click()
      cy.get('.el-dialog__body > .compTable tr').eq(1).dblclick()
      /* 주노드 */
      cy.get(':nth-child(4) > td > .el-input > .el-input__suffix > .el-input__suffix-inner > .el-button').click()
      cy.get('table > :nth-child(3) > .el-button').click()
      cy.get('.el-dialog__body > .compTable tr').eq(1).dblclick()

      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertTbLvlBas').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* 계위관리 */
    it('org management', ()=> {
      cy.intercept('POST', '**/opermgmt/orgmgmt/validTbLvlBas.json').as('validTbLvlBas')
      cy.intercept('POST', '**/opermgmt/orgmgmt/updateTbLvlMove.json').as('updateTbLvlMove')

      cy.get('.el-pager > :nth-child(3)').click()
      cy.wait('@viewListLvlnBas')
      cy.get('#element-table tr').eq(2).find('td').eq(4).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.wait(500)
      /* 계위 선택 */
      cy.get('.top > td > .el-select > .el-input > .el-input__inner').click()
      cy.get('._ssvcLineTypeCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get(':nth-child(2) > td > .el-select > .el-input > .el-input__inner').click()
      cy.get('._ssvcGroupCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      
      cy.get(':nth-child(3) > td > .el-select > .el-input > .el-input__inner').click()
      cy.get('._ssvcObjCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()

      cy.get('.dialog-footer > :nth-child(1)').click()
      cy.wait('@validTbLvlBas').then((interception) => {
        if(interception.response.body.commonMsg !== 'SUCCESS') {
          cy.wait('@updateTbLvlMove').then((interception2) => {
            expect(interception2.response.statusCode).to.equal(200)
            // expect(interception2.response.body.commonMsg).to.equal('SUCCESS')
          })
        }
      })
    })
    /* 오더(SON)계위관리(오더 노드국 관리) */
    it('order org management', () => {
      cy.intercept('POST', '**/opermgmt/orgmgmt/viewInsertLvlSonMgmtPop.model').as('viewInsertLvlSonMgmtPop')
      cy.intercept('POST', '**/opermgmt/orgmgmt/selectSloffice.json').as('selectSloffice')
      cy.intercept('POST', '**/opermgmt/orgmgmt/insertTbLvlSubCd.json').as('insertTbLvlSubCd')
      cy.intercept('POST', '**/opermgmt/orgmgmt/deleteTbLvlSubCd.json').as('deleteTbLvlSubCd')
      
      cy.get('.el-pager > :nth-child(3)').click()
      cy.wait('@viewListLvlBas')
      cy.get('#element-table tr').eq(2).find('td').eq(6).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      /* 노드국 관리 > 등록된 노드국 조회 */
      cy.wait('@viewInsertLvlSonMgmtPop').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
      /* 노드국 관리 > 등록 */
      cy.get('.el-input__suffix-inner > .el-button').click()
      cy.get('table > :nth-child(3) > .el-button').click()
      cy.wait('@selectSearchLvlCd')
      cy.get('.el-dialog__body > .compTable tr').eq(1).dblclick()

      cy.get('td > .el-button--primary').click()
      /* 이미 등록된 국사인지 조회 */
      cy.wait('@selectSloffice').then((interception) => {
        if(interception.response.body.commonMsg === 'SUCCESS') {
          /* 노드국 등록 */
          cy.wait('@insertTbLvlSubCd').then((interception2) => {
            expect(interception2.response.statusCode).to.equal(200)
            expect(interception2.response.body.commonMsg).to.equal('SUCCESS')
          })
        }
      })
      cy.get('.el-message-box__btns > .el-button').click()
      /* 노드국 재조회 */
      cy.wait('@viewInsertLvlSonMgmtPop')
      /* 노드국 관리 > 삭제 */
      cy.get('tr > :nth-child(3) > .el-button').click()
      cy.wait('@deleteTbLvlSubCd').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
      cy.wait('@viewInsertLvlSonMgmtPop')
    })

    /* 시설(FM)계위관리(시설 수용국 관리) */
    it('facility org management', () => {
      cy.intercept('POST', '**/opermgmt/orgmgmt/viewInsertLvlRoleSub.model').as('viewInsertLvlRoleSub')
      cy.intercept('POST', '**/opermgmt/orgmgmt/insertLvlRoleSub.json').as('insertLvlRoleSub')
      cy.intercept('POST', '**/opermgmt/orgmgmt/deleteLvlRoleSub.json').as('deleteLvlRoleSub')

      cy.get('.el-pager > :nth-child(3)').click()
      cy.wait('@viewListLvlBas')
      cy.get('#element-table tr').eq(2).find('td').eq(8).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      /* 시설 수용국 관리 > 등록된 수용국 조회 */
      cy.wait('@viewInsertLvlRoleSub').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
      /* 시설 수용국 관리 > 등록 */
      cy.get('.el-input__suffix-inner > .el-button').click()
      cy.get('table > :nth-child(3) > .el-button').click()
      cy.wait('@selectSearchLvlCd')
      cy.get('.el-dialog__body > .compTable tr').eq(1).dblclick()
      cy.get('.textflex > .el-button--primary').click()
      cy.wait('@insertLvlRoleSub').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
      cy.get('.el-message-box__btns > .el-button').click()
      cy.wait('@viewInsertLvlRoleSub')
      /* 시설 수용국 관리 > 삭제 */
      cy.get('tbody > :nth-child(1) > :nth-child(3) > .el-button').click()
      cy.wait('@deleteLvlRoleSub').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
      cy.wait('@viewInsertLvlRoleSub')
    })
  })
})