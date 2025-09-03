/*
vscode Search All (정규표현식)
  "cmd": [^\n]*
  mock\json\datahub\*.json
  => copy all
re (python) 실행
  "cmd": ([^\n]*)
  => 추출
  => 결과 복사 & 아래 apis 에 붙여넣기
*/

export const apis = [
    '{"url": "/dh/api-test/WS_000", "sqlId": "", "param": "{\\"USER_ID\\": \\"T021239112\\", \\"MENU_ID\\": \\"M0000788\\", \\"INT\\": 999}"}',
    '{"url": "/dh/api-test/WS_005", "sqlId": "", "param": null}',
    '{"url": "/dh/api-test/WS_008", "sqlId": "", "param": "{\\"USER_ID\\": \\"T021239112\\", \\"MENU_ID\\": \\"M0000788\\", \\"INT\\": 999}"}',
    '{"url": "/dh/api-test/WS_009", "sqlId": "", "param": "{\\"USER_ID\\": \\"T021239112\\", \\"MENU_ID\\": \\"M0000788\\", \\"INT\\": 999}"}',
    '{"url": "/selectOneFile", "sqlId": "", "param": "{\\"sqlId\\": \\"SELECT_NEA_LIST.json\\"}"}',
    '{"url": "/getkey", "sqlId": "", "param": null}',
    '{"url": "/selectList/SELECT_API_FAIL_COUNT_LIST", "sqlId": "", "param": "{\\"api_id\\": \\"\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_AUTH_LIST", "sqlId": "", "param": "{\\"api_id\\": 38, \\"start_create_time\\": null, \\"end_create_time\\": null, \\"limit\\": 10, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_AUTH_LIST", "sqlId": "", "param": "{\\"api_id\\": 39, \\"start_create_time\\": null, \\"end_create_time\\": null, \\"limit\\": 10, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_AUTH_LIST", "sqlId": "", "param": "{\\"api_id\\": 40, \\"start_create_time\\": null, \\"end_create_time\\": null, \\"limit\\": 10, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_AUTH_LIST", "sqlId": "", "param": "{\\"api_id\\": 41, \\"start_create_time\\": null, \\"end_create_time\\": null, \\"limit\\": 10, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_AUTH_LIST", "sqlId": "", "param": "{\\"api_id\\": 53, \\"start_create_time\\": null, \\"end_create_time\\": null, \\"limit\\": 10, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_AUTH_LIST", "sqlId": "", "param": "{\\"api_id\\": \\"\\", \\"system_nm\\": \\"\\", \\"status_cd\\": [], \\"exec_mode_cd\\": \\"O\\", \\"limit\\": 50, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_AUTH_LIST", "sqlId": "", "param": "{\\"api_name\\": \\"\\", \\"status_cd\\": [], \\"start_create_time\\": null, \\"end_create_time\\": null, \\"start_expird_date\\": null, \\"end_expird_date\\": null, \\"limit\\": 50, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 38, \\"result_cd\\": \\"F\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 38, \\"result_cd\\": \\"S\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 39, \\"result_cd\\": \\"F\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 39, \\"result_cd\\": \\"S\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 40, \\"result_cd\\": \\"F\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 40, \\"result_cd\\": \\"S\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 41, \\"result_cd\\": \\"F\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 41, \\"result_cd\\": \\"S\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 53, \\"result_cd\\": \\"F\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_HIST_LIST", "sqlId": "", "param": "{\\"api_id\\": 53, \\"result_cd\\": \\"S\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_INFO_LIST", "sqlId": "", "param": null}',
    '{"url": "/selectList/SELECT_API_INFO_LIST", "sqlId": "", "param": "{\\"limit\\": 20, \\"page\\": 1, \\"api_name\\": \\"\\", \\"exec_mode_cd\\": []}"}',
    '{"url": "/selectList/SELECT_API_LIST", "sqlId": "", "param": null}',
    '{"url": "/selectList/SELECT_API_SUCCESS_COUNT_LIST", "sqlId": "", "param": "{\\"api_id\\": \\"\\", \\"start_date\\": null, \\"end_date\\": null, \\"limit\\": 20, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_TABLE_LIST", "sqlId": "", "param": "{\\"api_id\\": 38, \\"limit\\": 30, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_TABLE_LIST", "sqlId": "", "param": "{\\"api_id\\": 40, \\"limit\\": 30, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_TABLE_LIST", "sqlId": "", "param": "{\\"api_id\\": 41, \\"limit\\": 30, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_API_TABLE_LIST", "sqlId": "", "param": "{\\"api_id\\": 53, \\"limit\\": 30, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_DATA_SET_COL_LIST", "sqlId": "", "param": "{\\"dataset_id\\": 26, \\"page\\": 1, \\"limit\\": 30}"}',
    '{"url": "/selectList/SELECT_DATA_SET_COL_LIST", "sqlId": "", "param": "{\\"dataset_id\\": 41, \\"page\\": 1, \\"limit\\": 30}"}',
    '{"url": "/selectList/SELECT_DATA_SET_COL_LIST", "sqlId": "", "param": "{\\"dataset_id\\": 69, \\"page\\": 1, \\"limit\\": 30}"}',
    '{"url": "/selectList/SELECT_DATA_SET_HIST_LIST", "sqlId": "", "param": "{\\"dataset_nm\\": \\"\\", \\"status_cd\\": [], \\"page\\": 1, \\"limit\\": 30}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 30, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 2}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 3}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 4}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 5}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 6}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 7}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 8}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"\\", \\"column_nm\\": \\"\\", \\"metadata_desc\\": \\"\\", \\"limit\\": 50, \\"page\\": 9}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_ACCESS_NET_LINK_MST\\"}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_AGWEQUIP\\"}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_AL_CURRENT_SUB_DTL\\"}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_EQUIP_MST\\"}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_EQUIPPORT_CRCERR_LOG\\"}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_LINK_CURTRF_DTL\\"}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_TCA_CURRENT_DTL\\"}"}',
    '{"url": "/selectList/SELECT_DATA_SET_LIST", "sqlId": "", "param": "{\\"table_nm\\": \\"TB_VOC_DTL\\"}"}',
    '{"url": "/selectList/SELECT_DATA_TABLE_LIST", "sqlId": "", "param": null}',
    '{"url": "/selectList/SELECT_LINK_SYSTEM_LIST", "sqlId": "", "param": "{\\"system_nm\\": \\"\\", \\"limit\\": 30, \\"page\\": 1}"}',
    '{"url": "/selectList/SELECT_VW_ORG_MST_HQ", "sqlId": "", "param": null}',
    '{"url": "/setting", "sqlId": "", "param": null}',
    '{"url": "/signin", "sqlId": "", "param": "{\\"data\\": {\\"key\\": \\"8fcf4f5e9d1b68e263579ffd49691f82c57b72822378844e695ee1a4f29090e29ce7943ba27b9833e6eb02ca77b0c26df99dee62aa4f722fdb137f7e1dd89c9c5efd5475d887b82876415f5210b9760675572aad8b692f317c435743a14a4b56243993c5384d3b2f255a6a730babc7f9328a8e6d899792d0978b9c069711f439\\", \\"value\\": \\"bldjUHcvdUR6NFlIYmVqS1A0ditOODg1VnpyVncza3BhQjdBa3VWeGxBbVp3VVpkMU1tR2VkZWVxSXJGL1Y0bnRMSGp1UlJVYmxjY3lSYy80aGFDdXVoNFIrU1FUNXV0RHkwZ0VUemgvRE9sVTZqZTgveXNJVFdqbGhYcnNHL3M1dCtIRTNYVzEvRUxLSVJ1MnhLcFFuVkFZNGI5MTlCQU4yRndHZXpJTktwNmtlL2FzTko3UzRyQ3VHNmhXRFNwekRCTjJzUEpjNlpRbTEyNXBnUEZtUT09\\", \\"m\\": \\"94c4a9e2b9e0332cc378fc7ab2545249160f4c8928fba43c27220bed3ba81cdf63d75ec34541c6c083478788f77542e6ec6b6cca8fa0eae58ed3022353352ef1016b32292aa3e29eb905a983a20f9bef742d0e8ddad9fe21920c0f65785f9443926efc773b5729531b821584f282a686ab6641535d5f375d10da5707ccad0cf9\\"}}"}',
]

