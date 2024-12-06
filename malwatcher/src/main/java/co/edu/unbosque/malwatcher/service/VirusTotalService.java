package co.edu.unbosque.malwatcher.service;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.malwatcher.model.AnalisisCaracteristicasDTO;
import co.edu.unbosque.malwatcher.model.AnalisisVirusDTO;
import co.edu.unbosque.malwatcher.model.AtributosArchivoDTO;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

@Service
public class VirusTotalService {

    private static final String SEND_URL = "https://www.virustotal.com/api/v3/files";
    private static final String RETRIEVE_URL = "https://www.virustotal.com/api/v3/files/{file_id}";
    private static final String APIKEY = "ee6e174455255fdd889af227c25a29c01279da0df016b64ee9af5bf2f7f24b6a";
    private static final String UPLOAD_URL_ENDPOINT = "https://www.virustotal.com/api/v3/files/upload_url";

    private static final int BUFFER_SIZE = 4096; // Prueba con un buffer de 4 KB

    public AnalisisVirusDTO scanFile(File fileToUpload) throws IOException, InterruptedException {
        // Verificar si el archivo es mayor a 200 MB antes de intentar subirlo
        if (fileToUpload.length() > 200 * 1024 * 1024) { // 200 MB en bytes
            throw new IOException("El archivo excede el tamaño máximo permitido de 200 MB.");
        }

        // Obtener la URL de carga de VirusTotal
        String uploadUrl = obtenerUploadUrl();
        if (uploadUrl == null) {
            throw new IOException("No se pudo obtener la URL de carga.");
        }

        // Subir el archivo usando la URL de carga obtenida
        String jsonResponse = subirArchivo(uploadUrl, fileToUpload);

        // Procesar la respuesta de VirusTotal y extraer el ID del análisis
        JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonObject data = responseJson.getAsJsonObject("data");

        if (data != null) {
            String analysisId = data.get("id").getAsString();

            // Esperar hasta que el análisis esté completo y devolver los resultados de antivirus
            for (int i = 0; i < 30; i++) {
                AnalisisVirusDTO analysis = getAntivirusAnalysis(analysisId);
                if (analysis != null && "completed".equals(analysis.getStatus())) {
                    return analysis;
                }
                System.out.println("El análisis aún no está disponible, reintentando...");
                Thread.sleep(20000); // 20 segundos de espera
            }
            throw new IOException("Error: El análisis de antivirus no está disponible después de varios intentos.");
        } else {
            throw new IOException("Error: No se encontró información en la respuesta.");
        }
    }
    
    private String obtenerUploadUrl() throws IOException {
        URL url = new URL(UPLOAD_URL_ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("x-apikey", APIKEY);

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Leer la respuesta y extraer la URL dentro del campo `data`
            String jsonResponse = readResponse(conn);
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            return jsonObject.get("data").getAsString(); // Devolver solo la URL de carga
        } else {
            System.out.println("Error al obtener la URL de carga: " + responseCode);
            return null;
        }
    }
    
    private String subirArchivo(String uploadUrl, File fileToUpload) throws IOException {
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        URL url = new URL(uploadUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setRequestProperty("x-apikey", APIKEY);

        try (OutputStream out = new DataOutputStream(conn.getOutputStream());
             FileInputStream fileInputStream = new FileInputStream(fileToUpload)) {

            // Escribir el encabezado de multipart/form-data para el campo de archivo "file"
            out.write(("--" + boundary + "\r\n").getBytes());
            out.write("Content-Disposition: form-data; name=\"file\"; filename=\"".getBytes());
            out.write(fileToUpload.getName().getBytes());
            out.write("\"\r\n".getBytes());
            out.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());

            // Escribir el contenido del archivo
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.write("\r\n".getBytes());

            // Cerrar la sección multipart/form-data
            out.write(("--" + boundary + "--\r\n").getBytes());
            out.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readResponse(conn);
        } else {
            String errorResponse = readResponse(conn);
            System.out.println("Error al subir el archivo: " + errorResponse);
            throw new IOException("Error al subir el archivo: " + responseCode + " - " + errorResponse);
        }
    }




    




