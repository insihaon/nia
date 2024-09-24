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
        조직계위 등록
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>조직계위</h4>
          <table class="tbl_data entry" summary="조회결과">
            <colgroup>
              <col width="30%" /><col width="70%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">서비스망</th>
                <!-- <td>KORNET - 강북운용단 - 혜화지사</td> -->
                <td>
                  <el-select v-model="ssvcLineTypeCd" size="mini" class="w-100">
                    <el-option
                      v-for="item in ssvcLineTypeCdOp"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">센터/지역본부</th>
                <td>
                  <el-input v-model="ssvcGroupNm" size="mini" type="text" class="txt w95" readonly="readonly">
                    <template #suffix>
                      <el-button
                        slot="trigger"
                        size="small"
                        style="font-size: larger; border: none; float: right"
                        icon="el-icon-search"
                        class="font-weight-bolder"
                        @click="fnViewSearchCenterLvlCd('센터/지역본부')"
                      />
                    </template>
                  </el-input>
                </td>

              </tr>
              <tr>
                <th class="first" scope="row">노드</th>
                <td>
                  <el-input v-model="ssvcObjNm" size="mini" type="text" class="txt w95" readonly="readonly">
                    <template #suffix>
                      <el-button
                        slot="trigger"
                        size="small"
                        style="font-size: larger; border: none; float: right"
                        icon="el-icon-search"
                        class="font-weight-bolder"
                        @click="fnViewSearchCenterLvlCd('노드')"
                      />
                    </template>
                  </el-input>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">주노드</th>
                <td>
                  <el-input v-model="ssvchighObjNm" size="mini" type="text" class="txt w95" readonly="readonly">
                    <template #suffix>
                      <el-button
                        slot="trigger"
                        size="small"
                        style="font-size: larger; border: none; float: right"
                        icon="el-icon-search"
                        class="font-weight-bolder"
                        @click="fnViewSearchCenterLvlCd('주노드')"
                      />
                    </template>
                  </el-input>
                </td>
              </tr>

            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="fnInsertValidTbLvlBas()">
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

const routeName = 'ModalLvlBasInsert'

export default {
  name: routeName,
  components: { ModalEntireOrgSearch },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ssvcLineTypeCd: '',
      ssvcLineTypeCdOp: [
        { label: 'KORNET', value: 'CL0001' },
        { label: 'PREMIUM', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: 'GNS', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: 'ICC', value: 'CL0006' },
        { label: '미분류', value: 'CL0007' },
        { label: 'SCHOOLNET', value: 'CL0008' }
      ],
      ssvcGroupNm: '-------',
      ssvcGroupCd: '000000',
      ssvcObjNm: '-------',
      ssvcObjCd: '000000',
      ssvchighObjNm: '-------',
      ssvchighObjCd: '000000'
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
      this.domElement.maxWidth = 600
    },
    onOpen(model, actionMode) {
      this.ssvcLineTypeCd = ''
      this.ssvcGroupNm = ''
      this.ssvcGroupCd = ''
      this.ssvcObjNm = ''
      this.ssvcObjCd = ''
      this.ssvchighObjNm = ''
      this.ssvchighObjCd = ''
    },
    async fnInsertSubmit() {
      if (this.resultVo.slvlNm === '') {
        this.$message('파라미터 오류, 계위명을 입력 하세요.')
        return
      }

      if (this.resultVo.sorgOfficeFlagYn === '') {
        this.$message('파라미터 오류, 계위구분을 입력 하세요.')
        return
      }

      let res
      try {
        const tbLvlCdVo = {
          smodifyId: this.$store.state.user.info.Uid,
          screateId: this.$store.state.user.info.Uid,
          slvlNm: this.resultVo.slvlNm,
          sexLinkUseTypeCd: 'CE0006',
          sorgOfficeFlagYn: this.resultVo.sorgOfficeFlagYn,
          scomment: this.resultVo.scomment,
        }
         res = await apiRequestJson(ipmsJsonApis.insertTbLvlCdVo, tbLvlCdVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('계위를 정상적으로 등록하였습니다.')
          this.$emit('reload')
          this.close()
        }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.error(error)
      }
    },
    fnViewSearchCenterLvlCd(type) {
      if (type === '센터/지역본부') {
        if (this.ssvcLineTypeCd === '') {
          this.$message('서비스망을 선택하세요')
          return
        }
      } else if (type === '노드') {
        if (this.ssvcLineTypeNm === '' || this.ssvcGroupNm === '') {
          this.$message('센터/지역본부를 선택하세요')
          return
        }
      } else if (type === '주노드') {
        if (this.ssvcLineTypeNm === '' || this.ssvcGroupNm === '' || this.ssvcObjNm === '') {
          this.$message('노드를 선택하세요')
          return
        }
      }
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: type })
    },
    setSelectedRow(row, title) {
      if (title === '센터/지역본부') {
        this.ssvcGroupNm = row?.slvlNm
        this.ssvcGroupCd = row?.slvlCd
      } else if (title === '노드') {
        this.ssvcObjNm = row?.slvlNm
        this.ssvcObjCd = row?.slvlCd
      } else {
        this.ssvchighObjNm = row?.slvlNm
        this.ssvchighObjCd = row?.slvlCd
      }
    },
    async fnInsertValidTbLvlBas() { /* 유효성 체크 */
      let res
      try {
        const TbLvlBasVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
        }
        res = await apiRequestJson(ipmsJsonApis.insertValidTbLvlBas, TbLvlBasVo)
        if (res.commonMsg === 'FAIL') {
          this.$message.error('사용중인 조직계위 정보입니다.')
        } else if (res.commonMsg === 'CENTERFAIL') {
          this.$message.error('해당 노드의 센터/지역본부 정보가 없습니다.')
        } else {
          this.fnInsertTbLvlBasSumit()
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnInsertTbLvlBasSumit() { /* 조직계위등록 */
      let res
      try {
        const TbLvlRoleMstVo = {
          ssvcLineTypeCd: this.ssvcLineTypeCd,
          ssvcGroupCd: this.ssvcGroupCd,
          ssvcObjCd: this.ssvcObjCd,
          ssvchighCd: this.ssvchighObjCd,
          smodifyId: this.$store.state.user.info.Uid,
        }
        res = await apiRequestJson(ipmsJsonApis.insertTbLvlBas, TbLvlRoleMstVo)
        if (res.commonMsg === 'SUCCESS') {
          this.$message('조직계위등록이 정상처리 되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          this.$message.error({ message: `${res.commonMsg}` })
        }
      } catch (error) {
        console.error(error)
      }
    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
