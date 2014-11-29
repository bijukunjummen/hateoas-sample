package univ.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import univ.domain.Teacher;

@RepositoryRestResource
public interface TeacherRepo extends JpaRepository<Teacher, Long> {
}
