package co.edu.unbosque.malwatcher.configuration;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.unbosque.malwatcher.model.Usuario;
import co.edu.unbosque.malwatcher.repository.UsuarioRepository;
import co.edu.unbosque.malwatcher.util.AESUtil;



@Configuration
public class LoadDatabase {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(UsuarioRepository userRepo) {

		return args -> {
			Optional<Usuario> found = userRepo.findByNombreUsuario(AESUtil.encrypt("admin"));
			if (found.isPresent()) {
				log.info("Admin already exists,  skipping admin creating  ...");
			} else {
				userRepo.save(new Usuario(0, AESUtil.encrypt("admin"),"", AESUtil.encrypt("1234567890"),"",new ArrayList<>()));
				log.info("Preloading admin user");
			}
		};
	}

}
