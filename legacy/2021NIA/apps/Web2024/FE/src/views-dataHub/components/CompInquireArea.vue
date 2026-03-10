<template>
  <div :style="customStyle">
    <el-collapse v-model="activeName" :is-show="isShow" @change="onChangeCollapse">
      <el-collapse-item :title="searchAreaTitle" name="1">
        <div style="display: flex; flex-direction:column" class="search-area w-100" :class="{ [name]: true }">
          <el-form
            ref="inquireAreaForm"
            :label-width="domElement.labelWidth"
            :rules="formRules"
            :model="searchModel"
          >
            <el-col
              v-for="(item, index) in items"
              :key="index"
              :span="item.size"
              :style="{height: item.height}"
            >
              <el-form-item
                :label="item.label || ''"
                :class="item.class"
                :prop="item.model"
              >
                <!-- input type -->
                <el-row v-if="item.type === 'input'" type="flex">
                  <el-input
                    v-model="searchModel[item.model]"
                    clearable
                    :type="item.inputType"
                    :placeholder="item.placeholder"
                    :disabled="item.disabled"
                    :readonly="item.readonly"
                    @keyup.native.enter="$emit('keyupEnter')"
                  />
                </el-row>

                <CompTreeSelector
                  v-if="item.type==='tree'"
                  ref="treeSelector"
                  :search-model="searchModel"
                  :item="item"
                />

                <!-- check select -->
                <CompCheckSelector
                  v-if="item.type === 'checkSelect'"
                  ref="checkSelector"
                  :item="item"
                  :search-model="searchModel"
                  @selectedChange="(value)=> $emit('selectedChange', value, {item, changeType: 'checkSelect'})"
                />

                <!-- select type -->
                <el-select
                  v-if="item.type === 'select'"
                  v-model="searchModel[item.model]"
                  :disabled="item.disabled"
                  :readonly="item.readonly || item.readonly"
                  :collapse-tags="isViewport('<', 'sm')"
                  @change="(value)=> $emit('selectedChange', value, {item, changeType: 'select'})"
                >
                  <el-option
                    v-for="(option, i) in item.options"
                    :key="i"
                    :label="option.label"
                    :value="option.value"
                  />
                </el-select>

                <!-- date type -->
                <el-row v-if="item.type === 'date'" type="flex">
                  <el-date-picker
                    v-model="searchModel[item.model]"
                    :disabled="item.disabled"
                    :readonly="item.readonly"
                    type="date"
                    class="w-100"
                  />
                </el-row>

                <el-row v-if="item.type === 'datetime'" type="flex">
                  <el-date-picker
                    v-model="searchModel[item.model]"
                    :disabled="item.disabled"
                    :readonly="item.readonly"
                    type="datetime"
                    class="w-100"
                  />
                </el-row>

                <!-- slot -->
                <el-row v-if="item.type === 'slot'" class="itemSlot" type="flex">
                  <slot :name="item.slotName" />
                </el-row>

                <div v-if="item.type === 'hr'" style="flex-grow: 1">
                  <hr style="width:100%">
                </div>

                <el-row v-if="item.type === 'blank'" type="flex" />

              </el-form-item>
            </el-col>
          </el-form>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>

import { Base } from '@/min/Base.min'
import CompCheckSelector from '@/views-t3d/components/CompCheckSelector'
import CompTreeSelector from '@/views-t3d/components/CompTreeSelector'
import { rulesRequireAam, rulesPhone, rulesIp, rulesUsername, rulesNodeName } from '@/utils/validate'

const routeName = 'CompInquireArea'

export default {
  name: routeName,
  components: { CompCheckSelector, CompTreeSelector },
  extends: Base,
  props: {
    node: {
      type: Object,
      default() { return {} }
    },
    items: {
      type: Array,
      default() { return [] }
    },
    searchAreaTitle: {
      type: String,
      default() { return '' }
    },
    searchModel: {
      type: Object,
      default() { return {} }
    },
    isShow: {
      type: String,
      default() { return '' }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      activeName: ['1'],
      testValue: []

    }
  },

  computed: {
    customStyle() {
      return {
        '--active-tab': this.searchAreaTitle && this.searchAreaTitle?.length > 0 ? 'flex' : 'none'
      }
    },

    formRules() {
      const Rule = this.items.reduce((acc, data, i) => {
        if (data.ruleProp) {
          switch (data.ruleProp) {
            case 'rulesRequire': acc[data.model] = rulesRequireAam(data.label); break
            case 'rulesPhone': acc[data.model] = rulesPhone(); break
            case 'rulesIp': acc[data.model] = rulesIp(); break
            case 'rulesUsername': acc[data.model] = rulesUsername(); break
            case 'rulesNodeName': acc[data.model] = rulesNodeName(); break
          }
        }
        return acc
      }, {})

      return Rule
    }

  },
  methods: {
    onChangeCollapse(value) {
      this.$emit('onChangeCollapse', value)
    },

    checkValidate() { /* ref 해서 사용 */
      let res = false

      this.$refs.inquireAreaForm.validate((valid) => {
        if (valid) {
          res = true
        } else {
          res = false
          this.$message('조회조건을 확인하세요')
        }
      })
      return res
    }

  }
}
</script>

<style lang="scss" scope>
@import '~@/styles/variables.scss';
@import '~@/styles/selectTree.scss';

.el-dialog__body{
  padding: 10px 10px;
}

.el-form-item__error{
  display: none;
  top: 30%;
  left: 30px;
}

.el-collapse{
  border-top: none;

  .el-collapse-item__header {
    display: var(--active-tab);
  }

  .el-collapse-item__wrap {
    border-bottom: none;
    overflow: visible !important;

    &.v-leave-active.collapse-transition.v-leave-to{
      transition: 0.3s;

      overflow: hidden !important;
    }
  }

  .el-select-dropdown.is-multiple .el-select-dropdown__item.selected::after{
    display: none;
  }

  .el-collapse-item__header{
    color: #FFF;
    font-size: 14px;
    font-family: "NanumSquare";
    padding-left: 10px;
    background-color:#484848ff;
    cursor: pointer;
    width: 100%;
    border-radius: 3px;
    height: 35px;
    margin-bottom: 3px;
  }

  .el-collapse-item__content{
    padding-bottom: 0px;
  }

}

.CompInquireArea {
  font-family: "NanumSquare";

  .el-input.is-disabled{
    input{
      margin-left: 0 !important;
    }
  }

  &.search-area {
    .el-col{
      height: 35px;
    }

    .el-form-item {
      height: 100%;
    }

    .el-form-item__content {
      flex-grow: 1;
      padding-bottom: 0px !important;
      padding-right: 5px;
    }

    .el-form-item__label{
      color:rgba(0, 0, 0, 0.822);
      border-radius: 3px;
      font-size: 13px;
      // padding-left: 8px;
    }

      .el-form-item__content {
        // margin-left: 100px !important;

      .el-input__inner {
        height: 30px;
      }

      input.el-input__inner {
        width: 100% !important;
      }

      .el-date-editor.el-input {
        width: 100% !important;
      }

      .el-select {
        width: 100%;
      }
    }
  }

  .search-btn {
    width: 130px;
    padding: 0 8px;

    button:not(.is-disable).cp-button {
      width: 115px;
    }
  }
}

// .vue-treeselect__menu-container{
//   width: 100%;
//   .vue-treeselect__menu{
//     overflow: visible;
//     width: 100%;
//     .vue-treeselect__list{
//       position: absolute;
//       width: 100%;
//       z-index: 9999;
//       background-color:white
//     }
//   }
// }

</style>
