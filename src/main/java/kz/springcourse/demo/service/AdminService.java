package kz.springcourse.demo.service;

import kz.springcourse.demo.model.Admin;
import kz.springcourse.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin findByUserId(Integer id){
        return adminRepository.findByUserId(id);
    }
}
