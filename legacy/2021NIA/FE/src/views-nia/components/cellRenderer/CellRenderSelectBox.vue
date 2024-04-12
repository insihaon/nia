<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="default-cell w-100 h-100" style="cursor: pointer">
      <el-select v-model="authValue" multiple collapse-tags style="margin-left: 20px" filterable placeholder="권한 선택">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button class="mx-sm-3" plain size="mini" type="info" @click="onHandleRow(params)">
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
      options: [
        {
          value: 1,
          label: '사용자',
        },
        {
          value: 2,
          label: '담당자',
        },
        {
          value: 4,
          label: '관리자',
        },
      ],
      authValue: [],
    }
  },
  computed: {},
  mounted() {
    this.authValue = this.setAuthValue()
  },
  methods: {
    setAuthValue() {
      const authValue = []
      // this.params.data.lvl를 이진수 문자열로 변환
      const binaryString = (this.params.data.lvl >>> 0).toString(2)

      // 이진수 문자열을 뒤집어서 각 비트를 확인
      const reversedBinaryString = binaryString.split('').reverse().join('')

      this.options.forEach((option, index) => {
        if (reversedBinaryString[index] === '1') {
          authValue.push(option.value)
        }
      })
      return authValue
    },
    onHandleRow(params) {
      params.action(params.data, params.type, this.authValue)
    },
  },
})
</script>

<style lang="scss" scope>
.CellRenderSelectBox {
  .el-input__inner {
    height: 27px !important;
  }
  .default-cell {
    padding-top: 2px !important;
  }
  .el-select .el-input .el-select__caret {
    line-height: 10px !important;
  }
}
</style>
