/* IPMS search Component Define */
export const componentMap = {
    // 공인/사설
    SipCreateType: () => import('@/views-ipms/conditionComponents/SipCreateType.vue'),
    // 일자 dateRange
    DateRange: () => import('@/views-ipms/conditionComponents/DateRange.vue'),
    // IP주소(key, value 두 쌍)
    IpAddress: () => import('@/views-ipms/conditionComponents/IpAddress.vue'),
}
