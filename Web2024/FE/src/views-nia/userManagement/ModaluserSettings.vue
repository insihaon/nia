<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :modal-append-to-body="true"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="nia-dialog"
      :class="{ [name]: true, 'isDeleteMode': isDeleteMode }"
      :style="{
        top: domElement.y +'px'
      }"
    >
      <span slot="title">
        <i class="el-icon-user mr-2" style="font-size: 17px;" />
        사용자 {{ isDeleteMode ? '계정삭제':'정보수정' }}
      </span>
      <el-form
        v-if="!isDeleteMode"
        ref="userUpdateInfo"
        :model="userUpdateInfo"
        :rules="formRules"
        class="h-full border rounded px-3 py-4"
        :style="{'height': isMobile ? '520px' : '300px'}"
      >
        <el-col v-for="form in userFormItem" :key="form.value" :span="getColSize">
          <el-form-item :prop="form.value" :label="form.label" class="d-flex">
            <el-input
              :key="form.value"
              :ref="form.value"
              v-model="userUpdateInfo[form.value]"
              :readonly="form.value === 'uid'"
              :placeholder="form.placeholder || form.label"
              tabindex="2"
              autocomplete="on"
              clearable
              :name="form.id"
              :show-password="form.value.includes('password')"
              :type="form.value.includes('password')?'password':'text'"
              @blur="capsTooltip = false"
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="분류" class="d-flex" prop="agency_name">
            <el-radio ref="NOC" v-model="userUpdateInfo.agency_name" label="NOC">NOC</el-radio>
            <el-radio ref="EMS" v-model="userUpdateInfo.agency_name" label="EMS">EMS</el-radio>
          </el-form-item>
        </el-col>
      </el-form>
      <el-form v-else ref="userUpdateInfo" :model="userUpdateInfo" :rules="formRules">
        <el-form-item prop="password" label="비밀번호 재확인">
          <el-input
            v-model="userUpdateInfo.password"
            show-password
            clearable
            type="password"
            placeholder="비밀번호 확인"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button icon="el-icon-edit" size="mini" @click="isDeleteMode ? isDeleteMode = false: onClickUpdateAccount()">
          정보수정
        </el-button>
        <el-button icon="el-icon-delete" type="danger" size="mini" @click="isDeleteMode ? onClickDeleteAccount() : isDeleteMode = true">
          계정삭제
        </el-button>
        <el-button icon="el-icon-close" type="info" size="mini" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal'
import { mapState } from 'vuex'
import { rulesPassword, rulesRePassword, rulesRequire, rulesTelephone, rulesEmail } from '@/utils/validate'
import { apiNiaUpsertUser, apiNiaDeleteUser } from '@/api/auth'

const routeName = 'ModaluserSettings'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      isDeleteMode: false,
      temp: '',
      userFormItem: [
        { label: '아이디', value: 'uid' },
        { label: '기존 비밀번호', value: 'orgpassword' },
        { label: '변경 비밀번호', value: 'password' },
        { label: '비밀번호 확인', value: 'repassword' },
        { label: '이름', value: 'name' },
        { label: '연락처', value: 'phone' },
        { label: 'E-MAIL', value: 'email' }
      ],
      userUpdateInfo: {
        uid: '',
        orgpassword: '',
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
    ...mapState({
      viewport: state => state.app.viewport,
      username: state => state.user.name,
    }),
    formRules() {
      return {
        id: rulesRequire(),
        password: rulesPassword('password'),
        orgpassword: rulesPassword('orgPassword'),
        repassword: rulesRePassword(this.userUpdateInfo.password),
        name: rulesRequire('name'),
        phone: rulesTelephone(),
        email: rulesEmail(),
      }
    },
    getColSize() {
      return this.isMobile ? 24 : 12
    }
  },
  watch: {
    isDeleteMode(newValue, oldValue) {
      if (!this.isMobile) {
        this.domElement.maxWidth = newValue ? 400 : 1000
      }
    }
  },
  mounted () {
    this.setUserInfo()
  },
  created() {
  },
  methods: {
    setUserInfo() {
      const { uid, name, mobile, email, agencyName } = this.$store.state.user.info
      this._merge(this.userUpdateInfo, { uid, name, phone: mobile, email, agency_name: agencyName })
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
    },
    onClickUpdateAccount() {
      let checkValidate = false
      this.$refs.userUpdateInfo.validate(async valid => {
        if (!valid) {
          this.$message('필수 입력 조건을 확인하세요.')
        } else {
          checkValidate = true
        }
      })
      if (!checkValidate) return
      this.confirm('정보를 수정 하시겠습니까?', '알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
      }).then(async () => {
          try {
            const res = await apiNiaUpsertUser(this.userUpdateInfo)
            if (res?.success) {
              this.confirm('정보수정이 완료되었습니다.<br >재로그인 하세요.', '알림', { dangerouslyUseHTMLString: true })
              this.close()
              this.$store.dispatch('user/logout')
            } else {
              if (res?.message === 'password mismatch') {
                await this.confirm('패스워드가 일치하지 않습니다.', '알림', {})
              } else {
                await this.confirm('정보수정에 실패하였습니다.<br >관리자에게 문의하세요.', '알림', { dangerouslyUseHTMLString: true })
              }
            }
          } catch (error) {
            return false
          }
        })
    },
    onClickDeleteAccount() {
      const checkValidate = this.userUpdateInfo.password === null || this.userUpdateInfo.password.length === 0

      if (checkValidate) {
        this.$message('비밀번호를 입력하세요.')
        return
      }
      this.confirm('계정을 삭제 하시겠습니까?', '알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
      }).then(async () => {
          try {
            const res = await apiNiaDeleteUser({ uid: this.$store.state.user.info.uid, password: this.userUpdateInfo.password })

            if (res?.success) {
              this.confirm('계정이 삭제되었습니다.<br >로그아웃 합니다.', '알림', { dangerouslyUseHTMLString: true })
              this.close()
              this.$store.dispatch('user/logout')
            } else {
              if (res?.message === 'password mismatch') {
                await this.confirm('패스워드가 일치하지 않습니다.', '알림', {})
              } else {
                await this.confirm(`계정삭제에 실패하였습니다.<br >관리자에게 문의하세요.`, '알림', { dangerouslyUseHTMLString: true })
              }
            }
          } catch (error) {
            return false
          }
        })
    },
    onClose() {
      this.isDeleteMode = false
    },
    onSubmit() {
        console.log('submit!')
      }
    }

  }
</script>
<style lang="scss" scoped>
.isDeleteMode ::v-deep {
  .el-dialog {
    width: 400px;
  }
  .el-dialog__body {
    .el-form-item__label {
      width: 120px;
    }
  }
}
.el-dialog {
  width: 1000px;
}
.el-col {
  padding-bottom: 10px;
}
.el-form-item ::v-deep {
  margin-bottom: 0px;
  .el-form-item__label {
    width: 75px;
    line-height: 25px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    word-break: auto-phrase;
  }
  .el-form-item__content {
    width: 320px;
    margin-bottom: 10px;
    .el-input__inner {
      border: solid 0px;
      border-bottom: solid 1px;
      border-radius: 0px;
      font-family: monospace;
    }
  }
}
</style>

