package gov.municipal.suda.usermanagement.model;

public class JwtResponse {

	private User user;

	private String jwtToken;
	
	public JwtResponse(User newuser, String jwtToken) {
		
		this.user = newuser;
		this.jwtToken = jwtToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	


	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	
}
