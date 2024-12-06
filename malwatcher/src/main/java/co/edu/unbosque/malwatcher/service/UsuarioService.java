package co.edu.unbosque.malwatcher.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.malwatcher.model.Usuario;
import co.edu.unbosque.malwatcher.repository.UsuarioRepository;
import co.edu.unbosque.malwatcher.util.AESUtil;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepo;

	public UsuarioService() {
	}

	public int create(Usuario newUsuario) {
		Optional<Usuario> found = usuarioRepo.findByNombreUsuario(newUsuario.getNombreUsuario());
		if (found.isPresent()) {
			return 409;
		}
		usuarioRepo.save(newUsuario);
		return 200;
	}

	public Usuario obtenerPorNombreUsuario(String userName) {
		Optional<Usuario> usuarioOpt = usuarioRepo.findByNombreUsuario(userName);
		return usuarioOpt.orElse(null);
	}

	public ArrayList<Usuario> getAll() {
		return (ArrayList<Usuario>) usuarioRepo.findAll();
	}

	public long count() {
		return usuarioRepo.count();
	}

	public boolean exists(Integer id) {
		return usuarioRepo.existsById(id);
	}

	public int updateById(Integer id, Usuario newData) {
		Optional<Usuario> found = usuarioRepo.findById(id);
		Optional<Usuario> newFound = usuarioRepo.findById(id);
		if (found.isPresent() && !newFound.isPresent()) {
			Usuario usuarioTemp = found.get();
			usuarioTemp.setNombreUsuario(newData.getNombreUsuario());
			usuarioTemp.setCorreo(newData.getCorreo());
			usuarioTemp.setContrasenia(newData.getContrasenia());
			usuarioTemp.setCodigo(newData.getCodigo());
			usuarioRepo.save(usuarioTemp);
			return 0;
		}
		if (found.isPresent() && newFound.isPresent()) {
			return 1;
		}
		if (!found.isPresent()) {
			return 2;
		} else {
			return 3;
		}
	}

	public Usuario getById(Integer id) {
		Optional<Usuario> found = usuarioRepo.findById(id);
		if (found.isPresent()) {
			return found.get();
		} else {
			return null;
		}
	}

	public boolean findUsernameAlreadyTaken(Usuario newUser) {
		Optional<Usuario> found = usuarioRepo.findByNombreUsuario(newUser.getNombreUsuario());
		if (found.isPresent()) {
			return true;
		} else {
			return false;
		}
	}


	
	public Usuario obtenerUsuarioPorNombreDeUsuario(String username) {
		Optional<Usuario> usuarioOpt = usuarioRepo.findByNombreUsuario(username);
		return usuarioOpt.orElse(null); // Retorna null si el usuario no se encuentra
	}

	public Usuario validateCredentials(String username, String password) {
		Optional<Usuario> found = usuarioRepo.findByNombreUsuarioAndContrasenia(username, password);
		return found.get();
	}

	public void agregarArchivoAlHistorial(int userId, String sha256) {
		Usuario usuario = usuarioRepo.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// Verifica que se esté agregando correctamente al historial
		usuario.getHistorialArchivos().add(sha256);
		usuarioRepo.save(usuario); // Guarda el usuario con el historial actualizado
	}

	public List<String> obtenerHistorial(int userId) {
		Usuario usuario = usuarioRepo.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		return usuario.getHistorialArchivos();
	}
	
	
	
	

}
