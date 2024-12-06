package co.edu.unbosque.malwatcher.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.malwatcher.model.AtributosArchivoDTO;
import co.edu.unbosque.malwatcher.model.AtributosArchivoDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class HttpHandler {

    private static final String API_KEY = "ee6e174455255fdd889af227c25a29c01279da0df016b64ee9af5bf2f7f24b6a"; // Reemplaza con tu clave de VirusTotal
    private static final String RETRIEVE_URL = "https://www.virustotal.com/api/v3/files/{file_id}";
    private static final HttpClient CLIENTE = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static AtributosArchivoDTO doGetAndConvertToDTO(String url) {
        HttpRequest solicitud = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java HttpClient Bot")
                .setHeader("x-apikey", API_KEY)
                .build();

        try {
            HttpResponse<String> respuesta = CLIENTE.send(solicitud, HttpResponse.BodyHandlers.ofString());
            if (respuesta.statusCode() == 200) {
                String jsonResponse = respuesta.body();
                System.out.println("Respuesta de VirusTotal: " + prettifyJson(jsonResponse));
                return parseFileAnalysis(jsonResponse);
            } else {
                System.out.println("Error en la respuesta: " + respuesta.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static AtributosArchivoDTO parseFileAnalysis(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonObject data = jsonObject.has("data") ? jsonObject.getAsJsonObject("data") : null;
        JsonObject attributes = (data != null && data.has("attributes")) ? data.getAsJsonObject("attributes") : null;

        AtributosArchivoDTO fileAnalysisDTO = new AtributosArchivoDTO();

        if (attributes != null) {
            fileAnalysisDTO.setMd5(attributes.has("md5") ? attributes.get("md5").getAsString() : null);
            fileAnalysisDTO.setSha1(attributes.has("sha1") ? attributes.get("sha1").getAsString() : null);
            fileAnalysisDTO.setSsdeep(attributes.has("ssdeep") ? attributes.get("ssdeep").getAsString() : null);
            fileAnalysisDTO.setTlsh(attributes.has("tlsh") ? attributes.get("tlsh").getAsString() : null);
            fileAnalysisDTO.setFileType(attributes.has("type_description") ? attributes.get("type_description").getAsString() : null);
            fileAnalysisDTO.setMagika(attributes.has("magika") ? attributes.get("magika").getAsString() : null);
            fileAnalysisDTO.setSize(attributes.has("size") ? attributes.get("size").getAsString() : null);
            fileAnalysisDTO.setMagic(attributes.has("magic") ? attributes.get("magic").getAsString() : null);
            fileAnalysisDTO.setFirstSubmissionDate(attributes.has("first_submission_date") ? attributes.get("first_submission_date").getAsString() : null);
            fileAnalysisDTO.setMeaningfulName(attributes.has("meaningful_name") ? attributes.get("meaningful_name").getAsString() : null);

            if (attributes.has("type_tags") && attributes.get("type_tags").isJsonArray()) {
                ArrayList<String> typeTags = new ArrayList<>();
                attributes.getAsJsonArray("type_tags").forEach(tag -> typeTags.add(tag.getAsString()));
                fileAnalysisDTO.setTypeTags(typeTags);
            }
        }

        return fileAnalysisDTO;
    }

    
    public static void main(String[] args) {
        // Reemplaza con un ID de archivo válido obtenido de la API de VirusTotal después de subir un archivo
        String fileId = "9e2efd40fd5bab0d273d5bc4ee8b1646820f58998864fca3221cb8e1ff19b858";
        String url = RETRIEVE_URL.replace("{file_id}", fileId);

        System.out.println("--- Get File Analysis (Solo DTO) ---");
        
        // Llamada a doGetAndConvertToDTO para obtener el análisis en formato DTO
        AtributosArchivoDTO fileAnalysis = doGetAndConvertToDTO(url);

        // Imprimir solo el contenido del DTO si la conversión fue exitosa
        if (fileAnalysis != null) {
            System.out.println("Datos del archivo en DTO:");
            System.out.println("MD5: " + fileAnalysis.getMd5());
            System.out.println("SHA-1: " + fileAnalysis.getSha1());
            System.out.println("SSDEEP: " + fileAnalysis.getSsdeep());
            System.out.println("TLSH: " + fileAnalysis.getTlsh());
            System.out.println("Tipo de archivo: " + fileAnalysis.getFileType());
            System.out.println("Magika: " + fileAnalysis.getMagika());
            System.out.println("Etiquetas de tipo: " + fileAnalysis.getTypeTags());
            System.out.println("Tamaño: " + fileAnalysis.getSize());
            System.out.println("Magic: " + fileAnalysis.getMagic());
            System.out.println("Fecha de primera subida: " + fileAnalysis.getFirstSubmissionDate());
            System.out.println("Nombre significativo: " + fileAnalysis.getMeaningfulName());
        } else {
            System.out.println("No se pudo obtener el análisis. Verifica el file_id y la conexión a la API.");
        }
    }



    static String doGet(String url) {
        HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
                .setHeader("User-Agent", "Java 22 HttpClient Bot").setHeader("x-apikey", API_KEY).build();

        HttpResponse<String> respuesta = null;

        try {
            respuesta = CLIENTE.send(solicitud, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Comentar o eliminar cualquier impresión del JSON aquí
        // System.out.println("Respuesta de VirusTotal: " + respuesta.body());

        return respuesta.body();
    }

    
    public static String prettifyJson(String jsonFeo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(jsonFeo);
        return gson.toJson(jsonElement);
    }
}
