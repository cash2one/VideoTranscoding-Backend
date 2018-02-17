package urjc.proc.videotranscoding.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import urjc.videotranscoding.entities.User;
import urjc.videotranscoding.entities.UserComponent;
import urjc.videotranscoding.entities.UserRoles;
import urjc.videotranscoding.repository.UserRepository;


@Component
public class UserRepositoryAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserComponent userComponent;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {

		User user = userRepository.findByNick(auth.getName());

		if (user == null) {
			throw new BadCredentialsException("User not found");
		}

		String password = (String) auth.getCredentials();
		if (!user.isValidPassword(password)) {
			throw new BadCredentialsException("Wrong password");
		}

		userComponent.setLoggedUser(user);

		List<GrantedAuthority> roles = new ArrayList<>();
		for (UserRoles role : user.getRoles()) {
			roles.add(new SimpleGrantedAuthority(role.toString()));
		}

		return new UsernamePasswordAuthenticationToken(user.getNick(), password, roles);
	}

	@Override
	public boolean supports(Class<?> authenticationObject) {
		return true;
	}
}
