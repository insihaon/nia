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
        HOST 상세 정보
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <div class="content_result bottom_dotted mt0">
          <h4 class="mt5">공통항목</h4>
          <table class="tbl_data entry mt5" summary="공통항목">
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">IP</th>
                <td class="view">
                  <span v-if="selectedRow">
                    {{ selectedRow.pipHostInet }}
                  </span>
                </td>
                <th scope="row">용도</th>
                <td>
                  <el-input
                    id="ipScomment"
                    type="text"
                    class="txt w95"
                    :value="selectedRow.scomment"
                  />
                </td>
                <th scope="row">IP 버전</th>
                <td class="view">
                  <span v-if="selectedRow">
                    {{ selectedRow.sipVersionTypeNm }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <div class="tit_group">
            <h4 class="mt5">세부항목</h4>
          </div>
          <table id="ipHostTable" class="tbl_list mt5" summary="목록">
            <caption>목록</caption>
            <colgroup>
              <col width="5%" />
              <col width="10%" />
              <col width="20%" />
              <col width="10%" />
              <col width="20%" />
              <col width="10%" />
              <col width="25%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col"></th>
                <th scope="col" colspan="2">기본 정보</th>
                <th scope="col" colspan="2">시설 정보</th>
                <th scope="col" colspan="2">회선 정보</th>
              </tr>
            </thead>
            <tbody>
              <!-- <tr v-if="resultListVo.totalCount == 0">
                <td colspan="7">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr> -->
              <tr v-for="(item, index) in hostDetailList" :key="index" :class="{'subbg': (index % 2) !== 0, 'last': index === hostDetailList.length - 1}">
                <template v-if="index % 4 === 0">
                  <th class="first" scope="row" :rowspan="4">
                    <input
                      type="radio"
                      class="radio"
                      name="radioIp"
                      :value="item.nipHostMstSeq"
                      :checked="item.sprorityYn === 'Y'"
                    />
                  </th>
                </template>
                <th scope="row">수용국</th>
                <td>{{ item.srssofficesNm }}</td>
                <th scope="row">I/F명</th>
                <td>{{ item.sipIfNm }}</td>
                <th scope="row">서비스</th>
                <td>{{ item.sassignTypeNm }}</td>
              </tr>
              <tr v-for="(item, index) in hostDetailList" :key="index + '-host'" :class="{'subbg': (index % 2) !== 0}">
                <th scope="row">HOST유형</th>
                <td>{{ item.sipHostTypeNm }}</td>
                <th scope="row">장비명</th>
                <td>{{ item.sipHostNm }}</td>
                <th scope="row">상품명</th>
                <td>{{ item.sexSvcNm }}</td>
              </tr>
              <tr v-for="(item, index) in hostDetailList" :key="index + '-link'" :class="{'subbg': (index % 2) !== 0}">
                <th scope="row">외부연동</th>
                <td>{{ item.sexLinkUseTypeNm }}</td>
                <th scope="row">모델명</th>
                <td>{{ item.smodelname }}</td>
                <th scope="row">SAID</th>
                <td>{{ item.ssaid }}</td>
              </tr>
              <tr v-for="(item, index) in hostDetailList" :key="index + '-leased'" :class="{'subbg': (index % 2) !== 0}">
                <th scope="row"></th>
                <td></td>
                <th scope="row"></th>
                <td></td>
                <th scope="row">전용회선</th>
                <td>{{ item.sllnum }}</td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini">
          대표IP 지정
        </el-button>
        <el-button size="mini">
          용도 저장
        </el-button>
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
import { onMessagePopup } from '@/utils/index'

const routeName = 'ModalHostInfoDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      radio: 1,
      selectedRow: null,
      hostDetailList: [
        { nipHostMstSeq: 'A', sprorityYn: 'Y', srssofficesNm: 'C', sipIfNm: 'D', sassignTypeNm: 'E', sipHostTypeNm: 'F', sipHostNm: 'G', sexSvcNm: 'H', sexLinkUseTypeNm: 'I', smodelname: 'J', ssaid: 'K', sllnum: 'L' }
      ],
       tableColumns: [
        // { prop: '', label: '선택', width: 50, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'a', label: '생성차수', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'b', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'c', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'd', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'e', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'f', label: '배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
        formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              props: {
                size: 'mini'
              },
              on: { click: () => {
                this.close()
                this.$router.push({ path: '/ipAssignMng/ipAssign', query: { ipAddress: row.b } })
              }
            } }, '배정')
          }
        },
      ],
      tableDatas: [
        { a: 'K200013000', b: '61.74.143.0/24', c: '61.74.143.0', d: '61.74.143.255', e: '1', f: '' }
      ]
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.$set(this, 'selectedRow', model)
    },
    onClose() {
    },
    fnUpdateSprorityYnBtnClick() {
      // Handle representative IP designation logic
    },
    fnUpdateScommentBtnClick() {
      // Handle usage save logic
    },
    fnCloseBtnClick() {
      // Handle close button logic
    },
    menuOver(event) {
      // Handle mouse over event
    },
    menuOut(event) {
      // Handle mouse out event
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
