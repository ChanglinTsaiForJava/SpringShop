//Spring Security 的安全配置
//下面全貼當字典不然會很煩
package com.shop.project.security;
import com.shop.project.model.AppRole;
import com.shop.project.model.Role;
import com.shop.project.model.User;
import com.shop.project.repository.RoleRepository;
import com.shop.project.repository.UserRepository;
import com.shop.project.security.jwt.AuthEntryPointJwt;
import com.shop.project.security.jwt.AuthTokenFilter;
import com.shop.project.security.services.UserDetailsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    //註冊自訂的 JWT 過濾器（AuthTokenFilter）給 Spring Security 用。
    //這個 Filter 通常會：
    //從每個 HTTP 請求的 Header 中抽出 Authorization: Bearer <token>
    //驗證這個 Token 是否正確（用 JWT Secret）
    //如果正確，就從 token 取得 username 並自動登入（設定 SecurityContext）
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    //✅ 目的：
    //這個是 Spring Security 內建的一種 認證提供者（AuthenticationProvider）。
    //用來做 帳號密碼登入時的邏輯：
    //調用 userDetailsService（ UserDetailsServiceImpl）
    //去資料庫查找帳號（比對 username）
    //使用 PasswordEncoder 來比對密碼是否正確（例如 BCrypt 加密比對）
    //設定：
    //setUserDetailsService(...)：你自訂的從資料庫查使用者資料的邏輯
    //setPasswordEncoder(...)：你設定的密碼加密方式（BCrypt）
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    //登入用的主核心
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




    // Spring Security 啟動時就會用這條 SecurityFilterChain 來攔截每一個進來的 HTTP 請求，照你設定的規則進行驗證與授權。
    // 定義了接收 HTTP 請求時，要怎麼處理安全性相關的行為>>目的是傳回一個經過驗證的http
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //關閉 CSRF 防護（因為是 REST API）
        //因為 REST API不用表單，所以 CSRF(Cross-Site Request Forgery 保護沒有意義，反而會影響測試與使用。
        //在前後端分離的情況下，CSRF 通常會被關閉。
        http.csrf(csrf -> csrf.disable())
                //設定未授權處理方式（透過 AuthEntryPointJwt）
                //如果使用者嘗試存取受保護資源，卻沒帶 token 或 token 錯誤，就會被導向這個 unauthorizedHandler。
                //這個 handler 會回傳類似 HTTP 401 Unauthorized 的錯誤給前端>>看到401就來這裡開始找問題
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                //JWT 是無狀態的，伺服器不需要紀錄誰已登入。
                //所以這裡設定為 STATELESS，避免 Spring Security 建立或使用 HTTP session。
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //開放匿名訪問(不需要帶 JWT Token 就可以訪問）測試用>>不然每次都要輸入token很煩）
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
//                                .requestMatchers("/api/admin/**").permitAll()
//                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                //除了上述這些，其他請求都需要驗證（需要 JWT token）
                                .anyRequest().authenticated()
                );
        //透過 DaoAuthenticationProvider 自訂的登入邏輯（從資料庫查帳號 + 密碼加密比對）
        http.authenticationProvider(authenticationProvider());


        //加入自訂的 JWT 驗證過濾器
        //在進入 Spring Security 驗證流程前，先讓這個 filter 去處理：
        //從請求 Header 抽出 JWT token
        //驗證 token 是否有效
        //如果有效就自動登入
        //加在 UsernamePasswordAuthenticationFilter 之前，讓它搶先處理驗證
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //下面是給h2測試用正常不用這段
//        http.headers(headers -> headers.frameOptions(
//                frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }


    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUserName("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }
}

