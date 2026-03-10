<template>
  <div v-if="loginVersion === 'v1'" class="flex flex-col min-h-screen h-100" :class="{ 'bg-gray-900': darkMode, 'bg-white': !darkMode }">
    <main class="flex flex-col items-center justify-center flex-1 p-6 space-y-4">
      <div class="flex justify-center items-center">
        <h3 class="font-bold" :class="{ 'text-white': darkMode, 'text-black': !darkMode }">Login</h3>
        <i :class="[darkMode ?'el-icon-moon text-white' : 'el-icon-sunny', 'ml-1']" style="font-size: 20px" @click="darkMode = !darkMode" />
      </div>
      <div class="w-full flex flex-col max-w-sm">
        <div class="space-y-2">
          <label class="text-black" :class="{ 'text-white': darkMode }" for="username">Username</label>
          <input id="username" class="w-full h-10 rounded border border-gray-300 focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" type="text" placeholder="m@example.com" required></div>
        <div class="space-y-2">
          <label class="text-black" :class="{ 'text-white': darkMode }" for="password">Password</label>
          <input id="password" class="w-full h-10 rounded border border-gray-300" type="password" required></div>
        <el-button class="w-full rounded bg-black text-white mt-2" type="submit" style="border-color: black;" @click="onLogin()">Login</el-button>
      </div>
    </main>
    <footer class="flex flex-col gap-2 py-6 w-full shrink-0 items-center px-4 md:px-6 border-t" :class="{ 'border-white': darkMode, 'border-black': !darkMode }">
      <p class="text-xs" :class="{ 'text-white': darkMode, 'text-black': !darkMode }">© 2024 Acme Inc. All rights reserved.</p>
    </footer>
  </div>
  <div v-else class="flex flex-col gap-2 lg:flex-row lg:items-center lg:gap-6 w-full h-full">
    <div class="lg:flex w-5/12 lg:min-h-[400px] ml-5">
      <lottie-vue-player
        src="https://lottie.host/eab0227d-466a-4a37-80fe-ee35a8869bf8/SxUzYO4JPp.json"
        name="lottie"
        class="w-full h-full bg-white"
        loop
        autoplay
      />
    </div>
    <div class="mx-auto space-y-4 w-5/12 lg:space-y-6">
      <div class="space-y-2 text-center lg:text-left lg:mx-0">
        <h1 class="text-3xl font-bold">Login</h1>
      </div>
      <div class="space-y-4">
        <div class="space-y-2">
          <label
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            for="username"
          >
            Username
          </label>
          <input
            id="username"
            v-model="loginForm.username"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            placeholder="Username"
            required=""
          >
        </div>
        <div class="space-y-2">
          <label
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            for="password"
          >
            Password
          </label>
          <input
            id="password"
            v-model="loginForm.password"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            placeholder="Password"
            required=""
            type="password"
          >
        </div>
        <div class="space-y-2">
          <button
            class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm text-white font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-black hover:bg-black/90 h-10 px-4 py-2 w-full"
            type="submit"
            @click="onLogin"
          >
            Login
          </button>
        </div>
      </div>
    </div>
    <footer class="flex flex-col gap-2 pt-6 w-full shrink-0 items-center px-4 md:px-6 border-t" :class="{ 'border-white': darkMode, 'border-black': !darkMode }">
      <p class="text-xs" :class="{ 'text-white': darkMode, 'text-black': !darkMode }">© 2024 Acme Inc. All rights reserved.</p>
    </footer>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import hotkeys from 'hotkeys-js'

const routeName = 'Login'

export default {
  name: routeName,
  extends: Base,
  data() {
    return {
      darkMode: false,
      loginForm: {
        username: '',
        password: ''
      },
      loginVersion: 'v1'
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created () {
    const _THIS = this
    hotkeys(`alt+v+1`, 'debug', function (event, handler) { _THIS.onChangeVersion('v1') })
    hotkeys(`alt+v+2`, 'debug', function (event, handler) { _THIS.onChangeVersion('v2') })
  },
  methods: {
    submitForm() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // Handle form submission
        } else {
          console.log('Validation failed')
          return false
        }
      })
    },
    onLogin() {
      this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
    },
    onChangeVersion(v) {
      this.loginVersion = v
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
  },
}
  </script>
<style lang="scss" scoped>
</style>
