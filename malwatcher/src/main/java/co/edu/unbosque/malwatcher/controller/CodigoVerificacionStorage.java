package co.edu.unbosque.malwatcher.controller;


import java.util.concurrent.ConcurrentHashMap;

public class CodigoVerificacionStorage {
    private static final ConcurrentHashMap<String, String> storage = new ConcurrentHashMap<>();

    public static void almacenarCodigo(String correo, String codigo) {
        storage.put(correo, codigo);
    }

    public static String obtenerCodigo(String correo) {
        return storage.get(correo);
    }

    public static void eliminarCodigo(String correo) {
        storage.remove(correo);
    }
}
