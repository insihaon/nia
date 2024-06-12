/*
    IPMS Search Component Define
    * Rule
        - (default값이 있을 경우)최상단에서 정의하는 value와 최하위 props value의 dataType이 일치하도록 default값을 설정한다.
            => 최하단 컴포넌트에서는 props 단방향 변경 에러에 유의하여 computed의 get, set을 사용하여 값을 변경하고 이벤트를 상위로 전달한다.
        - 최하단 컴포넌트가 select일 때 default options이 다를 경우 최상단에서 exceptOptions: { options }을 추가하여 정의한다.
        - 최하단 컴포넌트에 label과 최상단 label이 다를 경우 exceptOptions: { label }을 추가하여 정의한다.
        - 계위(=서비스망) 컴포넌트를 사용할 경우 exceptOptions: { level } 옵션을 추가하여 level을 정의한다.
            => multi로 설정할 level의 숫자를 setting => 예시) multi: [1,2]
            (cf. 계위 multi가 3개일 때 상위 2개 선택에 따라 수용국 활성/비활성 옵션 이벤트 고려)
*/
export const componentMap = {
    // 공인/사설
    SipCreateType: () => import('@/views-ipms/conditionComponents/SipCreateType.vue'),
    // 계위 or 서비스망 exceptOptions: { label: '서비스망', level: 3, multi: [1,2,3] }
    SsvcLineType: () => import('@/views-ipms/conditionComponents/SsvcLineType.vue'),
    // 작업일자, 등록기간 dateRange
    DateRange: () => import('@/views-ipms/conditionComponents/DateRange.vue'),
    // IP 주소
    IpAddress: () => import('@/views-ipms/conditionComponents/IpAddress.vue'),
    // 정렬
    SortType: () => import('@/views-ipms/conditionComponents/SortType.vue'),
    // 수용국
    SOffice: () => import('@/views-ipms/conditionComponents/SOffice.vue'),
}
