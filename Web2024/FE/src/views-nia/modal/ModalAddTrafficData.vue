<template>
  <div>
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="false"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-edit-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-user mr-2" style="font-size: 17px;" />
          {{ titleMode }}
          <hr>
        </span>
        <table class="basic">
          <th>{{ titleNameA }}</th>
          <td v-if="viewType === 'AGENCY'">
            <el-select
              v-model="agency_id"
            >
              <el-option
                v-for="item in agencyList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <td v-if="viewType === 'APP'">
            <el-input v-model="protocol" />
          </td>
          <tr>
            <th>{{ titleNameB }}</th>
            <td>
              <el-input v-model="input_item" />
            </td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button size="medium" @click.native="modeChange()">
            {{ '등록' }}
          </el-button>
          <el-button class="exit-btn" size="medium" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import { apiInsertAgencyIpList, apiSelectAgencyCodeList } from '@/api/nia'

const routeName = 'ModalAddTrafficData'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      rowInfo: {
      },
      agency_id: '',
      agencyList: [],
      protocol: '',
      input_item: ''
    }
  },
  computed: {
    ...mapState({
      viewport: state => state.app.viewport,
      username: state => state.user.name,
    }),
    titleMode() {
      if (this.viewType === 'AGENCY') {
        return '이용기관 등록'
      } else {
        return '어플리케이션 등록'
      }
    },
    titleNameA() {
      if (this.viewType === 'AGENCY') {
        return '이용기관'
      } else {
        return '프로토콜'
      }
    },
    titleNameB() {
      if (this.viewType === 'AGENCY') {
        return 'IP 주소'
      } else {
        return '포트'
      }
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.rowInfo = {}
      this.onloadAgencyCodeList()
    },
    async onloadAgencyCodeList() {
      try {
        const res = await apiSelectAgencyCodeList()
        this.selectCodeData = res.result.map(item => ({ label: item.name, value: item.id }))
        this.agencyList = this.selectCodeData
      } catch (error) {
          console.error(error)
        } finally {
          // this.closeLoading(target)
        }
    },
    modeChange() {
      if (this.viewType === 'AGENCY') {
        this.insertAgencyIpData()
      } else {
        this.insertAppData()
      }
    },
    async insertAgencyIpData() {
       this.$confirm('이용기관을 등록하시겠습니까?', `이용기관 등록`, {
          confirmButtonText: '예',
          cancelButtonText: '아니오',
          type: 'success'
        }).then(async() => {
          const selectedItem = this.agencyList.find(item => item.value === this.agency_id)
          const param = {
            nren_id: this.agency_id,
            nren_name: selectedItem.label,
            nren_ip: this.input_item
          }
          try {
            const res = await apiInsertAgencyIpList(param)
            if (res.result > 0) {
              this.$message('등록 되었습니다.')
              this.close()
            }
          } catch (error) {
            console.error(error)
          } finally {
            /*  */
          }
        })
    },
    insertAppData() {

    },
    onClose() { /* for Override */ },
    onSubmit() {
        console.log('submit!')
      }
    }
  }
</script>

