<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
<style>
@import "./assets/css/fonts/pretendard/pretendard-gov.scss";
*{
  font-family: "Pretendard GOV", sans-serif !important;
}
body{
  padding:0px;
  margin:0;
  background-color:#1a1a22;
  color:#FFFFFF;
}

div.page-title-wrap{
  border-bottom: 1px solid #0d465e;
  padding: 20px;
  margin-bottom:35px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  gap:15px;
  position:relative;
}
div.page-title-wrap::before{
  display: inline-block;
  position: absolute;
  content: "";
  width: 200px;
  border-bottom:1px solid #2d80f7;
  left:0;
  bottom:-1px;
}
div.page-title-wrap>div.page-title{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap:10px;
  padding-right:15px;
  height:100%;
}
div.page-title-wrap>div.page-title::before{
  content:"";
  display: inline-block;
  width:24px;
  height:24px;
  background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAAGXcA1uAAAACXBIWXMAAAsTAAALEwEAmpwYAAAH5WlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDI0LTA5LTI3VDE1OjAwOjI5KzA5OjAwIiB4bXA6TWV0YWRhdGFEYXRlPSIyMDI0LTA5LTI3VDE1OjAwOjI5KzA5OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAyNC0wOS0yN1QxNTowMDoyOSswOTowMCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo5NWQxNjNjYi05NDYwLTFjNDgtOTI0ZC1iYjUzMDFlNzczOTciIHhtcE1NOkRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDoyNTRkNjA3Yy0wMmQ3LWM2NGQtYTc3Yy01ZTMzNWM2Y2FmMTEiIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDplZjkxMWRiYS05MzE2LTVjNDAtOGM3OS00NTBkZmY5YjFkMDUiIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDplZjkxMWRiYS05MzE2LTVjNDAtOGM3OS00NTBkZmY5YjFkMDUiIHN0RXZ0OndoZW49IjIwMjQtMDktMjdUMTU6MDA6MjkrMDk6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6OTVkMTYzY2ItOTQ2MC0xYzQ4LTkyNGQtYmI1MzAxZTc3Mzk3IiBzdEV2dDp3aGVuPSIyMDI0LTA5LTI3VDE1OjAwOjI5KzA5OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDxwaG90b3Nob3A6RG9jdW1lbnRBbmNlc3RvcnM+IDxyZGY6QmFnPiA8cmRmOmxpPjU4NkZCNjBGMzUzNTg5RTBFRTk5ODI3N0RCRTlFMzFEPC9yZGY6bGk+IDxyZGY6bGk+NkFBMUQ1REJCQURGQUJDNzVCNDIyMUMyNzQzNTMxNTE8L3JkZjpsaT4gPHJkZjpsaT5BNTI1MDg2MzkxNzhFQUMxODRCOTNBQTBDM0JFQkNBMDwvcmRmOmxpPiA8cmRmOmxpPkJGODgyRTZFOTNGNUE4MzE1RDY4QjEyNUU3QTQ5RkRCPC9yZGY6bGk+IDxyZGY6bGk+QzEzQTc3QUUyNzZDQkIyM0NDODk2NUIxNDU3RTVCNjI8L3JkZjpsaT4gPHJkZjpsaT5hZG9iZTpkb2NpZDpwaG90b3Nob3A6ZjYzNzNmYmUtZjI2Ny1lNjRmLTkwYzktMDE1Y2VkNDkzNTA5PC9yZGY6bGk+IDxyZGY6bGk+eG1wLmRpZDozOTkzM2IxNC03NWYzLTM3NDItYmM0Ny0zMDliZTMwZjlmMjY8L3JkZjpsaT4gPHJkZjpsaT54bXAuZGlkOmRkM2FiMzk0LTI4YTYtMjA0My05MzgxLTJmMzE1MzMyNzRjYjwvcmRmOmxpPiA8L3JkZjpCYWc+IDwvcGhvdG9zaG9wOkRvY3VtZW50QW5jZXN0b3JzPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PjMugZUAAAHoSURBVEiJzZW9SxxRFMV/M8wYUNJpYWMsMkKKKNnYWLguSbr8A2nEpBIklaVNtEuqdIKNCHaBFIKN7AYksH6ARCyiaXQt/IgoojEJuzu4N0X2jTPPtzuzJIIHXvPuveecN/fOe4jIPanCEhEBrO6JothcwVIRAByV0nO4tu+olI32XlTaOzQEzADdE0UAnn/7OGkKiKooAU0aU84B3uv8qgKA16/mlz53PusLm6fKH2j1735aNgYAwmdXjiKu7pgM1DIVoY6DY9rU/QGkC9n8aXObZ1QIFai2BXGjQtiyvmEDObRWuBXfWD24PuUhIjlJhpKIdASNSIiSA7xtoKCpUQUxfqXHby7Et93I3ssvk3dH50YxKqhRV8jsLCyeNrd6qYPVB3F9AKDzbPvJpe3kZ1Ij5/UUwgHVQPMZtKQI9LmPRT0F3aulFMb0zMH1Ka+aEF64l2UsEckBT2t5/gcIkI1cbTeB4Ia8IX5puGuNItGkwt//48XG9P3ZR8PbtXIyOwuLKx3pga6Tze+pg1VvJjViJT6Bb7vUIwdoKf/cavt1VGz9ffz1w8OhHz2Ha3uJr2HTTZsAt6gHGuJGO/GzEEsQBxsoc/1F/2+wgS5gHihx9WqH1zhAupBdciu+KW5cbsWXdCGb/wPFAhNYzvZM1AAAAABJRU5ErkJggg==');
  background-repeat: no-repeat;
}
div.page-title-wrap>div.page-title>span{
  font-size:22px;
  color:#FFFFFF;
}
div.page-title-wrap>div.page-loc{
  margin-left:auto;
  display: flex;
  gap:5px;
  font-size:14px;
  font-weight: 400;
  border-left:1px solid #00839b66;
  padding: 5px 0 5px 20px;

}
div.page-title-wrap>div.page-loc>i{
  color:#00839b;
}
div.page-title-wrap>div.page-loc>span{
  display: flex;
  align-items: center;
  color:#00839b;
  gap:10px;
}
div.page-title-wrap>div.page-loc>span::before{
  display: inline-block;
  content:"";
  width:5px;
  height:5px;
  border-top:1px solid #00839b;
  border-right:1px solid #00839b;
  transform: rotate(45deg);
}
div.page-title-wrap>div.page-loc>span:last-child{
  font-weight: 600;
  color:#FFFFFF;
}
/* 공통 검색옵션 스타일 시트 */
div.searchOptionWrap{
  width:100%; margin-bottom: 30px;
}
div.searchOptionWrap>table{
  width:100%; border-collapse: collapse;
}
div.searchOptionWrap>table th,
div.searchOptionWrap>table td{
  border:1px solid #134d65; padding: 5px 5px;
}
div.searchOptionWrap>table th{
  background-color:rgba(49,51,64,0.5); font-size:14px; font-weight: 500; color:#00d9ff;
}
div.searchOptionWrap>table td.textcenter{
  text-align: center;
}
div.searchOptionWrap>table td.textflex{
  display: flex; align-items: center; gap:5px; justify-content: flex-start;
}

