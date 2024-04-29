<template>
  <div v-if="params.data.ticket_id" class="w-100 h-100" style="cursor: pointer" @click="openModal(params)">
    <i class="el-icon-search" /> 상세보기
  </div>
</template>

<script>
import Vue from 'vue'

const routeName = 'CellRenderTicketDetail'

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
    openModal(params) {
      params.action(params.data)
    }
  },
})
</script>
<style lang="scss" scope>
</style>
