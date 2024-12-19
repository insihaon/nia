describe('Ip AdressRoutingCompare Fuctionality', () => {
  beforeEach(() => {
    cy.visitPath('dbMng/ipAdressRoutingCompare')
    /* POST 요청 정의 */
    cy.intercept('POST', '**/ipmgmt/routmgmt/viewListRoutChkMst.model').as('viewListRoutChkMst') /* 목록 */
    // cy.wait(1000)
    /* 조회 버튼 클릭 */
    cy.get('.searchBtnWrap > .el-button--primary').then(($btn)=> {
      if($btn.length > 0) {
        cy.wrap($btn).click()
      } else {
        cy.log('non button')
      }
    })
    // cy.get('.searchBtnWrap > .el-button--primary').click()
  })
  context('IP AdressRoutingCompare View List', () => {
    afterEach(() => {
      cy.wait(1000) // 각 테스트 후에 1초 대기
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListRoutChkMst').then((interception) => {
        // cy.log('viewListCrtIPMst response:', JSON.stringify(interception.response))
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('IpBlock detail', () => {
      cy.wait('@viewListRoutChkMst')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/routmgmt/viewDetailRoutChkMst.model').as('viewDetailRoutChkMst')
      /* IP블록 컬럼 버튼 선택 */
      cy.get('#element-table tr').eq(2).find('td').eq(5).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        } else {
          cy.log('non button')
        }
      })
      // cy.get(':nth-child(1) > .el-table_2_column_18_column_19 > .cell > .el-button > span').click()
      cy.wait('@viewDetailRoutChkMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('Nexthop detail', () => {
      cy.wait('@viewListRoutChkMst')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/routmgmt/viewDetailNextHop.model').as('viewDetailNextHop')
      /* Nexthop 컬럼 버튼 선택 */
      cy.get('#element-table tr').eq(2).find('td').eq(9).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        } else {
          cy.log('non button')
        }
      })
      cy.wait('@viewDetailNextHop').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })
    })
    it('IP Allocation', () => {
      cy.wait('@viewListRoutChkMst')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/routmgmt/viewPopInsertAlcIPMst.model').as('viewPopInsertAlcIPMst') /* 할당 목록 조회 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/selectSearchtLnMst.json').as('selectSearchtLnMst') /* 링크 정보 조회 */
      cy.intercept('POST', '**/opermgmt/tacsmgmt/viewCheckTacsIpBlock.model').as('viewCheckTacsIpBlock') /* IP중복 체크 조회 결과 pop */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/insertAlcIPMst.json').as('insertAlcIPMst') /* 할당처리 */
      /* IP할당/해지 컬럼 버튼 클릭 (할당가능 데이터) */
      cy.get('#element-table tr').eq(4).find('td').eq(15).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        } else {
          cy.log('non button')
        }
      })
      cy.wait('@viewPopInsertAlcIPMst').then((interception) => {
        const responseData = interception.response.body.result.data
        // cy.log(responseData)
        expect(responseData).to.exist
      })

      /* 1. 링크 데이터 테스트 */
      /* 링크 조회 버튼 클릭 */
      cy.get('.el-input__suffix-inner > .el-button > .el-icon-search').click()
      /* 국사 선택 후 조회 */
      cy.get('td[data-v-f15bb1f2=""] > :nth-child(1) > .el-select > .el-input > .el-input__inner').click()
      cy.get('.SOffice > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get('.popupContentTable > .searchTable > :nth-child(2) > td > .searchBtnWrap > .el-button--primary').click()

      cy.wait('@selectSearchtLnMst').then((interception) => {
        const responseData = interception.response.body
        expect(responseData.commonMsg).to.equal('SUCCESS')
        expect(responseData.ipAllocOperMstVos).to.exist
      })
      /* 선택 */
      cy.get('.el-dialog__body > .compTable tr').eq(1).dblclick()
      /* 할당 버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      /* IP중복 체크 조회 결과 pop */
      cy.wait('@viewCheckTacsIpBlock').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
      })
      cy.get('[style="background: rgb(43, 88, 144);"]').click()
      /* 중복 체크 결과가 할당 불가능인 경우 > 메시지 팝업 확인버튼 클릭 */
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@insertAlcIPMst').then((interception) => {
        const result = interception.response.body.ipAllocOperMstVo
        expect(result.commonMsg).to.equal('SUCCESS')
      })
      /* 2. 회선 */
      /* 3. 시설 */
    })
    /* IP해지 */
    it('IP Return', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/viewDetailAlcIPMst.model').as('viewDetailAlcIPMst') /* 할당 상세 조회 */
      cy.intercept('POST', '**/ipmgmt/allocmgmt/deletAlcIPMst.json').as('deletAlcIPMst') /* 해지 */

      cy.wait('@viewListRoutChkMst')
      cy.get('#element-table tr').eq(2).find('td').eq(15).find('.el-button').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        } else {
          cy.log('non button')
        }
      })
      /* IP할당 메뉴에서 테스트 검증 했으므로 pass함 */
      cy.wait('@viewDetailAlcIPMst')
      /* 해지 버튼 클릭 */
      cy.get(':nth-child(1) > .btn_text > .el-button').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      cy.wait('@deletAlcIPMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        // const result = interception.response.body
        // expect(result.commonMsg).to.equal('SUCCESS')
      })
    })
    it('Routing DB Compare', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/routmgmt/viewPopRoutChkMst.model').as('viewPopRoutChkMst') /* 라우팅 수집 장비목록 조회  */
      cy.intercept('POST', '**/ipmgmt/routmgmt/insertListRoutChkMst.json').as('insertListRoutChkMst') /* 라우팅 수집 */

      cy.wait('@viewListRoutChkMst')
      cy.get(':nth-child(1) > :nth-child(2) > .el-input > .el-input__inner').click()
      cy.get('.ssvcGroupCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(8)').click()
      cy.get(':nth-child(1) > :nth-child(3) > .el-input > .el-input__inner').click()
      cy.get('.ssvcObjCd > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(3)').click()
      cy.get('.add-features > :nth-child(1)').click()
      /* 장비 목록 조회 */
      cy.wait('@viewPopRoutChkMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const responseData = interception.response.body.result.data
        expect(responseData).to.exist
      })

      /* 라우팅 수집 시작 버튼 클릭 */
      cy.get('.popupContentTableBottom > :nth-child(1)').click()
      cy.wait('@insertListRoutChkMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        // const result = interception.response.body
        // expect(result.commonMsg).to.equal('SUCCESS')
      })
    })
    it('Exception Management', () => {
      /* POST 요청 정의 */
      cy.intercept('POST', '**/ipmgmt/routmgmt/insertListExcptMst.json').as('insertListExcptMst') /* 예외처리 요청  */

      cy.wait('@viewListRoutChkMst')
      /* 예외처리 대상 선택 */
      cy.get('#element-table tr').eq(2).find('td').eq(11).find('.el-checkbox').then(($btn) => {
        if($btn.length > 0) {
          cy.wrap($btn).click()
        }
      })
      cy.get('.add-features > :nth-child(2)').click()
      cy.get('.el-message-box__btns > .el-button--primary').click()
      /* 예외처리 관리 팝업 */
      cy.get('.text-left > .el-select > .el-input > .el-input__inner').click()
      cy.wait(200)
      cy.get('.exceptOption > .el-scrollbar > .el-select-dropdown__wrap > .el-scrollbar__view > :nth-child(2)').click()
      cy.get('#sexcptRsn').type('test')
      cy.get('.popupContentTableBottom > :nth-child(1)').click()

      cy.wait('@insertListExcptMst').then((interception) => {
        expect(interception.response.statusCode).to.equal(200)
        const result = interception.response.body
        expect(result.commonMsg).to.equal('SUCCESS')
      })
    })
  })
})