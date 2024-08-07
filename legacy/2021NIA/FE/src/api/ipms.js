// import { getApiData } from '@/utils'
import http from '@/min/http'
import { AppOptions } from '@/class/appOptions'
// import { apiGetObjectFromJsonFile2 } from '@/api/json-file'
const { debug, isProd } = AppOptions.instance

export const filePath = __filename.replace(/\\/g, '/')

export const ipmsModelApis = {
  viewListIpAllocMst: { desc: 'IP할당 조회', url: '/ipmgmt/allocmgmt/viewListIpAllocMst' },
  viewListSvcStat: { desc: 'IP 서비스별 통계 조회', url: '/statmgmt/ipstatmgmt/viewListSvcStat' },
  viewListOrgSvcStat: { desc: 'IP 조직서비스별 통계 조회', url: '/statmgmt/ipstatmgmt/viewListOrgSvcStat' },
  viewListBlockSizeStat: { desc: 'IP 블록크기별 통계 조회', url: 'statmgmt/ipstatmgmt/viewListBlockSizeStat' },
  viewListIntgrmSvcStat: { desc: 'IP주소 라우팅 비교/점검 통계 조회', url: '/statmgmt/ipstatmgmt/viewListIntgrmSvcStat' },
  viewListIpAllocMstByMain: { desc: 'IP 블록 정보 조회', url: '/ipmgmt/allocmgmt/viewListIpAllocMstByMain' },
  viewDetailIpAllocMstByMain: { desc: 'IP 블록 상세정보', url: '/ipmgmt/allocmgmt/viewDetailIpAllocMstByMain' },
  viewListIpHistoryMst: { desc: 'IP 이력관리 조회', url: '/ipmgmt/historymgmt/viewListIpHistoryMst' },
  viewDetailIpHistMst: { desc: 'IP 이력 상세정보', url: '/ipmgmt/historymgmt/viewDetailIpHistMst' },
  viewDetailWhois: { desc: 'whois 상세정보', url: '/linkmgmt/socketmgmt/viewDetailWhois' },
  viewListAsgnIPMst: { desc: 'IP 배정 조회', url: '/ipmgmt/assignmgmt/viewListAsgnIPMst' },
  viewListCrtIPMst: { desc: 'IP 블록관리 조회', url: '/ipmgmt/createmgmt/viewListCrtIPMst' },
  viewListUnAssignIP: { desc: 'IP 미배정 조회', url: '/ipmgmt/assignmgmt/viewListUnAssignIP' },
  viewDetailUnAssignIP: { desc: 'IP 미배정 상세정보', url: '/ipmgmt/assignmgmt/viewDetailUnAssignIP' },
  viewDetailCrtIPMst: { desc: 'IP 블록관리 상세정보 ', url: '/ipmgmt/createmgmt/viewDetailCrtIPMst' },
  viewInsertCrtIPMst: { desc: 'IP 블록 추가 생성 ', url: '/ipmgmt/createmgmt/viewInsertCrtIPMst' },
}
export const ipmsJsonApis = {
  selectAuthCenterList: { desc: '센터 조회', url: '/opermgmt/orgmgmt/selectAuthCenterList' },
  selectAuthNodeList: { desc: '노드 조회', url: '/opermgmt/orgmgmt/selectAuthNodeList' },
  selectOfficeList: { desc: '수용국 조회', url: '/ipmgmt/linemgmt/selectOfficeList' },
  selectSassignTypeCdList: { desc: '서비스 조회', url: '/ipmgmt/allocmgmt/selectSassignTypeCdList' },
  deleteCrtIPMst: { desc: 'IP 블록관리 삭제', url: '/ipmgmt/createmgmt/deleteCrtIPMst' },
}