/* From To 달력 중간 라인 색상 */
div.el-picker-panel__content.el-date-range-picker__content.is-left{
  border-right: 1px solid #000000 !important;
}

div.tableListWrap{

}
div.tableListWrap>div.tableListHeader{
  width:100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom:10px;
}
div.tableListWrap>div.tableListHeader>div{
  display: flex;
  align-items: center;
  gap:5px;
}
div.tableListWrap>div.tableListHeader>div:nth-child(1){
  display: flex; justify-content: center;
}
div.tableListWrap>div.tableListHeader>div:nth-child(1)>span{
  display: inline-block;
}
div.tableListWrap>div.tableListHeader>div:nth-child(1)>span:nth-child(2){
  font-size:16px;
}
div.tableListWrap>div.tableListHeader>div:nth-child(1)>span:nth-child(3){
  font-size:13px;
  opacity: 0.75;
  font-weight: 300;
}
div.tableListWrap>table{
  width:100%;
  border-collapse: collapse;
}
div.tableListWrap>table th,
div.tableListWrap>table td{
  border:1px solid #134d65;
  padding: 5px 10px;
}
div.tableListWrap>table>tr>th{
  background-color:rgba(49,51,64,0.5);
  font-size:14px;
  color:#00839b;
  font-weight: 600;
}
div.tableListWrap>table>tr>td{
  font-size:14px;
}
div.tableListWrap>table>tr:hover>td{
  background-color:rgba(49,51,64,0.4);
}
div.tableListWrap>table td.textcenter{
  text-align: center;
}

div.tableListWrap>table td>input[type=radio]{
  width:16px;
  height:16px;
  appearance: none;
  border: max(2px, 0.1em) solid gray;
  border-radius: 50%;
  width: 1.25em;
  height: 1.25em;
  cursor:pointer;
}
div.tableListWrap>table td>input[type=radio]:checked {
  border: 0.4em solid #0099ff;
}
div.tableListWrap>table td>input[type=radio]:hover {
  box-shadow: 0 0 0 max(4px, 0.2em) lightgray;
  cursor: pointer;
}
div.tableListWrap>table td>input[type=radio]:disabled {
  background-color: lightgray;
  box-shadow: none;
  opacity: 0.7;
  cursor: not-allowed;
}

div.tableListWrap>div.tableListPaging{
  display: flex; align-items: center;
  margin-top:10px;
  justify-content: space-between;
}
div.tableListWrap>div.tableListPaging>div.pagingNumber{
  display: flex; align-items: center;
  padding: 0 20px;
}
div.tableListWrap>div.tableListPaging>div.pagingNumber>span{
  display: inline-block;
  padding: 5px 10px;
  font-size:14px;
  opacity: 0.5;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.25s;
}
div.tableListWrap>div.tableListPaging>div.pagingNumber>span.active{
  opacity: 1;
}
div.tableListWrap>div.tableListPaging>div.pagingNumber>span:hover{
  background-color:#00839b;
  opacity: 1;
}
div.tableListWrap>div.tableListPaging>div.rightBtns{
  margin-left:auto;
}
</style>
