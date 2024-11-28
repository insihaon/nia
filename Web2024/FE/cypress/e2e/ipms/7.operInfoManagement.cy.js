describe('operInfoManagement Fuctionality', () => {
  context('Facility View List', () => {
    beforeEach(() => {
      cy.visitPath('dbMng/operInfoFacilityMng')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/hostmgmt/viewListIpHostMst.model').as('viewListIpHostMst') /* 목록 */
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListIpHostMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      cy.intercept('POST', '**/ipmgmt/hostmgmt/viewDetailIPHostMst.model').as('viewDetailIPHostMst') /* 상세 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_12').dblclick()
      cy.wait('@viewDetailIPHostMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('insert', () => {

      cy.intercept('POST', '**/ipmgmt/hostmgmt/insertHostIPMst.json').as('insertHostIPMst') /* 등록 */

      cy.get('.add-features > .el-button').click()
      /* 장비명 입력 */
      cy.get(':nth-child(1) > td > .el-input > .el-input__inner').type('TEST')
      /* 수용국명 검색 */
      cy.get('.el-input__suffix-inner > .el-button').click()
      cy.get('.popupContentTable > table > tr > :nth-child(2) > .el-input > .el-input__inner').type('유성')
      cy.get('tr > :nth-child(3) > .el-button').click()
      cy.get('tbody > :nth-child(1) > .el-table_3_column_21').dblclick()
      /* 나머지 정보 입력 */
      cy.get(':nth-child(3) > .textflex > :nth-child(2) > .el-input__inner').type('0.0.0.1')
      cy.get('.txt > .el-input__inner').type('test')
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertHostIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/ipmgmt/hostmgmt/updateHostIPMst.json').as('updateHostIPMst') /* 수정 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_12').dblclick()
      cy.get('.el-icon-edit').click()
      cy.wait(200)
      cy.get('.el-icon-edit').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@updateHostIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })

    })
    /* case 1. */
    it('delete_1', () => {
      cy.intercept('POST', '**/ipmgmt/hostmgmt/deleteHostIPMst.json').as('deleteHostIPMst') /* 삭제 */

      cy.get(':nth-child(1) > .el-table_2_column_19 > .cell > .el-button').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteHostIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* case 2. */
    it('delete_2', () => {
      cy.intercept('POST', '**/ipmgmt/hostmgmt/deleteHostIPMst.json').as('deleteHostIPMst') /* 삭제 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_12').dblclick()
      cy.get('.popupContentTableBottom > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteHostIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
  context('Link View List', () => {
    beforeEach(() => {
      cy.visitPath('dbMng/operInfoLinkMng')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/linkmgmt/viewListIpLinkMst.model').as('viewListIpLinkMst') /* 목록 */
    })
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListIpLinkMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('detail', () => {
      cy.intercept('POST', '**/ipmgmt/linkmgmt/viewDetailIPLinkMst.model').as('viewDetailIPLinkMst') /* 상세 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_12').dblclick()
      cy.wait('@viewDetailIPLinkMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.result.data).to.exist
      })
    })
    it('insert', () => {

      cy.intercept('POST', '**/ipmgmt/linkmgmt/insertLinkIPMst.json').as('insertLinkIPMst') /* 등록 */
      cy.intercept('POST', '**/opermgmt/orgmgmt/selectSearchLvlCd.json').as('selectSearchLvlCd') /* 수용국 검색 */
      cy.get('.add-features > .el-button').click()

      /* 데이터 입력 */
      cy.get('.textflex > :nth-child(1) > .el-input__inner').type('0.84.6.84/30')
      /* 자국수용국 선택 */
      cy.get(':nth-child(2) > .el-input > .el-input__suffix > .el-input__suffix-inner > .el-button').click()
      cy.get('.popupContentTable > table > tr > :nth-child(2) > .el-input > .el-input__inner').type('유성')
      cy.get('tr > :nth-child(3) > .el-button').click()
      cy.wait('@selectSearchLvlCd')
      cy.get('tbody > :nth-child(1) > .el-table_3_column_27').dblclick()

      cy.get(':nth-child(3) > :nth-child(2) > .el-input > .el-input__inner').type('1')
      cy.get(':nth-child(4) > :nth-child(2) > .el-input > .el-input__inner').type('1')
      cy.get(':nth-child(5) > :nth-child(2) > .el-input > .el-input__inner').type('1')
      cy.get(':nth-child(6) > :nth-child(2) > .el-input > .el-input__inner').type('1')

      /* 대국수용국 선택 */
      cy.get(':nth-child(4) > .el-input > .el-input__suffix > .el-input__suffix-inner > .el-button').click()
      cy.get('.popupContentTable > table > tr > :nth-child(2) > .el-input > .el-input__inner').type('유성')
      cy.get('tr > :nth-child(3) > .el-button').click()
      cy.wait('@selectSearchLvlCd')
      cy.get('tbody > :nth-child(1) > .el-table_4_column_30').dblclick()
      cy.get(':nth-child(3) > :nth-child(4) > .el-input > .el-input__inner').type('2')
      cy.get(':nth-child(4) > :nth-child(4) > .el-input > .el-input__inner').type('2')
      cy.get(':nth-child(5) > :nth-child(4) > .el-input > .el-input__inner').type('2')
      cy.get(':nth-child(6) > :nth-child(4) > .el-input > .el-input__inner').type('2')

      cy.get(':nth-child(7) > td > .el-input > .el-input__inner').type('test')

      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertLinkIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    it('update', () => {
      cy.intercept('POST', '**/ipmgmt/linkmgmt/viewUpdateIPLinkMst.model').as('viewUpdateIPLinkMst') /* 수정 데이터 조회 */
      cy.intercept('POST', '**/ipmgmt/linkmgmt/updateLinkIPMst.json').as('updateLinkIPMst') /* 수정 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_12').dblclick()
      cy.get('.el-icon-edit').click()
      cy.wait('@viewUpdateIPLinkMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
      })
      cy.get('.el-icon-edit').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@updateLinkIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* case 1. */
    it('delete_1', () => {
      cy.intercept('POST', '**/ipmgmt/linkmgmt/deleteLinkIPMst.json').as('deleteLinkIPMst') /* 삭제 */

      cy.get(':nth-child(1) > .el-table_2_column_25 > .cell > .el-button').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteLinkIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
    /* case 2. */
    it('delete_2', () => {
      cy.intercept('POST', '**/ipmgmt/linkmgmt/deleteLinkIPMst.json').as('deleteLinkIPMst') /* 삭제 */
      cy.get('tbody > :nth-child(1) > .el-table_2_column_12').dblclick()
      cy.get('.popupContentTableBottom > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deleteLinkIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        expect(interception.response.body.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})