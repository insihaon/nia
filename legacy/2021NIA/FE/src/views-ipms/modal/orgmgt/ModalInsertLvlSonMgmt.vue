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
        오더 노드국 관리(SON)
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>조직계위</h4>
          <table class="tbl_list mt5" summary="조회결과">
            <colgroup>
              <col width="25%" /><col width="25%" /><col width="25%" /><col width="25%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">서비스망</th>
                <th scope="col">센터/지연본부</th>
                <th scope="col">주노드</th>
                <th scope="col">노드</th>
              </tr>
            </thead>
            <tbody>
              <tr class="top">
                <td>${resultListVo.bfssvcLineTypeNm}</td>
                <td>${resultListVo.bfslvlGroupNm}</td>
                <td>${resultListVo.bfslvlHighNm}</td>
                <td>${resultListVo.bfslvlNm}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <table class="tbl_data entry" summary="조회결과">
            <colgroup>
              <col width="20%" /><col width="60%" /><col width="20%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">국사선택</th>

                <td>
                  <el-input v-model="ssvcObjCdpop" size="mini" class="txt w99">
                    <template #suffix>
                      <el-button
                        slot="trigger"
                        size="small"
                        style="font-size: larger; border: none; float: right"
                        icon="el-icon-search"
                        class="font-weight-bolder"
                        @click="fnViewSearchCenterLvlCd()"
                      />
                    </template>
                  </el-input>
                </td>
              </tr>
            </tbody>
          </table>

        </div>

        <div class="content_result">
          <h4>노드국 목록</h4>

          <table id="orgTable" class="tbl_list mt5" summary="서비스망목록">
            <caption>조직계위정보 목록</caption>
            <colgroup>
              <col width="12%" />
              <col width="12%" />
              <col width="12%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">계위코드</th>
                <th scope="col">국사명</th>
                <th scope="col"></th>
              </tr>
            </thead>

            <tbody>
              <!-- 조회된 결과 목록이 없을 때 -->
              <template v-if="resultListVo.totalCount === 0">
                <tr class="subbg last">
                  <td class="first" colspan="3">조회된 결과 목록이 존재하지 않습니다.</td>
                </tr>
              </template>

              <!-- 조회된 결과 목록이 있을 때 -->
              <template v-if="resultListVo.totalCount > 0">
                <tr
                  v-for="(item, index) in resultListVo.tbLvlSubCdVos"
                  :key="index"
                  :class="{'subbg': index % 2 !== 0, 'last': index === resultListVo.tbLvlSubCdVos.length - 1}"
                >
                  <td class="ellipsis" :title="item.slofficecode">{{ item.slofficecode }}</td>
                  <td class="ellipsis" :title="item.slofficeNm">{{ item.slofficeNm }}</td>
                  <td>
                    <el-button size="mini" @click="fnDeleteTbLvlSubCd()">삭제</el-button>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini">
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

const routeName = 'ModalInsertLvlSonMgmt'

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
      ssvcGroupCd: '-------',
      ssvcObjCd: '-------',
      ssvchighObjCd: '-------',
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
    },
    fnViewSearchCenterLvlCd() {
      this.$refs.ModalEntireOrgSearch.open({ viewTitle: '노드' })
    },
    setSelectedRow(row) {

    },
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
