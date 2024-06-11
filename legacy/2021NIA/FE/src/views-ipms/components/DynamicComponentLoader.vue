<template>
  <div class="optionBox">
    <el-row class="optionRow">
      <template v-for="(component, index) in dynamicComponents">
        <component
          :is="component.component"
          v-if="component.component"
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
      <el-col :span="24" align="center" class="searchBtnGroup">
        <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="onClickSearch()">
          조회
        </el-button>
        <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh">
          초기화
        </el-button>
        <!-- <el-button type="button" size="mini" class="excel-form-export" icon="el-icon-download">
          엑셀 저장
        </el-button> -->
        <slot name="add-function" />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import { componentMap } from '@/views-ipms/SearchComponentMapping'

const routeName = 'DynamicComponentLoader'

export default {
  name: routeName,
  extends: Base,
  props: {
    componentKeys: {
      type: Array,
      required: true,
    },
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
      immediate: true,
      handler(newKeys) {
        this.dynamicComponents = []
        newKeys.forEach((keyObj, index) => {
          const { key, props = {} } = keyObj
          if (componentMap[key]) {
            componentMap[key]()
              .then((component) => {
                this.$set(this.dynamicComponents, index, { component: component.default, props })
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
      }
    }
  },
  methods: {
    onClickSearch() {
      const compKeys = this.componentKeys
      console.log(compKeys)
    },
    onUpdateOrgValue(index, value) {
      this.dynamicComponents[index].props.value = value
    },
    onUpdateKeyValue(keyValues = []) {
      if (Array.isArray(keyValues) && keyValues.length > 0) {
        keyValues.forEach(obj => {
          this.requestParameter[obj.key] = obj.value
        })
      }
    },
  },
}
</script>
<style lang="scss" scoped></style>
