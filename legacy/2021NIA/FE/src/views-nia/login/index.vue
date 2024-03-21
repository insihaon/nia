<template>
  <div class="login-container h-full d-flex justify-center flex-column">
    <video muted autoplay loop>
      <source src="@/assets/video/technology_network.mp4" type="video/mp4">
    </video>
    <div class="browserInfo" title="크롬 브라우저 다운로드 페이지로 이동합니다.">
      <img src="@/assets/icon/icon_chrome.png" @click="onClickDownloadChrome()">
      <span>NIA KOREN은 Chrome Browser 및 1920x1080 해상도에 최적화 되어 있습니다.</span>
    </div>
    <el-form v-if="!isJoin" ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form d-flex items-center flex-column" autocomplete="on" label-position="left">
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
      <div id="loginForm" class="pt-2">
        <span>
          <input id="ex_chk" type="checkbox"><label for="ex_chk" class="pr-2">아이디 저장</label>
        </span>
        <span class="pl-2" @click="isJoin= true">회원가입</span>
      </div>
    </el-form>

    <!-- 회원가입 FORM  -->
    <el-form v-else ref="joinForm" :model="joinForm" :rules="joinRules" class="login-form d-flex flex-column items-start">
      <div class="title-container d-flex w-full justify-center items-center pb-2">
        <span class="ml-2">회원가입</span>
      </div>
      <el-form-item v-for="form in joinFormItem" :key="form.value" :prop="form.value" :label="form.label" class="join-form-item d-flex">
        <el-input
          :key="form.value"
          :ref="form.value"
          v-model="joinForm[form.value]"
          :placeholder="form.placeholder || form.label+'을(를) 입력해주세요'"
          tabindex="2"
          autocomplete="on"
          :name="form.id"
          :show-password="form.value.includes('password')"
          :type="form.value.includes('password')?'password':'text'"
          @blur="capsTooltip = false"
          @keyup.enter.native="handleLogin"
        />
      </el-form-item>
      <el-form-item label="분류" class="join-form-item d-flex" prop="type">
        <el-radio ref="NOC" v-model="joinForm.agency_name" class="text-white" label="NOC">NOC</el-radio>
        <el-radio ref="EMS" v-model="joinForm.agency_name" class="text-white" label="EMS">EMS</el-radio>
      </el-form-item>
      <div id="loginForm" class="d-flex w-full justify-center pt-2">
        <span class="pr-2" style="border-right: 2px solid rgb(255 255 255 / 34%);" @click="isJoin= false">취소</span>
        <span class="pl-2" @click="handleJoin()">회원가입</span>
      </div>
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
import { rulesUsername, rulesPassword, rulesRePassword, rulesRequire, rulesTelephone, rulesEmail } from '@/utils/validate'
import { onDownloadChrome, exceptionLoginFail } from '@/utils/index'
import { apiNiaUpsertUser } from '@/api/auth'

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
    handleJoin() {
      this.$refs.joinForm.validate(async valid => {
        if (!valid) {
          this.$message('필수 입력 조건을 확인하세요')
          return
        }

        try {
          this.loading = true
          const res = await apiNiaUpsertUser(this.joinForm)
          if (res?.success) {
            await this.confirm('회원가입이 완료되었습니다.<br >로그인 화면으로 이동합니다.', '알림', { dangerouslyUseHTMLString: true })
            this.isJoin = false
          } else {
            await this.confirm('회원가입이 실패하였습니다.<br >관리자에게 문의하세요.', '알림', { dangerouslyUseHTMLString: true })
          }
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
    },
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
    .join-form-item ::v-deep {
      .el-form-item__label {
        width: 110px;
        color: white;
      }
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

  #loginForm {
    color: white;
    span {
      cursor: pointer;
    }
  }

  #loginForm input[type="checkbox"] {
    position: absolute;
    clip:rect(0,0,0,0);
  }

  #loginForm input[type="checkbox"] + label {
    cursor: pointer;
    font-weight: 400;
    // padding-right: 5px;
    border-right: 2px solid rgb(255 255 255 / 34%);
  }
  // 새로운 디자인의 체크박스 생성
  #loginForm input[type="checkbox"] + label:before {
    content: '';
    display: inline-block;
    width: 15px;
    height: 15px;
    line-height: 18px;
    margin: -2px 8px 0 0;
    text-align: center;
    vertical-align: middle;
    background-color: #fafafa;
    border: 3px solid #7a7c8db3;
    border-radius: 100%;
    transition: all .25s;
  }
  #loginForm input[type="checkbox"]:checked + label:before {
    content: '';
    background: #f1f4ff;
    border-color: #7a7c8db3;
    background-color: #373737eb;
  }
}

</style>
