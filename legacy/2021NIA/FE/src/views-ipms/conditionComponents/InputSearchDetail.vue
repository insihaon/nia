<template>
  <fragment>
    <th v-if="isShowLabel">
      {{ label }}
    </th>
    <td>
      <el-input
        v-model="value"
        size="small"
        :readonly="isReadOnly"
        clearable
        @input="formatInput"
        @change="handleChangeWord"
        @focus="handleClickArea"
      >
        <template #suffix>
          <el-button
            size="mini"
            style="font-size: larger;border: none;background:none;"
            @click="handleClickButton"
          >
            <i class="el-icon-search font-weight-bolder"></i>
          </el-button>
        </template>
      </el-input>
    </td>
    <ModalFacilityInformation ref="ModalFacilityInformation" @selected-value="setSelectedRow" />
    <!-- <ModalLinkInformation ref="ModalLinkInformation" @selected-value="setSelectedRow" />
    <ModalIpAllocCircuitDetail ref="ModalIpAllocCircuitDetail" /> -->
    <ModalProductInformation ref="ModalProductInformation" @selected-value="setSelectedRow" />
    <ModalOrgSearch ref="ModalOrgSearch" @selected-value="setSelectedRow" />
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'
import ModalFacilityInformation from '@/views-ipms/modal/search/ModalFacilityInformation.vue'
// import ModalLinkInformation from '@/views-ipms/modal/ModalLinkInformation.vue'
// import ModalIpAllocCircuitDetail from '@/views-ipms/modal/alloc/ModalIpAllocCircuitDetail.vue'
import ModalProductInformation from '@/views-ipms/modal/search/ModalProductInformation.vue'
import ModalOrgSearch from '@/views-ipms/modal/search/ModalOrgSearch.vue'

const routeName = 'InputSearchDetail'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { ModalFacilityInformation, ModalProductInformation, ModalOrgSearch },
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    isShowLabel: {
      type: Boolean,
      default: true
    },
    label: {
      type: String,
      default: ''
    },
     modalName: {
      type: String,
      default: ''
    },
    /*
      { key: value }을 쌍으로 한다.
      key: request 요청시에 parameter key가 될 값
      value: value값을 가져올 string
     */
    prop_parameterKey: {
      type: Object,
      default: null
    },
    /* modal내에서 조회시 필요한 조건이 있는 경우 */
    prop_datas: {
      type: Array,
      default: null
    },
    valueName: {
      type: String,
      default: null
    },
    isReadOnly: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      value: '',
    }
  },
  methods: {
    formatInput(val) {
      if (this.label === '회선정보') {
        this.value = val.replace(/\D/g, '')
      }
    },
    handleChangeWord(val) {
      if (this.parameterKey !== null) {
        const updateKey = Object.keys(this.parameterKey).find(key => {
          if (this.parameterKey[key] === this.valueName) {
            return key
          }
        })
        this.$emit('update-value', [{ key: updateKey, value: this.value }])
      }
    },
    setSelectedRow(params /* { returnFlag, selectedRow } */) {
      this.value = params.row[this.valueName]
      this.selectedRow = params.row

      this.parameterKey !== null && this.emitEventToParent(this.getParameter())
    },
    setParameter(params) {
      const keys = Object.keys(this.parameterKey)
      keys.forEach(key => {
        if (this.parameterKey[key] === this.valueName) {
          this.value = params[key]
        }
      })
    },
    getParameter() {
      const keys = Object.keys(this.parameterKey)
      const params = []
      keys.forEach(key => {
        params.push({ key, value: this.selectedRow ? this.selectedRow[this.parameterKey[key]] : '' })
      })
      return params
    },
    handleClickButton() {
      this.$refs[this.modalName].open({ rows: this.prop_datas })
    },
    handleClickArea() {
      if (!this.isReadOnly) return
      this.$refs[this.modalName].open()
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
