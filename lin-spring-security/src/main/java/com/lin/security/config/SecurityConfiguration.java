package com.lin.security.config;

import com.lin.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final static String SECRET_KEY = "12345";

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * 2、配置类方式。密码使用 BCryptPasswordEncoder 加密
     */
    /*@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("sc-admin")
                .password(bcryptPasswordEncoder().encode("8487f5cb-f55a-45de-bf8f-8a4e005bed43"))
                .roles("admin");
    }

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    /**
     * 3、数据库方式
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService)
          //      .passwordEncoder(new CustomPasswordEncoder());
        auth.parentAuthenticationManager(authenticationManagerBean());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().usernameParameter("username").passwordParameter("password")
                // 配置登录页并允许访问
                .loginPage("/login").permitAll()
                // 配置Basic登录
                //.and().httpBasic()
                // 配置登出页面
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
                // 开放接口访问权限，不需要登录授权就可以访问
                .and().authorizeRequests().antMatchers("/oauth/**", "/login/**", "/logout/**").permitAll()
                // api接口需要admin管理员才能访问
                .antMatchers("/api/**").hasRole("admin")
                // 其余所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                // 关闭跨域保护;
                .and().csrf().disable()
                // 开启记住密码功能
                .rememberMe()
                // 必须提供
                .rememberMeServices(getRememberMeServices())
                // 此SECRET需要和生成TokenBasedRememberMeServices的密钥相同
                .key(SECRET_KEY)
                // 权限不足自动跳转403
                .and().exceptionHandling().accessDeniedPage("/403");
    }

    /**
     * 如果要设置cookie过期时间或其他相关配置，请在下方自行配置
     */
    private TokenBasedRememberMeServices getRememberMeServices() {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(SECRET_KEY, customUserDetailsService);
        services.setCookieName("remember-cookie");
        // 默认14天
        services.setTokenValiditySeconds(100);
        return services;
    }

}
