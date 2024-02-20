<script>
import { testerConstants, isTestPage } from '@/test/commonTester.js'

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
        emitCurrentTestComponentData() {
            const THIS = this
            const defaultPropMap = Object.keys(this._props).reduce((map, propName) => {
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
                }
            }, {})

            const emitKeys = this.emitKeys || null

            let existComponentAutoTest = testerConstants.notExist
            if (this.componentAutoTest && typeof this.componentAutoTest === 'function') {
                existComponentAutoTest = testerConstants.exist
            }

            this.$emit('initComponentData', { propMap: defaultPropMap, emitKeys: emitKeys, existComponentAutoTest: existComponentAutoTest })
        },

        devEmit(emitKey, param) {
            this.$emit(emitKey, param)
            this.$emit('devEmit', { emitKey: emitKey, param: param })
        },

        async runComponentAutoTest() {
            await this.componentAutoTest()
            this.$message.info({ message: `테스트를 성공적으로 마무리했습니다.` })
        },

        testFunction(functionName, param) {
            console.log(functionName + '의 테스트를 시작합니다.')
            param = param || ''
            // eslint-disable-next-line no-eval
            eval('this.' + functionName + `(${param})`)
            console.log(functionName + '의 테스트를 종료합니다.')
        }

    }
}

</script>

<style scoped>

</style>

