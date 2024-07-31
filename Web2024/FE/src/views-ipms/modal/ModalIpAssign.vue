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
              <col width="15%" /><col width="30%" /><col width="30%" /><col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위</th>
                <td>
                  <select id="updSsvcLineTypeCd" v-model="ssvcLineTypeCd">
                    <option v-for="option in ssvcLineTypeOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
                </td>
                <td>
                  <select id="updSsvcGroupCd" v-model="ssvcGroupCd">
                    <option v-for="option in ssvcGroupOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
                </td>
                <td>
                  <select id="updSsvcObjCd" v-model="ssvcObjCd">
                    <option v-for="option in ssvcObjOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">배정상태</th>
                <td>
                  <select id="updSassignLevelCd" v-model="sassignLevelCd">
                    <option v-for="option in sassignTypeLevelOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
                </td>
                <th scope="row">서비스</th>
                <td>
                  <select id="updSassignTypeCd" v-model="sassignTypeCd">
                    <option v-for="option in sassignTypeOptions" :key="option.value" :value="option.value">
                      {{ option.label }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td colspan="3">
                  <textarea id="updScomment" v-model="scomment" class="w98" rows="3" maxlength="4000"></textarea>
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
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="12%" />
              <col width="16%" />
              <col width="12%" />
              <col width="10%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">서비스망</th>
                <th scope="col">본부</th>
                <th scope="col">노드</th>
                <th scope="col">공인/사설</th>
                <th scope="col">서비스</th>
                <th scope="col">IP블록</th>
                <th scope="col">배정범위</th>
                <th scope="col">단위블록수</th>
                <th scope="col">배정상태</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in ipBlockDetailList" :key="index">
                <td> {{ item.ssvcLineTypeNm }}</td>
                <td> {{ item.ssvcGroupNm }}</td>
                <td> {{ item.ssvcObjNm }}</td>
                <td> {{ item.sipCreateTypeNm }}</td>
                <td> {{ item.sassignTypeNm }}</td>
                <td> {{ item.pipPrefix }}</td>
                <td> {{ item.sfirstAddr }} ~ {{ item.slastAddr }}</td>
                <td> {{ item.nclassCnt }}</td>
                <td> {{ item.sassignLevelNm }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button icon="el-icon-document-checked" style="background: #2b5890;" type="primary" size="mini" @click.native="handle()">{{ '배정' }}</el-button>
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
      ssvcLineTypeOptions: [
        { label: '공인', value: '공인' },
        { label: 'Bogon', value: 'Bogon' },
        { label: '유/무선공용', value: '유/무선공용' },
      ],
      ssvcGroupOptions: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'SCHOOLNET', value: 'CL0005' }
      ],
      ssvcObjOptions: [
        { value: 'CV0001', label: 'IPv4' },
        { value: 'CV0002', label: 'IPv6' },
      ],
      sassignTypeLevelOptions: [
        { label: '미배정', value: 'IA0001' },
        { label: '예비배정', value: 'IA0002' },
        { label: '배정[미할당]', value: 'IA0003' },
        { label: '서비스배정[미할당]', value: 'IA0004' },
      ],
      sassignTypeOptions: [
        { label: '-------', value: '' },
        { label: '공통서비스', value: '공통서비스' },
        { label: 'KT사내망', value: 'KT사내망' },
        { label: 'POOL-LoT(고정)', value: 'POOL-LoT(고정)' },
        { label: 'POOL-M2M(고정)', value: 'POOL-M2M(고정)' },
        { label: 'IMS-SYSTEM', value: 'IMS-SYSTEM' },
      ],
      ssvcLineTypeCd: '',
      ssvcGroupCd: '',
      ssvcObjCd: '',
      sassignLevelCd: '',
      sassignTypeCd: '',
      scomment: '',
      ssvcLineTypeNm: '',
      ipBlockDetailList: []
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
      this.$set(this, 'selectedRow', model.row)
      this.ipBlockDetailList = this.selectedRow || []
      this.ssvcLineTypeCd = this.selectedRow[0].ssvcLineTypeCd
      this.ssvcGroupCd = this.selectedRow[0].ssvcGroupCd
      this.ssvcObjCd = this.selectedRow[0].ssvcObjCd
      this.sassignTypeCd = this.selectedRow[0].sassignTypeCd
      this.scomment = this.selectedRow[0].scomment
      this.ssvcLineTypeNm = this.selectedRow[0].ssvcLineTypeNm
    },
    onClose() { this.selectedRow = [] },
  },
}
</script>
<style lang="scss" scoped>
</style>
