<template>
  <div>
    <!-- <div layout="column" class="alarmListWapper" :style="{ height: '210px', transition: 'all 0.3s ease 0s' }">
            <md-toolbar class="md-whiteframe-1dp" v-if="options.showOption">
            <div class="md-toolbar-tools">
                <div class="md-title">Material Design Data Table</div>
            </div>
            </md-toolbar>

            <md-content class="alarm-content">
            <table v-if="ticketAlarmsType === 'RT'" md-table class="alarm-table rt-alarm" :md-row-select="options.rowSelection" :multiple="options.multiSelect"
                    v-model="selected" md-progress="promise">
                <thead v-if="!options.decapitate" md-head :md-order="query.order" @md-on-reorder="logOrder" style="background-color: #274073;">
                <tr md-row>
                    <th md-column :md-order-by="'alarmno'"><span>alarmno</span></th>
                    <th md-column><span>alarmlevel</span></th>
                    <th md-column :md-order-by="'sysname'"><span>sysname</span></th>
                    <th md-column :md-order-by="'equiptype'"><span>equiptype</span></th>
                    <th md-column :md-order-by="'alarmtime'"><span>alarmtime</span></th>
                    <th md-column :md-order-by="'receivetime'"><span>receivetime</span></th>
                    <th md-column :md-order-by="'alarmloc'"><span>alarmloc</span></th>
                    <th md-column :md-order-by="'alarmmsg'"><span>alarmmsg</span></th>
                    <th md-column :md-order-by="'unit'"><span>unit</span></th>
                </tr>
                </thead>
                <tbody data-vs-repeat="24" md-body style="height: calc(100% - 38px);">
                <tr md-row :md-select="alarm" @md-on-select="logItem" :md-auto-select="options.autoSelect"
                    :ng-disabled="alarm.calories.value > 400" :class="[{ diff_red: alarm.related_alarm !== true }, covertAlarmLevelToColor(alarm.alarmlevel, 'class')]"
                    v-for="alarm in filteredAlarms.result = (ticketRtAlarms | orderBy: query.order)" :key="alarm.alarmno">
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.alarmno }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    {{ covertAlarmLevelToColor(alarm.alarmlevel, 'class') | toUpper }}
                    <div class="alarm-level-box" :class="covertAlarmLevelToColor(alarm.alarmlevel, 'class')"></div>
                    </td>
                    <td md-cell class="slim-cell">
                    <a href="" @click="clickAlarms(alarm)">
                        <div class="over-cell" show-over-text :style="{ borderBottom: '1px solid #009688', color: clickAlarmData === alarm ? 'blue' : '' }">
                        {{ alarm.sysname }}
                        </div>
                    </a>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.equiptype }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.alarmtime | dateFormat }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.receivetime | dateFormat }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.alarmloc }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.alarmmsg }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.unit }}</div>
                    </td>
                </tr>
                </tbody>
            </table>

            <table v-if="ticketAlarmsType === 'PF'" md-table class="alarm-table pf-alarm" :md-row-select="options.rowSelection" :multiple="options.multiSelect"
                    v-model="selected" md-progress="promise">
                <thead v-if="!options.decapitate" md-head :md-order="query.order" @md-on-reorder="logOrder" style="background-color: #274073;">
                <tr md-row>
                    <th md-column :md-order-by="'sysname'"><span>sysname</span></th>
                    <th md-column style="text-align: left"><span>port</span></th>
                    <th md-column :md-order-by="'unit'"><span>unit</span></th>
                    <th md-column :md-order-by="'tmper'"><span>tmper</span></th>
                    <th md-column :md-order-by="'rxcur'"><span>rxcur</span></th>
                    <th md-column :md-order-by="'rxmin'"><span>rxmin</span></th>
                    <th md-column :md-order-by="'rxmax'"><span>rxmax</span></th>
                    <th md-column :md-order-by="'rxave'"><span>rxave</span></th>
                    <th md-column :md-order-by="'txcur'"><span>txcur</span></th>
                    <th md-column :md-order-by="'txmin'"><span>txmin</span></th>
                    <th md-column :md-order-by="'txmax'"><span>txmax</span></th>
                    <th md-column :md-order-by="'txave'"><span>txave</span></th>
                    <th md-column :md-order-by="'ocrtime'"><span>ocrtime</span></th>
                </tr>
                </thead>
                <tbody data-vs-repeat="24" md-body>
                <tr md-row :md-select="alarm" @md-on-select="logItem" :md-auto-select="options.autoSelect"
                    :ng-disabled="alarm.calories.value > 400" :class="{ diff_red: alarm.related_alarm !== true }"
                    v-for="alarm in filteredPFAlarms.result = (ticketPFAlarms | orderBy: query.order)" :key="alarm.alarmno">
                    <td md-cell class="slim-cell">
                    <a href="" @click="clickAlarms(alarm)">
                        <div class="over-cell" show-over-text :style="{ borderBottom: '1px solid #009688', color: clickAlarmData === alarm ? 'blue' : '' }">
                        {{ alarm.sysname }}
                        </div>
                    </a>
                    </td>
                    <td md-cell class="slim-cell" style="text-align: left">
                    <div class="over-cell" show-over-text>{{ alarm.port }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.unit }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.tmper }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.rxcur }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.rxmin }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.rxmax }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.rxave }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.txcur }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.txmin }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.txmax }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.txave }}</div>
                    </td>
                    <td md-cell class="slim-cell">
                    <div class="over-cell" show-over-text>{{ alarm.ocrtime | dateFormat }}</div>
                    </td>
                </tr>
                </tbody>
            </table>
            </md-content>

            <div layout="row" class="bottom-toolbar" style="overflow: hidden; background-color: #274073; height: 35px;">
            <span style="margin-top: -30px; margin-left: 20px; display: none; color: #F00;">...</span>
            <table class="map3d-legend-color border-left" style="background-color: transparent;">
                <tr class="map3d-legend-color_row">
                <td><div class="map3d-legend-color_swatch critical" @click="exportEquipExl(tools.constants.Action.SELECT_AL_EQUIP2TC_LIST, 'ff')"></div></td>
                <td class="map3d-legend-color_label" @click="debugger()">Critical</td>
                <td><div class="map3d-legend-color_swatch major"></div></td>
                <td class="map3d-legend-color_label">Major</td>
                <td><div class="map3d-legend-color_swatch minor"></div></td>
                <td class="map3d-legend-color_label">Minor</td>
                <td><div class="map3d-legend-color_swatch warning"></div></td>
                <td class="map3d-legend-color_label">Warning</td>
                <td><div class="map3d-legend-color_swatch clear"></div></td>
                <td class="map3d-legend-color_label">Clear</td>
                </tr>
            </table>
            <div flex></div>
            <div v-if="ticketAlarmsType === 'RT'" class="alarm-count">
                <span style="color: #fff; font-size: 15px;">TOTAL</span>&nbsp;
                <span style="color: #f37e7e; font-size: 18px;">{{ filteredAlarms.result.length }}</span>
            </div>
            </div>
        </div> -->

    <div id="template-2">
      <div class="gripper"></div>
      <div class="properties flex-column" style="display: none;">
        <div class="flex-item node" style="display: none;">
          <div>NODE INFORMATION</div>
          <table class="tg">
            <thead>
              <tr class="alias">
                <th>ALIAS</th>
                <th>Device1</th>
                <th>Device2</th>
              </tr>
            </thead>
            <tr class="id">
              <td>ID</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="ip">
              <td>IP</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="type">
              <td>TYPE</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="mac">
              <td>MAC</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="alarm">
              <td>ALARM</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="port">
              <td>PORT</td>
              <td></td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item link">
          <div>LINK INFOMATION</div>
          <table>
            <tr class="link_alias">
              <td>ALIAS</td>
              <td></td>
            </tr>
            <tr class="speed">
              <td>SPEED</td>
              <td></td>
            </tr>
            <tr class="status">
              <td>STATUS</td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item unit">
          <div id="template-POTN">
            <div class="template POTN">
              <div class="title-wapper">
                <div class="left">POTN <span class="potnSysname">${시스템명}</span></div>
              </div>
              <div class="table-wapper">
                <table style="font-size: 10px; width: 25em; word-break: break-all; line-height: 15px; display: table-cell; text-align: center;">
                  <tbody style="height: 15em;">
                    <tr>
                      <td rowspan="2" style="height: 6em;">OMU A</td>
                      <td class="S1 S01" rowspan="4">S01</td>
                      <td class="S2 S02" rowspan="4">S02</td>
                      <td class="S3 S03" rowspan="4">S03</td>
                      <td class="S4 S04" colspan="2" rowspan="4">S04</td>
                      <td class="S5 S05" rowspan="4">S05</td>
                      <td class="S11" rowspan="4">OXCU A</td>
                      <td class="S12" colspan="2" rowspan="4">OXCU B</td>
                      <td class="S6 S06" colspan="2" rowspan="4">S06</td>
                      <td class="S7 S07" rowspan="4">S07</td>
                      <td class="S8 S08" rowspan="4">S08</td>
                      <td class="S9 S09" rowspan="4">S09</td>
                      <td class="S10" rowspan="4">S10</td>
                    </tr>
                    <tr>
                    </tr>
                    <tr>
                      <td rowspan="2" style="height: 6em;">OMU B</td>
                    </tr>
                    <tr>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div id="template-3">
      <div class="gripper"></div>
      <div class="properties flex-column" style="display: none;">
        <div class="flex-item node" style="display: none;">
          <div>NODE INFORMATION</div>
          <table class="tg">
            <thead>
              <tr class="alias">
                <th>ALIAS</th>
                <th>Device1</th>
                <th>Device2</th>
              </tr>
            </thead>
            <tr class="id">
              <td>ID</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="ip">
              <td>IP</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="type">
              <td>TYPE</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="mac">
              <td>MAC</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="alarm">
              <td>ALARM</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="port">
              <td>PORT</td>
              <td></td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item link">
          <div>LINK INFOMATION</div>
          <table>
            <tr class="link_alias">
              <td>ALIAS</td>
              <td></td>
            </tr>
            <tr class="speed">
              <td>SPEED</td>
              <td></td>
            </tr>
            <tr class="status">
              <td>STATUS</td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item unit">
          <div id="template-ROADM">
            <div class="template ROADM">
              <div class="title-wapper">
                <div class="left">ROADM  <span class="roadmSysname">${시스템명}</span> </div>
              </div>
              <div class="table-wapper">
                <table style="font-size: 10px; width: 25em; word-break: break-all; line-height: 15px; display: table-cell; text-align: center;">
                  <tbody>
                    <tr class="slot-table" style="height: 15em;">
                      <td class="S1 S01">S1</td>
                      <td class="S2 S02">S2</td>
                      <td class="S3 S03">S3</td>
                      <td class="S4 S04">S4</td>
                      <td class="S5 S05">S5</td>
                      <td class="S6 S06">S6</td>
                      <td class="S7 S07">S7</td>
                      <td class="S8 S08">S8</td>
                      <td class="S9 S09">S9</td>
                      <td class="S10">S10</td>
                      <td class="S11">S11</td>
                      <td class="S12">S12</td>
                      <td class="S13">S13</td>
                      <td class="S14">S14</td>
                      <td class="S15">S15</td>
                      <td class="S16">S16</td>
                      <td class="S17" style="display: none;"></td>
                      <td class="S18" style="display: none;"></td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
</script>
