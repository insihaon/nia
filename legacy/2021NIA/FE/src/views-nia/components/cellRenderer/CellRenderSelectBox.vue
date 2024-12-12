<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="default-cell w-100 h-100" style="cursor: pointer">
      <el-select v-model="authValue" multiple collapse-tags style="margin-left: 20px" filterable placeholder="권한 선택">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button class="mx-sm-3 auth-btn" plain size="mini" type="info" @click="onHandleRow(params)">
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
        { value: 1,
          label: '사용자',
        },
        { value: 2,
          label: '담당자',
        },
        { value: 4,
          label: '관리자',
        }
      ],
      authValue: [],
    }
  },
  computed: {},
  created() {
  },
  mounted() {
    // this.options = Object.values(this.CONSTANTS.userGrant).map(v => { return { value: v.value, label: v.text } })
    this.authValue = this.setAuthValue()
  },
  methods: {
    setAuthValue() {
      const authValue = []
      const binaryString = (this.params.data.lvl >>> 0).toString(2).padStart(32, '0') // 32비트로 패딩
      const reversedBinaryString = binaryString.split('').reverse()

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
    height: 25px !important;
  }
  .el-select .el-input .el-select__caret {
    line-height: 5px !important;
  }
  .auth-btn {
    padding: 5px 8px;
  }
}
</style>
