package repository;

import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

import repository.File;
import repository.ApprovalHistory;


public class Repository {
    private static int repositoryIdCounter = 0;

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
            .filter(f -> f.getFileId().equals(fileId))
            .findFirst()
            .orElse(null);

        return foundFile;
    }

    public void showFileHistory() {
        int fileCounter = 0;

        // Omit logical_deleted files from the list
        List<File> existFiles = this.files
            .stream()
            .filter(f -> !f.getStatus().equals(File.LOGICAL_DELETED))
            .filter(f -> !f.getStatus().equals(File.DELETION_APPROVED))
            .collect(Collectors.toList());

        // Display the number of files at first.
        System.out.println("Number of files: " + existFiles.size());
        for (File file : this.files) {
            fileCounter++;

            System.out.println(
                "  " + fileCounter+ "(" + file.getStatus() + "): " + file.getContent()
                + " (author: " + file.getAuthorId() + ")"
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
                .filter(f -> (f.getStatus().equals(File.APPROVED)) || (f.getStatus().equals(File.DELETION_APPROVED)))
                .collect(Collectors.toList());
        }
        return cloneRepo;
    }

    public void push(File pushedFile) {
        File existingFile = this.findFile(pushedFile.getFileId());

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
        if (file.getStatus().equals(File.APPROVED)) {
            file.updateStatus(File.LOGICAL_DELETED);
        } else if (file.getStatus().equals(File.LOGICAL_DELETED)) {
            System.out.println("The File has already been logically deleted.");
            return;
        } else {
            this.files.remove(file);
        }
        System.out.println("File (" + file.getFileId() + ") is deleted.");
    }

    // Overload
    public void removeFile(File file, boolean force) {
        this.files.remove(file);
        System.out.println("File (" + file.getFileId() + ") is deleted.");
    }

    private void replaceFile(File oldFile, File newFile) {
        this.removeFile(oldFile, true);
        this.files.add(newFile);
    }

    public void merge(Repository anotherRepository) {
        for (File file : anotherRepository.files) {
            // If there is a file having the same fileId, overwrite it with the file from anotherRepository 
            if (this.files.stream().anyMatch(f -> f.getFileId().equals(file.getFileId()))) {
                this.files.removeIf(f -> f.getFileId().equals(file.getFileId()));
                this.files.add(file);
            // Otherwise, add the file to this repository.
            } else {
                this.files.add(file);
            }
        }
    }

    public int approve(String fileId, int approverId) {
        File file = this.findFile(fileId);
        if (Objects.isNull(file)) {
            System.out.println("[WARN] The File you are going to approve doesn't exist in the remote repository.");
            return -1;
        }

        // Behavior differs depending on the status of the file.
        String previousStatus = file.getStatus();
        String nextStatus = "";

        if (previousStatus.equals(File.CREATED)) {
            nextStatus = File.APPROVED;
        } else if (previousStatus.equals(File.LOGICAL_DELETED)) {
            nextStatus = File.DELETION_APPROVED;
        } else {
            System.out.println("[WARN] The File can't be approved. Current status: " + previousStatus);
            return -1;
        }
        file.updateStatus(nextStatus);

        ApprovalHistory approvalHistory = new ApprovalHistory(
            approverId, file.getAuthorId(), fileId, previousStatus
        );
        this.approvalHistories.add(approvalHistory);
        System.out.println("File (" + fileId + ") is successfully approved.");

        return approvalHistory.getHistoryId();
    }
}
