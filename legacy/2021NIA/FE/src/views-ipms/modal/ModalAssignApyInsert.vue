<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="false"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP 배정신청
        <hr>
      </span>

      <div class="content_result mt0">
        <table class="tbl_data entry">
          <colgroup>
            <col width="14%" /><col width="20%" />
            <col width="13%" /><col width="20%" />
            <col width="13%" /><col width="20%" />
          </colgroup>
          <tbody>
            <tr class="top">
              <th class="first" scope="row">제목</th>
              <td colspan="5">
                <el-input id="txtStitle" v-model="txtStitle" size="mini" type="text" class="txt w98" maxlength="30" /></td>
            </tr>
            <tr>
              <th class="first" scope="row">서비스망</th>
              <td>
                <el-select v-model="updSsvcLineTypeCd" class="w-100" size="mini" name="selInsertSvcLine" @change="handleChangeLvl1()">
                  <el-option
                    v-for="item in ssvcLineTypeNmOp"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </td>
              <th scope="row">본부</th>
              <td>
                <el-select v-model="updSsvcGroupCd" class="w-100" size="mini" name="selInsertSvcLine" @change="handleChangeLvl2()">
                  <el-option
                    v-for="item in ssvcGroupNmOp"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </td>
              <th scope="row">노드</th>
              <td>
                <el-select v-model="updSsvcObjCd" class="w-100" size="mini" name="selInsertSvcLine">
                  <el-option
                    v-for="item in ssvcObjNmOp"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>

              </td>
            </tr>
            <tr class="view">
              <th class="first" scope="row">신청자</th>
              <td><!-- {{sessionScope.user.suserNm}} --></td>
              <th scope="row">소속부서</th>
              <td colspan="3"><!-- {{ sessionScope.user.sposDeptOrgNm }} --></td>
            </tr>
            <tr>
              <th class="first" scope="row">요청 IP개수 (/24)</th>
              <td colspan="5">
                <el-input id="txtApyIpCnt" v-model="txtApyIpCnt" size="mini" type="text" class="txt w-90" maxlength="5" @input="validateNumberInput" /> 개(/24 단위)</td>
            </tr>
            <tr class="last">
              <th class="first" scope="row">신청내용</th>
              <td colspan="5">
                <textarea id="txtApyContents" v-model="txtApyContents" rows="10" class="w98" /></td>
            </tr>

          </tbody>
        </table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-check" @click="fnInsertIpAssignApy()">{{ $t('등록') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalAssignApyInsert'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: null,
      ssvcLineTypeCd: '',
      ssvcLineTypeNmOp: [
        { label: '-------', value: '000000' },
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      updSsvcLineTypeCd: '',
      ssvcGroupNmOp: [
       { label: '-------', value: '000000' }
      ],
      updSsvcGroupCd: '',
      ssvcObjNmOp: [
       { label: '-------', value: '000000' }
      ],
      updSsvcObjCd: '',
      txtApyIpCnt: '',
      txtApyContents: '',
      txtStitle: ''
    }
  },
  computed: {

  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.handleChangeLvl1('000000')
    },
    validateNumberInput() {
      const regExp = /[^0-9a-zA-Z]/g // 숫자만 허용하고 그 외의 모든 문자 제거 (유니코드 플래그 추가)
      this.txtApyIpCnt = this.txtApyIpCnt.replace(regExp, '')
    },
    async handleChangeLvl1(num) {
      try {
        let ssvcLineTypeCd
        if (num) {
          ssvcLineTypeCd = num
        } else {
         ssvcLineTypeCd = this.updSsvcLineTypeCd
        }
        const tbLvlBasVo = { ssvcLineTypeCd: ssvcLineTypeCd }
        const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
        this.ssvcGroupNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => { return { value: v.ssvcGroupCd, label: v.ssvcGroupNm } })
        } catch (error) {
          console.log(error)
       }
    },
    async handleChangeLvl2() {
      const tbLvlBasVo = {
        ssvcLineTypeCd: this.updSsvcLineTypeCd,
        ssvcGroupCd: this.updSsvcGroupCd,
      }
      const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
      this.ssvcObjNmOp = res?.tbLvlBasVos?.filter(v => v.ssvcObjNm !== '전체').map(v => { return { value: v.ssvcObjCd, label: v.ssvcObjNm } })
    },
    fnInsertIpAssignApy() {
      if (this.txtStitle === null || this.txtStitle === '') {
        this.$message('제목을 입력 하세요')
        return
      }

      if (this.txtApyIpCnt === null || this.txtApyIpCnt === '') {
        this.$message('요청 IP개수를 입력 하세요.')
        return
      }

      if (this.txtApyContents === null || this.txtApyContents === '') {
        this.$message('신청 내용을 입력 하세요.')
        return
      }

      if (this.updSsvcLineTypeCd === '000000' || this.updSsvcLineTypeCd === '') {
        this.$message('서비스망을 선택해 주세요.')
        return
      }

      if (this.updSsvcGroupCd === '000000' || this.updSsvcGroupCd === '') {
        this.$message('본부를 선택해 주세요')
        return
      }

      if (this.updSsvcObjCd === '000000' || this.updSsvcObjCd === '') {
        this.$message('노드를 선택해 주세요')
        return
      }

      this.$confirm('배정 신청 하시겠습니까?', '배정 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          // const sessionUserId = sessionScope.user.suserId
          const TbRequestAssignMstVo = {
            stitle: this.txtStitle,
            napyIpCnt: this.txtApyIpCnt,
            scontents: this.txtApyContents,
            ssvcLineTypeCd: this.updSsvcLineTypeCd,
            ssvcGroupCd: this.updSsvcGroupCd,
            ssvcObjCd: this.updSsvcObjCd,
            // sapyUserId: this.sessionUserId,
            srequestAssignTypeCd: 'RS0301',
            // screateId: this.sessionUserId,
            // smodifyId: this.sessionUserId,
          }
          const res = await apiRequestJson(ipmsJsonApis.insertAssignApyTxn, TbRequestAssignMstVo)
           if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `IP 배정 신청 내용을 정상적으로 등록 하였습니다.` })
            this.$emit('reloadData')
            }
          } catch (error) {
            this.$message.error({ message: `배정 신청에 실패했습니다.` })
            console.log(error)
          }
        })
    },

    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
</style>
