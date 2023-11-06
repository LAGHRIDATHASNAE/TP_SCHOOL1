package ma.projet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.projet.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

}
