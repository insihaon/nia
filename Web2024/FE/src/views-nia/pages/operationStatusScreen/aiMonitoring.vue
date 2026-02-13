<template>
  <div :class="{ [name]: true }">
    <!-- src="http://116.89.191.45:82/" -->
    <iframe
      sandbox="allow-forms allow-same-origin allow-scripts"
      scrolling="yes"
      :src="$store.state.app.server.aiMonitorUrl"
      width="100%"
      height="100%"
      frameBorder="0"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'

const routeName = 'AiMonitoring'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
    }
  },
  computed: {
  },
  mounted() {
  },
  methods: {

  }
}
</script>
