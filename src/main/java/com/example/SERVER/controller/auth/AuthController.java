package com.example.SERVER.controller.auth;

import com.example.SERVER.domain.dto.user.LoginDTO;
import com.example.SERVER.domain.dto.user.RegisterDTO;
import com.example.SERVER.domain.dto.user.ResLoginDTO;
import com.example.SERVER.domain.dto.user.ResRegisterDTO;
import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.candidate.CandidateDetail;
import com.example.SERVER.domain.entity.candidate.CandidateWishList;
import com.example.SERVER.domain.entity.candidate.LinkSocial;
import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.CompanyDetail;
import com.example.SERVER.domain.entity.user.Role;
import com.example.SERVER.domain.entity.user.User;
import com.example.SERVER.service.role.RoleService;
import com.example.SERVER.service.user.UserService;
import com.example.SERVER.util.SecurityUtil;
import com.example.SERVER.util.exception.custom.EmailRegisteredException;
import com.example.SERVER.util.exception.custom.IdInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class AuthController {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final UserService userService;
	private final SecurityUtil securityUtil;
	private final RoleService roleService;
	
	@Value("${jwt.refresh-token-validity-in-seconds}")
	private long refreshTokenExpiration;
	
	public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
	                      UserService userService,
	                      SecurityUtil securityUtil,
	                      RoleService roleService) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userService = userService;
		this.securityUtil = securityUtil;
		this.roleService = roleService;
	}
	
	@PostMapping("/auth/login")
	public ResponseEntity<ResLoginDTO> login(@RequestBody LoginDTO loginDTO) {
		// Tạo token chứa thông tin là username và password
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			loginDTO.getEmail(), loginDTO.getPassword()
		);

		// Xác thực người dùng dựa trên authenticationToken vừa mới được tạo
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		// Lưu thông tin xác thực vào SecurityContext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		/* Trả về token cho client */
		ResLoginDTO resLoginDTO  = new ResLoginDTO();
		
		// Lấy User từ DB để thêm thông tin vào JSON
		User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getEmail());
		
		// Check null
		if (currentUserDB != null) {
			
			
			// Thêm thông tin người đăng nhập vào phản hồi ResLoginDTO
			ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
					currentUserDB.getId(),
					currentUserDB.getEmail(),
					roleService.getRoleNameByUserName(currentUserDB.getEmail())
			);
			resLoginDTO.setUserLogin(userLogin);
		}
		
		// Tạo JWT Token
		String access_token = this.securityUtil.createAccessToken(
				authentication.getName(),
				resLoginDTO.getUserLogin()
		);
		
		// Thêm token vào đối tượng trả về JSON
		resLoginDTO.setAccessToken(access_token);
		
		// Tạo refresh_token để lưu vào cơ sở dữ liệu
		String refresh_token = this.securityUtil.createRefreshToken(
				loginDTO.getEmail(), resLoginDTO);
		
		// Update refresh_token ở database của user
		this.userService.updateUserToken(refresh_token, loginDTO.getEmail());
		
		// Lưu token vào cookies
		ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
				.httpOnly(true) // cookie này chỉ có thể được truy cập bởi máy chủ thông qua HTTP
								// và không thể bị đọc hoặc thao tác mã bởi JavaScript trên trình duyệt
				.secure(true) // cookie này chỉ được truyền qua kết nối HTTPS
				.path("/") // Quy định cookies sẽ được sử dụng trong cả dự án thay vì 1 endpoint cụ thể
				.maxAge(refreshTokenExpiration) // Set thời gian sống cho cookies
				.build();
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(resLoginDTO);
	}
	
	@PostMapping("/auth/register")
	public ResponseEntity<ResRegisterDTO> register(@RequestBody RegisterDTO registerDTO) throws EmailRegisteredException {
		User user = this.userService.handleGetUserByUsername(registerDTO.getEmail());

		if (user != null) {
			throw new EmailRegisteredException("Email đã được đăng kí");
		}

		User registerUser = new User();
		registerUser.setEmail(registerDTO.getEmail());
		registerUser.setPassword(registerDTO.getPassword());

		// Set role cho user
		Role role = this.roleService.getRoleByName(registerDTO.getRole());
		registerUser.setRoles(Set.of(role));

		if (registerDTO.getRole().equals("ROLE_CANDIDATE")) {
			//=== Tạo các thực thể con của user và liên kết lại
			
			// Tạo candidate và liên kết với user
			Candidate candidate = new Candidate();
			candidate.setFullName(registerDTO.getFullName());
			
			
			// Tạo candidateDetail và liên kết với candidate
			CandidateDetail candidateDetail = new CandidateDetail();
			candidateDetail.setCandidate(candidate);
			candidate.setCandidateDetail(candidateDetail);
			
			// Tạo linkSocial và liên kết với candidate
			LinkSocial linkSocial = new LinkSocial();
			linkSocial.setCandidate(candidate);
			candidate.setLinkSocial(linkSocial);
			
			// Tạo candidateWishList và liên kết với candidate
			CandidateWishList candidateWishList = new CandidateWishList();
			candidateWishList.setCandidate(candidate);
			candidate.setCandidateWishList(candidateWishList);
			
			candidate.setUser(registerUser);
			registerUser.setCandidate(candidate);
			
			// Lưu user
			this.userService.handleRegisterUser(registerUser);
			
		} else if (registerDTO.getRole().equals("ROLE_COMPANY")) {
			//=== Tạo các thực thể con của user và liên kết lại
			
			// Liên kết company với user
			Company company = new Company();
			company.setCompanyName(registerDTO.getFullName());
			
			CompanyDetail companyDetail = new CompanyDetail();
			companyDetail.setCompany(company);
			company.setCompanyDetail(companyDetail);
			
			company.setUser(registerUser);
			registerUser.setCompany(company);
			
			// Lưu user
			this.userService.handleRegisterUser(registerUser);
		}

		// Tạo ResRegisterDTO trả về JSON
		ResRegisterDTO resRegisterDTO = new ResRegisterDTO();
		resRegisterDTO.setFullName(registerDTO.getFullName());
		resRegisterDTO.setEmail(registerDTO.getEmail());
		resRegisterDTO.setRolename(role.getRoleName());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(resRegisterDTO);
	}
	
	@GetMapping("/auth/account")
	public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
				SecurityUtil.getCurrentUserLogin().get(): "";
		User currentUserDB = this.userService.handleGetUserByUsername(email);
		ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
		
		// Check null
		if (currentUserDB != null) {
			// Phần thêm thông tin người dùng đã đăng nhập vào resLoginDTO
			userLogin.setId(currentUserDB.getId());
			userLogin.setEmail(currentUserDB.getEmail());
			userLogin.setRoleName(roleService.getRoleNameByUserName(currentUserDB.getEmail()));
		}
		
		return ResponseEntity.ok(userLogin);
	}
	
	//	Frontend tự động gọi API /auth/refresh trong background khi phát
	//	hiện Access Token hết hạn.
	@GetMapping("/auth/refresh")
	public ResponseEntity<ResLoginDTO> getRefreshToken(
			@CookieValue(name="refresh_token") String refresh_token // Tự động lấy từ trình duyệt
	) throws IdInvalidException {
		// Kiểm tra token có hợp lệ hay không
		Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
		String email = decodedToken.getSubject();
		
		// Check user by token & email
		User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
		if (currentUser == null) {
			throw new IdInvalidException("refresh token không hợp lệ");
		}
		
		// Trả về token cho client
		ResLoginDTO resLoginDTO = new ResLoginDTO();
		
		// Lấy user từ db để thêm thông tin vào JSON
		User currentUserDB = this.userService.handleGetUserByUsername(email);
		
		// Check null
		if (currentUserDB != null) {
			// Thêm thông tin người dùng đăng nhập vào resLoginDTO
			ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
					currentUserDB.getId(),
					currentUserDB.getEmail(),
					roleService.getRoleNameByUserName(currentUserDB.getEmail())
			);
			
			resLoginDTO.setUserLogin(userLogin);
		}
		
		// Tạo JWT Token
		String access_token = this.securityUtil.createAccessToken(email, resLoginDTO.getUserLogin());
		resLoginDTO.setAccessToken(access_token);
		
		// Tạo refreshToken
		String new_refresh_token = this.securityUtil.createRefreshToken(email, resLoginDTO);
		
		// Update user trong database với refreshtoken
		this.userService.updateUserToken(new_refresh_token, email);
		
		// Lưu refresh_token vào cookies ở trình duyệt client
		ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
				.httpOnly(true) // Cookies chỉ được sử dụng với https (thay vì http)
				.secure(true) //..
				.path("/") // Quy định cookies sẽ được sử dụng trong dự án chứ ko phải là 1 endpoint nhất định
				.maxAge(refreshTokenExpiration) // Set thời gian sống cho cookies
				.build();
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(resLoginDTO);
		
	}
	
	@PostMapping("/auth/logout")
	public ResponseEntity<Void> logout() throws IdInvalidException {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
				SecurityUtil.getCurrentUserLogin().get() : "";
		
		if(email.isEmpty()){
			throw new IdInvalidException("Access token không hợp lệ");
		}
		
		// update resfresh token = null
		this.userService.updateUserToken(null, email);
		
		// remove refresh token cookies
		ResponseCookie deleteSpringCookie = ResponseCookie
				.from("refresh_token", null)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.build();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
				.body(null);
	}
	
}
