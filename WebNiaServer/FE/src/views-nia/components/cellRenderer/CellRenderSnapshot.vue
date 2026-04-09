<template>
  <div class="w-100 h-100" :class="{ [name]: true }" @click="onClickEvent(params)">
    <i :class="params.type === 'downroadUrl' ?'el-icon-download':'el-icon-delete'" />
  </div>
</template>

<script>
import Vue from 'vue'

const routeName = 'CellRenderSnapshot'

export default Vue.extend({
  data() {
    return {
      name: routeName
    }
  },
  computed: {
    text() {
      return this.params.value
    }
  },
  methods: {
    onClickEvent(params) {
      params.action(params.data)
    }
  },
})
</script>

<style lang="scss" scoped>
.CellRenderSnapshot ::v-deep {
  &:hover {
    cursor: pointer;
    i {
      font-weight: bold;
    }
  }
}
</style>