export function apiTest(params) {
  return http({
    url: '/ipmgmt/allocmgmt/viewListIpAllocMst.model',
    method: 'post',
    filePath: filePath,
    data: params || { 'searchBgnDe': '', 'searchCnd': '', 'searchEndDe': '', 'searchWrd': '', 'sortType': 'PIP_PREFIX', 'sortOrdr': 'ASC', 'searchUseYn': '', 'searchBgnCd': '', 'searchEndCd': '', 'pageIndex': 1, 'pageUnit': 10, 'pageSize': 0, 'firstIndex': 1, 'lastIndex': 10, 'recordCountPerPage': 10, 'rowNo': 0, 'typeFlag': '', 'dcreateDt': null, 'screateId': null, 'screateNm': null, 'dmodifyDt': null, 'smodifyId': null, 'smodifyNm': null, 'commonMsg': null, 'screateEmail': null, 'smodifyEmail': null, 'moveType': '', 'moveSearchWrd': '', 'moveSipVersionTypeCd': null, 'url': '/ipmgmt/allocmgmt/viewListIpAllocMst.model', 'menuType': null, 'paramMap': null, 'pipPrefix': null, 'sipVersionTypeCd': 'CV0001', 'sipVersionTypeNm': null, 'sipBlock': null, 'nbitmask': null, 'snetmask': null, 'sfirstAddr': null, 'slastAddr': null, 'nfirstAddr': null, 'nlastAddr': null, 'sfirstIpPreferred': null, 'slastIpPreferred': null, 'sfirstAddrBinary': null, 'slastAddrBinary': null, 'ncnt': null, 'nclassCnt': null, 'nuseIpCnt': null, 'nfreeIpCnt': null, 'nsubnetmask': null, 'ssvcLineTypeCd': 'CL0002', 'ssvcLineTypeNm': null, 'ssvcGroupCd': '', 'ssvcGroupNm': null, 'ssvcObjCd': null, 'ssvcObjNm': null, 'nipBlockMstSeq': null, 'nlvlMstSeq': null, 'nipAssignMstSeq': null, 'nipAllocMstCnt': null, 'sassignLevelCd': '', 'sassignLevelNm': null, 'sipCreateSeqCd': null, 'sipCreateSeqNm': null, 'scomment': null, 'nipmsSvcSeq': null, 'sassignTypeCd': '', 'sassignTypeNm': null, 'sexSvcCd': null, 'sexSvcNm': null, 'sipCreateTypeCd': 'CT0001', 'sipCreateTypeNm': null, 'sipAllocExTypeCd': null, 'sipAllocExTypeNm': null, 'lvlMstSeqListVo': null, 'sllnum': '', 'ssubscnealias': null, 'ssubsclgipportdescription': null, 'sicisofficescode': null, 'sicisofficesname': null, 'sofficecode': '', 'sofficename': null, 'smodelname': null, 'ssubscmstip': null, 'svalidCheck': null, 'sconnAlias': null, 'nipAllocMstSeq': null, 'ssubscnnescode': '', 'ssubscnescode': null, 'sgatewayip': null, 'sneSrchTypeCd': null, 'sicisofficescodeNe': 'XXXXXX', 'ssubscnealiasNe': '', 'smodelnameNe': '', 'ssubscmstipNe': '', 'sssvcMgroupNm': null, 'ssvcLgroupNm': null, 'ssvcUseTypeNm': null, 'ssaid': '', 'scustName': null, 'sipAssignSubNm': null, 'llSrchTypeCd': 'llnum', 'sordernum': '', 'sregyn': null, 'slacpsid': null, 'ssvcUseTypeCd': null, 'sexPushYn': null, 'nticketActSeq': null, 'sipmsSvcNm': null, 'sneossDdYn': null, 'sfirstAddrGwip': null, 'slastAddrGwip': null, 'sAlcSrchTypeCd': null, 'nwhoisSeq': null, 'nroutingChkMstSeq': null, 'pifSerialIp': null, 'sanealias': null, 'samstip': null, 'saifname': null, 'salocationcode': null, 'salocationcodeNm': null, 'saofficescode': null, 'saofficescodeNm': null, 'sznealias': null, 'szmstip': null, 'szifname': null, 'szlocationcode': null, 'szlocationcodeNm': null, 'szofficescode': null, 'szofficescodeNm': null, 'sofficescodeSrch': null, 'snealiasSrch': null, 'smstipSrch': null, 'sifipSrch': null, 'slocationcodeNmSrch': null, 'sllnumSrch': null, 'pifSerialIpSrch': null, 'nipLinkMstSeq': null, 'sconnalias': null, 'sGubun': null, 'sNextHop': null, 'totalCount': 0, 'nsummaryCnt': '', 'snull0Yn': '', 'sintgrmYn': '', 'spageType': null, 'ssubscnealiasType': null, 'bUploadFlag': false, 'sassignTypeCds': null, 'sassignTypeCdMultiStr': '', 'sassignTypeMulti': null, 'ssvcGroupCdMultiStr': '', 'ssvcGroupCdMulti': null, 'ssubsclgipportip': null, 'ssubscrouterserialip': null }
  })
}

