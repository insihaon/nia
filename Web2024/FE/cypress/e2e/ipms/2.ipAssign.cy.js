describe('Ip Assign Fuctionality', () => {
  beforeEach(() => {
    cy.intercept('POST', '**/ipmgmt/assignmgmt/viewListAsgnIPMst.model').as('viewListAsgnIPMst') /* 배정 목록 */
    cy.intercept('POST', '**/ipmgmt/assignmgmt/viewDetailAsgnIPMst.model').as('viewDetailAsgnIPMst') /* 배정 상세 */
    cy.intercept('POST', '**/ipmgmt/assignmgmt/updateAsgnIPMst.json').as('updateAsgnIPMst') /* 배정, 반납 */
  })
  context('IP Assign View List', () => {
    beforeEach(() => {
      cy.visitLocal()
      cy.login()
      cy.wait(['@getkey', '@signin', '@setting'])
      /* 페이지 이동 */
      cy.visit('http://localhost:4000/#/ipAssignMng/ipAssign')
      cy.wait(1000)
    })
    it('list lookup', () => {
      /* 조회 버튼 클릭 */ 
      cy.get('.searchBtnWrap > .el-button--primary').click()
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListAsgnIPMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
      cy.wait('@viewListAsgnIPMst')
      /* 상세버튼 클릭 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_2 > .cell').dblclick()
      cy.wait('@viewDetailAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/updateScommentAsgnIPMst.json').as('updateScommentAsgnIPMst')

      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
      cy.wait('@viewListAsgnIPMst')
      /* 상세버튼 클릭 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_2 > .cell').dblclick()
      cy.wait('@viewDetailAsgnIPMst')
      /* 수정버튼 클릭 */
      cy.get(':nth-child(1) > .popupContentTableBottom > .el-button').click()
      cy.wait('@updateScommentAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })
    it('assign', () => {

      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
      cy.wait('@viewListAsgnIPMst')

      cy.get('tbody > :nth-child(1) > .el-table_2_column_1 > .cell > .el-checkbox > .el-checkbox__input > .el-checkbox__inner').click()
      cy.get('.add-features > :nth-child(2)').click()
      cy.wait(1000)
      cy.get(':nth-child(1) > :nth-child(4) > .el-select > .el-input > .el-input__inner').click()
      cy.get('[style="min-width: 270px; position: fixed; top: 280px; left: 1200px; transform-origin: center top; z-index: 2003;"] > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@updateAsgnIPMst').then((interception) => {
          expect(interception.response.statusCode).to.equal(200)
          const commonMsg = interception.response.body.commonMsg
          expect(commonMsg).to.equal('SUCCESS')
      })
      cy.get('.el-message-box__btns > .el-button').click()
    })
    it('return', () => {
      /* 조건 선택 (배정된 데이터) */
      cy.get('.IpBlockStatus > .el-select > .el-input > .el-input__inner').click();
      cy.wait(500)
      cy.get('[style="min-width: 362.859px; position: absolute; top: 210px; left: 883px; transform-origin: center top; z-index: 2001;"] > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(4) > span').click()
      /* 조회 버튼 클릭 */
      cy.get('.searchBtnWrap > .el-button--primary').click()
      cy.wait('@viewListAsgnIPMst')
      /* 상세 버튼 클릭 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_2 > .cell').dblclick()
      cy.wait('@viewDetailAsgnIPMst')
      /* 반납 버튼 클릭 */
      cy.get('.el-dialog__body > :nth-child(3) > :nth-child(2)').click()
      cy.wait('@updateAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
      cy.get('.el-message-box__btns > .el-button').click()
    })
    // it('merge', () => {}) /* 병합 */
    // it('division', () => {}) /* 분할 */
    // it('CheckTacsIpBlock', () => {}) /* IP블럭 중복체크 - 로컬에서 확인불가 */
  })
})