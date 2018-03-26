package es.urjc.videotranscoding.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
@EnableWebSecurity()
public class SecurityRestConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	public UserRepositoryAuthenticationProvider userRepoAuthProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/api/watcher/**").permitAll()
		.antMatchers("/api/downloader/**").permitAll()
		.antMatchers("/api/user/register").permitAll()
		.antMatchers("/api/**").hasRole("USER")//TODO CHANGE THE NEXT LINE FOR THE RELEASE 1.0
		.antMatchers("/**").hasRole("USER");

		http.csrf().disable();
		http.cors().disable();
		http.httpBasic();
		// Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> {
		});
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// AQUI HAY JALEEEOOO, DESCOMENTAR
		auth.authenticationProvider(userRepoAuthProvider);
	}
}
