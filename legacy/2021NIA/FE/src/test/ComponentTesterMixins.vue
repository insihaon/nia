<script>

import { isTestPage, testData } from '@/test/commonTesterUtil.js'

export default {
    data() {
        return {
            src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
            emitKeys: []
        }
    },

    computed: {
    },

    watch: {

    },

    mounted() {
        if (isTestPage) {
            this.emitCurrentTestComponentData()
        }
    },

    destroyed() {
    },

    methods: {
        setDefaultProps(propMap) {
            const fK = Object.keys(testData).find(oKey => oKey === this.name)
            const pageTestData = testData[fK]

            if (pageTestData) {
                Object.keys(pageTestData).forEach((pageTestDataKey) => {
                    if (Object.hasOwnProperty.call(propMap, pageTestDataKey)) {
                        propMap[pageTestDataKey] = pageTestData[pageTestDataKey]
                    }
                })
            }

            return propMap
        },

        emitCurrentTestComponentData() {
            const THIS = this
            let propMap = Object.keys(this._props).reduce((map, propName) => {
                try {
                    if (!this._props[propName]) {
                        // map[propName] = this.$options.props[propName].type()
                        if (typeof this.$options.props[propName].type === 'function') {
                            map[propName] = this.$options.props[propName].type()
                        } else if (Array.isArray(this.$options.props[propName].type)) {
                            debugger
                            map[propName] = 'componentType-multitype-specialType'
                        } else {
                            throw new Error('해당 prop 처리중 에러발생 : ' + propName)
                        }
                    } else {
                        map[propName] = this._props[propName]
                    }
                    return map
                } catch (e) {
                    console.error('해당 prop 처리중 에러발생 : ' + propName)
                    console.log(THIS)
                    console.error(e)
                    debugger
                }
            }, {})

            propMap = this.setDefaultProps(propMap)
            const emitKeys = this.emitKeys || null
            this.$emit('emitComponentData', { propMap: propMap, emitKeys: emitKeys })
        },

        devEmit(emitKey, param) {
            this.$emit(emitKey, param)
            this.$emit('devEmit', { emitKey: emitKey, param: param })
        }

    }
}

</script>

<style scoped>

</style>

