<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="true"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        조직계위정보 이동
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>변경전 조직계위</h4>
          <table class="tbl_data entry">
            <colgroup>
              <col width="30%" /><col width="70%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">서비스망</th>
                <td>{{ resultVo.ssvcLineTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">센터/지역본부</th>
                <td>{{ resultVo.ssvcGroupNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">노드</th>
                <td>{{ resultVo.ssvcObjNm }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">주노드</th>
                <td>{{ resultVo.ssvchighObjNm }}</td>

              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>변경후 조직계위</h4>
          <table class="tbl_data entry">
            <colgroup>
              <col width="30%" />
              <col width="70%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">서비스망</th>
                <td>
                  <el-select v-model="ssvcLineTypeCd" class="w-100" size="mini" @change="handleChangeLvl1()">
                    <el-option
                      v-for="item in ssvcLineTypeCdOp"
                      :key="item.value"
                      :value="item.value"
                      :label="item.label "
                    >
                    </el-option>
                  </el-select>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">센터/지역본부</th>
                <td>
                  <el-select v-model="ssvcGroupCd" :disabled="isDisabledCt" class="w-100" size="mini" @change="handleChangeLvl2()">
                    <el-option
                      v-for="item in ssvcGroupCdOp"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">노드</th>
                <td>
                  <el-select v-model="ssvcObjCd" class="w-100" :disabled="isDisabledNd" size="mini">
                    <el-option
                      v-for="item in ssvcObjCdOp"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">주노드</th>
                <td>
                  <div class="search w98">
                    <el-input
                      v-model="ssvchighObjNm"
                      size="mini"
                      readonly
                    >
                      <template #suffix>
                        <el-button
                          size="mini"
                          icon="el-icon-search"
                          class="font-weight-bolder"
                          style="font-size: larger;border: none"
                          @click="fnViewSearchCenterLvlCd()"
                        />
                      </template>
                    </el-input>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="fnValidationLvlValue()">
          {{ $t('등록') }}
        </el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
      <ModalEntireOrgSearch ref="ModalEntireOrgSearch" @selected-value="setSelectedRow" />

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import _ from 'lodash'
import ModalEntireOrgSearch from '@/views-ipms/modal/search/ModalEntireOrgSearch.vue'

const routeName = 'ModalUpdateLvlBas'

export default {
  name: routeName,
  components: { ModalEntireOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ssvcLineTypeCdOp: [
        { label: '-선택-', value: 'CL0000' },
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      ssvcGroupCdOp: [],
      ssvcObjCdOp: [],
      ssvcLineTypeCd: 'CL0000',
      ssvcGroupCd: '',
      ssvcObjCd: '',
      ssvchighObjNm: '',
      ssvchighObjCd: '',
    }
  },
  computed: {
    isDisabledCt() {
      return this.ssvcLineTypeCd === 'CL0000'
    },
    isDisabledNd() {
      return this.ssvcLineTypeCd === 'CL0000' || this.ssvcGroupCd === ''
    }
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      this.resultVo = this._cloneDeep(model.row)
      const { ssvcGroupNm, ssvcObjNm, ssvchighObjNm, ssvchighObjCd } = this.resultVo
      this.ssvcGroupCd = ssvcGroupNm // 센터지역본부
      this.ssvcObjCd = ssvcObjNm // 노드
      this.ssvchighObjNm = ssvchighObjNm // 주노드
      this.ssvchighObjCd = ssvchighObjCd //
    },
    async handleChangeLvl1() {
      this.ssvcGroupCd = ''

      const tbLvlBasVo = { ssvcLineTypeCd: this.ssvcLineTypeCd }

      this.ssvcGroupCd = null
      this.ssvcObjCd = null

      const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, tbLvlBasVo)
      this.ssvcGroupCdOp = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => {
        return { value: v.ssvcGroupCd, label: v.ssvcGroupNm }
      })

      this.ssvcObjCdOp = []
    },
    async handleChangeLvl2() {
      this.ssvcObjCd = ''

      const tbLvlBasVo = {
        ssvcLineTypeCd: this.ssvcLineTypeCd,
        ssvcGroupCd: this.ssvcGroupCd,
      }

      this.ssvcObjCd = null

      const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, tbLvlBasVo)
      this.ssvcObjCdOp = res?.tbLvlBasVos?.filter(v => v.ssvcObjNm !== '전체').map(v => {
        return { value: v.ssvcObjCd, label: v.ssvcObjNm }
      })
    },
    fnViewSearchCenterLvlCd() {
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: '노드' })
    },
    setSelectedRow(row) {
      this.ssvchighObjNm = row.slvlNm
      this.ssvchighObjCd = row.slvlCd
    },
    async fnValidationLvlValue() {
      if (this.ssvcLineTypeCd === 'CL0000' || this.ssvcLineTypeCd === '' || this.ssvcGroupCd === null || this.ssvcGroupCd === '' || this.ssvcObjCd === null || this.ssvcObjCd === '') {
        return this.$message('계위 정보가 없습니다.')
      }

        // 주노드 변경
        if ((this.resultVo.ssvcLineTypeCd === this.ssvcLineTypeCd) && (this.resultVo.ssvcGroupCd === this.ssvcGroupCd) && (this.resultVo.ssvcObjCd === this.ssvcObjCd) && (this.resultVo.ssvchighObjCd !== this.ssvchighObjCd)) {
            if (this.ssvcObjCd === '000000' || this.ssvcObjCd === null) {
                this.$message('노드정보가 올바르지 않습니다.')
                return
            }
        }
        // 노드 정보 변경
        if (this.resultVo.ssvcLineTypeCd !== this.ssvcLineTypeCd || this.resultVo.ssvcGroupCd !== this.ssvcGroupCd || this.resultVo.ssvcObjCd !== this.ssvcObjCd) {
            if (this.ssvcObjCd === '000000' || this.ssvcObjCd === null || this.ssvcObjCd === '') {
              this.$message('노드정보가 올바르지 않습니다.')
              return
            }
        }
        // 데이터 변경 없음
        if ((this.resultVo.ssvcLineTypeCd === this.ssvcLineTypeCd) && (this.resultVo.ssvcGroupCd === this.ssvcGroupCd) && (this.resultVo.ssvcObjCd === this.ssvcObjCd) && (this.resultVo.ssvchighObjCd === this.ssvchighObjCd)) {
           this.$message('변경할 데이터가 없습니다.')
           return
        }

      let res
      try {
        const TbLvlBasVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
          ssvchighObjCd: this.ssvchighObjCd,
        }
        res = await apiRequestJson(ipmsJsonApis.validTbLvlBas, TbLvlBasVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('이동할 계위정보가 없습니다.')
        } else {
          this.fnLvlMoveSubmit()
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnLvlMoveSubmit() {
      let res
      try {
        const TbLvlMstVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
          ssvchighCd: this.ssvchighObjCd,
          nlvlSeq: this.resultVo.nlvlBasSeq,
          bfsSsvcLineTypeCd: this.resultVo.ssvcLineTypeCd,
          bfsSsvcGroupCd: this.resultVo.ssvcGroupCd,
          bfSsvcObjCd: this.resultVo.ssvcObjCd,
        }
        res = await apiRequestJson(ipmsJsonApis.updateTbLvlMove, TbLvlMstVo)
         if (res.commonMsg === 'SUCCESS') {
          this.$message('정상처리 되었습니다.')
          this.$emit('reload')
          this.close()
         }
      } catch (error) {
        console.error(error)
        this.$message.error({ message: `${res.message}` })
      }
    },
    onClose() {
      // this.ssvcLineTypeCd = 'CL0000'
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
