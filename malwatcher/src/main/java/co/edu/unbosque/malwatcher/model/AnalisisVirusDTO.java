package co.edu.unbosque.malwatcher.model;

import java.util.ArrayList;
import java.util.List;

public class AnalisisVirusDTO {
    private String sha256;
	private String malicious;
	private String suspicious;
	private int undetected;
	private String harmless;
	private int timeout;
	private int confirmedTimeout;
	private int failure;
	private int typeUnsupported;
	private String status;
	private List<AnalisisCaracteristicasDTO> antivirusResults = new ArrayList<>();

	

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public String getMalicious() {
		return malicious;
	}

	public void setMalicious(String malicious) {
		this.malicious = malicious;
	}

	public String getSuspicious() {
		return suspicious;
	}

	public void setSuspicious(String suspicious) {
		this.suspicious = suspicious;
	}

	public int getUndetected() {
		return undetected;
	}

	public void setUndetected(int undetected) {
		this.undetected = undetected;
	}

	

	public String getHarmless() {
		return harmless;
	}

	public void setHarmless(String harmless) {
		this.harmless = harmless;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getConfirmedTimeout() {
		return confirmedTimeout;
	}

	public void setConfirmedTimeout(int confirmedTimeout) {
		this.confirmedTimeout = confirmedTimeout;
	}

	public int getFailure() {
		return failure;
	}

	public void setFailure(int failure) {
		this.failure = failure;
	}

	public int getTypeUnsupported() {
		return typeUnsupported;
	}

	public void setTypeUnsupported(int typeUnsupported) {
		this.typeUnsupported = typeUnsupported;
	}

	public List<AnalisisCaracteristicasDTO> getAntivirusResults() {
		return antivirusResults;
	}

	public void setAntivirusResults(List<AnalisisCaracteristicasDTO> antivirusResults) {
		this.antivirusResults = antivirusResults;
	}

	

}
