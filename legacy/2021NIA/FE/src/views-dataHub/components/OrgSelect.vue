<template>
  <div>
    <!-- HEAD -->
    <el-select
      v-model="searchModel.HEAD"
      collapse-tags
      :style="{ width: '20%' }"
      style="margin-right: 5px"
      filterable
      reserve-keyword
      remote
      @change="$emit('orgChange', 'HEAD')"
    >
      <el-option
        v-for="(option, i) in item.HEAD.options"
        :key="i"
        :label="option.label"
        :value="option.value"
        :style="{ width: '100%' }"
      />
    </el-select>

    <!-- CENTER -->
    <el-select
      v-model="searchModel.CENTER"
      collapse-tags
      :style="{ width: '20%' }"
      style="margin-right: 5px"
      filterable
      reserve-keyword
      remote
      @change="$emit('orgChange', 'CENTER')"
    >
      <el-option
        v-for="(option, i) in item.CENTER.options"
        :key="i"
        :label="option.label"
        :value="option.value"
        :style="{ width: '100%' }"
      />
    </el-select>

    <!-- TEAM -->
    <el-select
      v-model="searchModel.TEAM"
      collapse-tags
      :style="{ width: '20%' }"
      style="margin-right: 5px"
      filterable
      reserve-keyword
      remote
      @change="$emit('orgChange', 'TEAM')"
    >
      <el-option
        v-for="(option, i) in item.TEAM.options"
        :key="i"
        :label="option.label"
        :value="option.value"
        :style="{ width: '100%' }"
      />
    </el-select>

    <!-- OFFICE -->
    <el-select
      v-model="searchModel.OFFICE"
      collapse-tags
      :style="{ width: '20%' }"
      filterable
      reserve-keyword
      remote
      @change="$emit('orgChange', 'OFFICE')"
    >
      <el-option
        v-for="(option, i) in item.OFFICE.options"
        :key="i"
        :label="option.label"
        :value="option.value"
        :style="{ width: '100%' }"
      />
    </el-select>
  </div>
</template>

<script>
const routeName = 'CompCheckSelector'
import { Base } from '@/min/Base.min'
import ComponentTesterMixins from '@/test/ComponentTesterMixins'

export default {
  name: routeName,
  components: {},
  extends: Base,
  mixins: [ComponentTesterMixins],
  props: {
    item: {
      type: Object,
      default() { return { } }
    },
    searchModel: {
      type: Object,
      default() { return [] }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`
    }
  },
  computed: {
  },

  watch: {
  },

  mounted() {
  },
  methods: {
  }

}
</script>

<style lang="scss">

</style>
