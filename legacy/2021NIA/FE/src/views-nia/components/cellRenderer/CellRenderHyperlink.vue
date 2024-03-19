<template>
  <div v-if="params.value" class="cell-container" :class="{ [name]: true }">
    <div class="default-cell w-100 h-100" style="cursor: pointer;" @click="openModal(params)">
      {{ text }}
    </div>
  </div>
</template>

<script>
import Vue from 'vue'

const routeName = 'CellRenderHyperlink'

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
      params.action(params.data, params.type)
    }
  },
})
</script>

<style lang="scss" scope>
.CellRenderHyperlink {
  .default-cell {
    color: blue;
    text-decoration: underline;
    cursor: pointer;
  }
}
</style>
