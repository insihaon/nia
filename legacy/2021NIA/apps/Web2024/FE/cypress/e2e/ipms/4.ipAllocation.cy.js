describe('Ip Allocation Fuctionality', () => {
  beforeEach(() => {
    /* POST 요청 정의 */
    cy.intercept('POST', '**/ipmgmt/allocmgmt/viewListIpAllocMst.model').as('viewListIpAllocMst') /* 할당 목록 */
    cy.intercept('POST', '**/ipmgmt/allocmgmt/viewDetailAlcIPMst.model').as('viewDetailAlcIPMst') /* 할당 상세 */
    cy.visitPath('ipAllocationMng/ipAllocation')
    /* 조회 버튼 클릭 */
    cy.get('.searchBtnWrap > .el-button--primary').then(($btn)=> {
      if($btn.length > 0) {
        cy.wrap($btn).click()
      } else {
        cy.log('non button')
      }
    })
  })
  context('IP Allocation View List', () => {
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListIpAllocMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      cy.wait('@viewListIpAllocMst')
      cy.get('#element-table tr').eq(1).dblclick()
      cy.wait('@viewDetailAlcIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('update', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/updateScommentAsgnIPMst.json').as('updateScommentAsgnIPMst')
      cy.wait('@viewListIpAllocMst')
      /* 상세버튼 클릭 */
      cy.get('#element-table tr').eq(1).dblclick()
      cy.wait('@viewDetailAlcIPMst')
      /* 수정버튼 클릭 */
      cy.get('.popupContentTable > .popupContentTableBottom > .el-button').click()
      cy.wait('@updateScommentAsgnIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })
    it('alloc', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/viewDetailSubSvcMst.model').as('viewDetailSubSvcMst')
      cy.intercept('POST', '**/opermgmt/tacsmgmt/viewCheckTacsIpBlock.model').as('viewCheckTacsIpBlock')
      cy.intercept('POST', '**/ipmgmt/allocmgmt/insertAlcIPMst.json').as('insertAlcIPMst')
      /* 1. 회선정보 할당 테스트 */
      cy.wait('@viewListIpAllocMst')
      cy.get('#element-table tr').eq(1).find('td').eq(0).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(2)').click()
      cy.get('.d-flex > :nth-child(2) > .el-input__inner').type('021800002122')
      cy.get(':nth-child(2) > .el-input__suffix > .el-input__suffix-inner > .el-button').click()
      // 회선정보 조회
      cy.wait('@viewDetailSubSvcMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.result.data.sresultMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
      cy.get('.ModalIpAllocCircuitDetail > .el-dialog > .el-dialog__body > .popupContentTableBottom > .el-button').click()
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      // IP블럭 중복체크 조회
      cy.wait('@viewCheckTacsIpBlock').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.result.data.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
      cy.get('.ModalCheckTacsIpBlock > .el-dialog > .el-dialog__body > .popupContentTableBottom > :nth-child(1)').click()
      // 중복체크 결과 할당불가능일 때 확인 버튼 클릭
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@insertAlcIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const commonMsg = interception.response.body.ipAllocOperMstVo.commonMsg
        expect(commonMsg).to.equal('SUCCESS')
      })
    })
    it('division', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/assignmgmt/viewInsertDivAsgnIPMst.model').as('viewInsertDivAsgnIPMst')
      cy.intercept('POST', '**/ipmgmt/assignmgmt/appendDivAsgnIPMst.json').as('appendDivAsgnIPMst')
      cy.intercept('POST', '**/ipmgmt/assignmgmt/insertListDivAsgnIPMst.json').as('insertListDivAsgnIPMst')
      cy.get('#element-table tr').eq(1).find('td').eq(16).find('.el-button').then(($btn) => {
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