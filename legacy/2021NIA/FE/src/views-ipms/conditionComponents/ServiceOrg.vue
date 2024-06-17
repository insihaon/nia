<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      서비스
    </label>
    <el-select
      v-model="localValue"
      filterable
      :multiple="isMulti"
      clearable
      collapse-tags
      size="mini"
      @change="handleChange"
    >
      <el-option
        v-if="isSettingAllOption"
        label="전체"
        value="ALL"
      >
        <span
          style="width: 100%; height: 100%; display: block;"
          @click.prevent="onClickAll"
        >
          전체
        </span>
      </el-option>

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
      value: null
    }
  },
  computed: {
    isMulti() {
      return this.multi
    },
    isSettingAllOption() {
      return true
    },
    localValue: {
      get() {
        return this.value
      },
      set(newValue) {
        this.value = newValue
        this.$emit('update-value', [{ key: 'serviceOrg', value: newValue }])
      }
    },
    fullOptions() {
      return this.options.map(option => option.value).filter(value => value !== 'ALL')
    },
    toggleAll() {
      return this.localValue.length === this.fullOptions.length
    }
  },
  watch: {
    /* 페이지별 선택 가능수 상이함 */
    // value(n, o) {
    //   if (n.length > 10 && !n.includes('ALL')) {
    //     this.$message('서비스는 최대 10개까지 선택 가능합니다.')
    //     this.value = o
    //   }
    // }

    value(n, o) {
      // console.log(n, o)
    }
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
      if (this.localValue.length === this.fullOptions.length && !this.localValue.includes('ALL')) {
        this.localValue.push('ALL')
      } else if (this.localValue.includes('ALL') && this.localValue.length !== this.fullOptions.length + 1) {
        this.localValue = this.localValue.filter(value => value !== 'ALL')
      }
      this.$emit('update-value', [{ key: 'sassignTypeCd', value: this.localValue }])
    },
    onClickAll() {
      if (this.localValue.includes('ALL')) {
        this.localValue = []
      } else {
        this.localValue = ['ALL', ...this.fullOptions]
      }
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
