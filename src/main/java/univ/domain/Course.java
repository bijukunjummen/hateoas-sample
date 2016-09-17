package univ.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "courses")
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "courseId")
	@JsonProperty("course_id")
	private Long courseId;

	@Size(min = 1, max = 10)
	@Column(name = "coursecode")
	@NotNull
	@JsonProperty("course_code")
	private String courseCode;

	@Size(min = 1, max = 50)
	@Column(name = "coursename")
	@JsonProperty("course_name")
	private String courseName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;


	public Teacher getTeacher() {
		return this.teacher;
	}


	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Version
	@Column(name = "version")
	private Integer version;


	public Long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseCode() {
		return this.courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Course{" +
				"courseId=" + courseId +
				", courseCode='" + courseCode + '\'' +
				", courseName='" + courseName + '\'' +
				", teacher=" + teacher +
				", version=" + version +
				'}';
	}
}
