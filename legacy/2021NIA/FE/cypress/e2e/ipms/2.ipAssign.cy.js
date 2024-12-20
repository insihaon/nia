describe('Ip Assign Fuctionality', () => {
  beforeEach(() => {
    /* POST 요청 정의 */
    cy.intercept('POST', '**/ipmgmt/assignmgmt/viewListAsgnIPMst.model').as('viewListAsgnIPMst') /* 배정 목록 */
    cy.intercept('POST', '**/ipmgmt/assignmgmt/viewDetailAsgnIPMst.model').as('viewDetailAsgnIPMst') /* 배정 상세 */
    cy.intercept('POST', '**/ipmgmt/assignmgmt/updateAsgnIPMst.json').as('updateAsgnIPMst') /* 배정, 반납 */
    
    cy.visitPath('ipAssignMng/ipAssign')
    
    /* 조회 버튼 클릭 */
    cy.get('.searchBtnWrap > .el-button--primary').then(($btn)=> {
      if($btn.length > 0) {
        cy.wrap($btn).click()
      }
    })
  })
  context('IP Assign View List', () => {
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
      cy.clearCookies()
      cy.clearLocalStorage()
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListAsgnIPMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      cy.wait('@viewListAsgnIPMst')
      /* 상세버튼 클릭 */
      cy.get('#element-table tr').eq(1).dblclick()
      cy.wait('@viewDetailAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/updateScommentAsgnIPMst.json').as('updateScommentAsgnIPMst')
      cy.wait('@viewListAsgnIPMst')
      /* 상세버튼 클릭 */
      cy.get('#element-table tr').eq(1).dblclick()
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
      cy.intercept('POST', '**/ipmgmt/assignmgmt/viewUpdateAsgnIPMst.model').as('viewUpdateAsgnIPMst')
      cy.wait('@viewListAsgnIPMst')
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(2)').click()
      cy.wait(1000)
      cy.wait('@viewUpdateAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const data = interception.response.body.result.data
        expect(data).to.exist
      })

      /* 배정상태 배정[미할당]으로 변경(*/
      cy.get(':nth-child(2) > :nth-child(2) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.sassignLevelCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(3)').click()

      cy.get('.popupContentTableBottom > :nth-child(1)').click()

      cy.wait('@updateAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })
    it('return', () => {
      /*
        mock환경에서는 상세 데이터가 동일하므로 선택 데이터 상관없음
        실데이터일 땐 '서비스배정[미할당]', '배정[미할당]' 상태인 데이터만 반납처리가 가능함
       */
      cy.get('#element-table tr').eq(1).dblclick()
      cy.get('.el-dialog__body > :nth-child(3) > :nth-child(2)').click()
      cy.wait('@updateAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })
    it('merge', () => {
      cy.intercept('POST', '**/ipmgmt/assignmgmt/viewInsertMrgAsgnIPMst.model').as('viewInsertMrgAsgnIPMst')
      cy.intercept('POST', '**/ipmgmt/assignmgmt/insertMrgAsgnIPMst.json').as('insertMrgAsgnIPMst')
      /* 1, 2 번째 있는 데이터를 병합 */
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('#element-table tr').eq(2).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(3)').click()
      cy.wait('@viewInsertMrgAsgnIPMst').then((interception) => {
        const responseData = interception.response.body.result.data
        expect(responseData.srcIpAssignMstVo).to.exist
        expect(responseData.destIpAssignMstVos).to.exist
      })
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertMrgAsgnIPMst').then((interception) => {
        const responseData = interception.response.body
        expect(responseData.commonMsg).to.equal('SUCCESS')
      })
    })
    it('division', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/assignmgmt/viewInsertDivAsgnIPMst.model').as('viewInsertDivAsgnIPMst')
      cy.intercept('POST', '**/ipmgmt/assignmgmt/appendDivAsgnIPMst.json').as('appendDivAsgnIPMst')
      cy.intercept('POST', '**/ipmgmt/assignmgmt/insertListDivAsgnIPMst.json').as('insertListDivAsgnIPMst')
      cy.get('#element-table tr').eq(1).find('td').eq(15).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.wait('@viewInsertDivAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
      })
      /* 블록분할 팝업 내에서 분할: bitmask default  */
      cy.get('.textflex > .el-button').click()
      cy.wait('@appendDivAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const tbIpAssignMstVos = interception.response.body.tbIpAssignMstVos
        expect(tbIpAssignMstVos).to.exist
      })
      /* 분할확정 */
      cy.get('.popupContentTableBottom > :nth-child(2)').click()
      cy.wait('@insertListDivAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })
    // it('CheckTacsIpBlock', () => {}) /* IP블럭 중복체크 - 로컬에서 확인불가 */
  })
})