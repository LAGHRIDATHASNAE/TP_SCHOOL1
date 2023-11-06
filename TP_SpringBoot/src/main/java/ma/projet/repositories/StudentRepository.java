package ma.projet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.projet.entities.Filiere;
import ma.projet.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	public List<Student> findByFiliere(Filiere filiere);

}
