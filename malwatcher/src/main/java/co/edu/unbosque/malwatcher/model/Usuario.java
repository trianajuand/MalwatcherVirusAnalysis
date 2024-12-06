package co.edu.unbosque.malwatcher.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nombreusuario", nullable = false, unique = true)
	private String nombreUsuario;

	@Column(name = "correo", nullable = false, unique = true)
	private String correo;

	@Column(name = "contrasenia", nullable = false, unique = false)
	private String contrasenia;

	@Column(name = "codigo")
	private String codigo;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> historialArchivos = new ArrayList<>();
	
	public List<String> getHistorialArchivos() {
		return historialArchivos;
	}

	public void setHistorialArchivos(List<String> historialArchivos) {
		this.historialArchivos = historialArchivos;
	}

	public Usuario() {
	}

	

	public Usuario(int id, String nombreUsuario, String correo, String contrasenia, String codigo,
			List<String> historialArchivos) {
		super();
		this.id = id;
		this.nombreUsuario = nombreUsuario;
		this.correo = correo;
		this.contrasenia = contrasenia;
		this.codigo = codigo;
		this.historialArchivos = historialArchivos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombreUsuario=" + nombreUsuario + ", correo=" + correo + ", contrasenia="
				+ contrasenia + ",codigo=" + codigo + " ]";
	}

}
