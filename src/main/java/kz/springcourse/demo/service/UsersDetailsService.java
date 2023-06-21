package kz.springcourse.demo.service;

import jakarta.transaction.Transactional;
import kz.springcourse.demo.model.Users;
import kz.springcourse.demo.repository.UsersRepository;
import kz.springcourse.demo.security.UsersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UsersDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UsersDetailsService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsersDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("User with this id not found");
        }

        return new UsersDetails(user);
    }

    public Users register(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    public Users findByEmail(String email){
        try{
            return usersRepository.findByEmail(email);
        } catch (Exception e){
            return null;
        }
    }
    public void delete(Users users){
        usersRepository.delete(users);
    }
}
