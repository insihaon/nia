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
            prop: '', label: '', children: [
                { prop: 'sassign_type_nm', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
                { prop: 'sip_create_type_nm', label: '공인/사설', align: 'center', columnVisible: true, showOverflow: true },
            ], align: 'center', columnVisible: true, showOverflow: true,
        },
    ] : [
        {
            prop: '', label: '계위', children: [
                { prop: 'ssvc_line_type_nm', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true },
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
        const column = { prop: '', label: svc.code !== 'all' && type === 'blockSize' ? '/' + svc.name : svc.name, children, align: 'center', columnVisible: true, showOverflow: true }
        resultColumns.push(column)
    })
    return resultColumns
}
