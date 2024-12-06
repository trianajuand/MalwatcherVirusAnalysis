package co.edu.unbosque.malwatcher.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import co.edu.unbosque.malwatcher.model.Usuario;
import java.util.List;


public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	
	public Optional<Usuario> findByNombreUsuario(String userName);
	
	public Optional<Usuario> findByNombreUsuarioAndContrasenia(String nombreUsuario, String contrasenia);

}