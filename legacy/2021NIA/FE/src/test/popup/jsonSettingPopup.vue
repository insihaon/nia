<template>
  <div style="height: 100%">
    <div class="popupHeader" />

    <div class="popupBody">
      <JSONEditor
        id="editor"
        ref="editor"
        :json="jsonData"
        :options="options"
        :on-change="onChange"
      />
    </div>

    <div class="popupFooter">
      <el-button v-if="!jsonEditorDisabled" size="mini" style="float: right" @click="apply">적용</el-button>
    </div>
  </div>
</template>

<script>
/* eslint-disable */
import dialogOpenMixin from "@/mixin/dialogOpenMixin";
import JSONEditor from '@/test/jsonEditor/jsonEditor.vue'

import _ from 'lodash';

export default {  
  components: { JSONEditor },
  mixins: [dialogOpenMixin],
  props: {
      wdata: Object
  },
  data() {
      return {
        jsonData: {},
        jsonEditorDisabled: false,
        options: { }
      }
  },

  computed:{
  },

  created() {
    this.jsonData = this.wdata.params.propData
    this.jsonEditorDisabled = this.wdata.params.jsonEditorDisabled || false

    if(this.jsonEditorDisabled){
      this.options.mode = 'view'
    }
  },

  methods: {
    onChange(newProp){
      this.jsonData = newProp
    },

    search(){
    },

    apply(){
      this.$emit('callback', this.jsonData) 
      this.$emit('windowClose')
    },

    handleInput(newValue){
    }
  }
}
</script>

<style lang="scss" scoped>
.el-select{
    width: 100%;
}

.popupHeader{
    height: 30px;
    line-height: 30px;
}

.popupBody::v-deep{
    overflow-y: auto;
    height: calc(100% - 80px);

    .jsoneditor{
      overflow: unset !important;
      .jsoneditor-tree{
        overflow: unset !important;
      }
    }
}
.popupFooter{
    height: 30px;
    width: calc(100% - 50px);
    line-height: 30px;
    position:absolute;
    bottom:20px;
    /* padding-left: 10px */
}




</style>