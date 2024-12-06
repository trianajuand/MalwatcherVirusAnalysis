package co.edu.unbosque.malwatcher.model;

import java.util.ArrayList;

public class AtributosArchivoDTO {
    private String md5 = "";
    private String sha1 = "";
    private String ssdeep = "";
    private String tlsh = "";
    private String fileType = "";
    private String magika = "";
    private ArrayList<String> typeTags = new ArrayList<>();
    private String size = "";
    private String magic = "";
    private String firstSubmissionDate = "";
    private String meaningfulName = "";
	
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	public String getSsdeep() {
		return ssdeep;
	}
	public void setSsdeep(String ssdeep) {
		this.ssdeep = ssdeep;
	}
	public String getTlsh() {
		return tlsh;
	}
	public void setTlsh(String tlsh) {
		this.tlsh = tlsh;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getMagika() {
		return magika;
	}
	public void setMagika(String magika) {
		this.magika = magika;
	}
	public ArrayList<String> getTypeTags() {
		return typeTags;
	}
	public void setTypeTags(ArrayList<String> typeTags) {
		this.typeTags = typeTags;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getMagic() {
		return magic;
	}
	public void setMagic(String magic) {
		this.magic = magic;
	}
	public String getFirstSubmissionDate() {
		return firstSubmissionDate;
	}
	public void setFirstSubmissionDate(String firstSubmissionDate) {
		this.firstSubmissionDate = firstSubmissionDate;
	}
	public String getMeaningfulName() {
		return meaningfulName;
	}
	public void setMeaningfulName(String meaningfulName) {
		this.meaningfulName = meaningfulName;
	}

    
    
}
