<template>
  <div :class="{ [name]: true }">
    <div class="search-container">
      <div class="optionBox mb-xl-2" :class="{'minizeOption': minizeOption }">
        <!-- 조회 옵션상자 -->
        <el-row class="optionRow">
          <el-col v-for="(item, index) in items" :key="index" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <div class="optionItem">
              <label style="width : 100px">
                {{ item.label }}
              </label>
              <div>
                <!-- INPUT -->
                <el-input
                  v-if="item.type === 'input'"
                  v-model="searchModel[item.model]"
                  type="text"
                  size="mini"
                  clearable
                  :disabled="item.disabled === true"
                  :placeholder="item.placeholder"
                  @keyup.native.enter="$emit('keyupEnter', searchModel)"
                />
                <!-- SELECT -->
                <el-select
                  v-if="item.type === 'select' && !item.multiple"
                  v-model="searchModel[item.model]"
                  collapse-tags
                  filterable
                  clearable
                  size="mini"
                  :placeholder="item.placeholder"
                  reserve-keyword
                  remote
                >
                  <el-option
                    v-for="(option, i) in item.options"
                    :key="i"
                    :label="option.label"
                    :value="option.value"
                  />
                </el-select>
                <!-- DATE_RANGE -->
                <el-date-picker
                  v-if="item.type === 'date'"
                  v-model="searchModel[item.model]"
                  type="daterange"
                  size="mini"
                  start-placeholder="시작 일자"
                  end-placeholder="종료 일자"
                  :default-time="['00:00:00','23:59:59']"
                  :disabled="item.disabled === true"
                />
                <!-- DATE_TIME -->
                <el-date-picker
                  v-if="item.type === 'dateTime'"
                  v-model="searchModel[item.model]"
                  type="datetimerange"
                  size="mini"
                  range-separator="to"
                  start-placeholder="Start date"
                  end-placeholder="End date"
                  :disabled="item.disabled === true"
                />
                <!-- DATE -->
                <el-date-picker
                  v-if="item.type === 'basicDate'"
                  v-model="searchModel[item.model]"
                  type="date"
                  size="mini"
                />
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" align="center" class="searchBtnGroup" :class="{'is-mobile': isMobile}">
            <el-button class="btn-r" type="info" size="mini" icon="el-icon-search">
              조회
            </el-button>
            <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh">
              초기화
            </el-button>
            <el-button v-if="isExcel" type="button" size="mini" class="excel-form-export" icon="el-icon-download">
              엑셀 저장
            </el-button>
            <slot name="add-function" />
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import { mdiBugOutline } from '@mdi/js'

const routeName = 'CompInquiryPannel'
export default {
  name: routeName,
  extends: Base,
  props: {
    isModal: {
      type: Boolean,
      default: false
    },
    isExcel: {
      type: Boolean,
      default: false
    },
    items: {
      type: Array,
      default() { return [] }
    },
    searchModel: {
      type: Object,
      default() { return {} }
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedItem: [],
      path: mdiBugOutline,
      minizeOption: false
    }
  },
  methods: {
    toggleMinizeOption() {
      this.minizeOption = !this.minizeOption
    },
    getHeight() {
      return this.$parent?.$parent?.name?.includes('Modal') || this.isModal ? '500px' : '100%'
    },
  }
}
</script>
<style lang="scss" scoped>
.CompInquiryPannel {
  height: auto;
  ::v-deep .el-range-editor.el-input__inner {
    width: 100%;
  }
}
</style>
