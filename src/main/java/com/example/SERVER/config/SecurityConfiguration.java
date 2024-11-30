package com.example.SERVER.config;


import com.cloudinary.Cloudinary;
import com.example.SERVER.util.SecurityUtil;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Bật @PreAuthorize
public class SecurityConfiguration {
	
	private final UserDetailsService userDetailsService;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${cloudinary.cloud-name}")
	private String cloud_name;
	
	@Value("${cloudinary.api-key}")
	private String api_key;
	
	@Value("${cloudinary.api-secret}")
	private String api_secret;

	@Value("${secret-key}")
	private String jwtKey;
	
	private String[] publicEndpoint = {
			"/",
			"/auth/login",
			"/auth/register",
			"/cloudinary/upload"
	};
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	// Cấu hình Cloudinary
	@Bean
	public Cloudinary getCloudinary() {
		Map config = new HashMap();
		config.put("cloud_name", cloud_name);
		config.put("api_key", api_key);
		config.put("api_secret", api_secret);
		config.put("secure", true);
		return new Cloudinary(config);
	}
	
	@Autowired
	public SecurityConfiguration(
			UserDetailsService userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
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
						.jwt(jwt -> jwt
								.jwtAuthenticationConverter(jwtAuthenticationConverter()))
						.authenticationEntryPoint(customAuthenticationEntryPoint)
				)
				.exceptionHandling(
						exception -> exception
								.authenticationEntryPoint(customAuthenticationEntryPoint) // 401
								.accessDeniedHandler(new BearerTokenAccessDeniedHandler()) // 403
				)
				.formLogin(form -> form.disable())
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
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			Map<String, Object> claims = jwt.getClaims();
			Map<String, Object> user = (Map<String, Object>) claims.get("user");
			List<GrantedAuthority> authorities = new ArrayList<>();
			if (user != null && user.get("roleName") != null) {
				String roleName = (String) user.get("roleName");
				authorities.add(new SimpleGrantedAuthority(roleName));
			}
			return authorities;
		});
		return converter;
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



