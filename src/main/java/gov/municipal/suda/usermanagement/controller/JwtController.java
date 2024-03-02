package gov.municipal.suda.usermanagement.controller;

import gov.municipal.suda.securityconfig.JWTVerifierUsingRSA256;
import gov.municipal.suda.usermanagement.dao.NewUserDao;
import gov.municipal.suda.usermanagement.dao.UserDao;
import gov.municipal.suda.usermanagement.model.JwtRequest;
import gov.municipal.suda.usermanagement.model.JwtResponse;
import gov.municipal.suda.usermanagement.model.NewUser;
import gov.municipal.suda.usermanagement.model.User;
import gov.municipal.suda.usermanagement.service.JwtServiceImpl;
import gov.municipal.suda.util.JwtUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
public class JwtController {

	@Autowired
	private JwtServiceImpl jwtService;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserDao userDao;
	
//	@Autowired
//	private NewUserDao newUserDao;

	@Autowired
	private JWTVerifierUsingRSA256 jWTVerifierUsingRSA256;

	@PostMapping({"/login/"})
	public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {

		return jwtService.createJwtToken(jwtRequest);
	}
	
	@GetMapping({"/refreshtoken/{userName}"})
	public ResponseEntity<?> refreshtoken(@PathVariable("userName")String userName) throws Exception {
		System.out.println("userName = " + userName);
		String token = jwtUtil.generateTokenFromUsername(userName);
		String username=jwtUtil.getUserNameFromToken(token);
		User newuser=userDao.findByUserName(username).get();
		return ResponseEntity.ok(new JwtResponse(newuser,token));
	}
	

	
	
	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
	@GetMapping({"/tokenValidation/{token}"})
	public Boolean isValidToken(@PathVariable("token")String token){
		return  jWTVerifierUsingRSA256.isValidToken1(token);
	}
}
