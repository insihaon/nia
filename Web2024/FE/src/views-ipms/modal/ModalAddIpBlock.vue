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
        IP블록생성
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4 class="mt5">IP 블록 생성</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" /><col width="35%" /><col width="15%" /><col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">공인/사설</th>
                <td>
                  <el-select id="insertSipCreateTypeCd" v-model="sipCreateTypeCd" size="mini" :disabled="isDisabled">
                    <el-option v-for="option in sipCreateOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </el-option>
                  </el-select>
                </td>
                <th scope="row">생성차수</th>
                <td>
                  <el-input id="insertSipCreateSeqCd" v-model="sipCreateSeqCd" size="mini" type="text" class="txt w95" readonly="readonly" disabled="disabled" />
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">서비스망</th>
                <td>
                  <el-select id="insertSsvcLineTypeCd" v-model="ssvcLineTypeCd" size="mini" :disabled="isDisabled">
                    <el-option v-for="option in ssvcLineOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </el-option>
                  </el-select>
                </td>
                <th scope="row">IP 버전</th>
                <td>
                  <el-select id="insertSipVersionTypeCd" v-model="sipVersionTypeNm" size="mini" :disabled="isDisabled">
                    <el-option v-for="option in sipVersionOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </el-option>
                  </el-select>
                </td>
              </tr>

              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td colspan="3">
                  <textarea id="insertScomment" v-model="scomment" class="w98" rows="3" maxlength="4000"></textarea>
                </td>
              </tr>
            </tbody>
          </table>

          <table class="my-3 tbl_data entry">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">IP 주소</th>
                <td>
                  <el-input id="insertPipPrefix" v-model="pipPrefix" size="mini" type="text" class="txt w50" maxlength="40" style="width : 85%" />
                  <el-button id="appendBtn" class="mx-2" size="mini" @click="fnAppendBtnClick">추가</el-button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <div class="scroll_area">
            <table id="baseTable" class="tbl_list my-3" summary="목록">
              <caption>목록</caption>
              <colgroup>
                <col width="6%" />
                <col width="15%" />
                <col width="15%" />
                <col width="24%" />
                <col width="12%" />
                <col width="20%" />
                <col width="8%" />
              </colgroup>
              <thead>
                <tr>
                  <th class="first" scope="col">순번</th>
                  <th scope="col">IP 블록</th>
                  <th scope="col">시작 IP</th>
                  <th scope="col">끝 IP</th>
                  <th scope="col">단위블록수</th>
                  <th scope="col">총 IP수</th>
                  <th scope="col">삭제</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in ipBlockDetailList" :key="index">
                  <td> {{ index + 1 }}</td>
                  <td> {{ item.pipPrefix }}</td>
                  <td> {{ item.sfirstAddr }}</td>
                  <td> {{ item.slastAddr }}</td>
                  <td> {{ item.nclassCnt }}</td>
                  <td> {{ item.ncnt }}</td>
                  <td> <el-button size="mini" @click="fnRemoveBtnClick()">삭제</el-button> </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="p-1 btn_area mt5">
            <span>
              <el-button class="mx-sm-1" size="mini" @click="fnInitBtnClick()"> 초기화</el-button>
            </span>
            <span>
              <el-button size="mini" @click="fnSaveBtnClick()"> 등록 </el-button>
            </span>
          </div>
        </div>

        <el-input id="commonMsg" v-model="commonMsg" readonly size="mini">
        </el-input>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'

