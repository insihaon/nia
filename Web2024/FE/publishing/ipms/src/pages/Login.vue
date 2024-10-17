<template>
  <div>
    <div>IPMS</div>
    <div>INTERNET PROTOCOL MANAGEMENT SYSTEM</div>
    <el-button @click="goto('/Home')">로그인</el-button>
  </div>
</template>

<script>
export default {
  name: 'Login',
  methods: {
    goto(pageName) {
      console.log(pageName)
      this.$router.push({ path: pageName })
    }
  }
}
</script>
<style scoped>
</style>
