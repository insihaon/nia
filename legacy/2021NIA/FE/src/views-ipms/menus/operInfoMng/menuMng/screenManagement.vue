<template>
  <el-row class="w-100 h-100">
    <div class="searchOptionWrap">
      <table>
        <th>
          <label>화면유형</label>
        </th>
        <td>
          <el-select
            v-model="scrnType"
            size="mini"
            class="w-100"
          >
            <el-option
              v-for="(option, i) in channelOptions"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </td>
        <th>
          <label>조회조건</label>
        </th>
        <td class="textflex">
          <el-select
            v-model="inquiryValue"
            size="mini"
            class="w-100"
            @change="handleChangeCondition"
          >
            <el-option
              v-for="(option, i) in inquiryOptions"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
          <el-input v-model="inquiryTxt" size="mini" clearable />
        </td>
        <td colspan="4">
          <div class="searchBtnWrap">
            <el-button type="info" size="mini" icon="el-icon-refresh" round @click="handleRefresh()">
              초기화
            </el-button>
            <el-button type="primary" size="mini" icon="el-icon-search" round @click="handleSearch()">
              조회
            </el-button>
          </div>
        </td>
      </table>
    </div>
    <el-col :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="resultListVo"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
        :prop-on-select="handleClickTableCheck"
        :prop-enabled-excel-down="false"
      >
        <template slot="text-description">
          <span>
            화면
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px;">
            <el-button type="primary" icon="el-icon-document-add" size="mini" round @click="fnInsertScrnBas('insert')">등록</el-button>
            <el-button type="primary" icon="el-icon-success" size="mini" round @click="fnUpdateSscrnUseYn('Y')">사용</el-button>
            <el-button type="primary" icon="el-icon-remove" size="mini" round @click="fnUpdateSscrnUseYn('N')">미사용</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalDetailScrn ref="ModalDetailScrn" @reload="fnViewListScrnBas()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalDetailScrn from '@/views-ipms/modal/menumgmt/ModalDetailScrn.vue'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
const routeName = 'ScreenManagement'

export default {
  name: routeName,
  components: { CompTable, ModalDetailScrn },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'sscrnId', label: '화면ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sscrnNm', label: '화면명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sscrnUrlAdr', label: '화면 URL 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sscrnTypeNm', label: '화면유형', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sdgnScrnId', label: '설계화면 ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sscrnUseYn', label: '화면사용여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      channelOptions: [
        { label: '전체', value: '' },
        { label: '미분류', value: 'UD0000' },
        { label: 'MAIN', value: 'UD0001' },
        { label: 'SUB', value: 'UD0002' },
        { label: 'POPUP', value: 'UD0003' },
      ],
      scrnType: '',
      inquiryOptions: [
        { label: '화면ID', value: 'sscrnId' },
        { label: '화면명', value: 'sscrnNm' },
        { label: '설계화면', value: 'sdgnScrnId' },
      ],
      inquiryValue: 'sscrnId',
      inquiryTxt: '',
      resultListVo: [],
      selectedChecks: [],
    }
  },
  mounted() {
    this.fnViewListScrnBas()
  },
  methods: {
    handleRefresh() {
      this.scrnType = ''
      this.inquiryValue = 'sscrnId'
      this.inquiryTxt = ''
      this.fnViewListScrnBas()
    },
    async fnViewListScrnBas() {
      const params = {
        sscrnTypeCd: this.scrnType,
        searchCnd: this.inquiryValue,
        searchWrd: this.inquiryTxt,
      }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListScrnBas, params)
        this.resultListVo = res?.result?.data
      } catch (error) {
        console.error(error)
      }
    },
    handleChangeCondition() {
      this.inquiryTxt = ''
    },
    handleClickTableCheck(all, cur) {
      this.selectedChecks = all
    },
    onClcikRow(row) {
      this.fnSelectDetailView(row, 'detail')
    },
    async fnSelectDetailView(row, type) { /* 수정 */
      try {
        const tbScrnBas = {
          sscrnId: row.sscrnId
        }
        const res = await apiRequestModel(ipmsModelApis.viewDetailScrnBas, tbScrnBas)
        if (res) {
          this.$refs.ModalDetailScrn.open({ row: res.result.data, type: type })
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnInsertScrnBas(type) { /* 등록 */
      this.$refs.ModalDetailScrn.open({ type: type })
    },
    async fnUpdateSscrnUseYn(type) { /* 사용여부 수정 */
      if (this.selectedChecks.length === 0) {
        this.$message('변경할 대상이 없습니다.')
        return
      }
      try {
          const tbScrnBasListVo = {
              sscrnUseYn: type,
              tbScrnBasVos: this.selectedChecks.map(item => {
                return { sscrnId: item.sscrnId }
              })
            }

          const res = await apiRequestJson(ipmsJsonApis.updateScrnUseYn, tbScrnBasListVo)
          if (res.commonMsg === 'SUCCESS') {
            this.$message('사용여부가 정상적으로 수정되었습니다.')
          } else {
            this.$message(`${res.commonMsg}`)
          }
          this.fnViewListScrnBas()
        } catch (error) {
          console.error(error)
        }
    }

  }
}
</script>
<style lang="css" scoped>
</style>
