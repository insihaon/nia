/* IPMS search Component Define */
export const componentMap = {
    // 공인/사설
    SipCreateType: () => import('@/views-ipms/conditionComponents/SipCreateType.vue'),
    // 작업일자, 등록기간 dateRange
    DateRange: () => import('@/views-ipms/conditionComponents/DateRange.vue'),
    // IP 주소
    IpAddress: () => import('@/views-ipms/conditionComponents/IpAddress.vue'),
}
