<template>
  <div class="login-container h-full d-flex justify-center flex-column">
    <video muted autoplay loop>
      <source src="@/assets/video/technology_network.mp4" type="video/mp4">
    </video>
    <div class="browserInfo" title="크롬 브라우저 다운로드 페이지로 이동합니다.">
      <img src="@/assets/icon/icon_chrome.png" @click="onClickDownloadChrome()">
      <span>NIA KOREN은 Chrome Browser 및 1920x1080 해상도에 최적화 되어 있습니다.</span>
    </div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form d-flex items-center flex-column" autocomplete="on" label-position="left">
      <div class="title-container d-flex justify-center flex-column items-center pb-4">
        <img src="@/assets/images/nia/login_logo_koren.png">
        <span>AI기반 KOREN 모니터링 시스템</span>
        <span class="sub-title">AI based KOREN Monitoring System</span>
      </div>
      <el-form-item prop="username">
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="아이디를 입력해주세요"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
        /></el-form-item>
      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" effect="light">
        <el-form-item prop="password">
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            placeholder="비밀번호를 입력해주세요"
            tabindex="2"
            autocomplete="on"
            name="password"
            show-password
            type="password"
            @blur="capsTooltip = false"
            @keyup.native="checkCapslock"
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
      </el-tooltip>

      <el-button :loading="loading" @click="handleLogin">Login</el-button>
    </el-form>
    <div class="bottom">
      <span>(주)코드제이 대전광역시 유성구 관평동 803</span>
      <span>Copyright© ## All rights reserved.</span>
      <div class="logo gap-y-12">
        <img src="@/assets/images/nia/login_bottom_logo_1.png">
        <img src="@/assets/images/nia/login_bottom_logo_2.png">
      </div>
    </div>
  </div>
</template>

<script>
import Encrypt from '@/assets/libs/Encrypt.min'
import { Base } from '@/min/Base.min'
import { AppOptions } from '@/class/appOptions'
import { rulesUsername, rulesPassword } from '@/utils/validate'
import { onDownloadChrome } from '@/utils/index'

const routeName = 'Login'

export default {
  name: routeName,
  extends: Base,
  data() {
    return {
      name: routeName,
      loginForm: {
        username: null,
        password: null
      },
      loginRules: {
        username: rulesUsername(),
        password: rulesPassword()
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      otherQuery: {}
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
  created() {
    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    // Encrypt.testEncryptToDecrypt2()
    const remember = Encrypt.toDecrypt(AppOptions.instance.lastUser)
    this.loginForm.username = remember?.id
    this.loginForm.password = remember?.pw
    if (!this.loginForm.username) {
      this.$refs.username.focus()
    } else if (!this.loginForm.password) {
      this.$refs.password.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin(event) {
      this.$refs.loginForm.validate(async valid => {
        if (!valid) return

        try {
          this.loading = true
          await this.$store.dispatch('user/login', Object.assign(this.loginForm, { ctrlKey: event.ctrlKey }))
          this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
        } catch (error) {
          return false
        } finally {
          this.loading = false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    },
    onClickDownloadChrome() {
      onDownloadChrome()
    }
    // afterQRScan() {
    //   if (e.key === 'x-admin-oauth-code') {
    //     const code = getQueryObject(e.newValue)
    //     const codeMap = {
    //       wechat: 'code',
    //       tencent: 'code'
    //     }
    //     const type = codeMap[this.auth_type]
    //     const codeName = code[type]
    //     if (codeName) {
    //       this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
    //         this.$router.push({ path: this.redirect || '/' })
    //       })
    //     } else {
    //       alert('로그인 실패')
    //     }
    //   }
    // }
  }
}
</script>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  background-color: #002c55;
  font-family: NanumSquare,sans-serif;
  align-items: center;

  video {
    position:absolute;
    width:100%;
    height:100%;
    object-fit: cover;
    pointer-events: none;
    user-select: none;
    opacity: 0.25;
  }
  .browserInfo {
    position: absolute;
    right: 40px;
    top: 40px;
    width: 230px;
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: default;

    img {
      padding: 0 20px;
      transition: all .5s;
      cursor: pointer;
      filter: grayscale(100%);
    }

    span{
      display:inline-block;
      font-size: 12px;
      transform: rotate(.03deg);
      text-align: center;
      letter-spacing: -.25px;
      color: #fff;
      margin-top: 5px;
      font-weight: 600;
    }
    &:hover > img {
      filter: none;
      transform: rotate(360deg);
      transition: all 0.5s;
    }
  }
  .login-form {
    z-index: 0;
    .title-container {
      color: #fff;
      font-size: 42px;
      font-weight: 800;
      text-align: center;
      .sub-title {
        font-size: 27px;
        font-weight: 200;
        white-space: nowrap;
      }
    }
    .el-form-item {
      margin-bottom: 17px;
      .show-pwd {
        position: absolute;
        top: 10px;
        right: 10px;
        color: black;
      }
    }
    ::v-deep .el-input__inner {
      width: 330px;
      height: 45px;
      border-radius: 25px;
      padding: 0px 40px;
      color: black;
      font-family: monospace;
      background-color: #ffffffcc;
      &:focus {
        background-color: #fff;
      }
    }
    ::v-deep .el-button {
      width: 330px;
      height: 45px;
      border-radius: 25px;
      background-color: #609fe6;
      font-weight: 900;
      color: #141414;
    }
  }
  .bottom {
    position: fixed;
    bottom: 0px;
    width: 100%;
    .logo {
      display: flex;
      align-items: center;
      justify-content: center;
      background: #fff;
      padding: 20px;
      img {
        padding: 0px 15px;
      }
    }
  }
}

</style>
