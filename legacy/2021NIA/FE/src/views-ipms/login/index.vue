<template>
  <!-- wrap -->
  <div id="ipms">
    <!-- container -->
    <div id="container" class="login">

      <div class="login_top">
        <h1><a href="#none"><img src="@/assets/images/ipms/content/h1_logo_login.gif" alt="IPMS"></a></h1>
        <span>IP ADDRESS MANAGEMENT SYSTEM</span>
      </div>

      <div class="content_result">
        <img src="@/assets/images/ipms/content/img_login.gif" alt="IPMS Log-in">
        <div class="form_login">
          <!-- <fieldset>
            <legend>로그인 영역</legend>
            <span>
              <input type="text" class="txt" placeholder="아이디를 입력해주세요." title="id" />
              <input type="text" class="txt" placeholder="패스워드를 입력해 주세요." title="password" />
            </span>
            <span>
              <a href="#" @click="handleLogin">
                <img src="@/assets/images/ipms/content/btn_login_off.gif" alt="로그인" @click="handleLogin">
              </a>
            </span>
          </fieldset> -->
          <el-form v-if="!isJoin" ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form d-flex items-start" autocomplete="on" label-position="left">
            <div>
              <el-form-item prop="username">
                <input
                  ref="username"
                  v-model="loginForm.username"
                  class="txt"
                  placeholder="아이디를 입력해주세요"
                  name="username"
                  type="text"
                  tabindex="1"
                  autocomplete="on"
                >
              </el-form-item>
              <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" effect="light">
                <el-form-item prop="password">
                  <input
                    :key="passwordType"
                    ref="password"
                    v-model="loginForm.password"
                    class="txt"
                    placeholder="패스워드를 입력해주세요"
                    tabindex="2"
                    autocomplete="on"
                    name="password"
                    show-password
                    type="password"
                    @blur="capsTooltip = false"
                    @keyup="checkCapslock"
                    @keyup.enter="handleLogin"
                  >
                </el-form-item>
              </el-tooltip>
            </div>
            <el-button @click="handleLogin">
              <img src="@/assets/images/ipms/content/btn_login_off.gif" alt="로그인" @click="handleLogin">
            </el-button>
          </el-form>
          <p class="txtblue">* 아이디, 비밀번호는 kate/NeOSS와 동일합니다.</p>
        </div>
      </div>
      <!-- footer in login -->
      <div class="footer_above">
        <ul class="footer_menu">
          <li><a href="#none">정보제공</a></li>
          <li><a href="#none">이용약관 법적고지</a></li>
          <li><a href="#none"><strong>개인정보취급방침</strong></a></li>
          <li><a href="#none">이메일 무단 수집거부</a></li>
          <li><a href="#none">상호접속협정</a></li>
          <li class="last"><a href="#none">문의/연락처</a></li>
        </ul>
        <span>Copyright(c) 2014 kt IPMS. all rights reserved.</span>
      </div>
      <!-- //footer -->
    </div>
    <!-- //container -->
  </div>
</template>
<script>
import Encrypt from '@/assets/libs/Encrypt.min'
import { Base } from '@/min/Base.min'
import { AppOptions } from '@/class/appOptions'
import { rulesUsername, rulesPassword, rulesRePassword, rulesRequire, rulesTelephone, rulesEmail } from '@/utils/validate'
import { onDownloadChrome, exceptionLoginFail } from '@/utils/index'

const routeName = 'Login'

export default {
  name: routeName,
  extends: Base,
  data() {
    return {
      name: routeName,
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: rulesUsername(),
        password: rulesPassword()
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      otherQuery: {},
      isJoin: false,
      joinFormItem: [
        { label: '아이디', value: 'uid' },
        { label: '비밀번호', value: 'password' },
        { label: '비밀번호 확인', value: 'repassword', placeholder: '비밀번호를 다시 한번 입력해주세요' },
        { label: '이름', value: 'name' },
        { label: '연락처', value: 'phone' },
        { label: 'E-MAIL', value: 'email' }
      ],
      joinForm: {
        uid: '',
        password: '',
        repassword: '',
        name: '',
        phone: '',
        email: '',
        agency_name: 'NOC'
      }
    }
  },
  computed: {
    joinRules() {
      return {
        uid: rulesRequire(),
        password: rulesPassword('password'),
        repassword: rulesRePassword(this.joinForm.password),
        name: rulesRequire('name'),
        phone: rulesTelephone(),
        email: rulesEmail(),
      }
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
          exceptionLoginFail(this)
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
  }
}
</script>

<style lang="scss" scoped>
</style>
