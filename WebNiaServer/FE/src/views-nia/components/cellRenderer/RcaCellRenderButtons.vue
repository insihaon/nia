<template>
  <div class="cell-container" :class="{ [name]: true }">
    <el-button type="text" size="small" @click.native.prevent="showObject('Show 2D Topology', params.data)"> 2D </el-button>
    <el-button type="text" size="small" @click.native.prevent="showObject('Show 3D Topology', params.data)"> 3D </el-button>
  </div>
</template>

<script>
import Vue from 'vue'

const routeName = 'CellRenderButtons'

export default Vue.extend({
  data() {
    return {
      name: routeName
    }
  },
  beforeMount() {
  },
  methods: {
    showObject(title, obj) {
      this.$message({
        showClose: true,
        dangerouslyUseHTMLString: true,
        message: `<strong>${title}</string><br><br>
          <div class="print-json">
            <pre>${JSON.stringify(obj, null, 4)}<pre>
          </div>
          `
      })
    }
  }
})
</script>
