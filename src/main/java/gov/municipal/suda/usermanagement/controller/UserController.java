package gov.municipal.suda.usermanagement.controller;


import com.itextpdf.text.exceptions.BadPasswordException;
import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.OwnerDetailsDao;
import gov.municipal.suda.modules.property.dao.master.PropertyMasterDao;
import gov.municipal.suda.modules.property.dto.DueDTO;
import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import gov.municipal.suda.usermanagement.dao.DesignationDao;
import gov.municipal.suda.usermanagement.dao.LoginOtpDao;
import gov.municipal.suda.usermanagement.dto.PwdChangeDTO;
import gov.municipal.suda.usermanagement.dto.UserDetailDTO;
import gov.municipal.suda.usermanagement.model.Designation;
import gov.municipal.suda.usermanagement.model.OTPBean;
import gov.municipal.suda.usermanagement.model.User;
import gov.municipal.suda.usermanagement.model.UserRequest;
import gov.municipal.suda.usermanagement.service.LoginOtpValidatorService;
import gov.municipal.suda.usermanagement.service.UserServiceImpl;
import gov.municipal.suda.util.Generate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	PropertyMasterDao propertyMasterDao;
	@Autowired
	OwnerDetailsDao ownerDetailsDao;
	@Autowired
	LoginOtpDao loginOtpDao;

	@Autowired
	LoginOtpValidatorService loginOtpValidatorService;
	@Autowired
	private DesignationDao designationDao;

	
	
	@PostMapping({"/user/create"})
	public ResponseEntity<String> newUserRegister(@Valid @RequestBody UserRequest newUserRequest
												)  {
		userService.newUserRegister(newUserRequest);

		return ResponseEntity.ok("Registration Success");
	}
	
	
	
	
	@GetMapping({"/admin/forAdmin"})
	public String forAdmin() {
		return "This is for Admin Role";
	}
	@GetMapping({"/admin/forSuperAdmin"})
	public String forSuperAdmin() {
		return "This is for Super Admin Role";
	}

	@GetMapping({"/TL/forTL"})
	public String forTL() {
		return "This is for TL Role";
	}

	@GetMapping({"/user/forUser"})
	public String forUser() {
		return "This is for User Role";
	}
	@GetMapping({"/login_otp/{mobile_no}"})
	public String getUserTopByMobileNo(@PathVariable String mobile_no) throws IOException {
	String OTP = null;
	OTPBean results=null;
	if(mobile_no==null || !mobile_no.matches("\\d+")) {
		throw new BadRequestException("Invalid mobile no");
	}
		try {
			// below code must go into the service layer as per design perspective, but as it is less than 10 line of code and no other
			// related method as of now for develop that's why I put it here, if in future anything related to log in will come than
			// it will be taken care of as a refactored code

				OTP = Generate.createOTPByMobileNo(mobile_no);
				OTPBean otpBean = new OTPBean();
				otpBean.setMobile_no(Long.valueOf(mobile_no));
				otpBean.setOtp(Long.valueOf(OTP));
				otpBean.setDate_time(LocalDateTime.now());
				results = loginOtpDao.save(otpBean);

		} catch (Exception e) {
			throw new BadRequestException(e.getMessage());

	}
	return results.getId().toString();
}
			@GetMapping("/login_otp/validate")
			public ResponseEntity<Boolean> otpValidator(@RequestParam("messageId") Long messageId, @RequestParam("otp") Long receivedOtp) {
		if(messageId==null || receivedOtp == null) {
			throw new BadRequestException("Mandatory parameter shouldn't be blank");
		}
		return ResponseEntity.ok(loginOtpValidatorService.LoginOtpValidator(messageId,receivedOtp));
	}
			@GetMapping({"/get_user_otp_by_property_no/{property_no}"})
			public String getUserOTPByPropertyNo(@PathVariable String property_no) throws IOException {
		//System.out.println("mobile_no = " + mobile_no);
		String OTP = null;
		try {
			Long prop_id = propertyMasterDao.findIdByPropNo(property_no);
			String ownerName = null;
			String mobileNo = null;
			if (prop_id != null) {
				Optional<OwnerDetailsBean> ownerDetailsBean = ownerDetailsDao.findOwnerDetailsByPropId(prop_id);
				if (ownerDetailsBean.isPresent()) {
					ownerName = ownerDetailsBean.get().getOwner_name();
					mobileNo = ownerDetailsBean.get().getMobile_no().toString();
					if(ownerName!=null && mobileNo !=null) {
						OTP = Generate.createOTPByPropertyNo(mobileNo, ownerName);
					}
					else {
						throw new BadRequestException("mobile and owner number can't be blank at database level, please rectify your record");
					}
				}
			}
		}
		catch (Exception e) {
			throw new BadRequestException(e.getMessage());
		}
		//final String OTP= Generate.createOTP(mobile_no);
		return OTP;
	}

	@PutMapping({"/admin/changePassword"})
	public ResponseEntity<String> changePassword(@Valid @RequestBody PwdChangeDTO pwdChangeDTO)
	{
		userService.changePassword(pwdChangeDTO.getUserPassword(),pwdChangeDTO.getId());
		return ResponseEntity.ok("Password Change Success");
	}

	@GetMapping({"/admin/getAllUser"})
	public ResponseEntity<List<UserDetailDTO>> getAllUser()throws Exception {
		List<Object[]> results=userService.getAllUser();
		List<UserDetailDTO> detailsUser= new ArrayList<>();
		for (Object[] result : results) {
			UserDetailDTO user = new UserDetailDTO();
			user.setId((BigInteger) result[0]);
			user.setUser_id((BigInteger) result[1]);
			user.setEmployee_name((String) result[2]);
			user.setUser_name((String) result[3]);
			user.setDesignation((String) result[4]);
			user.setIs_active((String) result[5]);
			detailsUser.add(user);
		}
		return ResponseEntity.ok(detailsUser);
	}
	
	
	//calling super-admin controller calling the previous service
	@GetMapping({"/super-admin/getAllUser"})
	public ResponseEntity<List<UserDetailDTO>> getAllUserBySuperAdmin()throws Exception {
		List<Object[]> results=userService.getAllUser();
		List<UserDetailDTO> detailsUser= new ArrayList<>();
		for (Object[] result : results) {
			UserDetailDTO user = new UserDetailDTO();
			user.setId((BigInteger) result[0]);
			user.setUser_id((BigInteger) result[1]);
			user.setEmployee_name((String) result[2]);
			user.setUser_name((String) result[3]);
			user.setDesignation((String) result[4]);
			user.setIs_active((String) result[5]);
			detailsUser.add(user);
		}
		return ResponseEntity.ok(detailsUser);
	}

	
		
	
	
	
	
	@PutMapping({"/admin/deactivateUser"})
	public ResponseEntity<String> deactivateUser(@RequestParam("user_id") String user_id
	)  {
		String returnResponse=userService.deactivateUser(user_id);
		return ResponseEntity.ok(returnResponse);
	}
	@GetMapping({"/admin/getAllDesignation"})
	public ResponseEntity<List<Designation>> getAllDesignation()throws Exception {
		return ResponseEntity.ok(designationDao.findAll());
	}
	
	
	@GetMapping({"/super-admin/getAllDesignation"})
	public ResponseEntity<List<Designation>> getAllDesignationBySuperAdmin()throws Exception {
		return ResponseEntity.ok(designationDao.findAll());
	}
	

	
	
	
	
	
	
	
}
