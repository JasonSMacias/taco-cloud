package tacos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder encoder() {
		return new StandardPasswordEncoder("53cr3t");
	}

	// which roles/authstatus can access which endpoint, and specify login page
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/design", "/orders").hasRole("ROLE_USER")
			.antMatchers("/", "/**").permitAll()
			// the following and() signifies bridging of completed prior auth config with further
			// http config.  In general, and is used between config sections
			.and().formLogin().loginPage("/login");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
		
		//Below code for setting up in-memory user store for two users defined directly in configuration
//		auth.inMemoryAuthentication().withUser("buzz").password("infinity").authorities("ROLE_USER")
//			.and().withUser("woody").password("bullseye").authorities("Role_User");
		
		// Below is the code for authenticating against a jdbc-based user store with custom queries
		// If I just want to use the default queries, the last two chained methods are not necessary
//		auth.jdbcAuthentication().dataSource(dataSource)
//			.usersByUsernameQuery("select username, password, enabled from Users where username=?")
//			.authoritiesByUsernameQuery("select username, authority from UserAuthorities where username=?")
//			.passwordEncoder(new StandardPasswordEncoder("53cr3t"));
		
		// Below is the code for authenication using LDAP (lightweight directory access protocol)
		// optional userSearchBase and groupSearchBase methods indicate where in the ldap hierarchy 
		// the search will begin, default is root
//		auth.ldapAuthentication()
//		.userSearchBase("ou=people").userSearchFilter("(uid={0})")
//		.groupSearchBase("ou=groups").groupSearchFilter("member={0}")
//		.passwordCompare().passwordEncoder(new BCryptPasswordEncoder())
//		.passwordAttribute("passcode")
//		.contextSource().url("ldap://tacocloud.com:389/dc=tacocloud,dc=com");
		
		
	}
}
