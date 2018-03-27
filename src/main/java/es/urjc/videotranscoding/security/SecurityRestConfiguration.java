package es.urjc.videotranscoding.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
@EnableWebSecurity()
public class SecurityRestConfiguration extends WebSecurityConfigurerAdapter implements Filter {

	@Autowired
	public UserRepositoryAuthenticationProvider userRepoAuthProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/api/watcher/**").permitAll()
			.antMatchers("/api/downloader/**").permitAll().antMatchers("/api/user/register").permitAll()
			.antMatchers("/api/**").hasRole("USER")
			// TODO CHANGE THE NEXT LINE FOR THE RELEASE 1.0
			.antMatchers("/**").hasRole("USER");

		http.csrf().disable();
		//.cors().
		http.httpBasic();
		http.logout().logoutSuccessHandler((rq, rs, a) -> {
		});
	}


	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(userRepoAuthProvider);
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type, authorization, strict-transport-security");
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// NOT USED
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// NOT USED
	}
}
