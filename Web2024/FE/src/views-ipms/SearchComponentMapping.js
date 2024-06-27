/*
    IPMS Search Component Define
    * Rule
        - (default값이 있을 경우)최상단에서 props: { defaultValue }를 정의하여 최하위 value값을 설정한다.
            => 최하단 컴포넌트에서는 props 단방향 변경 에러에 유의하여 computed의 get, set을 사용하여 값을 변경하고 이벤트를 상위로 전달한다.
        - 최하단 컴포넌트가 select일 때 default options이 다를 경우 최상단에서 props: { 추가할 option : 값 }을 추가하여 정의한다.
        - 최하단 컴포넌트에 label과 최상단 label이 다를 경우 props : { label }을 추가하여 정의한다.
        - 계위(=서비스망) 컴포넌트를 사용할 경우 props : { lvl } 옵션을 추가하여 level을 정의한다.
            => multi로 설정할 level의 숫자를 setting => 예시) multi: [1,2]
            (cf. 계위 multi가 3개일 때 상위 2개 선택에 따라 수용국 활성/비활성 옵션 이벤트 고려)
*/
export const componentMap = {
    // 공인/사설
    SipCreateType: () => import('@/views-ipms/conditionComponents/SipCreateType.vue'),
    // 계위 or 서비스망 props: { label: '서비스망', level: 3, multi: [1,2,3] }
    SsvcLineType: () => import('@/views-ipms/conditionComponents/SsvcLineType.vue'),
    // 작업일자, 등록기간 dateRange
    DateRange: () => import('@/views-ipms/conditionComponents/DateRange.vue'),
    // 조회일자, single date
    DatePicker: () => import('@/views-ipms/conditionComponents/DatePicker.vue'),
    // IP 주소, IP 버전
    IpAddress: () => import('@/views-ipms/conditionComponents/IpAddress.vue'),
    // 정렬
    SortType: () => import('@/views-ipms/conditionComponents/SortType.vue'),
    // 수용국
    SOffice: () => import('@/views-ipms/conditionComponents/SOffice.vue'),
    // 생성차수
    GenerationDegree: () => import('@/views-ipms/conditionComponents/GenerationDegree.vue'),
    // 서비스
    ServiceOrg: () => import('@/views-ipms/conditionComponents/ServiceOrg.vue'),
    // Block 크기별 조건
    ConditionByBlocksize: () => import('@/views-ipms/conditionComponents/ConditionByBlocksize.vue'),
    // IP 블록상태, 할당상태
    IpBlockStatus: () => import('@/views-ipms/conditionComponents/IpBlockStatus.vue'),
    // 진행상태
    Progress: () => import('@/views-ipms/conditionComponents/Progress.vue'),
    // 상태(Non-KT IP관리 > 신청 상태)
    IpMngProgress: () => import('@/views-ipms/conditionComponents/IpMngProgress.vue'),
    // 예외여부
    ExceptionYN: () => import('@/views-ipms/conditionComponents/ExceptionYN.vue'),
    // INPUT 입력 prop_parameterKey로 입력값 구분
    InputType: () => import('@/views-ipms/conditionComponents/InputType.vue'),
    // PROTOCOL
    Protocol: () => import('@/views-ipms/conditionComponents/Protocol.vue'),
    // 회선정보
    LineInformation: () => import('@/views-ipms/conditionComponents/LineInformation.vue'),
    // 작업시스템
    WorkSystem: () => import('@/views-ipms/conditionComponents/WorkSystem.vue'),
    // 상세작업분류
    DetailedWorkClassification: () => import('@/views-ipms/conditionComponents/DetailedWorkClassification.vue'),
    // 조회년도
    CheckYear: () => import('@/views-ipms/conditionComponents/CheckYear.vue'),
    // 포함여부 사항 체크, parameterKey 전달
    IncludeYN: () => import('@/views-ipms/conditionComponents/IncludeYN.vue'),
    // 사용여부 (장비별 명령어 정보관리, 조직기준 관리)
    UsageYN: () => import('@/views-ipms/conditionComponents/UsageYN.vue'),
    // Block 크기
    BlockSize: () => import('@/views-ipms/conditionComponents/BlockSize.vue'),
    // 장비명, 상품, 소속조직
    InputSearchDetail: () => import('@/views-ipms/conditionComponents/InputSearchDetail.vue'),
    // 공지구분
    NoticeGubun: () => import('@/views-ipms/conditionComponents/NoticeGubun.vue'),
    // 게시판 > 조회 조건
    BoardSearchCondition: () => import('@/views-ipms/conditionComponents/BoardSearchCondition.vue'),
    // IP할당관리 > VPN IP현황, 게시판 > 신청 처리 현황, 요청사항 구분, 진행상태, 이용목적
    ApplyStatus: () => import('@/views-ipms/conditionComponents/ApplyStatus.vue'),
    // 외부 연계
    ExtrnLnkgs: () => import('@/views-ipms/conditionComponents/ExtrnLnkgs.vue'),
    // 사용자 권한등급
    AuthLevel: () => import('@/views-ipms/conditionComponents/AuthLevel.vue'),
}
