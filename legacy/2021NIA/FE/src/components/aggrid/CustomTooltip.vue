<template>
  <div class="custom-tooltip">
    <p><span>{{ athlete }}</span></p>
    <p><span>Country: </span>{{ country }}</p>
    <p><span>Total: </span>{{ total }}</p>
  </div>
</template>

<script>
import Vue from 'vue'

export default Vue.extend({
  data: function() {
    return {
      color: null,
      athlete: null,
      country: null,
      total: null
    }
  },
  beforeMount() {
    const { data } = this.params.api.getDisplayedRowAtIndex(this.params.rowIndex)
    data.color = this.params.color || 'white'
    this.setState(data)
  },
  mounted() {
    this.$el.style['background-color'] = this.color
  },
  methods: {
    setState(obj) {
      const that = this

      Object.keys(obj).forEach(function(key) {
        that[key] = obj[key]
      })
    }
  }
})
</script>
