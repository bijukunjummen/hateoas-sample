package univ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import univ.domain.Course;
import univ.domain.Teacher;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HALRestTemplateIntegrationTests {

	@Value("${local.server.port}")
	private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("halJacksonHttpMessageConverter")
    private TypeConstrainedMappingJackson2HttpMessageConverter halJacksonHttpMessageConverter;

	@Test
	public void testCreatingTeachersAndCourses() throws Exception{
		RestTemplate testRestTemplate = getRestTemplateWithHalMessageConverter();
		String teachersUri = String.format("http://localhost:%s/api/teachers", port);
		String coursesUri = String.format("http://localhost:%s/api/courses", port);
		/*
			Create a Teacher
		 */
		Teacher teacher1 = new Teacher();
		teacher1.setName("Teacher 1");
		teacher1.setDepartment("Department 1");
		URI teacher1Uri =
				testRestTemplate.postForLocation(teachersUri, teacher1);

		ResponseEntity<Resource<Teacher>> teacherResponseEntity
				= testRestTemplate.exchange(teacher1Uri, HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Teacher>>() {
		});

		//Retrieve the teacher.
		Resource<Teacher> teacherResource = teacherResponseEntity.getBody();

		Link teacherLink = teacherResource.getLink("self");
		String teacherUri = teacherLink.getHref();

		Teacher teacher = teacherResource.getContent();

		assertThat(teacher.getName()).isEqualTo("Teacher 1");

		/*
			Create a Course with the teacher assigned to this course
		 */
		Course course1 = new Course();
		course1.setCourseCode("Course1");
		course1.setCourseName("Course Name 1");

		ObjectNode jsonNodeCourse1 = objectMapper.valueToTree(course1);
		jsonNodeCourse1.put("teacher", teacherUri);
		URI course1Uri = testRestTemplate.postForLocation(coursesUri, jsonNodeCourse1);

		ResponseEntity<Resource<Course>> courseResponseEntity
				= testRestTemplate.exchange(course1Uri, HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Course>>() {});

		Resource<Course> courseResource = courseResponseEntity.getBody();
		Link teacherLinkThroughCourse = courseResource.getLink("teacher");

		String expectedTeacherLinkThroughCourse = String.format("http://localhost:%s/api/courses/%d/teacher", port, courseResource.getContent().getCourseId());
		assertThat(teacherLinkThroughCourse.getHref()).isEqualTo(expectedTeacherLinkThroughCourse);

	}

	public RestTemplate getRestTemplateWithHalMessageConverter() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
		List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
		newConverters.add(halJacksonHttpMessageConverter);
		newConverters.addAll(existingConverters);
		restTemplate.setMessageConverters(newConverters);
		return restTemplate;
	}

}