const routeName = 'ModalAddIpBlock'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      sipCreateTypeCd: '',
      sipCreateTypeNm: '',
      sipCreateSeqCd: '',
      ssvcLineTypeCd: '',
      sipVersionTypeNm: '',
      sipVersionTypeCd: '',
      sipCreateOptions: [
        { label: '공인', value: '공인' },
        { label: 'Bogon', value: 'Bogon' },
        { label: '유/무선공용', value: '유/무선공용' },
      ],
      ssvcLineOptions: [
        { value: 'KORNET', label: 'KORNET' },
        { value: 'PREMIUM', label: 'PREMIUM' },
        { value: 'MOBILE', label: 'MOBILE' },
        { value: 'GNS', label: 'GNS' },
        { value: 'SCHOOLNET', label: 'SCHOOLNET' }
      ],
      sipVersionOptions: [
        { value: 'CV0001', label: 'IPv4' },
        { value: 'CV0002', label: 'IPv6' },
      ],
      sipCreateSeqNm: '',
      scomment: '',
      type: 'create',
      IpBlockDetail: [],
      tableDatas: [],
      ipBlockResult: '',
      description: '',
      viewType: '',
      pipPrefix: '',
      ipBlockDetailList: [],
      resultListVo: null,
      commonMsg: ''
    }
  },
  computed: {
    isDisabled() {
      return this.viewType !== 'create'
    }
  },
  mounted() {
  },
  methods: {
    sipCreate() {
      console.log(this.sipCreateTypeCd)
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.commonMsg = ''
      this.resultListVo = model?.row
      this.viewType = this.model.type
      this.fnViewUpdateCrtIPMstCallBack(model)
      if (model.type === 'generate') {
        const { sipCreateTypeCd, sipCreateTypeNm, sipCreateSeqCd, ssvcLineTypeCd, sipVersionTypeNm, sipVersionTypeCd } = this.resultListVo
        this.sipCreateTypeCd = sipCreateTypeCd
        this.sipCreateTypeNm = sipCreateTypeNm
        this.sipCreateSeqCd = sipCreateSeqCd
        this.ssvcLineTypeCd = ssvcLineTypeCd
        this.sipVersionTypeNm = sipVersionTypeNm
        this.sipVersionTypeCd = sipVersionTypeCd
      } else {
        this.sipCreateTypeCd = ''
        this.sipCreateTypeNm = ''
        this.sipCreateSeqCd = ''
        this.ssvcLineTypeCd = ''
        this.sipVersionTypeNm = ''
        this.sipVersionTypeCd = ''
      }
      },
      async fnViewUpdateCrtIPMstCallBack(model) {
        try {
          const res = await apiRequestModel(ipmsModelApis.viewInsertCrtIPMst, model)
          this.resultListVo = res?.result.data
        } catch (error) {
          console.error(error)
        }
      },
      onResetForm() {
        this.ipBlockDetailList = []
        this.pipPrefix = ''
      },

      onClose() { this.resultListVo = [] },
      async fnSaveBtnClick() {
        // ip 등록
        if (this.ipBlockDetailList.length === 0) {
          this.$message('등록할 목록이 없습니다.')
        return
      }

      this.confirm('등록하시겠습니까?', 'IP블록생성', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        try {
          const tbIpBlockMstVos = []

          for (let i = 0; i < this.ipBlockDetailList.length; i++) {
            const ipBlock = this.ipBlockDetailList[i]

            tbIpBlockMstVos.push({
              sipCreateTypeCd: this.sipCreateTypeCd,
              ssvcLineTypeCd: this.ssvcLineTypeCd,
              sipCreateSeqCd: this.sipCreateSeqCd,
              sipVersionTypeCd: this.sipVersionTypeCd,
              pipPrefix: ipBlock.pipPrefix,
              scomment: this.scomment || '',
            })
          }

          const tbIpBlockListVo = {
            tbIpBlockMstVos: tbIpBlockMstVos,
          }

          const res = await apiRequestJson(ipmsJsonApis.insertListCrtIPMst, tbIpBlockListVo)

          if (res.commonMsg === 'SUCCESS') {
            this.$message('IP블록 등록이 정상적으로 처리되었습니다.')
            this.commonMsg = 'IP블록이 정상적으로 등록되었습니다.'
            this.onResetForm()
            this.$emit('reloadData')
          } else {
            this.$message.error({ message: 'IP블록 등록에 실패했습니다.' })
          }
        } catch (error) {
          this.$message.error({ message: 'IP블록 등록에 실패했습니다.' })
          console.error(error)
        }
      })
    },
    async fnAppendBtnClick() {
      // ip 추가
      if (this.sipCreateTypeCd === '') {
        this.$message('공인/사설이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.ssvcLineTypeCd === '') {
        this.$message('서비스망이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.sipVersionTypeNm === '') {
        this.$message('IP버전이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
      this.viewType = 'generate'

      try {
          const ipBLockCheckVo = {
            srcIpBlockMstVo: {
              sipCreateTypeCd: this.sipCreateTypeCd,
              ssvcLineTypeCd: this.ssvcLineTypeCd,
              sipCreateSeqCd: this.sipCreateSeqCd,
              sipVersionTypeCd: this.sipVersionTypeCd,
              pipPrefix: this.pipPrefix,
            },
            destIpBlockMstVos: []
          }
          const res = await apiRequestJson(ipmsJsonApis.appendCrtIPMst, ipBLockCheckVo)
          if (res.commonMsg === 'SUCCESS') {
             const resultData = res
              this.ipBlockDetailList.push({
                pipPrefix: resultData.pipPrefix,
                sfirstAddr: resultData.sfirstAddr,
                slastAddr: resultData.slastAddr,
                nclassCnt: resultData.nclassCnt,
                ncnt: resultData.ncnt,
              })
          } else {
            this.$message({
              message: res.commonMsg,
              type: 'success',
              duration: 3000
            })
          }
      } catch (error) {
        this.message
        console.error(error)
      }
    },
    fnInitBtnClick() {
      // 초기화
      this.ipBlockDetailList = []
    },
    fnRemoveBtnClick(index) {
      // 블럭 삭제
      this.ipBlockDetailList.splice(index, 1)
    }
  },
}
</script>
<style lang="scss" scoped>
  .ModalAddIpBlock ::v-deep{
    .el-select {
      width: 100%;
    }
  }
</style>
