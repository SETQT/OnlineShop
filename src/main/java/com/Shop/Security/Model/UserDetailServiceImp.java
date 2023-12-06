package com.Shop.Security.Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@ComponentScan("com.Shop.Security.Model.UserLoginRepository")

@Service
public class UserDetailServiceImp implements UserDetailsService {

	@Autowired(required = true)
	private UserLoginRepository userRepo;
//
//	@Autowired
//	UserDetailServiceImp(UserRepository u) {
//		this.userRepo = u;
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		List<User> result = userRepo.findByUsername(username);
		if (result.size() > 1) {
			throw (new UsernameNotFoundException("There are Many User"));
		}
		return new CustomUserDetail(result.get(0));
	}

}
