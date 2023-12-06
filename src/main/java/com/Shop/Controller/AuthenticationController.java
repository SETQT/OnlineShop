package com.Shop.Controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Shop.DTO.AuthenRequest;
import com.Shop.DTO.ResponseObject;
import com.Shop.Model.Admin;
import com.Shop.Security.JwtTokenHelper;
import com.Shop.Security.Model.Role;
import com.Shop.Security.Model.RoleRepository;
import com.Shop.Security.Model.User;
import com.Shop.Service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = { "Content-Type", "Authorization" })
public class AuthenticationController {

	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private UserService uService;

	@Autowired
	private PasswordEncoder passEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenHelper jWTTokenHelper;

	@PostMapping("/shopLogin")
	public ResponseEntity<?> loginClient(@RequestBody AuthenRequest authentRequest)
			throws InvalidKeySpecException, NoSuchAlgorithmException {

		try {
			List<User> userExist = uService.findByUsername(authentRequest.getUsername());
			if (userExist.size() == 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Tài khoản không tồn tại ", ""));
			}

			final Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(authentRequest.getUsername(),
							authentRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			Set<Role> roles = new HashSet<>();
			roles = (Set<Role>) userExist.get(0).getRoles();
			boolean check = false;
			Iterator<Role> iterator = roles.iterator();
			while (iterator.hasNext()) {
				Role role = iterator.next();
				if (Objects.equals(role.getNameRole(), "SHOP")) {
					check = true;
					break;
				}
			}
			String jwt = jWTTokenHelper.generateToken(authentRequest.getUsername());

			if (!check) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Mật khẩu chưa chính xác !", ""));
			}

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Đăng nhập thành công", jwt));

		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Đăng nhập không thành công", ""));
		}

	}

	@PostMapping(value = "/signup", consumes = { "application/json" })
	public ResponseEntity<ResponseObject> createAccountCDD(@RequestBody AuthenRequest user) {

		List<User> userExist = uService.findByUsername(user.getUsername());
		if (userExist.size() != 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "Tài khoản đã tồn tại ", ""));
		}
//		System.out.println("ok" + user.getEmail());
		if (user.getUsername().equals("") || user.getPassword().equals(""))
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Đăng kí không thành công,thiếu thông tin !", ""));

		Role initRole = roleRepo.findRoleByName("SHOP");
		User newUser = new User(user.getUsername(), user.getPassword(), true, initRole);
		Admin ad = new Admin();
		uService.create(newUser);
		try {
			uService.save(newUser);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ok", "Đăng kí thành công,bạn có 30 ngày dùng thử!", ""));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "Lỗi server !....", ""));

		}
	}
//
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Đăng kí thành công", ""));
//
//	}

//	@PostMapping("/forgotPassword")
//	public ResponseEntity<ResponseObject> forgotPassword(@RequestParam("email") String email,
//			@RequestParam("role") String role) {
//		try {
//
//			List<User> userExist = UService.findByUsername(email);
//			User user = null;
//			if (userExist.size() == 0) {
//				return ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject("failed", "Tài khoản này không tồn tại", ""));
//			} else {
//				// kiem tra xem trong listUser và lấy ra user có role cần lấy
//				for (User u : userExist) {
//					Set<Role> roles = new HashSet<>();
//					roles = (Set<Role>) u.getRoles();
//					Iterator<Role> iterator = roles.iterator();
//					while (iterator.hasNext()) {
//						Role role1 = iterator.next();
//						if (Objects.equals(role1.getNameRole(), role)) {
//							user = u;
//							break;
//						}
//					}
//				}
//			}
//
//			if (user == null) {
//				return ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject("failed", "Tài khoản này không tồn tại", ""));
//			}
//
//			// gui mail
//			ResetPassEmailThread resetPassEmailThread = new ResetPassEmailThread(email, role, environment, passEncoder,
//					emailService);
//			Thread thread = new Thread(resetPassEmailThread);
//			thread.start();
//
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject("ok", "Đã gửi mail reset password thành công !", null));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
//					.body(new ResponseObject("failed", "Lỗi server!...", null));
//		}
//	}

//	@GetMapping("/reset_password")
//	public ResponseEntity<byte[]> formResetPassword(@RequestParam(name = "email") String email,
//			@RequestParam(name = "token") String token, @RequestParam(name = "role") String role) {
//		try {
//			boolean check = passEncoder.matches(email, token);
//
//			if (!check) {
//				return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("failed".getBytes());
//			} else {
//
//				String html = null;
//				try {
//					html = FileUtils.readFileToString(
//							new File("src/main/java/com/setqt/Hiring/Utils/formResetPassword.html"),
//							StandardCharsets.UTF_8);
//				} catch (IOException e) {
//					throw new RuntimeException(e);
//				}
//				html = html.replace(("${APP_URL}"), Objects.requireNonNull(environment.getProperty("app_url")));
//				html = html.replace("{{email}}", email);
//				html = html.replace("{{role}}", role);
//				html = html.replace("{{token}}", token);
//
//				byte[] htmlContent = html.getBytes(StandardCharsets.UTF_8);
//				return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.ok().contentType(MediaType.TEXT_HTML)
//					.body("Lỗi server !...".getBytes(StandardCharsets.UTF_8));
//		}
//	}

}
