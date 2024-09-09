<template>
  <div class="optionBox">
    <el-row class="optionRow">
      <template v-for="(component, index) in dynamicComponents">
        <component
          :is="component.component"
          v-if="component.component"
          :ref="component.key"
          :key="index"
          v-bind="component.props"
          class="optionItem"
          @set-value="value => onUpdateOrgValue(index, value)"
          @update-value="keyValues => onUpdateKeyValue(keyValues)"
        />
        <!-- componentKeys[index].key, value -->
      </template>
    </el-row>
    <el-row>
      <el-col :span="24" class="searchBtnGroup">
        <div v-if="isShowProfile">
          <SearchConditionSaver ref="SearchConditionSaver" :parameter="requestParameter" :prop-name="propName" />
        </div>
        <div>
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="handleSearch()">
            조회
          </el-button>
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh">
            초기화
          </el-button>
          <el-button type="button" size="mini" class="export-excel" icon="el-icon-download" @click="handleClickExcel()">
            엑셀 저장
          </el-button>
        </div>
        <slot name="add-function" />
      </el-col>
    </el-row>
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
      requestParameter: {}
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
  methods: {
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
        // this.$store.dispatch('ipms/setCurProfileByVue', { key: this.propName, profileName: '' })
      }
    },
    setParameter(parameter) {
      this.$set(this, 'requestParameter', parameter)
    }
  },
}
</script>
<style lang="scss" scoped></style>
