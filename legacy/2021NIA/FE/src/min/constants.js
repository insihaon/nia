import { safeArray, safeString } from '@/utils'

export default {
  channels: {
    UNKNOWN: { name: 'UNKNOWN', enable: true },
    HEARTBEAT: { name: 'HEARTBEAT', enable: true },
  },
  message: {
    areYouSureWantToDelete: '삭제하시겠습니까?',
    areYouSureWantToSave: '저장하시겠습니까?',
    areYouSureWantToProceed: '진행하시겠습니까?',
    isInvalidOperator: '정의된 연산자가 아닙니다',
    isNotNumber: '숫자가 아닙니다',
    pleaseInputName: '이름을 입력해 주세요',
    save: '저장',
    cancel: '취소',
    error: '에러'
  },
  authOptions: [
    { text: '사용자', code: 'user', value: 1, index: 0 },
    { text: '담당자', code: 'manager', value: 2, index: 1 },
    { text: '관리자', code: 'admin', value: 4, index: 2 }
  ],
  authManagement: {
    GRANT: { code: 'GRANT', label: '승인', index: 0 },
    REJECT: { code: 'REJECT', label: '반려', index: 1 },
    APPLY: { code: 'APPLY', label: '신청', index: 2 },
    REVOKE: { code: 'REVOKE', label: '회수', index: 3 }
  },
  authDataSet: {
    APPLY: { code: 'A', label: '요청', index: 0 },
    GRANT: { code: 'G', label: '승인', index: 1 },
    REJECT: { code: 'R', label: '반려', index: 2 },
    COMPLETE: { code: 'C', label: '완료', index: 3 },
  },
  apiAlarm: {
    onDemand: { code: 'O', state: 'OnDemand', label: '재처리' },
    batch: { code: 'B', state: 'Batch', label: '' },
    retry: { code: 'T', label: '재처리 성공' }
  },
  nia: {
    alarm_type: [
      { value: 'ALL', text: '전체' },
      { value: 'RT', text: '장애' },
      { value: 'FTT', text: '비장애' },
      { value: 'PF', text: '광레벨' },
      { value: 'ATT2', text: '이상 트래픽' },
      { value: 'NTT', text: '유해 트래픽' },
      { value: 'NFTT', text: '장비부하장애' },
      // { value: 'TRAFFIC', text: '트래픽(TRAFFIC)' }
    ],
    status_type: [
      { code: 'INIT', hex: '', text: '발생', fnCount: (d) => safeString(d.status).trim() === 'INIT', fnFilter: (d) => safeString(d.status).trim() === 'INIT' },
      { code: 'ACK', hex: '', text: '인지', fnCount: (d) => safeString(d.status).trim() === 'ACK', fnFilter: (d) => safeString(d.status).trim() === 'ACK' },
      { code: 'FIN', hex: '', text: '마감', fnCount: (d) => safeString(d.status).trim() === 'FIN', fnFilter: (d) => safeString(d.status).trim() === 'FIN' },
      { code: 'AUTO_FIN', hex: '', text: '자동마감', fnCount: (d) => safeString(d.status).trim() === 'AUTO_FIN', fnFilter: (d) => safeString(d.status).trim() === 'AUTO_FIN' },
    ]
  }
}

export function objectToArray(obj) {
  return Object.entries(obj).map(i => i[1])
}
