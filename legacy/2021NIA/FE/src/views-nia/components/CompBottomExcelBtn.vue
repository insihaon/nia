<template>
  <el-button :class="{ [name]: true }" size="mini" class="excelBtn">
    <i class="el-icon-document" />
    {{ text }}
  </el-button>
</template>

<script>
const routeName = 'CompBottomExcelBtn'

export default {
  name: routeName,
  components: { },
  props: {
    text: {
      type: String,
      default: '엑셀 저장'
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`
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

<style lang="scss" scope>
button:not(.is-disable).excelBtn.CompBottomExcelBtn {
    background-color: white;
    color: green;
    border: 1px solid #7ebe7e;
    transition: all 0.3s;
    padding: 5px;
    font-weight: 600;
  }

button:not(.is-disable).excelBtn.CompBottomExcelBtn:hover {
  background-color: #00800014;
}
</style>
