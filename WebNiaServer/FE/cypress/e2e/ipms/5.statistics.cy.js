describe('IP STATISTICS Management Fuctionality', () => {
  context('IP AddressRoutingStat View List', () => {
    beforeEach(() => {
      cy.visitPath('ipStatMng/ipAddressRoutingStat')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/statmgmt/ipstatmgmt/viewListIntgrmSvcStat.model').as('viewListIntgrmSvcStat') /* IP주소 라우팅 비교/점검 통계 목록 */
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListIntgrmSvcStat').then((interception) => {
        const responseData = interception.response.body.data
        expect(responseData.resultStatus).to.equal('SUCCESS')

        const result = JSON.parse(responseData.result)
        const svcList = JSON.parse(responseData.svcList)
        expect(result).to.exist
        expect(svcList).to.have.length.greaterThan(1)
      })
    })
  })
  context('IP StatByOrgService View List', () => {
    beforeEach(() => {
      /* IP 조직서비스별 통계 목록 */
      cy.visitPath('ipStatMng/ipStatByOrgService')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/statmgmt/ipstatmgmt/viewListOrgSvcStat.model').as('viewListOrgSvcStat')
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListOrgSvcStat').then((interception) => {
        const responseData = interception.response.body.data
        expect(responseData.resultStatus).to.equal('SUCCESS')

        const result = JSON.parse(responseData.result)
        const svcList = JSON.parse(responseData.svcList)
        expect(result).to.exist
        expect(svcList).to.have.length.greaterThan(1)
      })
    })
  })
  context('IP StatByService View List', () => {
    beforeEach(() => {
      /* IP 서비스별 통계 목록 */
      cy.visitPath('ipStatMng/ipStatByService')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/statmgmt/ipstatmgmt/viewListSvcStat.model').as('viewListSvcStat')
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListSvcStat').then((interception) => {
        const responseData = interception.response.body.data
        expect(responseData.resultStatus).to.equal('SUCCESS')

        const result = JSON.parse(responseData.result)
        const svcLineList = JSON.parse(responseData.svcLineList)
        expect(result).to.exist
        expect(svcLineList).to.have.length.greaterThan(1)
      })
    })
  })
  context('IP StatByBlockSize View List', () => {
    beforeEach(() => {
      /* IP 블록크기별 통계 목록 */
      cy.visitPath('ipStatMng/ipStatByBlockSize')
      /* POST 요청 정의 */
      cy.intercept('POST', '**/statmgmt/ipstatmgmt/viewListBlockSizeStat.model').as('viewListBlockSizeStat')
    })
    it('list lookup', () => {
      /* 조회 결과 성공여부 확인 */
      cy.wait('@viewListBlockSizeStat').then((interception) => {
        const responseData = interception.response.body.data
        expect(responseData.resultStatus).to.equal('SUCCESS')

        const result = JSON.parse(responseData.result)
        const blockSizeCds = JSON.parse(responseData.blockSizeCds)
        const blockSizeCdsList = JSON.parse(responseData.blockSizeCdsList)
        expect(result).to.exist
        expect(blockSizeCds).to.have.length.greaterThan(1)
        expect(blockSizeCdsList).to.have.length.greaterThan(1)
      })
    })
  })
})