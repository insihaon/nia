<template>
  <div class="loginWrap">
    <div class="loginWrap-top">
      <div>
        <div class="loginWrap-top-left"><img src="../assets/images/login_visual.png"></div>
        <div class="loginWrap-top-right">
          <div>
            <img src="../assets/images/login_logo.png">
          </div>
          <div class="loginWrap-top-right-bottom">
            <div class="system-name-k">IP 주소 관리 시스템</div>
            <div class="system-name-e">IP ADDRESS MANAGEMENT SYSTEM</div>
            <input type="text" placeholder="아이디를 입력하세요">
            <input type="password" placeholder="패스워드를 입력하세요">
            <Button @click="goto('/Home')">로그인</Button>
            <div class="des">아이디, 비밀번호는 kate/NeOSS와 동일합니다.</div>
          </div>
        </div>
      </div>
    </div>

    <div class="loginWrap-bottom">
      <div><img src="../assets/images/kt_logo.png"></div>
      <div class="loginWrap-bottom-right">
        <ul>
          <li><a href="#">정보제공</a></li>
          <li><a href="#">이용약관 법적고지</a></li>
          <li><a href="#">개인정보취급방침</a></li>
          <li><a href="#">이메일 무단 수집거부</a></li>
          <li><a href="#">상호접속협정</a></li>
          <li><a href="#">문의/연락처</a></li>
        </ul>
        <div>
          (주)케이티 대표이사 김영섭 경기도 성남시 분당구 불정로 90 (정자동) 사업자등록번호 : 102-81-42945 통신판매업신고 : 2002-경기성남-0048  사업자정보확인
          <br>
          고객센터 : [모바일] 휴대폰+114(무료), 080-000-1618(무료), 1588-0010(유료) , [인터넷/TV/전화] 국번없이 100 (kt고객 무료), 080-000-9999(무료), [발신전용] 1514, 1524
        </div>
        <div>Copyright© KT Corp. All rights reserved.</div>
      </div>
      <div class="bottom_visual" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  methods: {
    goto(pageName) {
      console.log(pageName)
      this.$router.push({ path: pageName })
    }
  }
}
</script>
<style scoped scss>
.loginWrap{
  width:100%; height:100vh;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}
.loginWrap-top{
  background-image: url("../assets/images/login_background.png");
  width:100%;
  height:100%;
  box-sizing: border-box;
  background-position: center top;
  background-repeat: no-repeat;
  background-size: cover;
  display: flex;
  justify-content: center;
  flex-direction: row;
}
.loginWrap-top>div{
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 40px;
}
.loginWrap-bottom{
  height:150px; min-height:150px; max-height:150px;
  background-color:#252531;
  display: flex;
  align-items: center;
  gap:50px;
}
.loginWrap-bottom div.loginWrap-bottom-right{
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
}
.loginWrap-bottom div.loginWrap-bottom-right ul{
  list-style: none;
  display: flex;
  gap:30px;
  padding: 0;
  margin:0;
  margin-bottom:20px;
}
.loginWrap-bottom div.loginWrap-bottom-right ul>li{
  float: left;
  font-size:14px;
}
.loginWrap-bottom div.loginWrap-bottom-right ul>li>a{
  color:#FFFFFF;
  opacity: 0.75;
  text-decoration: none;
  cursor:pointer;
  transition: all 0.25s;
}
.loginWrap-bottom div.loginWrap-bottom-right ul>li>a:hover{
  opacity: 1;
  text-decoration: underline;
}
.loginWrap-bottom div.loginWrap-bottom-right ul>li>a:active{
  opacity: 0.75;
}
.loginWrap-bottom div.loginWrap-bottom-right>div:nth-child(2){
  font-weight: 200;
}
.loginWrap-bottom div.loginWrap-bottom-right>div{
  font-size:13px;
  margin-bottom:10px;
}
.loginWrap-top-right div:nth-child(1){
  font-size:54px;
}
.loginWrap-top-right div.loginWrap-top-right-bottom{
  display: flex;
  flex-direction: column;
}
.loginWrap-top-right div.loginWrap-top-right-bottom div.system-name-k{
  font-size:40px;
  font-weight: 200;
  letter-spacing: -2px;
}
.loginWrap-top-right div.loginWrap-top-right-bottom div.system-name-e{
  font-size:17px;
  font-weight: 100;
  letter-spacing: 0.5px;
}
.loginWrap-top-right div.loginWrap-top-right-bottom input{
  width: 300px;
  height:40px;
  line-height:40px;
  outline: none;
  border:none;
  border-radius: 20px;
  margin-top:10px;
  box-sizing: border-box;
  text-align:center;
  font-size:18px;
}
.loginWrap-top-right div.loginWrap-top-right-bottom Button{
  width:300px;
  height:50px;
  border-radius: 25px;
  outline: none;
  border:none;
  margin:0;
  box-sizing: border-box;
  margin-top:10px;
  opacity: 0.5;
  background-color:#ed1b23;
  color:#FFFFFF;
  font-size:18px;
  cursor:pointer;
  transition: all 0.25s;
}
.loginWrap-top-right div.loginWrap-top-right-bottom Button:hover,
.loginWrap-top-right div.loginWrap-top-right-bottom Button:focus{
  opacity: 1;
}
.loginWrap-top-right div.loginWrap-top-right-bottom Button:active{
  opacity: 0.75;
}
.loginWrap-top-right div.loginWrap-top-right-bottom div.des{
  font-size:14px; text-align: center;
  margin-top:10px;
}

.bottom_visual{
  position:absolute;
  left:0;
  bottom:0;
  width:337px;
  height:375px;
  background-image: url("../assets/images/bottom_visual.png");
}
</style>
