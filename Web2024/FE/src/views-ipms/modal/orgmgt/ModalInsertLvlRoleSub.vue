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
        시설 수용국 관리(FM)
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
                <td>${{ resultVo.ssvcLineTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">센터/지역본부</th>
                <td>${resultVo.ssvcGroupNm }</td>

              </tr>
              <tr>
                <th class="first" scope="row">노드</th>
                <td>${resultVo.ssvcObjNm}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">주노드</th>
                <td>${resultVo.ssvchighObjNm}</td>

              </tr>

            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>변경후 조직계위</h4>
          <table class="tbl_data entry">
            <colgroup>
              <col width="30%" /><col width="70%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">서비스망</th>
                <td>
                  <select id="ssvcLineTypeCdpop" name="ssvcLineTypeCdpop" onkeypress="fnSrchEnterPress();">
                    <option value="">-선택-</option>
                    <c:forEach var="item" items="${isvcLineListVo.tbLvlBasVos}" var-status="status">
                      <c:if test="${item.ssvcLineTypeCd != 'CL0000'}">
                        <option value="${item.ssvcLineTypeCd }">${item.ssvcLineTypeNm }</option>
                      </c:if>
                    </c:forEach>
                  </select>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">센터/지역본부</th>
                <td>
                  <select id="ssvcGroupCdpop" name="ssvcGroupCdpop" onkeypress="fnSrchEnterPress();">
                    <c:forEach var="item" items="${icenterListVo.tbLvlBasVos}" var-status="status">
                      <option value="${item.ssvcGroupCd }">${item.ssvcGroupNm }</option>
                    </c:forEach>
                  </select>
                </td>

              </tr>
              <tr>
                <th class="first" scope="row">노드</th>
                <td>
                  <select id="ssvcObjCdpop" name="ssvcObjCdpop" onkeypress="fnSrchEnterPress();">
                    <c:forEach var="item" items="${inodeListVo.tbLvlBasVos}" var-status="status">
                      <option value="${item.ssvcObjCd }">${item.ssvcObjNm }</option>
                    </c:forEach>
                  </select>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">주노드</th>
                <td>
                  <input id="ssvcHighCdpop" type="hidden" class="txt w99" name="ssvcHighCdpop" />
                  <div class="search w98">
                    <input id="ssvcHighCdNmpop" type="text" class="txt w95" name="ssvcHighCdNmpop" readonly="readonly" value="${resultVo.ssvchighObjNm}" />
                    <a href="#none">
                      <img id="btnInsertSearchHighLvlCd" src="<c:url value='/resources/images/content/btn_data_search_off.gif'/>" alt="search" class="sc_btn" onmouseover="menuOver(this);" onmouseout="menuOut(this);" />
                    </a>
                  </div>
                </td>
              </tr>
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

const routeName = 'ModalInsertLvlRoleSub'

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
    onClose() {
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
