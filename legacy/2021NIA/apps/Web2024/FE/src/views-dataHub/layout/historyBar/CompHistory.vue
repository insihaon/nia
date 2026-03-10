<template>
  <!-- <div :class="{ [name]: true }" /> -->
  <!-- 잠정 보류 -->
  <div class="side-bar" style="height:82vh">
    <!--  -->

  </div>
</template>

<script>

import { Base } from '@/min/Base.min'

const routeName = 'CompHistory'
export default {
  name: routeName,
  components: {

  },
  extends: Base,

  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,

    }
  },

  computed: {

  },
  watch: {
  },
  mounted() {

  },

  created() {

  },

  methods: {

}

  }
</script>

<style lang="scss" scoped>
  @import '~@/styles/variables.scss';

.CompHistory {
  font-family: "NanumSquare";
  padding: 25px;
  overflow: hidden !important;
  user-select: none;

.side-bar {
  position: fixed;
  top: 0;
  left: -250px;
  width: 250px;
  height: 100%;
  background-color: #f4f4f4;
  transition: all 0.3s ease-in-out;
}

.side-bar:hover {
  left: 0;
}

}

</style>

