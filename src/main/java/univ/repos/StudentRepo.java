package univ.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import univ.domain.Student;

@RepositoryRestResource
public interface StudentRepo extends JpaRepository<Student, Long> {
}
