package univ.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "teachers")
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "teacherId")
	@JsonProperty("teacher_id")
	private Long teacherId;

	@Size(min = 2, max = 50)
	@Column(name = "name")
	private String name;

	@Column(name = "department")
	@Size(min = 2, max = 50)
	private String department;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Teacher{" +
				"teacherId=" + teacherId +
				", name='" + name + '\'' +
				", department='" + department + '\'' +
				", version=" + version +
				'}';
	}
}
