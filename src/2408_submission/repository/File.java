package repository;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
// import java.security.DigestException;
import java.nio.charset.StandardCharsets;


public class File {
    public String fileId;
    public String content;
    public String status = "not_exist";

    private String generateNewFileId() {

        String num = UUID.randomUUID().toString();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] digest = md.digest(num.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getEncoder().encodeToString(digest);
            // System.out.println(encoded);

            return num;
            // TODO: If UUID doesn't work production server, then use the following line.
            // return encoded;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Something is wrong");
            // throw new DigestException("couldn't make digest of partial content");
            // TODO: Actually, this program should stop here.
            return "error!";
        }
    }

    public File(String content) {
        this.fileId = this.generateNewFileId();
        this.content = content;
        this.status = "created";
    }

    public File(String id, String content, String status) {
        this.fileId = id;
        this.content = content;
        this.status = status;
    }

    public void updateStatus(String newStatus) {
        // validate newStatus
        List<String> validStatuses = Arrays.asList("created", "approved", "logical_deleted");
        if (!validStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }
        this.status = newStatus;
    }

    @Override
    public File clone() {
        File file = null;
        try {
            file = (File) super.clone();
        } catch (CloneNotSupportedException e) {
            file = new File(
            this.fileId, this.content, this.status);
        }
        return file;
    }
}
