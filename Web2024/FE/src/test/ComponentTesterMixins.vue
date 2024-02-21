<script>
import { isTestPage } from '@/test/commonTester.js'

export default {
    data() {
        return {
            src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
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

            this.$emit('initComponentData', { propMap: defaultPropMap })
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

