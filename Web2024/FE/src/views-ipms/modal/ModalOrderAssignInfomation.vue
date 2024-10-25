<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="오더 할당 정보"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <el-row class="w-100 h-100">
      <div class="popupContentTable mb-1">
        <div class="popupContentTableTitle">회선 정보</div>
        <table>
          <tr v-for="(info, index) in ipBlockInfo" :key="index">
            <template v-for="(item, itemIndex) in info">
              <th :key="itemIndex">
                <label>{{ item.label }}</label>
              </th>
              <td :key="itemIndex" :colspan="item.colspan || 1">
                <el-input v-model="tempVal" readonly size="mini" />
              </td>
            </template>
          </tr>
        </table>
      </div>
      <el-row>
        <el-col :span="24">
          <compTable
            ref="compTable"
            :prop-name="name"
            :prop-data="tableDatas"
            :prop-table-height="200"
            :prop-column="tableColumns"
            :prop-is-pagination="false"
            :prop-is-check-box="false"
            prop-grid-menu-id="inputSpeed"
            :prop-grid-indx="1"
          >
            <template slot="text-description">
              <span>
                IP 할당 정보
              </span>
            </template>
          </compTable>
        </el-col>
      </el-row>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="small" round @click="$refs.ModalIpBlockInfomation.open()">할당 대상블록 조회</el-button>
      <el-button v-if="tableDatas.length > 0" size="small" round @click="close()">할당</el-button>
      <el-button size="small" type="primary" class="el-icon-close" round @click.native="close()">
        {{ $t('exit') }}
      </el-button>
    </div>
    <ModalIpBlockInfomation ref="ModalIpBlockInfomation" @selected-value="setSelectedRow" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalIpBlockInfomation from '@/views-ipms/modal/ModalIpBlockInfomation'

const routeName = 'ModalOrderAssignInfomation'

export default {
  name: routeName,
  components: { CompTable, ModalIpBlockInfomation },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      tempVal: '',
      tableColumns: [
        { prop: 'mang', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'node', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'type', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'service', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'block', label: '블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'gwip', label: 'GW IP', width: 250, align: 'center', sortable: true, columnVisible: true, showOverflow: false,
          formatter: (row, col, value, index) => {
            return this.$createElement('div', [
              this.$createElement('el-select', {
                props: {
                  value: row.gwipType,
                  size: 'mini'
                },
                style: {
                  width: '90px'
                },
                on: {
                  change: (value) => {
                    // this.$set(row, 'gwipType', value)
                    this._merge(row, { gwipType: value })
                    this.$set(this.tableDatas, index, row)
                    // console.log('Selected:', selectedValue);
                  }
                }
              }, [
                this.$createElement('el-option', {
                  props: {
                    label: '직접입력',
                    value: 'direct'
                  }
                }),
                this.$createElement('el-option', {
                  props: {
                    label: '시작IP',
                    value: 'first'
                  }
                }),
                this.$createElement('el-option', {
                  props: {
                    label: '끝IP',
                    value: 'last'
                  }
                })
              ]),
              this.$createElement('el-input', {
                props: {
                  value: row.gwip,
                  size: 'mini'
                },
                style: {
                  width: '140px'
                },
                on: {
                  input: (value) => {
                    // this.$set(row, 'gwip', value)
                    this._merge(row, { gwip: value })
                    this.$set(this.tableDatas, index, row)
                  }
                }
              })
            ])
          }
        },
        { prop: 'status', label: '작업상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'allocCnt', label: '회선', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '삭제', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              props: {
                size: 'mini'
              },
              on: { click: () => {
                  this.handleClickDeleteRow(row, index)
              } } }, '삭제')
          }
        },
      ],
      tableDatas: []
    }
  },
  computed: {
    ipBlockInfo() {
      return [
        [
          { label: '노드국' },
          { label: '수용국' },
          { label: '사업용' }
        ],
        [
          { label: '오더번호' },
          { label: '전용번호' },
          { label: 'SAID' }
        ],
        [
          { label: '고객명' },
          { label: '속도' },
          { label: '희망일' }
        ],
        [
          { label: '상품명', colspan: 3 },
          { label: '접수일' },
        ],
        [{ label: '설치장소(지번)', colspan: 5 }],
        [{ label: '설치장소(도로명)', colspan: 5 }],
        [{ label: '접수자 참고사항', colspan: 5 }],
      ]
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.$set(this, 'selectedRow', model.row)
    },
    onClose() {
    },
    setSelectedRow(row) {
      row['node'] = row['org']
      row['gwipType'] = 'last'
      row['gwip'] = 'aaa'
      /* TO DO
        - unique key로 이미 데이터가 선택되었는지 검증
        - GW IP setting 비즈니스 로직 적용 필요
      */
      this.tableDatas.push(row)
    },
    handleClickDeleteRow(row, index) {
      this.$delete(this.tableDatas, index)
    }
  },
}
</script>
<style lang="scss" scoped>
::v-deep div.optionRow label {
  min-width: 130px;
  width: 130px;
}
</style>
