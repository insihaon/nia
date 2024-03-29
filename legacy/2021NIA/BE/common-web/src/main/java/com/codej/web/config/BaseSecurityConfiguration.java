package com.codej.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.codej.base.dto.AppDto;
import com.codej.base.property.FileStorageProperties;
import com.codej.web.security.CustomAccessDeniedHandler;
import com.codej.web.security.CustomAuthenticationEntryPoint;
import com.codej.web.security.JwtAuthenticationFilter;
import com.codej.web.security.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Configuration
public class BaseSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final FileStorageProperties fileStorage;
    private final AppDto appDto;
    private static final String corsUrlPattern = "/**";
    private static final String dataUrlPattern = "/data/**";
    
    @Override
    protected void configure(HttpSecurity http) throws Exception { // http에 대한 보안 설정
        http
            .cors()
                .and()

            // SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정되는데 해당 내용을 해제한다.
            .headers()
                .frameOptions().sameOrigin() 
                .and()
            
            // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
            .httpBasic()
                .disable() 

            .authorizeRequests() 
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() 
                .antMatchers("/signin", "/signin/**", "/signup", "/signup/**", "/social/**", "/cache/**").permitAll() 
                .antMatchers("/getkey/**", "/currentkey/**", "/loginform/**", "/kickout/**").permitAll() 
                .antMatchers("/mock").permitAll() 
                .antMatchers(fileStorage.getAccessUrl()).permitAll()
                .antMatchers(HttpMethod.GET, "/exception/**", "/favicon.ico").permitAll() 
                .antMatchers(dataUrlPattern).authenticated()
                .antMatchers("/selectList/**","/selectOne/**", "/modify/**").authenticated()
                .anyRequest().permitAll()
                .expressionHandler(expressionHandler())
                .and()

            // rest api이므로 csrf 보안이 필요없으므로 disable처리.
            .csrf()
                .disable() 

            // jwt token으로 인증 할 것이므로 세션 필요없음. 생성안함.
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                .and()

            .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()

            .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

            // jwt token 필터를 id/password 인증 필터 전에 넣어야 한다.
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); 

    }

    // cors 처리
    // https://stackoverflow.com/questions/40286549/spring-boot-security-cors#answer-40431994
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowCredentials(true);    // false 로 설정하면 websock connection 시 cors 에러가 발생한다.
        configuration.addAllowedOriginPattern("*"); // 모든 도메인에서 요청을 허용
        // configuration.setAllowedOrigins(Arrays.asList("*"));
        // configuration.addAllowedMethod(HttpMethod.GET.name());
        // configuration.addAllowedMethod(HttpMethod.POST.name());
        // configuration.addAllowedMethod(HttpMethod.OPTIONS.name());
        configuration.addExposedHeader("Client-Addr"); 
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsUrlPattern, configuration);
        return source; 
    }

    @Override // ignore swagger security config
    public void configure(WebSecurity web) { // web에 대한 보안 설정
        if (appDto.isDevProfile()) {
            web
                .ignoring()
                .antMatchers("/**");
        } else {
            web
                .ignoring()
                .antMatchers("/preview/**", "/external/**", "/webjars/**");
                // .antMatchers("/oauth/**")
                // .antMatchers("/ws-stomp/**")
        }
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder)
    throws Exception {
        // custom user인증 서비스를 사용하기위한 설정입니다. 
        builder.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return  encoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // custom user인증 서비스를 사용하기위한 설정입니다. 
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    //RoleHierarchy : 하위 권한을 가진 사용자에게 상위 권한도 부여하는 역할
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        /* 
         * 역할 상속 구현하기 
            + ROLE_MANAGER 은 ROLE_POST, ROLE_COMMENT, ROLE_FILE 역할들이 소유한 모든 역할을 가진다.
            + ROLE_ADMIN 은 ROLE_MANAGER 이 소유한 역할을 모두 가진다.
         */
        // Map<String, List<String>> roleHierarchyMap = new HashMap<>();
        // roleHierarchyMap.put("ROLE_ADMIN", Arrays.asList("ROLE_MANAGER"));
        // roleHierarchyMap.put("ROLE_MANAGER", Arrays.asList("ROLE_POST", "ROLE_COMMENT", "ROLE_FILE"));
        // roleHierarchyMap.put("ROLE_USER", Arrays.asList("ROLE_POST"));

        // String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
        // roleHierarchy.setHierarchy(roles);
        return roleHierarchy;
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return webSecurityExpressionHandler;
    }

}
