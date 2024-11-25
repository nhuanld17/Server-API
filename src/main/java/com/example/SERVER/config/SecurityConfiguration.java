package com.example.SERVER.config;

import com.example.SERVER.util.SecurityUtil;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	private final UserDetailsService userDetailsService;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Value("${secret-key}")
	private String jwtKey;
	
	private String[] publicEndpoint = {
			"/",
			"/auth/login",
			"/auth/register",
			"/auth/register-candidate"
	};
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public SecurityConfiguration(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
		http
				.csrf(c->c.disable())
				.cors(Customizer.withDefaults()) // Khai báo cors mặc định
				.authorizeHttpRequests(
						authz -> authz
								.requestMatchers(publicEndpoint).permitAll()
								.anyRequest().authenticated()
				)
				// Cấu hình OAuth2 Resource Server
				.oauth2ResourceServer((oauth2) -> oauth2
						.jwt(Customizer.withDefaults())
						.authenticationEntryPoint(customAuthenticationEntryPoint)
				)
				.exceptionHandling(
						exception -> exception
								.authenticationEntryPoint(customAuthenticationEntryPoint) // 401
								.accessDeniedHandler(new BearerTokenAccessDeniedHandler()) // 403
				).formLogin(form -> form.disable())
				// Thiết lập chính sách quản lí phiên thành STATELESS, nghĩa là ứng dụng
				// sẽ không tạo hoặc duy trì bất kỳ phiên (session) HTTP nào.
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
				
				return http.build();
		
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) throws Exception {
		// Cấu hình global authentication
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	
	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
				.macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
		
		// Triển khai phương thức decode() của JWTDecoder
		return token -> {
			try{
				return jwtDecoder.decode(token);
			} catch (Exception e) {
				System.out.println("JWT error: " + e.getMessage());
				throw e;
			}
		};
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
	}
	
	private SecretKey getSecretKey(){
		byte[] keyBytes = Base64.from(jwtKey).decode();
		return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
	}
}



