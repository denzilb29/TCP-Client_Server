import java.io.Serializable;

public class FileEvent implements Serializable {

public FileEvent() {
}

private static final long serialVersionUID = 1L;


private String sourceDirectory;
private String filename;
private long fileSize;
private byte[] fileData;
private String status;

public String getSourceDirectory() {
return sourceDirectory;
}



public String getFilename() {
return filename;
}



public String getStatus() {
return status;
}



public byte[] getFileData() {
return fileData;
}


}