export function apiTest2(params) {
  return http({
    url: '/ipmgmt/allocmgmt/viewListIpAllocMst.model',
    method: 'post',
    filePath: filePath,
    data: params || { 'zzz': 'bbb', 'searchBgnDe': '', 'searchCnd': '', 'searchEndDe': '', 'searchWrd': '', 'sortType': 'PIP_PREFIX', 'sortOrdr': 'ASC', 'searchUseYn': '', 'searchBgnCd': '', 'searchEndCd': '', 'pageIndex': 1, 'pageUnit': 10, 'pageSize': 0, 'firstIndex': 1, 'lastIndex': 10, 'recordCountPerPage': 10, 'rowNo': 0, 'typeFlag': '', 'dcreateDt': null, 'screateId': null, 'screateNm': null, 'dmodifyDt': null, 'smodifyId': null, 'smodifyNm': null, 'commonMsg': null, 'screateEmail': null, 'smodifyEmail': null, 'moveType': '', 'moveSearchWrd': '', 'moveSipVersionTypeCd': null, 'url': '/ipmgmt/allocmgmt/viewListIpAllocMst.model', 'menuType': null, 'paramMap': null, 'pipPrefix': null, 'sipVersionTypeCd': 'CV0001', 'sipVersionTypeNm': null, 'sipBlock': null, 'nbitmask': null, 'snetmask': null, 'sfirstAddr': null, 'slastAddr': null, 'nfirstAddr': null, 'nlastAddr': null, 'sfirstIpPreferred': null, 'slastIpPreferred': null, 'sfirstAddrBinary': null, 'slastAddrBinary': null, 'ncnt': null, 'nclassCnt': null, 'nuseIpCnt': null, 'nfreeIpCnt': null, 'nsubnetmask': null, 'ssvcLineTypeCd': 'CL0002', 'ssvcLineTypeNm': null, 'ssvcGroupCd': '', 'ssvcGroupNm': null, 'ssvcObjCd': null, 'ssvcObjNm': null, 'nipBlockMstSeq': null, 'nlvlMstSeq': null, 'nipAssignMstSeq': null, 'nipAllocMstCnt': null, 'sassignLevelCd': '', 'sassignLevelNm': null, 'sipCreateSeqCd': null, 'sipCreateSeqNm': null, 'scomment': null, 'nipmsSvcSeq': null, 'sassignTypeCd': '', 'sassignTypeNm': null, 'sexSvcCd': null, 'sexSvcNm': null, 'sipCreateTypeCd': 'CT0001', 'sipCreateTypeNm': null, 'sipAllocExTypeCd': null, 'sipAllocExTypeNm': null, 'lvlMstSeqListVo': null, 'sllnum': '', 'ssubscnealias': null, 'ssubsclgipportdescription': null, 'sicisofficescode': null, 'sicisofficesname': null, 'sofficecode': '', 'sofficename': null, 'smodelname': null, 'ssubscmstip': null, 'svalidCheck': null, 'sconnAlias': null, 'nipAllocMstSeq': null, 'ssubscnnescode': '', 'ssubscnescode': null, 'sgatewayip': null, 'sneSrchTypeCd': null, 'sicisofficescodeNe': 'XXXXXX', 'ssubscnealiasNe': '', 'smodelnameNe': '', 'ssubscmstipNe': '', 'sssvcMgroupNm': null, 'ssvcLgroupNm': null, 'ssvcUseTypeNm': null, 'ssaid': '', 'scustName': null, 'sipAssignSubNm': null, 'llSrchTypeCd': 'llnum', 'sordernum': '', 'sregyn': null, 'slacpsid': null, 'ssvcUseTypeCd': null, 'sexPushYn': null, 'nticketActSeq': null, 'sipmsSvcNm': null, 'sneossDdYn': null, 'sfirstAddrGwip': null, 'slastAddrGwip': null, 'sAlcSrchTypeCd': null, 'nwhoisSeq': null, 'nroutingChkMstSeq': null, 'pifSerialIp': null, 'sanealias': null, 'samstip': null, 'saifname': null, 'salocationcode': null, 'salocationcodeNm': null, 'saofficescode': null, 'saofficescodeNm': null, 'sznealias': null, 'szmstip': null, 'szifname': null, 'szlocationcode': null, 'szlocationcodeNm': null, 'szofficescode': null, 'szofficescodeNm': null, 'sofficescodeSrch': null, 'snealiasSrch': null, 'smstipSrch': null, 'sifipSrch': null, 'slocationcodeNmSrch': null, 'sllnumSrch': null, 'pifSerialIpSrch': null, 'nipLinkMstSeq': null, 'sconnalias': null, 'sGubun': null, 'sNextHop': null, 'totalCount': 0, 'nsummaryCnt': '', 'snull0Yn': '', 'sintgrmYn': '', 'spageType': null, 'ssubscnealiasType': null, 'bUploadFlag': false, 'sassignTypeCds': null, 'sassignTypeCdMultiStr': '', 'sassignTypeMulti': null, 'ssvcGroupCdMultiStr': '', 'ssvcGroupCdMulti': null, 'ssubsclgipportip': null, 'ssubscrouterserialip': null }
  })
}

export function apiRequestModel(api, params) {
  return http({
    url: `${api.url}.model`,
    method: 'post',
    filePath: filePath,
    data: params
  })
}
export function apiRequestJson(api, params) {
  return http({
    url: `${api.url}.json`,
    method: 'post',
    filePath: filePath,
    data: params
  })
}

window.apiTest = apiTest
window.apiTest2 = apiTest2

