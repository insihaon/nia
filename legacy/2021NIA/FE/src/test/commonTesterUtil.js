import router from '@/router'

export function isTestPage() {
    return router.history.current.name === 'testComponentTest'
}

// 컴포넌트의 name과 일치해야함
export const testData = {
    CompAgGrid: {
        value: {
            'options': {
                'name': 'ApiUseAuthApplytable1',
                'checkable': false,
                'rowGroupPanel': false,
                'rowHeight': 40,
                'rowSelection': 'multiple',
                'rowMultiSelection': false
            },
            'columns': [
                {
                    'type': '',
                    'prop': 'api_id',
                    'name': 'API_ID(degubMode 전용)',
                    'minWidth': 30,
                    'flex': 1,
                    'suppressMenu': true,
                    'alignItems': 'left',
                    'sortable': true,
                    'filterable': false
                },
                {
                    'type': '',
                    'prop': 'api_name',
                    'name': 'API 명',
                    'minWidth': 30,
                    'flex': 1,
                    'suppressMenu': true,
                    'alignItems': 'left',
                    'sortable': true,
                    'filterable': false
                },
                {
                    'type': '',
                    'prop': 'api_url',
                    'name': '접근 URL',
                    'minWidth': 80,
                    'flex': 1,
                    'suppressMenu': true,
                    'alignItems': 'left',
                    'sortable': true,
                    'filterable': false
                },
                {
                    'type': '',
                    'prop': 'exec_mode_cd',
                    'name': '연동 방식',
                    'minWidth': 100,
                    'flex': 1,
                    'suppressMenu': true,
                    'alignItems': 'left',
                    'sortable': true,
                    'filterable': false
                },
                {
                    'type': '',
                    'prop': 'api_desc',
                    'name': '설명',
                    'minWidth': 100,
                    'flex': 1,
                    'suppressMenu': true,
                    'alignItems': 'left',
                    'sortable': true,
                    'filterable': false
                },
                {
                    'type': '',
                    'prop': '_',
                    'name': '기능',
                    'minWidth': 30,
                    'flex': 1,
                    'suppressMenu': true,
                    'alignItems': 'left',
                    'cellRendererFramework': 'CellRenderApplybuttons',
                    'sortable': false,
                    'cellRendererParams': {
                        'useApply': {
                            'name': '권한신청',
                            'type': 'auth'
                        },
                        'selectDataSet': {
                            'name': 'API 데이터',
                            'type': 'dataSet'
                        }
                    }
                }
            ],
            'data': [
                {
                    'api_id': 38,
                    'expird_time': null,
                    'update_time': '2023-11-15T14:10:16.035+09:00',
                    'api_desc': 'TEST_서비스_000',
                    'exec_mode_cd': 'OnDemand',
                    'api_url': '/WS_000',
                    'api_name': 'TEST_서비스_000',
                    'system_id': 10,
                    'intgr_interval': null
                },
                {
                    'api_id': 39,
                    'expird_time': null,
                    'update_time': null,
                    'api_desc': 'TEST_서비스_000_Batch',
                    'exec_mode_cd': 'Batch',
                    'api_url': 'http://10.220.178.141:28000/WS_000',
                    'api_name': 'TEST_서비스_000_Batch',
                    'system_id': 13,
                    'intgr_interval': null
                },
                {
                    'api_id': 40,
                    'expird_time': null,
                    'update_time': null,
                    'api_desc': '교환 TT 재해 정보 연동',
                    'exec_mode_cd': 'OnDemand',
                    'api_url': '/WS_008',
                    'api_name': '교환 TT 재해 정보 연동',
                    'system_id': 10,
                    'intgr_interval': null
                },
                {
                    'api_id': 41,
                    'expird_time': null,
                    'update_time': null,
                    'api_desc': 'TT조치이력 송신',
                    'exec_mode_cd': 'OnDemand',
                    'api_url': '/WS_005',
                    'api_name': 'TT조치이력 송신',
                    'system_id': 11,
                    'intgr_interval': null
                },
                {
                    'api_id': 53,
                    'expird_time': null,
                    'update_time': null,
                    'api_desc': 'TT고장 발생/회복 정보',
                    'exec_mode_cd': 'OnDemand',
                    'api_url': '/WS_009',
                    'api_name': 'TT고장 발생/회복 정보',
                    'system_id': 13,
                    'intgr_interval': null
                }
            ]
        },

        paginationInfo: {
            currentPage: 1, // 현재 페이지
            pageSize: 20, // 페이지당 항목 수
            totalCount: 0, // 총 항목 수
            totalPages: null, // 전체 페이지 수
            pagerCount: 11
        }
    },

    OrgSelect: {
        item: {
            'label': '관리센터/국사',
            'type': 'orgSelect',
            'size': 18,
            'model': 'ORG',
            'HEAD': {
                'options': [
                    { value: '467044', label: '강북&강원NW운용본부' },
                    { value: '468338', label: '대구&경북NW운용본부' },
                    { value: '468994', label: '충남&충북NW운용본부' },
                    { value: '482973', label: '제주단' },
                    { value: '468633', label: '전남&전북NW운용본부' },
                    { value: '467653', label: '강남&서부NW운용본부' },
                    { value: '468042', label: '부산&경남NW운용본부' },
                ]
            },
            'CENTER': {
                'options': []
            },
            'TEAM': {
                'options': []
            },
            'OFFICE': {
                'options': []
            }
        }
    },

    CompCheckSelector: {
        item: {
            options: [
                { label: 'test1', value: 'test1' },
                { label: 'test2', value: 'test2' },
                { label: 'test3', value: 'test3' },
                { label: 'test4', value: 'test4' },
            ],
            placeholder: 'test123',
            disabled: false,
            readonly: false
        }
    }

}
