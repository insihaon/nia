import { onMessagePopup } from '@/utils/index'
import { ipmsJsonApis, apiRequestExcel } from '@/api/ipms'
/* text input 체크
 * example : text 속성에 onkeyup='checkInput(this,'IPonly')'
 *             형태로 사용
 */
import _ from 'lodash'
export function checkInput(event, type) {
    var inputValue = event.currentTarget.value.toString()
    var regexp
    var regexpchg

    if (type === 'IPonly') {
        regexp = /[^a-f0-9:.]/i
        regexpchg = /[^a-f0-9:.]/gi
    } else if (type === 'IP') {
        regexp = /[^a-f0-9:./]/i
        regexpchg = /[^a-f0-9:./]/gi
    } else if (type === 'NUM') {
        regexp = /[^0-9]/i
        regexpchg = /[^0-9]/gi
    } else if (type === 'ENG') {
        regexp = /[^a-z]/i
        regexpchg = /[^a-z]/gi
    } else if (type === 'ENGNUM') {
        regexp = /[^a-z0-9]/i
        regexpchg = /[^a-z0-9:.]/gi
    }
    if (regexp.test(inputValue)) {
        event.currentTarget.blur()
        event.currentTarget.value = inputValue.replace(regexpchg, '')
        event.currentTarget.focus()
    }
}

export function getStatColumn(type, svcList) {
    /* type : routing, orgService, service, blockSize */
    const resultColumns = type === 'service' ? [
        {
            prop: 'level', label: '계위', children: [
                { prop: 'sassign_type_nm', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
                { prop: 'sip_create_type_nm', label: '공인/사설', align: 'center', columnVisible: true, showOverflow: true },
            ], align: 'center', columnVisible: true, showOverflow: true,
        },
    ] : [
        {
            prop: 'level', label: '계위', children: [
                { prop: 'ssvc_line_type_nm', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row.ssvc_line_type_nm === '소계' ? '총계' : row.ssvc_line_type_nm } },
                { prop: 'ssvc_group_nm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
                { prop: 'ssvc_obj_nm', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
                { prop: 'sip_create_type_nm', label: '공인/사설', align: 'center', columnVisible: true, showOverflow: true },
            ],
            align: 'center', columnVisible: true, showOverflow: true,
        },
    ]
    /* column set */
    let childrenPropByLabel
    if (type === 'routing') {
        childrenPropByLabel = { nip_all_cnt_: '총합', nip_match_cnt_: '일치', nip_unmatch_cnt_: '불일치', nip_match_per_: '현행화율' }
    } else {
        childrenPropByLabel = { nall_cnt_: '총수량', nalloc_cnt_: '할당', nassign_cnt_: '미할당', nall_cnt: '할당율' }
    }
    const propKey = Object.keys(childrenPropByLabel)

    const svcListClone = _.cloneDeep(svcList)
    svcListClone.unshift({ name: '전체', code: 'all' })

    svcListClone.forEach(svc => {
        let rateProp = `nall_cnt_${svc.code.toLowerCase()}`
        if (type === 'routing') {
            rateProp = `${propKey[3]}${svc.code.toLowerCase()}`
        } else if (type === 'orgService') {
            rateProp = `${propKey[3]}_${svc.code.toLowerCase()}_rate`
        }
        const children = [
            { prop: `${propKey[0]}${svc.code.toLowerCase()}`, label: childrenPropByLabel[propKey[0]], align: 'center', columnVisible: true, showOverflow: true },
            { prop: `${propKey[1]}${svc.code.toLowerCase()}`, label: childrenPropByLabel[propKey[1]], align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: `${propKey[2]}${svc.code.toLowerCase()}`, label: childrenPropByLabel[propKey[2]], align: 'center', sortable: true, columnVisible: type !== 'routing', showOverflow: true },
            {
                prop: rateProp, label: childrenPropByLabel[propKey[3]], align: 'center', columnVisible: ['routing', 'orgService'].includes(type), showOverflow: true, formatter: (row, col, value, index) => { return typeof value === 'number' ? value + '%' : value }
            },
        ]
        const label = svc.code !== 'all' && type === 'blockSize' ? '/' + svc.name : svc.name
        const column = { prop: label, label: label, children, align: 'center', columnVisible: true, showOverflow: true }
        resultColumns.push(column)
    })
    return resultColumns
}
// IP할당 상세 > IP블록 중복 체크
export function fnViewCheckTacsIpBlock(THIS, rows = []) {
    if (rows.length === 0) {
        onMessagePopup(THIS, 'IP블럭 중복체크할 대상이 없습니다. 선택해주세요.')
        return
    }
    if (rows.length > 1) {
        onMessagePopup(THIS, 'IP블럭 중복체크할 대상을 다건 선택 할 수 없습니다. 확인해주세요.')
        return
    }
    const { sipVersionTypeCd, ssvcLineTypeCd, nipAssignMstSeq } = rows[0]
    if (sipVersionTypeCd !== 'CV0001') {
        onMessagePopup(THIS, 'IP블럭 중복체크는 IPv4만 가능합니다.')
        return
    }
    if (ssvcLineTypeCd !== 'CL0001' && ssvcLineTypeCd !== 'CL0002' && ssvcLineTypeCd !== 'CL0003') {
        onMessagePopup(THIS, 'IP블럭 중복체크는 KOREAN, PREMIUM, MOBILE망만 가능합니다.')
        return
    }
    // const res = await api({ nipAssignMstSeq })
    THIS.$refs.ModalCheckTacsIpBlock.open({ row: rows[0] })
}

export async function downloadExcel(THIS = null, apiKey) {
    if (THIS === null) return
    const target = ({ vue: THIS.$refs.container })
    const parameter = THIS.$refs.searchCondition.requestParameter
     try {
        THIS.openLoading(target)
        const res = await apiRequestExcel(ipmsJsonApis[apiKey], parameter)
        if (res && res.commonMsg) {
            onMessagePopup(THIS, res?.commonMsg)
        }
    } catch (error) {
        THIS.error(error)
    } finally {
        THIS.closeLoading(target)
    }
  }
