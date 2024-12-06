package co.edu.unbosque.malwatcher.controller;

import co.edu.unbosque.malwatcher.model.AnalisisVirusDTO;
import co.edu.unbosque.malwatcher.model.AtributosArchivoDTO;
import co.edu.unbosque.malwatcher.model.Usuario;
import co.edu.unbosque.malwatcher.service.UsuarioService;
import co.edu.unbosque.malwatcher.service.VirusTotalService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/virustotal")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081", "*" })
public class VirusTotalController {

	@Autowired
	private final VirusTotalService virusTotalService;

	public VirusTotalController(VirusTotalService virusTotalService) {
		this.virusTotalService = virusTotalService;
	}

	@PostMapping("/scan")
	public ResponseEntity<AnalisisVirusDTO> scanFile(@RequestParam("file") MultipartFile file)
			throws IOException, InterruptedException {
		File fileToUpload = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
		file.transferTo(fileToUpload);

		AnalisisVirusDTO analysis = virusTotalService.scanFile(fileToUpload);
		return ResponseEntity.ok(analysis);
	}

	@GetMapping("/report/{scanId}")
	public ResponseEntity<AtributosArchivoDTO> getReport(@PathVariable String scanId) throws IOException {
		// Obtiene el reporte completo en formato DTO
		AtributosArchivoDTO report = virusTotalService.getReport(scanId);
		return ResponseEntity.ok(report);
	}

}
