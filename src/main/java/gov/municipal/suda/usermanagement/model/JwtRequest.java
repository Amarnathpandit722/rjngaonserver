package gov.municipal.suda.usermanagement.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtRequest {


	private String userName;
	private String userPassword;



	public JwtRequest(@JsonProperty("userName") String userName, @JsonProperty("userPassword")String userPassword) {


			this.userName = userName;
			this.userPassword = userPassword;
		}

//		public String getUserName() {
//			return userName;
//		}
//		public void setUserName(String userName) {
//			this.userName = userName;
//		}
//		public String getUserPassword() {
//			return userPassword;
//		}
//		public void setUserPassword(String userPassword) {
//			this.userPassword = userPassword;
//		}


	}