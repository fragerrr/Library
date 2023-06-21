package kz.springcourse.demo.repository;

import kz.springcourse.demo.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUserId(Integer id);
}
