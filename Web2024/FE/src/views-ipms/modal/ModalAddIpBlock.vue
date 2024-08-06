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
                  <select id="insertSipCreateTypeCd" v-model="sipCreateTypeNm" :disabled="isDisabled">
                    <option v-for="option in sipCreateOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
                </td>
                <th scope="row">생성차수</th>
                <td>
                  <input id="insertSipCreateSeqCd" v-model="sipCreateSeqCd" type="text" class="txt w95" readonly="readonly" disabled="disabled" />
                </td>
              </tr>

              <tr>
                <th class="first" scope="row">서비스망</th>
                <td>
                  <select id="insertSsvcLineTypeCd" v-model="ssvcLineTypeNm" :disabled="isDisabled">
                    <option v-for="option in ssvcLineOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
                </td>
                <th scope="row">IP 버전</th>
                <td>
                  <select id="insertSipVersionTypeCd" v-model="sipVersionTypeNm" :disabled="isDisabled">
                    <option v-for="option in sipVersionOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
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
        </div>
        <div class="content_result">
          <table class="tbl_data entry">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">IP 주소</th>
                <td>
                  <input id="insertPipPrefix" v-model="pipPrefix" type="text" class="txt w50" maxlength="40" width="85%" />
                  <el-button id="appendBtn" class="mx-2" size="mini" @click="fnAppendBtnClick">추가</el-button>
                </td>
              </tr>
            </tbody>
          </table>

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

          <div class="btn_area mt5">
            <span>
              <el-button size="mini" @click="fnInitBtnClick()"> 초기화</el-button>
            </span>
            <span>
              <el-button size="mini" @click="fnSaveBtnClick()"> 등록 </el-button>
            </span>
          </div>
        </div>

        <div class="content_result">
          <h4>IP 블록 처리결과</h4>
          <div class="handling_msg">
            <p id="resultMsg"></p>
          </div>
        </div>
      </div>
      <!-- <input id="commonMsg" type="hidden" value="${resultVo.commonMsg}"> -->

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
      sipCreateTypeNm: '',
      sipCreateSeqCd: '',
      ssvcLineTypeNm: '',
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
      ipBlockDetailList: [
        { pipPrefix: '112.221.217.32/32', sfirstAddr: '112.221.217.32', slastAddr: '112.221.217.32', nclassCnt: '0.00390625', ncnt: '1', deleteBtn: '삭제' }
      ]
    }
  },
  computed: {
    isDisabled() {
      return this.viewType !== 'create'
    }
  },
  mounted() {
    // this.onloadIpDetailList
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.$set(this, 'selectedRow', model.row)
      if (model.type === 'generate') {
        this.sipCreateTypeNm = this.selectedRow.sipCreateTypeNm
        this.sipCreateSeqCd = this.selectedRow.sipCreateSeqCd
        this.ssvcLineTypeNm = this.selectedRow.ssvcLineTypeNm
        this.sipVersionTypeNm = this.selectedRow.sipVersionTypeNm
        this.sipVersionTypeCd = this.selectedRow.sipVersionTypeCd
      } else {
        this.sipCreateTypeNm = ''
        this.sipCreateSeqCd = ''
        this.ssvcLineTypeNm = ''
        this.sipVersionTypeNm = ''
        this.sipVersionTypeCd = ''
      }
     this.viewType = this.model.type
    },
    onClose() { this.selectedRow = [] },
    onloadIpDetailList() {
     /*  const { key: seq } = this.selectedRow
      const param = seq
      try {
        const res = await apiSelectIpDetailList(param)
        this.IpBlockDetail = res?.result
      } catch (error) {
        console.error(error)
      } */
    },
  async fnSaveBtnClick() {
    // ip 등록 확인 대화상자
    this.confirm('등록하시겠습니까?', 'IP블록생성', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'success',
    }).then(async () => {
      try {
        for (let i = 0; i < this.ipBlockDetailList.length; i++) {
          const ipBlock = this.ipBlockDetailList[i]
          const tbIpBlockMstComplexVo = {
            sipCreateTypeCd: this.selectedRow.sipCreateTypeCd,
            ssvcLineTypeCd: this.selectedRow.ssvcLineTypeCd,
            sipCreateSeqCd: this.selectedRow.sipCreateSeqCd,
            sipVersionTypeCd: this.selectedRow.sipVersionTypeCd,
            pipPrefix: ipBlock.pipPrefix,
            scomment: this.selectedRow.scomment
          }
          const res = await /* apiEditIpBlockList */(tbIpBlockMstComplexVo)
          if (res.success) {
            this.$message('IP블록 수정이 정상적으로 처리되었습니다.')
            this.$emit('reloadData')
          }
        }
      } catch (error) {
        this.$message.error({ message: 'IP블록 등록에 실패했습니다.' })
        console.error(error)
      }
    })
  },
    async fnAppendBtnClick() {
      // ip 추가
      if (this.sipCreateTypeNm === '') {
        this.$message('공인/사설이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.ssvcLineTypeNm === '') {
        this.$message('서비스망이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
       if (this.sipVersionTypeNm === '') {
        this.$message('IP버전이 미분류입니다. 다시 선택해 주시기 바랍니다.')
        return
      }
      this.viewType = 'generate'

      try {
          const ipBlockCheckVo = {
            sipCreateTypeCd: this.sipCreateTypeCd,
            ssvcLineTypeCd: this.ssvcLineTypeCd,
            sipCreateSeqCd: this.sipCreateSeqCd,
            sipVersionTypeCd: this.sipVersionTypeCd,
            pipPrefix: this.pipPrefix,
            scomment: this.selectedRow.scomment,
            destIpBlockMstVos: []
          }
          const destIpBlock = {
            sipCreateTypeCd: this.sipCreateTypeCd,
            ssvcLineTypeCd: this.ssvcLineTypeCd,
            sipCreateSeqCd: this.sipCreateSeqCd,
            sipVersionTypeCd: this.sipVersionTypeCd,
            pipPrefix: this.pipPrefix,
          }

          ipBlockCheckVo.destIpBlockMstVos.push(destIpBlock)
          const res = await /* apiAddIpBlockList */(ipBlockCheckVo)
          if (res.commonMsg === 'SUCCESS') {
             const resultData = res?.data
              this.ipBlockDetailList.push({
                pipPrefix: this.pipPrefix,
                sfirstAddr: resultData.sfirstAddr,
                slastAddr: resultData.slastAddr,
                nclassCnt: resultData.nclassCnt,
                ncnt: resultData.ncnt,
              })
          }
      } catch (error) {
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
</style>
