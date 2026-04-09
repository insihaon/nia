<template>
  <div>

    <!-- 페이지 타이틀 -->
    <div class="page-title-wrap">
      <div class="page-title">
        <span>IP블록관리</span>
      </div>
      <div class="page-loc">
        <i class="el-icon-s-home" />
        <span>IP배정관리</span>
        <span>IP블록관리</span>
      </div>
    </div>

    <div class="searchOptionWrap">
      <table>
        <tr>
          <th>공인/사설</th>
          <td>
            <el-select v-model="value" placeholder="선택하세요" size="small">
              <el-option label="공인" value="0" />
              <el-option label="사설" value="1" />
            </el-select>
          </td>
          <th>생성차수</th>
          <td>
            <el-select v-model="value" placeholder="선택하세요" size="small">
              <el-option label="전체" value="0" />
            </el-select>
          </td>
          <th>IP주소</th>
          <td class="textflex">
            <el-select v-model="value" placeholder="선택하세요" size="small">
              <el-option label="IPv4" value="0" />
              <el-option label="IPv6" value="0" />
            </el-select>
            <el-input placeholder="---" size="small" />
          </td>
          <td rowspan="2" class="textcenter">
            <el-button type="primary" icon="el-icon-search" round>조회</el-button>
          </td>
        </tr>
        <tr>
          <th>서비스망</th>
          <td>
            <el-select v-model="value" placeholder="선택하세요" size="small">
              <el-option label="KORNET" value="0" />
            </el-select>
          </td>
          <th>작업일자</th>
          <td>
            <el-date-picker
              type="daterange"
              align="right"
              size="small"
              unlink-panels
              range-separator="-"
              start-placeholder="시작"
              end-placeholder="종료"
            />
          </td>
          <th>정렬</th>
          <td class="textflex">
            <el-select v-model="value" placeholder="선택하세요" size="small">
              <el-option label="IP" value="0" />
            </el-select>
            <el-radio v-model="radio" label="1">오름차순</el-radio>
            <el-radio v-model="radio" label="2">내침차순</el-radio>
          </td>
        </tr>
      </table>
    </div>

    <div class="tableListWrap">
      <div class="tableListHeader">
        <div>
          <i class="el-icon-document" />
          <span>IP 블록관리 조회 결과</span>
          <span>( 전체항목 : 538 건 )</span>
        </div>
        <div>
          <el-button type="info" size="mini" icon="el-icon-download" round>엑셀 파일로 저장</el-button>
          <el-select v-model="value" placeholder="보기옵션" size="mini">
            <el-option label="10개보기" value="0" />
            <el-option label="20개보기" value="0" />
          </el-select>
        </div>
      </div>
      <table>
        <tr>
          <th width="30px" />
          <th>공인/사설</th>
          <th>생성자수</th>
          <th>서비스망</th>
          <th>IP블록</th>
          <th>시작IP</th>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
        <tr>
          <td class="textcenter"><input type="radio"></td>
          <td>공인</td>
          <td>K00000000</td>
          <td>KORNET</td>
          <td>14.33.0.0/13</td>
          <td>14.33.0.0</td>
        </tr>
      </table>
      <div class="tableListPaging">
        <el-button icon="el-icon-d-arrow-left" type="text" />
        <el-button icon="el-icon-arrow-left" type="text" />
        <div class="pagingNumber">
          <span class="active">1</span>
          <span>2</span>
          <span>3</span>
          <span>4</span>
          <span>5</span>
          <span>6</span>
          <span>7</span>
          <span>8</span>
          <span>9</span>
          <span>10</span>
        </div>
        <el-button icon="el-icon-arrow-right" type="text" />
        <el-button icon="el-icon-d-arrow-right" type="text" />

        <div class="rightBtns">
          <el-button type="primary" size="mini" icon="el-icon-tickets" round>신규생성</el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit-outline" round>추가생성</el-button>
          <el-button type="primary" size="mini" icon="el-icon-finished" round>상세</el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit-outline" round>수정</el-button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      radio: '1'
    }
  },
  methods: {
    goto(pageName) {
      this.$router.push({ name: pageName })
    }
  }
}
</script>
<style scoped lang="scss">

</style>
