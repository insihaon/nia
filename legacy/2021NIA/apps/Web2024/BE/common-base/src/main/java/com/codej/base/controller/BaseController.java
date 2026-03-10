package com.codej.base.controller;

public class BaseController {

   // 사용안함
   // AuthInterceptor 에서 isAuth 를 호출하여 사용자 접근 허용을 판단하는데 사용
   // ROLE 에 따른 접근까지 제어할 수 있기 때문에 BaseSecurityConfiguration 를 사용하는게 더 좋음. 
   // 나중에 IP 등 다른 방식으로 접근 허용을 판단할 때 구현할 수 있으므로 남겨 놓음

   public Boolean canAccess() {
      return true;
   }
}
