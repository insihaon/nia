// import { getApiData } from '@/utils'
import http from '@/min/http'
import { AppOptions } from '@/class/appOptions'
// import { apiGetObjectFromJsonFile2 } from '@/api/json-file'
const { debug, isProd } = AppOptions.instance

export const filePath = __filename.replace(/\\/g, '/')

export const ipmsModelApis = {
  viewListIpAllocMst: { desc: 'IP할당 조회', url: '/ipmgmt/allocmgmt/viewListIpAllocMst' },
  viewDetailAlcIPMst: { desc: 'IP할당 상세정보', url: '/ipmgmt/allocmgmt/viewDetailAlcIPMst' },
  viewInsertDivAsgnIPMst: { desc: '배정/할당 > IP분할 대상정보', url: '/ipmgmt/assignmgmt/viewInsertDivAsgnIPMst' },
  viewInsertMrgAsgnIPMst: { desc: '배정/할당 > IP병합 대상정보', url: '/ipmgmt/assignmgmt/viewInsertMrgAsgnIPMst' },
  deletAlcIPMst: { desc: 'IP할당 해지', url: '/ipmgmt/allocmgmt/deletAlcIPMst' },
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
  viewDetailSummary: { desc: '라우팅 중복상세 조회', url: '/ipmgmt/assignmgmt/viewDetailSummary' },
  viewDetailSubSvcMst: { desc: '회선 상세조회', url: '/ipmgmt/allocmgmt/viewDetailSubSvcMst' },
  viewCheckTacsIpBlock: { desc: 'IP블럭 중복체크', url: '/opermgmt/tacsmgmt/viewCheckTacsIpBlock' },
  viewListNotice: { desc: '공지사항 리스트', url: '/opermgmt/boardmgmt/viewListNotice' },
  viewDetailNotice: { desc: '공지사항 상세정보', url: '/opermgmt/boardmgmt/viewDetailNotice' },
  viewDetailUnAssignIP: { desc: 'IP 미배정 상세정보', url: '/ipmgmt/assignmgmt/viewDetailUnAssignIP' },
  viewDetailCrtIPMst: { desc: 'IP 블록관리 상세정보 ', url: '/ipmgmt/createmgmt/viewDetailCrtIPMst' },
  viewInsertNotice: { desc: '공지사항 등록 화면 ', url: '/opermgmt/boardmgmt/viewInsertNotice' },
  viewUpdateNotice: { desc: '공지사항 수정 화면 ', url: '/opermgmt/boardmgmt/viewUpdateNotice' },
  viewInsertCrtIPMst: { desc: 'IP 블록 생성 상세모달(신규,추가) ', url: '/ipmgmt/createmgmt/viewInsertCrtIPMst' },
  viewUpdateAsgnIPMst: { desc: 'IP 블록관리 > IP배정 대상정보', url: '/ipmgmt/assignmgmt/viewUpdateAsgnIPMst' },
  viewDetailAsgnIPMst: { desc: 'IP배정 상세정보', url: '/ipmgmt/assignmgmt/viewDetailAsgnIPMst' },
}
export const ipmsJsonApis = {
  selectAuthCenterList: { desc: '센터 조회', url: '/opermgmt/orgmgmt/selectAuthCenterList' },
  selectAuthNodeList: { desc: '노드 조회', url: '/opermgmt/orgmgmt/selectAuthNodeList' },
  selectOfficeList: { desc: '수용국 조회', url: '/ipmgmt/linemgmt/selectOfficeList' },
  selectSassignTypeCdList: { desc: '서비스 조회', url: '/fviewInsertCrtIPMstipmgmt/allocmgmt/selectSassignTypeCdList' },
  updateScommentAsgnIPMst: { desc: '할당상세 > 비고수정', url: '/ipmgmt/allocmgmt/updateScommentAsgnIPMst' },
  appendDivAsgnIPMst: { desc: '배정/할당 > IP분할 > 분할 예정 정보 요청', url: '/ipmgmt/assignmgmt/appendDivAsgnIPMst' },
  appendMergeDivAsgnIPMst: { desc: '배정/할당 > IP분할 > 분할 예정 정보 > 병합', url: '/ipmgmt/assignmgmt/appendMergeDivAsgnIPMst' },
  insertListDivAsgnIPMst: { desc: '배정/할당 > IP분할 > 분할 확정 처리', url: '/ipmgmt/assignmgmt/insertListDivAsgnIPMst' },
  allocUpdateAsgnIPMst: { desc: '배정/할당 반납처리', url: '/ipmgmt/allocmgmt/allocUpdateAsgnIPMst' },
  insertMrgAsgnIPMst: { desc: '배정/할당 > 병합처리', url: '/ipmgmt/assignmgmt/insertMrgAsgnIPMst' },
  deleteCrtIPMst: { desc: 'IP 블록관리 삭제', url: '/ipmgmt/createmgmt/deleteCrtIPMst' },
  insertNotice: { desc: '공지사항 등록', url: '/opermgmt/boardmgmt/insertNotice' },
  updateNotice: { desc: '공지사항 수정', url: '/opermgmt/boardmgmt/updateNotice' },
  deleteNotice: { desc: '공지사항 삭제', url: '/opermgmt/boardmgmt/deleteNotice' },
  appendCrtIPMst: { desc: 'IP 블록관리 > IP 블록생성 > IP 주소 추가', url: '/ipmgmt/createmgmt/appendCrtIPMst' },
  insertListCrtIPMst: { desc: 'IP 블록관리 > IP 블록생성 > IP 주소 등록', url: '/ipmgmt/createmgmt/insertListCrtIPMst' },
  updateCrtIPMst: { desc: 'IP 블록관리  > 수정', url: '/ipmgmt/createmgmt/updateCrtIPMst' },
  updateAsgnIPMst: { desc: 'IP 블록관리 > 배정', url: '/ipmgmt/assignmgmt/updateAsgnIPMst' },
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

