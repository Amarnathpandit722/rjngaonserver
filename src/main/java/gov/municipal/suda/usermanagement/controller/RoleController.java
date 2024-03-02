package gov.municipal.suda.usermanagement.controller;

import gov.municipal.suda.usermanagement.model.Role;
import gov.municipal.suda.usermanagement.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class RoleController {

	@Autowired
	private RoleServiceImpl roleService;
	
	@PostMapping({"/createNewRole"})
	public Role createNewRole(@RequestBody Role role) {
		return roleService.createNewRole(role);
	}
	
	
	@PutMapping({"/updateUserRole/{user_id}/{role_id}"})
		public void updateUserRole(@PathVariable("user_id") String user_id,
							   @PathVariable("role_id")String role_id) {
		roleService.updateUserRole(user_id,role_id);
	}
}
