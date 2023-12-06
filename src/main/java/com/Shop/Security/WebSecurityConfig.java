package com.Shop.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@ComponentScan("com.setqt.Hiring.Security")
public class WebSecurityConfig {

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Autowired
	JwtFilter jwtFilter;

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
//		return new LoginUrlAuthenticationEntryPoint("https://baomoi.com/");
		return new CustomEntryPoint("Lỗi đăng nhập", 401);
	}
//

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authProvider);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		try {
			return
// 				http.csrf().disable().cors().disable().and().headers().frameOptions().disable().and()
// http// Tắt CORS
			http
// .cors().disable() // Tắt CORS
					.cors().and()// Tắt CORS
					.csrf().disable() // Tắt CSRF
					.authorizeHttpRequests(auth -> {

//						auth.requestMatchers("/company/**" ).permitAll();
//						auth.requestMatchers("/getAll" ).hasRole("ADMIN");

						auth.requestMatchers("/auth/**").permitAll();
						auth.requestMatchers("/product/getAll").permitAll();
//						auth.requestMatchers("/**").authenticated();

						auth.requestMatchers("/backup/**").permitAll();
						auth.requestMatchers("/FileUpload/**").permitAll();
//						auth.requestMatchers("employer/**").hasRole("EMPLOYER");

						auth.anyRequest().authenticated();
					}).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

					.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()

					.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
