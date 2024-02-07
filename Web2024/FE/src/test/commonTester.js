import router from '@/router'

export function getDataType(data) {
    try {
        if (data === 'componentType-multitype-specialType') return 'multitype'

        if (typeof data === 'object') {
            if (Array.isArray(data)) return 'array'
            else return typeof data
        } else {
            return typeof data
        }
    } catch (e) {
        console.error(e)
    }
}

export function convertArrayTotypeComponentDatas(typeComponentDatas, datas) {
    typeComponentDatas.length = 0 // 배열을 비운다.

    datas.forEach((d) => {
        try {
            typeComponentDatas.push({
                type: getDataType(d),
                objectKey: 'array',
                dataValue: d
            })
        } catch (e) {
            console.error(e)
        }
    })
}

export function getConvertSinglePropToSingleComponentTypeData(prop) {
    if (Object.keys(prop).length === 1) {
        const key = Object.keys(prop).pop()
        return {
            type: getDataType(prop[key]),
            objectKey: key,
            dataValue: prop[key]
        }
    } else {
        throw new Error('예상치 못한 에러')
    }
}

export function convertObjectTotypeComponentDatas(typeComponentDatas, object) {
    typeComponentDatas.length = 0 // 배열을 비운다.

    Object.keys(object).forEach(key => {
        try {
            typeComponentDatas.push({
                type: getDataType(object[key]),
                objectKey: key,
                dataValue: object[key]
            })
        } catch (e) {
            console.error(e)
        }
    })
}

export function getConvertTypeComponentDatasToObject(typeComponentDatas) {
    return typeComponentDatas.reduce((map, typeComponentData) => {
        map[typeComponentData.objectKey] = typeComponentData.dataValue
        return map
    }, {})
}

export function getConvertTypeComponentDatasToArray(typeComponentDatas) {
    return typeComponentDatas.reduce((array, typeComponentData) => {
        array.push(typeComponentData.dataValue)
        return array
    }, [])
}

export const typeList = [
    'string', 'object', 'boolean', 'function', 'array'
]

export function makeSelectTypeOptions() {
    return typeList.map(type => {
        return { label: type, value: type }
    })
}

export function getDataTypeDefaultValue(type) {
    try {
        switch (type) {
            case 'number': return 0
            case 'string': return ''
            case 'boolean': return false
            case 'array': return []
            case 'object': return {}
            case 'function': return () => { }
            default:
                throw new Error(`정의하지 않은 type입니다 ${type}`)
        }
    } catch (e) {
        console.error(e)
    }
}

export function isTestPage() {
    if (router.history.current.path === '/test') {
        return true
    }

    if (/^\/test\/.*/.test(router.history.current.path)) {
        return true
    }

    return false
}

export const defaultTypeComponentData = {
    type: undefined,
    dataValue: undefined,
    objectKey: undefined
}
