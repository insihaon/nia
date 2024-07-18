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
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP블록할당
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <div class="content_result mt0">
          <h4>할당 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">할당구분</th>
                <td class="view">
                  <el-radio-group v-model="allocType" size="mini">
                    <el-radio label="allocNe">시설</el-radio>
                    <el-radio label="allocLn">링크</el-radio>
                    <el-radio label="allocTel">회선</el-radio>
                  </el-radio-group>
                </td>
                <th class="first">시설정보</th>
                <td class="view"></td>
              </tr>
              <tr>
                <th class="first">수용국</th>
                <td class="view"></td>
                <th class="first">장비명</th>
                <td class="view"></td>
              </tr>
              <tr>
                <th class="first">전용번호</th>
                <td class="view">
                  <el-input v-model="srcIpAllocMst.sllnum" size="mini" />
                </td>
                <th class="first">모델명</th>
                <td class="view"></td>
              </tr>
              <tr>
                <th class="first">I/F명</th>
                <td colspan="3" class="view">
                  <!-- <el-input v-model="srcIpAllocMst.sllnum" size="mini" /> -->
                </td>
              </tr>
              <tr>
                <th class="first">비고</th>
                <td colspan="3" class="view">
                  <textarea v-model="srcIpAllocMst.scomment" class="w98" rows="3" maxlength="4000" />
                </td>
              </tr>
              <!-- Add the rest of the table rows here following the same pattern -->
            </tbody>
          </table>
        </div>

        <div id="divSvcAloArea" class="content_result">
          <h4>할당 정보</h4>
          <table id="baseTable" class="tbl_list mt5" summary="할당 대상 정보">
            <caption>할당 정보</caption>
            <colgroup>
              <col width="9%" /><col width="10%" /><col width="9%" /><col width="10%" /><col width="23%" /><col width="27%" /><col width="12%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">서비스망</th>
                <th scope="col">노드</th>
                <th scope="col">공인/사설</th>
                <th scope="col">서비스</th>
                <th scope="col">IP블록</th>
                <th scope="col">GW IP</th>
                <th scope="col">단위블록수</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!ipAllocOperMstVos.length">
                <td class="first" colspan="8">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <tr v-for="(item, index) in ipAllocOperMstVos" :key="index" :class="{'subbg': index % 2 !== 0, 'last': index === ipAllocOperMstVos.length - 1}">
                <td class="first ellipsis">{{ item.ssvcLineTypeNm }}</td>
                <td class="ellipsis">{{ item.ssvcObjNm }}</td>
                <td class="ellipsis">{{ item.sipCreateTypeNm }}</td>
                <td class="ellipsis">{{ item.sassignTypeNm }}</td>
                <td class="ellipsis">{{ item.pipPrefix }}</td>
                <td>
                  <div class="inline-block w33">
                    <select v-model="item.gwipType" @change="fnSetGwip(item)">
                      <option value="direct">직접입력</option>
                      <option value="first">시작IP</option>
                      <option value="last">끝IP</option>
                    </select>
                  </div>
                  <div class="inline-block d-flex items-center w60">
                    <input v-model="item.slastAddrGwip" type="text" class="txt w95" style="height: 20px" @keyup="checkInput(item, 'IPonly')" />
                  </div>
                </td>
                <td class="ellipsis">{{ item.nclassCnt }}</td>
                <!-- <td style="display: none;">{{ item.nipAssignMstSeq }}</td>
                <td style="display: none;">{{ item.sassignTypeCd }}</td>
                <td style="display: none;">{{ item.sipCreateTypeCd }}</td>
                <td style="display: none;">{{ item.ssvcLineTypeCd }}</td>
                <td style="display: none;">{{ item.nlvlMstSeq }}</td>
                <td style="display: none;">{{ item.nipAllocMstCnt }}</td>
                <td style="display: none;">{{ item.slastAddrGwip }}</td>
                <td style="display: none;">{{ item.sfirstAddrGwip }}</td>
                <td style="display: none;">{{ item.nipmsSvcSeq }}</td>
                <td style="display: none;">{{ item.sipVersionTypeCd }}</td> -->
              </tr>
            </tbody>
          </table>

          <!-- <div class="btn_area mt10">
            <span>
              <a href="#none" @click="fnViewAllocCheckTacsIpBlock">
                <input type="image" src="path_to_image/btn_delegate_off.gif" value="배정" @mouseover="menuOver" @mouseout="menuOut" />
              </a>
            </span>
            <span>
              <a href="#none" @click="fnInsertAlcCloseBtnClick">
                <input type="image" src="path_to_image/btn_close_off.gif" value="닫기" @mouseover="menuOver" @mouseout="menuOut" />
              </a>
            </span>
          </div> -->
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click="close()">
          닫기
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'

const routeName = 'ModalIpAllocInsert'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      srcIpAllocMst: {
        sllnum: '',
        scomment: ''
      },
      allocType: 'allocNe',
      srhSvcTypeCd: 'llnum',
      srchSvcNum: '',
      resultList: [] // This should be populated with your data
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.ipAllocOperMstVos) {
        this.ipAllocOperMstVos = model.ipAllocOperMstVos
      }
    },
    onClose() {
    },
    searchSvcNum() {
      // Implement search functionality
    },
    menuOver(event) {
      // Implement hover effect
    },
    menuOut(event) {
      // Implement hover out effect
    },
    fnSetGwip(item) {
      // Implement the function to set GW IP
    },
    checkInput(item, type) {
      // Implement input check
    },
    fnViewAllocCheckTacsIpBlock() {
      // Implement the function to check TACS IP block
    },
    fnInsertAlcCloseBtnClick() {
      // Implement the function to close the insert allocation
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
