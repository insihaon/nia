<template>
  <div class="searchOptionWrap">
    <table>
      <tr v-for="(componentRow, index) in chunkedList" :key="index">
        <template v-for="(component, itemIndex) in componentRow">
          <component
            :is="component.component"
            v-if="component.component"
            :ref="component.key"
            :key="itemIndex"
            v-bind.sync="component.props"
            @set-value="value => onUpdateOrgValue(index, value)"
            @update-value="keyValues => onUpdateKeyValue(keyValues)"
          />
        </template>
        <td v-if="(index + 1) === chunkedList.length && getEmptyColNum > 0" :colspan="getEmptyColNum * 2"></td>
      </tr>
      <tr>
        <td colspan="6">
          <div class="searchBtnWrap">
            <SearchConditionSaver v-if="isShowProfile" ref="SearchConditionSaver" :parameter="requestParameter" :prop-name="propName" />
            <el-button type="info" icon="el-icon-refresh" size="mini" style="margin-left:0px" round @click="handleRefresh()">초기화</el-button>
            <el-button type="primary" icon="el-icon-search" size="mini" round @click="handleSearch()">조회</el-button>
          </div>
        </td>
      </tr>
    </table>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import { componentMap } from '@/views-ipms/SearchComponentMapping'
import SearchConditionSaver from '@/views-ipms/components/SearchConditionSaver'

const routeName = 'DynamicComponentLoader'

export default {
  name: routeName,
  components: { SearchConditionSaver },
  extends: Base,
  props: {
    propName: {
      type: String,
      default: ''
    },
    componentKeys: {
      type: Array,
      required: true,
    },
    isShowProfile: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      dynamicComponents: [],
      defaultRequestParameter: {},
      requestParameter: {},
      loopUpdateValueCount: 0,
      columnsPerRow: 3
    }
  },
  computed: {
    chunkedList() {
      // Function to split colList into chunks of 3 items
      const chunkedArray = []
      for (let i = 0; i < this.dynamicComponents.length; i += this.columnsPerRow) {
        chunkedArray.push(this.dynamicComponents.slice(i, i + this.columnsPerRow))
      }
      return chunkedArray
    },
    getEmptyColNum() {
      if (this.dynamicComponents.length !== 1 && (this.dynamicComponents.length % this.columnsPerRow !== 0)) {
        return this.columnsPerRow - (this.dynamicComponents.length % this.columnsPerRow)
      } else {
        return 0
      }
    }
  },
  watch: {
    componentKeys: {
      handler(newKeys) {
        this.dynamicComponents = []
        newKeys.forEach((keyObj, index) => {
          const { key, props = {} } = keyObj
          if (componentMap[key]) {
            componentMap[key]()
              .then((component) => {
                this.$set(this.dynamicComponents, index, { component: component.default, props, key })
              })
              .catch((error) => {
                console.error(`Failed to load component for key ${key}:`, error)
                this.$set(this.dynamicComponents, index, { component: null, props })
              })
          } else {
            console.warn(`No component found for key ${key}`)
            this.$set(this.dynamicComponents, index, { component: null, props })
          }
        })
      },
      immediate: true
    }
  },
  mounted() {
    Eventbus.$on(EventType.setSavedParameter, (params) => { this.setParameter(params) })
  },
  beforeDestroy() {
    Eventbus.$off(EventType.setSavedParameter)
  },
  destroyed() {
    // view키별로 구분해서 값 셋팅해야함 (modal이랑 중첩될 때 버그)
    // this.$store.dispatch('ipms/resetCurrentCondition')
  },
  methods: {
    handleRefresh() {
      Eventbus.$emit(EventType.resetCondition)
      this.requestParameter = this._cloneDeep(this.defaultRequestParameter)
      this.$emit('handle-search', this.requestParameter)
    },
    handleSearch() {
      this.$emit('handle-search', this.requestParameter)
    },
    handleClickExcel() {
      this.$emit('save-excel', this.requestParameter)
    },
    onUpdateOrgValue(index, value) {
      this.dynamicComponents[index].props.value = value
    },
    onUpdateKeyValue(keyValues = []) {
      if (Array.isArray(keyValues) && keyValues.length > 0) {
        keyValues.forEach(obj => {
          this.requestParameter[obj.key] = obj.value
        })
        this.$store.dispatch('ipms/mergeCurrentCondition', this.requestParameter)
        if (this.loopUpdateValueCount !== null) {
          this.loopUpdateValueCount += 1
        }
      }
      if (this.loopUpdateValueCount !== null && this.loopUpdateValueCount === this.componentKeys.length) {
        this.defaultRequestParameter = this._cloneDeep(this.requestParameter)
        this.loopUpdateValueCount = null
      }
    },
    setParameter(parameter) {
      this.$set(this, 'requestParameter', parameter)
    }
  },
}
</script>
<style lang="scss" scoped></style>
