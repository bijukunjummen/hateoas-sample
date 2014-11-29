package univ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import univ.domain.Course;
import univ.domain.Teacher;

import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class HALRestTemplateIntegrationTests {

	@Value("8080")
	private int port;

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

		Resource<Teacher> teacherResource = teacherResponseEntity.getBody();

		Link teacherLink = teacherResource.getLink("self");
		String teacherUri = teacherLink.getHref();

		Teacher teacher = teacherResource.getContent();

		/*
			Create a Course with the teacher assigned to this course
		 */
		Course course1 = new Course();
		course1.setCourseCode("Course1");
		course1.setCourseName("Course Name 1");

		ObjectMapper objectMapper = getObjectMapperWithHalModule();
		ObjectNode jsonNodeCourse1 = (ObjectNode) objectMapper.valueToTree(course1);
		jsonNodeCourse1.put("teacher", teacher1Uri.getPath());
		URI course1Uri = testRestTemplate.postForLocation(coursesUri, jsonNodeCourse1);

		ResponseEntity<Resource<Course>> courseResponseEntity
				= testRestTemplate.exchange(course1Uri, HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Course>>() {
		});

		Resource<Course> courseResource = courseResponseEntity.getBody();
		Link teacherLinkThroughCourse = courseResource.getLink("teacher");
		System.out.println("Teacher Link through Course = " + teacherLinkThroughCourse);
	}

	public RestTemplate getRestTemplateWithHalMessageConverter() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
		List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
		newConverters.add(getHalMessageConverter());
		newConverters.addAll(existingConverters);
		restTemplate.setMessageConverters(newConverters);
		return restTemplate;
	}

	private HttpMessageConverter getHalMessageConverter() {
		ObjectMapper objectMapper = getObjectMapperWithHalModule();
		MappingJackson2HttpMessageConverter halConverter = new TypeConstrainedMappingJackson2HttpMessageConverter(ResourceSupport.class);
		halConverter.setSupportedMediaTypes(Arrays.asList(HAL_JSON));
		halConverter.setObjectMapper(objectMapper);
		return halConverter;
	}

	private ObjectMapper getObjectMapperWithHalModule() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
		return objectMapper;
	}


}