    private AnalisisVirusDTO getAntivirusAnalysis(String analysisId) throws IOException {
        String url = "https://www.virustotal.com/api/v3/analyses/" + analysisId;
        String jsonResponse = HttpHandler.doGet(url);

        JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonObject data = responseJson.getAsJsonObject("data");
        if (data == null) {
            System.out.println("No se encontró la sección 'data' en el JSON.");
            return new AnalisisVirusDTO();
        }

        JsonObject attributes = data.getAsJsonObject("attributes");
        if (attributes == null) {
            System.out.println("No se encontró la sección 'attributes' en 'data'.");
            return new AnalisisVirusDTO();
        }

        JsonObject results = attributes.has("results") ? attributes.getAsJsonObject("results") : null;
        JsonObject stats = attributes.has("stats") ? attributes.getAsJsonObject("stats") : null;

        AnalisisVirusDTO analysisDTO = new AnalisisVirusDTO();

        // Extraer y configurar el estado
        if (attributes.has("status") && !attributes.get("status").isJsonNull()) {
            analysisDTO.setStatus(attributes.get("status").getAsString());
        } else {
            analysisDTO.setStatus("unknown");
        }

        // Extraer y configurar las estadísticas de `stats`
        if (stats != null) {
            analysisDTO.setMalicious(stats.has("malicious") && stats.get("malicious").getAsInt() == 1 ? "Sí" : "No");
            analysisDTO.setSuspicious(stats.has("suspicious") && stats.get("suspicious").getAsInt() == 1 ? "Sí" : "No");
        }

            analysisDTO.setUndetected(stats.has("undetected") ? stats.get("undetected").getAsInt() : 0);
            analysisDTO.setHarmless(stats.has("harmless") && stats.get("harmless").getAsInt() == 1 ? "Sí" : "No");
            analysisDTO.setTimeout(stats.has("timeout") ? stats.get("timeout").getAsInt() : 0);
            analysisDTO.setConfirmedTimeout(stats.has("confirmed-timeout") ? stats.get("confirmed-timeout").getAsInt() : 0);
            analysisDTO.setFailure(stats.has("failure") ? stats.get("failure").getAsInt() : 0);
            analysisDTO.setTypeUnsupported(stats.has("type-unsupported") ? stats.get("type-unsupported").getAsInt() : 0);

        

        // Extraer información de `meta.file_info`
        JsonObject meta = responseJson.has("meta") ? responseJson.getAsJsonObject("meta") : null;
        if (meta != null && meta.has("file_info")) {
            JsonObject fileInfo = meta.getAsJsonObject("file_info");
            analysisDTO.setSha256(fileInfo.has("sha256") ? fileInfo.get("sha256").getAsString() : "");
        } else {
            System.out.println("No se encontró la sección 'file_info' en 'meta'.");
        }

        // Extraer y configurar los resultados de antivirus
        if (results != null) {
            List<AnalisisCaracteristicasDTO> antivirusResults = new ArrayList<>();

            for (String engine : results.keySet()) {
                JsonObject engineData = results.getAsJsonObject(engine);

                AnalisisCaracteristicasDTO antivirusResult = new AnalisisCaracteristicasDTO();
                antivirusResult.setEngineName(engineData.has("engine_name") && !engineData.get("engine_name").isJsonNull()
                        ? engineData.get("engine_name").getAsString() : "Unknown");
                antivirusResult.setEngineVersion(engineData.has("engine_version") && !engineData.get("engine_version").isJsonNull()
                        ? engineData.get("engine_version").getAsString() : "Unknown");
                antivirusResult.setEngineUpdate(engineData.has("engine_update") && !engineData.get("engine_update").isJsonNull()
                        ? engineData.get("engine_update").getAsString() : "Unknown");
                antivirusResult.setCategory(engineData.has("category") && !engineData.get("category").isJsonNull()
                        ? engineData.get("category").getAsString() : "undetected");
                antivirusResult.setResult(engineData.has("result") && !engineData.get("result").isJsonNull()
                        ? engineData.get("result").getAsString() : "clean");

                antivirusResults.add(antivirusResult);
            }

            analysisDTO.setAntivirusResults(antivirusResults);
        } else {
            System.out.println("No se encontró la sección 'results' en 'attributes'.");
        }

        return analysisDTO;
    }







    public AtributosArchivoDTO getReport(String fileId) throws IOException {
        // Reemplaza {file_id} en la URL con el valor de fileId
        String url = RETRIEVE_URL.replace("{file_id}", URLEncoder.encode(fileId, StandardCharsets.UTF_8));
        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("x-apikey", APIKEY);

        int responseCode = conn.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new IOException("El archivo con ID " + fileId + " no fue encontrado en VirusTotal.");
        } else if (responseCode != HttpURLConnection.HTTP_OK) {
            String errorResponse = readResponse(conn);
            throw new IOException("Error al obtener el informe: " + responseCode + " - " + errorResponse);
        }

        // Leer el JSON de respuesta una vez que se confirme el código de respuesta
        String jsonResponse = readResponse(conn);
        JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();

        JsonObject data = responseJson.getAsJsonObject("data");
        if (data == null) {
            System.out.println("No se encontró la sección 'data' en el JSON.");
            return new AtributosArchivoDTO(); // Retorna un DTO vacío si no hay datos
        }

        JsonObject attributes = data.getAsJsonObject("attributes");
        if (attributes == null) {
            System.out.println("No se encontró la sección 'attributes' en 'data'.");
            return new AtributosArchivoDTO(); // Retorna un DTO vacío si no hay atributos
        }

        // Crear y llenar AtributosArchivoDTO con los valores extraídos de 'attributes'
        AtributosArchivoDTO atributosDTO = new AtributosArchivoDTO();
        atributosDTO.setMd5(attributes.has("md5") ? attributes.get("md5").getAsString() : "");
        atributosDTO.setSha1(attributes.has("sha1") ? attributes.get("sha1").getAsString() : "");
        atributosDTO.setSsdeep(attributes.has("ssdeep") ? attributes.get("ssdeep").getAsString() : "");
        atributosDTO.setTlsh(attributes.has("tlsh") ? attributes.get("tlsh").getAsString() : "");
        atributosDTO.setFileType(attributes.has("type_description") ? attributes.get("type_description").getAsString() : "");
        atributosDTO.setMagika(attributes.has("magika") ? attributes.get("magika").getAsString() : "");
        atributosDTO.setSize(attributes.has("size") ? attributes.get("size").getAsString() : "");
        atributosDTO.setMagic(attributes.has("magic") ? attributes.get("magic").getAsString() : "");
        atributosDTO.setFirstSubmissionDate(attributes.has("first_submission_date") ? attributes.get("first_submission_date").getAsString() : "");
        atributosDTO.setMeaningfulName(attributes.has("meaningful_name") ? attributes.get("meaningful_name").getAsString() : "");
  

        if (attributes.has("type_tags") && attributes.get("type_tags").isJsonArray()) {
            ArrayList<String> typeTags = new ArrayList<>();
            attributes.getAsJsonArray("type_tags").forEach(tag -> typeTags.add(tag.getAsString()));
            atributosDTO.setTypeTags(typeTags);
        }

        return atributosDTO;
    }




    private static String readResponse(HttpURLConnection conn) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST ? conn.getErrorStream() : conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }
    
    
    
}