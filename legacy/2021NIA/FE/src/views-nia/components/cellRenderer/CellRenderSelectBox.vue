<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="default-cell w-100 h-100" style="cursor: pointer;" @click="openModal(params)">
      <el-select
        v-if="params.type === 'auth'"
        v-model="authValue"
        multiple
        collapse-tags
        style="margin-left: 20px;"
        filterable
        allow-create
        default-first-option
        placeholder="권한 선택"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button v-if="params.type === 'authSetting'" plain size="mini" type="info"> {{ params.name }}</el-button>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'

const routeName = 'CellRenderSelectBox'

export default Vue.extend({
  data() {
    return {
      name: routeName,
      options: [{
          value: 1,
          label: '사용자'
        }, {
          value: 3,
          label: '담당자'
        }, {
          value: 7,
          label: '관리자'
      }],
      authValue: []
    }
  },
  computed: {
  },
  methods: {
    openModal(params) {
      params.action(params.data, params.type)
    }
  },
})
</script>

<style lang="scss" scope>
.CellRenderSelectBox {
  .el-input__inner {
    height: 27px !important;
  }
  .default-cell{
    padding-top: 2px !important;
  }
  .el-select .el-input .el-select__caret {
    line-height: 10px !important;
  }
}
</style>
