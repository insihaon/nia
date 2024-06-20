<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-input
      v-model="word"
      size="mini"
      clearable
      @change="handleChangeWord"
    >
      <template #suffix>
        <el-button
          size="mini"
          style="font-size: larger;"
          @click="handleButtonClick"
        >
          <i class="el-icon-search font-weight-bolder"></i>
        </el-button>
      </template>
    </el-input>
    <ModalFacilityInformation ref="ModalFacilityInformation" @set-value="emitData(value)" />
    <ModalProductInformation ref="ModalProductInformation" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import ModalFacilityInformation from '@/views-ipms/modal/ModalFacilityInformation.vue'
import ModalProductInformation from '@/views-ipms/modal/ModalProductInformation.vue'

const routeName = 'InputSearchDetail'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { ModalFacilityInformation, ModalProductInformation },
  extends: Base,
  props: {
    label: {
      type: String,
      default: ''
    },
    componentKey: {
      type: String,
      default: ''
    },
    modalName: {
      type: String,
      default: ''
    },
    detailData: {
      type: Object,
      default: () => {
        return {}
      },
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      word: '',
    }
  },
  computed: {
  },
  methods: {
    emitData(value) {
      this.word = value
    },
    handleChangeWord() {
      this.$emit('update-value', [{ key: this.componentKey, value: this.word }])
    },
    handleButtonClick() {
      this.$refs[this.modalName].open({ row: 'row' })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
