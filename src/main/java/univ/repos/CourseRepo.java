package univ.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import univ.domain.Course;

@RepositoryRestResource
public interface CourseRepo extends JpaRepository<Course, Long> {
}
