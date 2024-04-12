<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="default-cell w-100 h-100" style="cursor: pointer;">
      <el-select
        v-model="authValue"
        multiple
        collapse-tags
        style="margin-left: 20px;"
        filterable
        placeholder="권한 선택"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="mx-sm-3" plain size="mini" type="info" @click="openModal(params)">
        {{ params.name }}
      </el-button>
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
          value: '1',
          label: '사용자'
        }, {
          value: '3',
          label: '담당자'
        }, {
          value: '7',
          label: '관리자'
      }],
      authValue: []
    }
  },
  computed: {
  },
  mounted() {
    this.authValue = this.setAuthValue()
  },
  methods: {
    setAuthValue() {
      const authValue = []
        if (this.params.data.lvl === 1) {
          authValue.push('1')
        } else if (this.params.data.lvl === 3) {
          authValue.push('1', '3')
        } else {
          authValue.push('1', '3', '7')
        }
        return authValue
    },
    openModal(params) {
      params.action(params.data, params.type, this.authValue)
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
