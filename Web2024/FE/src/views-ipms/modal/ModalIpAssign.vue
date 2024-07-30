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
        IP블록배정
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4 class="mt5">배정 정보</h4>
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
          <h4 class="mt5">배정 대상 정보</h4>
          <table class="tbl_data entry">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>

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
              </tr>
            </tbody>
          </table>

        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button icon="el-icon-document-checked" style="background: #2b5890;" type="primary" size="mini" @click.native="close()">{{ '배정' }}</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'

const routeName = 'ModalIpAssign'

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
      sipCreateTypeCd: '',
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
    fnSaveBtnClick() {
      // ip 등록
      this.confirm('등록하시겠습니까?', 'IP블록생성', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        try {
          const param = {
            nipBlockMstSeq: this.selectedRow.nipBlockMstSeq,
            sipCreateSeqNm: this.selectedRow.sipCreateSeqNm,
            scomment: this.selectedRow.scomment,
          }
          const res = await /* apiEditIpBlockList */(param)
          if (res.success) {
            this.$message('IP블록 수정이 정상적으로 처리되었습니다.')
            this.$emit('reloadData')
          }
        } catch (error) {
          this.$message.error({ message: `IP블록 수정에 실패했습니다.` })
          console.error(error)
        }
      })
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
