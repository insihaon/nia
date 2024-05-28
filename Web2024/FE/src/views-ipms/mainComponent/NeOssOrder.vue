<template>
  <!-- content -->
  <div>
    NeOssOrder
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'NeOssOrder'

export default {
    name: routeName,
    components: {},
    extends: Base,
    data() {
        return {
        name: routeName,
        src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        }
    },
}
</script>
<style lang="css" scoped>
</style>
