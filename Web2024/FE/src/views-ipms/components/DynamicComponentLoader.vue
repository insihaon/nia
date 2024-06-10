<template>
  <div class="optionBox">
    <el-row class="optionRow">
      <template v-for="(component, index) in dynamicComponents">
        <component :is="component.component" v-if="component.component" :key="index" v-bind="component.props" class="optionItem" />
      </template>
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
}
</script>
<style lang="scss" scoped></style>
