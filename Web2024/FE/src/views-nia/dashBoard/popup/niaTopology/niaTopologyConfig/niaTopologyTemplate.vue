<template>
  <div>
    <div id="template-2">
      <div class="gripper"></div>
      <div class="properties flex-column" style="display: none">
        <div class="flex-item node" style="display: none">
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
                <table style="font-size: 10px; width: 25em; word-break: break-all; line-height: 15px; display: table-cell; text-align: center">
                  <tbody style="height: 15em">
                    <tr>
                      <td rowspan="2" style="height: 6em">OMU A</td>
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
                    <tr></tr>
                    <tr>
                      <td rowspan="2" style="height: 6em">OMU B</td>
                    </tr>
                    <tr></tr>
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
      <div class="properties flex-column" style="display: none">
        <div class="flex-item node" style="display: none">
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
                <div class="left">ROADM <span class="roadmSysname">${시스템명}</span></div>
              </div>
              <div class="table-wapper">
                <table style="font-size: 10px; width: 25em; word-break: break-all; line-height: 15px; display: table-cell; text-align: center">
                  <tbody>
                    <tr class="slot-table" style="height: 15em">
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
                      <td class="S17" style="display: none"></td>
                      <td class="S18" style="display: none"></td>
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

<script></script>
