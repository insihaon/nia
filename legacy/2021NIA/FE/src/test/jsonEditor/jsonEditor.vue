<template>
  <div ref="jsoneditor" />
</template>

<script>

import JSONEditor from '@/test/jsonEditor/jsoneditor.js'
import '@/test/jsonEditor/jsoneditor.css'
import _ from 'lodash'

/* eslint-disable */
export default {
  name: 'JsonEditor',
  props: {
    json: {
      required: true,
    },
    options: {
      type: Object,
      default: () => {
        return {}
      }
    },
    onChange: {
      type: Function
    }
  },
  data () {
    return {
      editor: null
    }
  },
  watch: {
    json: {
      handler (newJson) {
        // if (this.editor) {
        //   this.editor.set(newJson)
        // }
      },
      deep: true
    }
  },
  mounted () {
    const container = this.$refs.jsoneditor
    const options = _.extend({
      onChange: this._onChange
    }, this.options)

    this.editor = new JSONEditor(container, options)
    this.editor.set(this.json)
  },
  beforeDestroy () {
    if (this.editor) {
      this.editor.destroy()
      this.editor = null
    }
  },
  methods: {
    _onChange (e) {
      if (this.onChange && this.editor) {
        this.onChange(this.editor.get())
      }
    }
  }
}
</script>

<style lang="scss">
// .jsoneditor-menu{
//   li{
//     button{
//       text-align: center !important;
//       background: red;
//       #text{
//         margin-right: 10px;
//       }
//     }
//   }
// }

</style>
