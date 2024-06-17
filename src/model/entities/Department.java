package model.entities;

import java.io.Serializable;
import java.util.Objects;

/* Uma classe Serializable permite que seus objetos sejam transformados em
 * sequência de bytes, na linguagem java uma classe implementa a interface
 * serializable quando se deseja que os objetos instanciados a partir dela,
 * sejam gravados em arquivo, trafegado em rede e assim por diante.
 * */
public class Department implements Serializable {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	
	public Department() {
		
	}
	
	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
	
	
	
	
}
