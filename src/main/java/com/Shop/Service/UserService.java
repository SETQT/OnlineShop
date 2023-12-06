package com.Shop.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Shop.Security.Model.User;
import com.Shop.Security.Model.UserLoginRepository;

@Service
public class UserService {
	@Autowired
	private UserLoginRepository repository;

	@Autowired
	private PasswordEncoder passEncoder;

//	public UserService(UserRepository repo) {
//		this.repository = repo;
//	}

	public Optional<User> findById(Long id) {
		return Optional.ofNullable(repository.findById(id).orElse(null));
	}

//	public void updateOne(long id, Company company) {
//	        if (repository.findById(id).isEmpty()) throw new EntityNotFoundException();
//	        repository.updateById(id,company) ;
//	        }
	public List<User> findByUsername(String username) {
		List<User> result = repository.findByUsername(username);
		if (result == null) {
			result = Collections.emptyList();
			return result;
		}
		return result;
	}

	public User findOneByUsername(String username) {
		List<User> result = repository.findByUsername(username);
		if (result == null) {
			result = Collections.emptyList();
			return null;
		}
		return result.get(0);
	}

	public List<User> findAll() {
		return repository.findAll().stream().toList();
	}

	public User create(User user) {
		String pass;
		pass = passEncoder.encode(user.getPassword());
		user.setPassword(pass);
		return repository.save(user);
	}

	public User save(User user) {
		return repository.save(user);
	}

	public void deleteById(long id) {
		repository.deleteById(id);
	}

	public void deleteAll() {
		repository.deleteAll();
	}
}
