<template>
  <div style="color: blue; cursor: pointer; text-decoration-line: underline;" @click="onClickHyperLinkText">
    {{ params.value }}
  </div>

</template>

<script>
const routeName = 'hyperLinkTextRender'

export default {
  name: routeName,
  data() {
    return {
      name: routeName
    }
  },

  methods: {
    onClickHyperLinkText() {
      // let rowData
      // switch (this.params.data ? '일반' : '그룹') {
      //   case '일반':
      //     rowData = this.params.data
      //     break
      //   case '그룹':
      //     rowData = Object.assign(this.params.node.aggData, {
      //       'GROUP_PROP': this.params.node.key
      //     })
      //     break
      //   default:
      //     throw new Error('예상치 못한 에러')
      // }

      this.params.clickFn(this.params)
    }
  }

}

</script>
