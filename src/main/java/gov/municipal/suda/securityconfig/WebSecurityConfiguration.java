package gov.municipal.suda.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration	
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	public static final String[] PUBLIC_URLS={
		"/v3/api-docs",
			"/v2/api-docs",
		"/swagger-resources/**",
		"/swagger-ui/**",
		"/webjars/**"
	};

	private static final String HASANYROLE_ANONYMOUS = "hasAnyRole('ANONYMOUS', 'ADMIN', 'SUPERADMIN')";
	private static final String HASROLE_USER = "hasAnyRole('TL', 'TC', 'BOTL', 'BO', 'CO', 'JSK','ACC', 'ADMIN', 'SUPERADMIN')";
	private static final String HASROLE_ADMIN = "hasAnyRole('ADMIN', 'SUPERADMIN')";
	private static final String HASROLE_SUPER_ADMIN ="hasRole('SUPERADMIN')";
	private static final String HASROLE_PM = "hasAnyRole('PM', 'ADMIN', 'SUPERADMIN')";
	private static final String HASROLE_APM = "hasAnyRole('APM','PM', 'ADMIN', 'SUPERADMIN')";
	
	
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private UserDetailsService jwtService;

//	@Bean
//   public UserDetailsService jwtService(){
//       return new JdbcUserDetailsManager(dataSource());
//   }
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(final HttpSecurity httpSecurity) throws Exception{
		httpSecurity.cors();
		httpSecurity.csrf().disable();
		httpSecurity.authorizeRequests(authorizeRequests -> authorizeRequests
		.antMatchers(HttpHeaders.ALLOW).permitAll()
						.antMatchers(PUBLIC_URLS).permitAll()
		.antMatchers("/").access(HASANYROLE_ANONYMOUS)
		.antMatchers("/login", "/user/create", "/getUserOTP/", "/tokenValidation/**").access(HASANYROLE_ANONYMOUS)
	    .antMatchers("/admin/**").access(HASROLE_ADMIN)
	    .antMatchers("/super-admin/**").access(HASROLE_SUPER_ADMIN)
	    .antMatchers("/user/**").access(HASROLE_USER)
	    .antMatchers("/user/**").access(HASROLE_PM)
	    .antMatchers("/user/**").access(HASROLE_APM)
	    
		
		
		);
		// The default AccessDeniedException
		httpSecurity.exceptionHandling(handler -> handler
				.accessDeniedPage("/errors/403")
		);

		// Login Configuration
		httpSecurity
				.formLogin(form -> form
				//.loginPage("/login/form")
				.loginProcessingUrl("/login")
				//.loginProcessingUrl("/user/login")
				//.failureUrl("/login/form?error")
				.usernameParameter("username") // redundant
				.passwordParameter("password") // redundant
				//.defaultSuccessUrl("/default", true)
				.permitAll()
		);

		// Logout Configuration
		httpSecurity.logout(form -> form
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login/form?logout")
				.permitAll()
		);

		httpSecurity
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

		authenticationManagerBuilder.userDetailsService(jwtService).passwordEncoder(passwordEncoder());
	}

	@Description("Configure Web Security")
	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/resources/**")
				.antMatchers("/css/**")
				.antMatchers("/favicon.ico")
				.antMatchers("/img/**")
				.antMatchers("/webjars/**");



	}

//	@Bean
//	public DataSource dataSource(){
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setUrl("jdbc:postgresql://localhost:5432/suda");
//		dataSource.setUsername("postgres");
//		dataSource.setPassword("root");
//		return dataSource;
//	}

//	@Bean
//	public JdbcUserDetailsManager jdbcUserDetailsManager()
//	{
//		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//		jdbcUserDetailsManager.setDataSource(dataSource());
//
//		return jdbcUserDetailsManager;
//	}

}
