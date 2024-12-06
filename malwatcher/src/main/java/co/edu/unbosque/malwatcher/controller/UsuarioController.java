package co.edu.unbosque.malwatcher.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.malwatcher.model.Usuario;
import co.edu.unbosque.malwatcher.service.UsuarioService;
import co.edu.unbosque.malwatcher.util.MailSender;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081", "*" })
@Transactional
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioServ;

	public UsuarioController() {
	}

	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createNewWithJSON(@RequestBody Usuario newUsuario) {
		usuarioServ.create(newUsuario);
		return new ResponseEntity<String>("{persona creada con exito}", HttpStatus.CREATED);
	}

	private String generarCodigoVerificacion() {
		return UUID.randomUUID().toString().substring(0, 6);
	}

	@PostMapping(path = "/create")
	public ResponseEntity<String> createNew(@RequestParam String nombreUsuario, @RequestParam String correo,
			@RequestParam String contrasenia) {

		String generatedCodigoVerificacion = generarCodigoVerificacion();

		boolean emailSent = MailSender.sendEmail(correo, nombreUsuario, contrasenia, generatedCodigoVerificacion);

		if (emailSent) {
			CodigoVerificacionStorage.almacenarCodigo(correo, generatedCodigoVerificacion);
			return new ResponseEntity<>("Correo de verificación enviado con éxito. Confirma tu cuenta.",
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Error al enviar correo de verificación.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(path = "/verify")
	public ResponseEntity<String> verify(@RequestParam String nombreUsuario, @RequestParam String correo,
			@RequestParam String contrasenia, @RequestParam String codigoVerificacion) {
		// Obtener el código almacenado para el correo dado
		String storedCodigo = CodigoVerificacionStorage.obtenerCodigo(correo);

		if (storedCodigo == null) {
			return new ResponseEntity<>("No se encontró un código de verificación para este usuario.",
					HttpStatus.NOT_FOUND);
		}

		if (storedCodigo.equals(codigoVerificacion)) {
			Usuario newUsuario = new Usuario(0, nombreUsuario, correo, contrasenia, codigoVerificacion,
					new ArrayList<>());
			int status = usuarioServ.create(newUsuario); // Guardar el usuario en la base de datos
			CodigoVerificacionStorage.eliminarCodigo(correo); // Eliminar el código ya usado
			if (status == 200) {
				return new ResponseEntity<>("Cuenta verificada y usuario creado con éxito.", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Nombre de usuario ya existe", HttpStatus.valueOf(status));
			}
		} else {
			return new ResponseEntity<>("Código de verificación incorrecto.", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "/checklogin")
	ResponseEntity<Usuario> checkLogIn(@RequestParam String nombreUsuario, @RequestParam String contrasenia) {

		Usuario usuario = usuarioServ.validateCredentials(nombreUsuario, contrasenia);
		if (usuario == null) {
			return new ResponseEntity<Usuario>(usuario, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		}

	}

	@GetMapping("/getall")
	ResponseEntity<List<Usuario>> getAll() {
		List<Usuario> usuarios = usuarioServ.getAll();
		if (usuarios.isEmpty()) {
			return new ResponseEntity<>(usuarios, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(usuarios, HttpStatus.ACCEPTED);
		}
	}

	@GetMapping("/count")
	ResponseEntity<Long> countAll() {
		Long count = usuarioServ.count();
		if (count == 0) {
			return new ResponseEntity<>(count, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(count, HttpStatus.ACCEPTED);
		}
	}

	@GetMapping("/exists/{id}")
	ResponseEntity<Boolean> exists(@PathVariable Integer id) {
		boolean found = usuarioServ.exists(id);
		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/getbyid/{id}")
	ResponseEntity<Usuario> getById(@PathVariable Integer id) {
		Usuario found = usuarioServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> updateNewWithJSON(@RequestParam Integer id, @RequestBody Usuario newUsuario) {

		int status = usuarioServ.updateById(id, newUsuario);

		if (status == 0) {
			return new ResponseEntity<>("Usuario updated successfully", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("New Usuarioname already taken", HttpStatus.IM_USED);
		} else if (status == 2) {
			return new ResponseEntity<>("Usuario not found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Error on update", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "/update")
	ResponseEntity<String> updateNew(@RequestParam Integer id, @RequestParam String newNombreUsuario,
			@RequestParam String newCorreo, @RequestParam String newPassword, @RequestParam String newCodigo) {
		Usuario newUsuario = new Usuario(0, newNombreUsuario, newCorreo, newPassword, newCodigo, new ArrayList<>());

		int status = usuarioServ.updateById(id, newUsuario);

		if (status == 0) {
			return new ResponseEntity<>("Usuario updated successfully", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("New Usuarioname already taken", HttpStatus.IM_USED);
		} else if (status == 2) {
			return new ResponseEntity<>("Usuario not found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Error on update", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/{userId}/historial")
	public ResponseEntity<Void> agregarArchivoAlHistorial(@PathVariable int userId, @RequestBody String sha256) {
		usuarioServ.agregarArchivoAlHistorial(userId, sha256);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userId}/historial")
	public ResponseEntity<List<String>> obtenerHistorial(@PathVariable int userId) {
		List<String> historial = usuarioServ.obtenerHistorial(userId);
		return ResponseEntity.ok(historial);
	}
	
	

}
