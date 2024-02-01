<template>
  <div
    class="optionBoxItem"
  >
    <el-tooltip :content="objectKey" placement="top">
      <el-input
        v-model="objectKey"
        class="bg_purple"
        style="
          min-width: 180px !important; max-width: 180px;
          overflow:hidden;
          text-overflow:ellipsis;
          "
        size="mini"
        :disabled="true"
      />
    </el-tooltip>

    <div class="optionBoxForm" style="padding: 0px 10px; display: flex; ">
      <!-- data의 type 변경 -->
      <div v-if="typeChangeSelectorToggle" style="width: 120px">
        <el-select
          v-model="typeComponentData.type"
          size="mini"
          class="typeChanger"
          @change="typeChange"
        >
          <el-option
            v-for="(item, index) in typeChangeConfig.options"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div ref="contentBox" class="contentBox">
        <el-checkbox v-if="typeComponentData.type === 'boolean'" v-model="tempPropData" />
        <el-input v-else-if="typeComponentData.type === 'string'" v-model="tempPropData" size="mini" :debounce="1000" />
        <el-input-number v-else-if="typeComponentData.type === 'number'" v-model="tempPropData" size="mini" :max="100" />
        <div v-else-if="typeComponentData.type === 'undefined'">prop의 default값이 없음 </div>
        <div v-else-if="typeComponentData.type === 'function'">function (미개발)</div>
        <div v-else-if="typeComponentData.type === 'object' || typeComponentData.type === 'array'">
          <el-button size="mini" @click="openPopup('jsonSettingPopup', componentProp)">
            JSON 데이터 <span style="color: blue; font-weight: bold;"> {{ jsonEditorDisabled ? '확인' : '편집' }}</span>
          </el-button>
        </div>
        <div v-else-if="typeComponentData.type === 'multitype'">multitype (미개발)</div>
        <div v-else>{{ typeComponentData.type }} 타입에 대한 처리를 재정의 해야합니다.</div>
      </div>
      <div v-if="visibleDeleteButton" style="width:150px">
        <el-button size="mini" @click="$emit('deleteTypeComponentData')">삭제</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { getConvertSinglePropToSingleComponentTypeData, getDataTypeDefaultValue, makeSelectTypeOptions } from '@/test/commonTester'
import _ from 'lodash'

export default {
    mixins: [dialogOpenMixin],
    props: {
        propKey: { type: String, default: '' },
        propData: { type: null, default: () => { return {} } },
        typeChangeSelectorToggle: { type: Boolean, default: false },
        visibleDeleteButton: { type: Boolean, default: false },
        jsonEditorDisabled: { type: Boolean, default: false }
    },

    data() {
        return {
          typeChangeConfig: {
            soptions: []
          }
        }
    },

    computed: {
      objectKey: {
        get() {
          return this.typeComponentData.objectKey
        },

        set(data) {
          this.$emit('update:propKey', data)
        }

      },

      tempPropData: {
        get() {
          return this.propData
        },

        set(data) {
          this.typeComponentData.dataValue = data
          this.$emit('changeDataValue', this.typeComponentData)
        }
      },

      componentProp: {
        get() {
          return { [this.propKey]: this.tempPropData }
        }
      },

      typeComponentData: {
        get() {
          return getConvertSinglePropToSingleComponentTypeData(this.componentProp)
        }
      }
    },

    created() {
        this.typeChangeConfig.options = makeSelectTypeOptions()
    },

    methods: {

      fn_projectDetailCallback(prop) {
        const componentTypeData = getConvertSinglePropToSingleComponentTypeData(prop)
        this.$emit('changeDataValue', componentTypeData)
      },

      openPopup(pageName, componentProp) {
        this.fn_openWindow(pageName, { propData: componentProp, jsonEditorDisabled: this.jsonEditorDisabled }, this.fn_projectDetailCallback)
      },

      typeChange(type) {
        this.tempPropData = getDataTypeDefaultValue(type)
      }
    }
}
</script>

<style lang="scss" scoped>

.optionBoxItem::v-deep{
  display: flex; margin-bottom:5px; align-items: center; justify-content: left; width:100%;
  box-sizing: border-box;

  .contentBox{
    width: 100%;
    text-align: left;
    padding-left: 10px;
  }

  .typeChanger.el-select{
      width: 120px;
  }

  .bg_purple{
    /* background: #d3dce6; */
    background: white;
    height: 28px;

    .el-input__inner{
      background: white !important;
      background-color: white !important;
      font-weight: bold;
      color:black;
      font-size:13px;
    }
  }

  &>label{
    white-space: nowrap;
    padding-left:10px;
    width:95px;
    display: inline-block;
    min-width: 95px;
    font-size:12px;
    display:inline-block;
    text-align:left;
    box-sizing: border-box;
    color:#000000;
    border-right:1px solid rgba(0,0,0,0.2);
    margin-right:10px;
    font-weight: 400;
    letter-spacing: -0.25px;
    background-image: linear-gradient(90deg, rgba(0,0,0,0.0), rgba(0,0,0,0.05));
    padding-top:4px; padding-bottom:4px;
    position:relative;
    text-indent: 5px;
  }

  div.optionBoxForm{
    width:100%;
  }
  div.optionBoxForm>div{
    white-space: nowrap;
  }

  div.optionBoxForm>div{
    white-space: nowrap;
  }

  div.optionBoxForm{
    width:100%;
  }
  div.optionBoxForm>div{
    white-space: nowrap;
  }
}

</style>

<style>
  [data-tooltip]{
    background:red;
    position:relative;
  }

  [data-tooltip]:before, [data-tooltip]:after{
    /* visibility:hidden; */
    /* opacity:0; */
    position:absolute;
    left:50%;
    transform:translateX(-50%);
    white-space:nowrap;
    transition:all .2s ease;
    font-size:11px;
    font-family:dotum;
    letter-spacing:-1px;
  }
  [data-tooltip]:before{
    content:attr(data-tooltip);
    height:13px;
    position:absolute;
    top:-20px;
    padding:5px 10px;
    border-radius:5px;
    color:#fff;
    background:#025272;
    box-shadow:0 3px 8px rgba(165, 165, 165, 0.5);
  }
  [data-tooltip]:after{
    content: '';
    border-left:5px solid transparent;
    top:2px;
    border-right:5px solid transparent;
    border-top:5px solid #025272;
  }
  /* [data-tooltip]:not([data-tooltip=""]):hover:before{
    visibility:visible;
    opacity:1;
    top:-30px
  }
  [data-tooltip]:not([data-tooltip=""]):hover:after{
    visibility:visible;
    opacity:1;
    top:-8px
  } */
</style>
