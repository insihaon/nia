<template>
  <div :class="{ [name]: true }">
    dashboard
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'NiaMain'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
    }
  },
  computed: {
  },
  mounted () {
  },
  created () {
  },
  methods: {

  }
}
</script>
<style lang="scss" scoped>
@import "~@/styles/variables.scss";
</style>
