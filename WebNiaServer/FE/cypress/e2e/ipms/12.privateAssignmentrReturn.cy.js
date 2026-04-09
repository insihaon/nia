describe('privateAssignmentrReturn Fuctionality', () => {
  context('privateAssignmentrReturn View List', () => {
    beforeEach(() => {
      cy.visitPath('board/privateAssignmentrReturn')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/asmgmt/viewListPrivateAs.model').as('viewListPrivateAs') /* 목록 */
      cy.intercept('POST', '**/opermgmt/asmgmt/viewDetailPrivateAs.model').as('viewDetailPrivateAs') /* 상세 */
      cy.intercept('POST', '**/opermgmt/asmgmt/updateNrequestAsSeqYn.json').as('updateNrequestAsSeqYn')
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListPrivateAs').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('insert', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/opermgmt/asmgmt/insertPrivateAs.json').as('insertPrivateAs') /* 등록 */
      cy.get('.add-features > .el-button').click()

      /* 내용입력 */
      cy.get(':nth-child(1) > table > tbody > tr > td > .el-input > .el-input__inner').type('테스트')
      cy.get(':nth-child(2) > table > tbody > :nth-child(1) > :nth-child(2) > .el-input > .el-input__inner').type('0')
      cy.get(':nth-child(2) > table > tbody > :nth-child(1) > :nth-child(3) > .el-input > .el-input__inner').type('1')
      cy.get(':nth-child(2) > table > tbody > :nth-child(2) > :nth-child(2) > .el-input > .el-input__inner').type('2')
      cy.get(':nth-child(2) > table > tbody > :nth-child(2) > :nth-child(3) > .el-input > .el-input__inner').type('3')

      cy.get(':nth-child(2) > .el-date-editor > .el-input__inner').click()
      cy.get('.nodeOnePicker > .el-picker-panel__body-wrapper > .el-picker-panel__body > .el-picker-panel__content > .el-date-table > tbody > :nth-child(5) > .today > div').click()

      cy.get(':nth-child(3) > .el-date-editor > .el-input__inner').click()
      cy.get('.nodeTwoPicker > .el-picker-panel__body-wrapper > .el-picker-panel__body > .el-picker-panel__content > .el-date-table > tbody > :nth-child(5) > .today > div').click()

      cy.get(':nth-child(4) > :nth-child(2) > .el-input > .el-input__inner').type('4')
      cy.get(':nth-child(4) > :nth-child(3) > .el-input > .el-input__inner').type('5')

      cy.get('textarea').type('test')

      cy.get(':nth-child(3) > table > tbody > tr > :nth-child(2) > .el-input > .el-input__inner').type('6')
      cy.get(':nth-child(4) > .el-input > .el-input__inner').type('7')
      cy.get(':nth-child(4) > table > tbody > :nth-child(1) > :nth-child(2) > .el-input > .el-input__inner').type('8')
      cy.get(':nth-child(4) > table > tbody > :nth-child(1) > :nth-child(3) > .el-input > .el-input__inner').type('9')
      cy.get(':nth-child(4) > table > tbody > :nth-child(2) > :nth-child(2) > .el-input > .el-input__inner').type('10')
      cy.get(':nth-child(4) > table > tbody > :nth-child(2) > :nth-child(3) > .el-input > .el-input__inner').type('11')

      // 등록 버튼 클릭
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('insertPrivateAs').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        // expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('detail', () => {
      cy.get('#element-table tr').eq(1).click()
      /* 상세 결과 성공여부 확인 */
      cy.wait('@viewDetailPrivateAs').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/opermgmt/asmgmt/updatePrivateAs.json').as('updatePrivateAs')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailPrivateAs')
      cy.get('.popupContentTableBottom > :nth-child(4)').then(($el) => {
        if($el.text().trim() !== '수정') {
          return
        }
        cy.wrap($el).click() /* 수정화면이동 */
        cy.get('.popupContentTableBottom > :nth-child(1)').click()
        cy.wait('@updatePrivateAs').then((interception) => {
          expect(interception.response.statusCode).to.equal(200)
          expect(interception.response.body.result.commonMsg).to.equal('SUCCESS')
        })
      })
    })
    it('delete', () => {
      cy.intercept('POST', '**/opermgmt/asmgmt/deletePrivateAs.json').as('deletePrivateAs')
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailPrivateAs')
      cy.get('.popupContentTableBottom > :nth-child(3)').then(($el) => {
        if($el.text().trim() !== '신청취소') {
          return
        }
        cy.wrap($el).click()
        cy.get('.el-message-box__btns > .el-button--primary').click()
        cy.wait('@deletePrivateAs').then((interception) => {
          expect(interception.response.statusCode).to.equal(200)
          expect(interception.response.body.result.commonMsg).to.equal('SUCCESS')
        })
      })
    }) 
    it('alloc', () => {
      cy.intercept('POST', '**/opermgmt/asmgmt/selectMinNrequestAsSeq.json').as('selectMinNrequestAsSeq')
      
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailPrivateAs')
      cy.get('.popupContentTableBottom > :nth-child(1)').then(($el) => {
        if($el.text().trim() !== '할당') {
          return
        }
        cy.wrap($el).click()
        cy.get('.el-message-box__btns > .el-button--primary').click()
        /* as번호 조회 */
        cy.wait('@selectMinNrequestAsSeq').then((interception) => {
          expect(interception.response.body.result.commonMsg).to.equal('SUCCESS')
        })
        cy.wait('@updateNrequestAsSeqYn').then((interception) => {
          expect(interception.response.statusCode).to.equal(200)
          expect(interception.response.body.result.commonMsg).to.equal('SUCCESS')
        })
      })
    }) /* 할당 */
    it('reject', () => {
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailPrivateAs')
      cy.get('.popupContentTableBottom > :nth-child(2)').then(($el) => {
        if($el.text().trim() !== '반려') {
          return
        }
        cy.wrap($el).click()
        cy.get('.el-message-box__btns > .el-button--primary').click()
        cy.wait('@updateNrequestAsSeqYn').then((interception) => {
          expect(interception.response.statusCode).to.equal(200)
          expect(interception.response.body.result.commonMsg).to.equal('SUCCESS')
        })
      })
    })
    it('return', () => {
      cy.get('#element-table tr').eq(1).click()
      cy.wait('@viewDetailPrivateAs')
      cy.get('.popupContentTableBottom > :nth-child(3)').then(($el) => {
        if($el.text().trim() !== '반납') {
          return
        }
        cy.wrap($el).click()
        cy.get('.el-message-box__btns > .el-button--primary').click()
        cy.wait('@updateNrequestAsSeqYn').then((interception) => {
          expect(interception.response.statusCode).to.equal(200)
          expect(interception.response.body.result.commonMsg).to.equal('SUCCESS')
        })
      })
    })
  })
})