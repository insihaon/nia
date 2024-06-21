<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-input
      v-model="value"
      size="mini"
      :readonly="isReadOnly"
      clearable
      @change="handleChangeWord"
      @focus="handleClickArea"
    >
      <template #suffix>
        <el-button
          size="mini"
          style="font-size: larger;"
          @click="handleClickButton"
        >
          <i class="el-icon-search font-weight-bolder"></i>
        </el-button>
      </template>
    </el-input>
    <ModalFacilityInformation ref="ModalFacilityInformation" @selected-value="setParameterKey" />
    <ModalProductInformation ref="ModalProductInformation" @selected-value="setParameterKey" />
    <ModalOrgSearch ref="ModalOrgSearch" @selected-value="setParameterKey" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import ModalFacilityInformation from '@/views-ipms/modal/ModalFacilityInformation.vue'
import ModalProductInformation from '@/views-ipms/modal/ModalProductInformation.vue'
import ModalOrgSearch from '@/views-ipms/modal/ModalOrgSearch.vue'

const routeName = 'InputSearchDetail'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { ModalFacilityInformation, ModalProductInformation, ModalOrgSearch },
  extends: Base,
  props: {
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
    parameterKey: {
      type: Object,
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
  computed: {
  },
  methods: {
    emitData(value) {
      this.value = value
    },
    handleChangeWord() {
      if (this.parameterKey !== null) {
        // const parameterKeys = Object.keys(this.parameterKey)
        // parameterKeys.forEach(key => {
        // })
        // this.$emit('update-value', [{ key: this.parameterKey, value: this.value }])
      }
    },
    handleClickButton() {
      this.$refs[this.modalName].open({ row: 'row' })
    },
    setParameterKey(selectedRow) {
      this.value = selectedRow[this.valueName]
      this.selectedRow = selectedRow

      if (this.parameterKey !== null) {
        const keys = Object.keys(this.parameterKey)
        const params = []
        keys.forEach(key => {
          params.push({ key, value: this.selectedRow[this.parameterKey[key]] })
        })
        this.$emit('update-value', params)
      }
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
