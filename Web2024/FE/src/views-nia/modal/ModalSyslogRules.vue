<template>
  <div>
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="false"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-edit-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-edit mr-2" style="font-size: 17px" />
          {{ title }}
          <hr>
        </span>
        <table class="basic">
          <tr>
            <th v-if="viewType === 'EDIT'">RULE ID</th>
            <td v-if="viewType === 'EDIT'" class="disable">
              <el-input v-model="syslog_rule_id" />
            </td>
            <th>RULE NAME</th>
            <td :colspan="isColspan">
              <el-input
                v-model="syslog_rule_nm"
                style="width: 70%; float: left"
              />
              <el-button size="mini" style="float: right; margin-top : 5px; padding : 7px 9px" :disabled="isDisabled" plain round type="info" @click="checkRuleName()">중복확인 </el-button>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="line-class">발생</th>
            <th colspan="2" class="line-class">제외</th>
          </tr>
          <tr>
            <th>keyword1</th>
            <td class="disable">
              <el-input v-model="occur_str1" />
            </td>
            <th>keyword1</th>
            <td class="disable">
              <el-input v-model="occur_except_str1" />
            </td>
          </tr>
          <tr>
            <th>keyword2</th>
            <td class="disable">
              <el-input v-model="occur_str2" />
            </td>
            <th>keyword2</th>
            <td class="disable">
              <el-input v-model="occur_except_str2" />
            </td>
          </tr>
          <tr>
            <th>keyword3</th>
            <td class="disable">
              <el-input v-model="occur_str3" />
            </td>
            <th>keyword3</th>
            <td class="disable">
              <el-input v-model="occur_except_str3" />
            </td>
          </tr>
          <tr>
            <th colspan="1">사용여부</th>
            <td colspan="3" class="disable">
              <el-select v-model="use_yn">
                <el-option
                  v-for="item in useMode"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button size="mini" icon="el-icon-edit" @click.native="onChangeMode()">
            {{ changeText }}
          </el-button>
          <el-button v-if="viewType !== 'OPEN'" icon="el-icon-delete" type="danger" size="mini" @click.native="deleteSyslogRule()">
            삭제
          </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiUpdateSyslogRule, apiInsertSyslogRule, apiDeleteSyslogRule, apiSelectCheckRuleName } from '@/api/nia'

const routeName = 'ModalSyslogRules'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      rowInfo: {},
      syslog_rule_id: '',
      syslog_rule_nm: '',
      occur_str1: '',
      occur_except_str1: '',
      occur_str2: '',
      occur_except_str2: '',
      occur_str3: '',
      occur_except_str3: '',
      use_yn: '',
      useMode: [
        { value: 'Y', label: '사용' },
        { value: 'N', label: '미사용' },
      ],
      isCheckRuleName: null
    }
  },
  computed: {
    changeText() {
      return this.viewType === 'OPEN' ? '저장' : '수정'
    },
    title() {
      return this.viewType === 'OPEN' ? 'SYSLOG RULE 등록' : 'SYSLOG RULE 수정'
    },
    isColspan() {
      return this.viewType === 'OPEN' ? '3' : ''
    },
    isDisabled() {
      if (this.viewType === 'EDIT') {
        return this.model?.row?.syslog_rule_nm === this.syslog_rule_nm
      } else {
        return this.syslog_rule_nm === ''
      }
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 900
    },
    onOpen(model) {
      this.viewType = model.type
      this.rowInfo = this._cloneDeep(model.row)
      if (this.viewType === 'OPEN') {
        this.setItem()
      } else {
        this.syslog_rule_id = this.rowInfo.syslog_rule_id
        this.syslog_rule_nm = this.rowInfo.syslog_rule_nm
        this.occur_str1 = this.rowInfo.occur_str1
        this.occur_except_str1 = this.rowInfo.occur_except_str1
        this.occur_str2 = this.rowInfo.occur_str2
        this.occur_except_str2 = this.rowInfo.occur_except_str2
        this.occur_str3 = this.rowInfo.occur_str3
        this.occur_except_str3 = this.rowInfo.occur_except_str3
        this.use_yn = this.rowInfo.use_yn
      }
    },

    insertSyslogRule() {
      this.confirm('등록하시겠습니까?', 'SYSLOG RULE 등록', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        if (!this.isCheckRuleName) {
          this.$message('RULE NAME 중복확인을 해주세요.')
          return false
        }
        const param = {
          syslog_rule_nm: this.syslog_rule_nm,
          occur_str1: this.occur_str1,
          occur_str2: this.occur_str2,
          occur_str3: this.occur_str3,
          occur_except_str1: this.occur_except_str1,
          occur_except_str2: this.occur_except_str2,
          occur_except_str3: this.occur_except_str3,
          use_yn: this.use_yn,
        }
        try {
          const res = await apiInsertSyslogRule(param)
          if (res.success) {
            this.$message('등록 되었습니다.')
            this.$emit('syslogRuleEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `등록에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    updateSyslogRule() {
      this.confirm('수정하시겠습니까?', 'SYSLOG RULE 수정', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        const param = {
          syslog_rule_id: this.syslog_rule_id,
          syslog_rule_nm: this.syslog_rule_nm,
          occur_str1: this.occur_str1,
          occur_str2: this.occur_str2,
          occur_str3: this.occur_str3,
          occur_except_str1: this.occur_except_str1,
          occur_except_str2: this.occur_except_str2,
          occur_except_str3: this.occur_except_str3,
          use_yn: this.use_yn,
        }
        try {
          const res = await apiUpdateSyslogRule(param)
          if (res.success) {
            this.$message('수정 되었습니다.')
            this.$emit('syslogRuleEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `수정에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    deleteSyslogRule() {
      this.confirm('삭제하시겠습니까?', 'SYSLOG RULE 삭제', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        const param = {
          syslog_rule_id: this.syslog_rule_id,
        }
        try {
          const res = await apiDeleteSyslogRule(param)
          if (res.success) {
            this.$message('삭제 되었습니다.')
            this.$emit('syslogRuleEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `삭제에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    async checkRuleName() {
      const param = {
        syslog_rule_nm: this.syslog_rule_nm
       }
      try {
        const res = await apiSelectCheckRuleName(param)
        if (res.result.length !== 0) {
          this.$message.error({ message: '중복된 규칙 이름이 존재합니다.' })
          this.isCheckRuleName = false
        } else {
          this.message('사용 가능합니다')
          this.isCheckRuleName = true
        }
      } catch (error) {
        console.error(error)
        this.$message.error({ message: '중복확인에 실패했습니다.' })
      }
    },
    onChangeMode() {
      if (this.viewType === 'OPEN') {
        this.insertSyslogRule()
      } else {
        this.updateSyslogRule()
     }
    },
    setItem() {
      this.syslog_rule_id = ''
      this.syslog_rule_nm = ''
      this.occur_str1 = ''
      this.occur_except_str1 = ''
      this.occur_str2 = ''
      this.occur_except_str2 = ''
      this.occur_str3 = ''
      this.occur_except_str3 = ''
      this.use_yn = ''
    }
  }
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
.ModalSyslogRules {
  .line-class {
    font-weight: bold;
    font-size: 15px;
  }
}
</style>
