<template>
  <el-dialog :visible.sync="dialogVisible" custom-class="medium_dialog" center :modal="true" :show-close="true" :before-close="cancel" :close-on-click-modal="false" :close-on-press-escape="false" :append-to-body="true" title="장애구간">
    <el-row :gutter="15">
      <el-col :span="24" style="height: 100%">
        <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%' }">
          <el-row class="d-flex flex-column justify-center h-100">
            <div class="node-section d-flex justify-center">
              <img src="@/assets/images/nia/node/switch.png" />
              <div class="blinking mt-8" />
              <img src="@/assets/images/nia/node/switch.png" />
            </div>
            <div class="node-info d-flex justify-evenly">
              <div style="width: 390px">
                <div style="float: left">
                  <div>{{ trafficInfo.root_cause_sysnamea }}</div>
                  <div>
                    <span v-if="trafficInfo.root_cause_porta">({{ trafficInfo.root_cause_porta }})</span>
                  </div>
                </div>
                <div style="float: right">
                  <div>{{ trafficInfo.root_cause_sysnamea }}</div>
                  <div>
                    <span v-if="trafficInfo.root_cause_portz">({{ trafficInfo.root_cause_portz }})</span>
                  </div>
                </div>
              </div>
            </div>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="cancel()"> <i class="el-icon-close"></i> 닫기 </el-button>
    </span>
  </el-dialog>
</template>

<script>
const routerName = 'errorRangeWidget'

export default {
  name: routerName,
  components: {},
  props: [],
  data() {
    return {
      dialogVisible: false,
      trafficInfo: {},
    }
  },
  computed: {},
  created: function () {},
  mounted(data) {},
  destroyed() {},
  methods: {
    setVisible: function (params) {
      this.dialogVisible = true
      this.trafficInfo = params.trafficInfo
    },
    cancel: function (done) {
      this.dialogVisible = false
      this.tableData = []
    },
  },
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog {
  width: 500px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.node-section {
  img {
    width: 85px;
    height: 80px;
    border-radius: 50%;
    padding: 5px 10px;
    border: solid 7px #c7bdbd;
  }
  div {
    height: 5px;
    width: 195px;
    border-bottom: 5px solid #e41f1f;
    animation: blink 0.7s ease-in-out infinite alternate;
  }
}
.node-info {
  div {
    font-size: 12px;
    width: 110px;
    color: #cb5252;
    text-align: center;
    font-weight: bolder;
  }
}
</style>

<style>
/* Dialog 스타일 개선 */
.medium_dialog {
  border-radius: 16px !important;
  overflow: hidden;
}

.medium_dialog .el-dialog__header {
  padding: 24px 24px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.medium_dialog .el-dialog__title {
  font-size: 20px;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: -0.5px;
  display: flex;
  align-items: center;
}

.medium_dialog .el-dialog__title::before {
  content: '📊';
  margin-right: 10px;
  font-size: 22px;
}

.medium_dialog .el-dialog__headerbtn {
  top: 24px;
  right: 24px;
}

.medium_dialog .el-dialog__headerbtn .el-dialog__close {
  color: #ffffff;
  font-size: 20px;
  font-weight: 600;
}

.medium_dialog .el-dialog__headerbtn .el-dialog__close:hover {
  color: #f0f0f0;
}

.medium_dialog .el-dialog__body {
  padding: 24px;
  background: #f8fafc;
}

.medium_dialog .el-dialog__footer {
  padding: 16px 24px 20px;
  background: #ffffff;
  border-top: 1px solid #e2e8f0;
}
</style>
