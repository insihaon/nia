<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      서비스
    </label>
    <el-select
      v-model="localValue"
      filterable
      :multiple="isMulti"
      collapse-tags
      size="mini"
      @change="handleChange"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll">전체</span></el-option>
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
import { onMessagePopup } from '@/utils/index'

const routeName = 'ServiceOrg'
export default {
  name: routeName,
  extends: Base,
  props: {
    isMulti: {
      type: Boolean,
      default: true
    },
    limit: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      options: [
        // { label: '전체', value: 'ALL' },
        { label: '기업고객(고정)', value: 'agency' },
        { label: 'VR1-Routing', value: 'VR1' },
        { label: 'VR2-Routing', value: 'VR2' },
        { label: 'VR3-Routing', value: 'VR3' },
        { label: '홈고객(유동)', value: 'flow' },
        { label: '홈고객(고정)', value: 'fix' },
        { label: '홈고객(시설)', value: 'facility' },
        { label: '미분류서비스', value: 'unclassified' },
        { label: '홈고객(Secured IP)', value: 'secured' },
        { label: '타사이관', value: 'transfer' },
        { label: '홍콩DC구축용', value: 'hongkong' },
        { label: '대군화 시설용', value: 'D-agency' },
        { label: 'Cloud', value: 'cloud' }
      ],
      localValue: [],
      multiValue: ''
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(option => option.value).filter(localValue => localValue !== 'ALL')
    },
  },
  watch: {
    /* 페이지별 선택 가능수 상이함 */
    // value(n, o) {
    //   if (n.length > 10 && !n.includes('ALL')) {
    //     this.$message('서비스는 최대 10개까지 선택 가능합니다.')
    //     this.value = o
    //   }
    // }
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
  // sassignTypeCd, sassignTypeCdMultiStr: SA0001;SA0002;SA0003;
  methods: {
    handleChange() {
      if (this.localValue.length === this.fullOptions.length && !this.localValue.includes('ALL')) {
        this.localValue.push('ALL')
      } else if (this.localValue.includes('ALL') && this.localValue.length !== this.fullOptions.length + 1) {
        this.localValue = this.localValue?.filter(value => value !== 'ALL')
      }

      if (this.limit !== null) {
        this.onCheckLimit()
      }

      let key = 'sassignTypeCd'
      let value = this.localValue
      if (this.isMulti) {
        key = 'sassignTypeCdMultiStr'
        value = this.localValue.filter(v => v !== 'ALL').join(';')
      }
      this.$emit('update-value', [{ key, value }])
    },
    toggleAll() {
      this.localValue = this.localValue.includes('ALL') ? [] : ['ALL', ...this.fullOptions]
      if (this.limit !== null && this.localValue.length > this.limit) {
        onMessagePopup(this, `서비스는 최대 ${this.limit}개까지 선택 가능합니다.`)
        this.localValue = []
      }
    },
    onLoadServiceList(params) {
      /*
      const res = await api(params)
      this.options = res.result (options set)
      */
    },
    onCheckLimit() {
      if (this.localValue.length > this.limit) {
        onMessagePopup(this, `서비스는 최대 ${this.limit}개까지 선택 가능합니다.`)
        this.localValue = []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
