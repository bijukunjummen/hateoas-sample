package univ.domain;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Size(min = 2, max = 50)
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "birthdate")
	@DateTimeFormat(iso = ISO.DATE)
	private Date birthDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "studentcourse", joinColumns = @JoinColumn(name = "Student_id"), inverseJoinColumns = @JoinColumn(name = "Course_id"))
	private List<Course> courses = new ArrayList<Course>();

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<Course> getCourses() {
		return this.courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
}