package com.Shop.Security.Model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetail implements UserDetails {

	@Autowired
	private User user;

	public CustomUserDetail(User user) {
		this.user = user;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Collection<Role> userRole = user.getRoles();
		ArrayList<SimpleGrantedAuthority> listRole = new ArrayList<SimpleGrantedAuthority>();
		for (Role i : userRole) {
//			System.out.println(i.getNameRole());
			listRole.add(new SimpleGrantedAuthority("ROLE_" + i.getNameRole()));
		}

		return listRole;

	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnable();
	}

}
