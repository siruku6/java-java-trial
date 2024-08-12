package repository;

import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

import repository.ApprovalHistory;


public class Repository {
    private static int repositoryIdCounter = 0;
    public static List<Repository> createdRepositories;

    private int repositoryId;
    private List<File> files;
    private List<ApprovalHistory> approvalHistories;
    
    public Repository() {
        this.repositoryId = this.repositoryIdCounter;
        this.repositoryIdCounter += 1;
        this.files = new java.util.ArrayList<File>();
        this.approvalHistories = new java.util.ArrayList<ApprovalHistory>();
    }

    public Integer getRepositoryId() {
        return this.repositoryId;
    }

    public List<File> getFiles() {
        return this.files;
    }

    public File findFile(String fileId) {
        File foundFile = this.files
            .stream()
            .filter(f -> f.fileId.equals(fileId))
            .findFirst()
            .orElse(null);

        return foundFile;
    }

    public void showFileHistory() {
        int fileCounter = 0;

        // Omit logical_deleted files from the list
        List<File> existFiles = this.files
            .stream()
            .filter(f -> !f.status.equals("logical_deleted"))
            .filter(f -> !f.status.equals("deletion_approved"))
            .collect(Collectors.toList());

        // Display the number of files at first.
        System.out.println("Number of files: " + existFiles.size());
        for (File file : this.files) {
            fileCounter++;

            System.out.println(
                "  " + fileCounter+ "(" + file.status + "): " + file.content
            );
        }
    }

    public Repository returnClone(boolean onlyApproved) {
        Repository cloneRepo = new Repository();
        cloneRepo.repositoryId = this.repositoryId;
        for (File file : this.files) {
            cloneRepo.addFile(file.clone());
        }

        if (onlyApproved) {
            // extract only approved files
            cloneRepo.files = cloneRepo
                .getFiles()
                .stream()
                .filter(f -> (f.status.equals("approved")) || (f.status.equals("deletion_approved")))
                .collect(Collectors.toList());
        }
        return cloneRepo;
    }

    public void pushFile(File pushedFile) {
        File existingFile = this.findFile(pushedFile.fileId);

        if (Objects.isNull(existingFile)) {
            this.addFile(pushedFile);
        } else {
            this.replaceFile(existingFile, pushedFile);
        }
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public void removeFile(File file) {
        if (file.status.equals("approved")) {
            file.updateStatus("logical_deleted");
        } else {
            this.files.remove(file);
        }
    }

    private void replaceFile(File oldFile, File newFile) {
        this.removeFile(oldFile);
        this.files.add(newFile);
    }

    public void merge(Repository anotherRepository) {
        for (File file : anotherRepository.files) {
            // If there is a file having the same fileId, overwrite it with the file from anotherRepository 
            if (this.files.stream().anyMatch(f -> f.fileId.equals(file.fileId))) {
                this.files.removeIf(f -> f.fileId.equals(file.fileId));
                this.files.add(file);
            // Otherwise, add the file to this repository.
            } else {
                this.files.add(file);
            }
        }
    }

    public int approve(String fileId, int approverId) {
        File file = this.findFile(fileId);

        // Behavior differs depending on the status of the file.
        String previousStatus = file.status;
        String nextStatus = "";

        if (previousStatus.equals("created")) {
            nextStatus = "approved";
        } else if (previousStatus.equals("logical_deleted")) {
            nextStatus = "deletion_approved";
        } else {
            throw new RuntimeException("File can't be approved.");
        }
        file.updateStatus(nextStatus);

        ApprovalHistory approvalHistory = new ApprovalHistory(
            approverId, file.authorId, fileId, previousStatus
        );
        this.approvalHistories.add(approvalHistory);

        return approvalHistory.getHistoryId();
    }
}
