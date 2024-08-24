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
    public static final String NOT_EXIST = "not_exist";
    public static final String CREATED = "created";
    public static final String APPROVED = "approved";
    public static final String LOGICAL_DELETED = "logical_deleted";
    public static final String DELETION_APPROVED = "deletion_approved";

    private String fileId;
    private int authorId;
    private String content;
    private String status = NOT_EXIST;

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

    public File(int authorId, String content) {
        this.fileId = this.generateNewFileId();
        this.authorId = authorId;
        this.content = content;
        this.status = CREATED;
    }

    public File(String id, int authorId, String content, String status) {
        this.fileId = id;
        this.authorId = authorId;
        this.content = content;
        this.status = status;
    }

    public String getFileId() {
        return this.fileId;
    }

    public int getAuthorId() {
        return this.authorId;
    }

    public String getContent() {
        return this.content;
    }

    public String getStatus() {
        return this.status;
    }

    public void updateStatus(String newStatus) {
        // validate newStatus
        List<String> validStatuses = Arrays.asList(
            CREATED, APPROVED, LOGICAL_DELETED, DELETION_APPROVED
        );
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
            file = new File(this.getFileId(), this.getAuthorId(), this.getContent(), this.getStatus());
        }
        return file;
    }
}
