<template>
  <li v-if="!item.hidden" :class="[{ ['sub'+idx]: true }]">
    <template v-if="hasOneShowingAll(item.children,item)">
      <router-link :to="resolvePath(onlyOneChild.path)">
        {{ item.meta.title }}
      </router-link>
    </template>
    <ul>
      <child-item
        v-for="(child, i) in item.children"
        :key="child.path"
        :base-path="resolvePath(child.path)"
        :item="child"
        :idx="idx+'_'+i"
      />
    </ul>
  </li>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'

const routeName = 'ChildItem'
export default {
  name: routeName,
  components: { },
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    idx: {
      type: String,
      required: true
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    this.onlyOneChild = null
    return {
      bgColor: '#101827',
      textColor: '#bfcbd9',
      buttonDisabled: false
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
    hasOneShowingAll(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })
      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length > 0) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }
    },
    handleButtonClick() {
      // Handle button click event
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    },
  }
}
</script>

<style lang="scss" scoped>
  li {
    color: black;
    padding: 3px 0px 0px 15px;
  }
  li > a {
    display: block;
    font-weight: 600;
    color: black;
    font-size: 18px;
  }
  li > ul > li {
    margin-top: 5px;
    padding-left: 5px;
    a {
      font-size: 16px;
      font-weight: 600;
      color: #141414;
    }
  }
  li > ul > li > ul > li {
    a {
      font-size: 14px;
      padding-left: 5px;
      color: gray;
    }
  }
</style>
