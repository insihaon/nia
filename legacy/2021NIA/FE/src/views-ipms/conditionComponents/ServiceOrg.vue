<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      서비스
    </label>
    <el-select
      v-model="value"
      filterable
      :multiple="isMulti"
      clearable
      size="mini"
      @change="handleChange"
    >
      <el-option
        v-for="(option, i) in options"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import { EventType } from '@/min/types'
import Eventbus from '@/utils/event-bus'

const routeName = 'ServiceOrg'
export default {
  name: routeName,
  extends: Base,
  props: {
    multi: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      options: [
        { label: '전체', value: 'ALL' },
        { label: '기업고객(고정)', value: 'agency' },
        { label: '홈고객(유동)', value: 'flow' },
        { label: '홈고객(고정)', value: 'fix' },
        { label: '홈고객(시설)', value: 'facility' },
        { label: '미분류서비스', value: 'unclassified' },
        { label: '홈고객(Secured IP)', value: 'secured' },
        { label: '타사이관', value: 'transfer' },
        { label: '홍콩DC구축용', value: 'hongkong' },
        { label: '대군화 시설용', value: 'cloud' },
        { label: 'Cloud', value: 'cloud' }
      ],
      value: null
    }
  },
  computed: {
    isMulti() {
      return this.multi
    },
  },
  mounted() {
    this.value = this.isMulti ? [] : ''
    Eventbus.$on(EventType.changeLvl1, (params) => {
      this.onLoadServiceList(params)
    })
  },
  beforeDestroy() {
    Eventbus.$off(EventType.changeLvl1)
  },
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'sassignTypeCd', value: this.value }])
    },
    onLoadServiceList(params) {
      /*
      const res = await api(params)
      this.options = res.result (options set)
      */
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
