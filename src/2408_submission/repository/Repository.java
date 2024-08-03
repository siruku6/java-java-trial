package repository;

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
        return this.files
            .stream()
            .filter(f -> f.fileId.equals(fileId))
            .findFirst().get();
    }

    public void showFiles() {
        int fileCounter = 0;

        // Display the number of files at first.
        System.out.println("Number of files: " + this.files.size());
        for (File file : this.files) {
            fileCounter++;

            System.out.println(
                "  " + fileCounter+ "(" + file.status + "): " + file.content
            );
        }
    }

    public Repository returnClone(boolean onlyApproved) {
        Repository clone = new Repository();
        clone.repositoryId = this.repositoryId;
        for (File file : this.files) {
            clone.addFile(file.clone());
        }

        if (onlyApproved) {
            // extract only approved files
            clone.files = clone
                .getFiles()
                .stream()
                .filter(f -> f.status.equals("approved"))
                .collect(Collectors.toList());
        }
        return clone;
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public void removeFile(File file) {
        this.files.remove(file);
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

    public int execApproval(String fileId, int approverId) {
        // System.out.println("All files in the repository:");
        // this.showFiles();

        // search file from fileId
        File file = this.files
            .stream()
            .filter(f -> f.fileId.equals(fileId))
            .findFirst().get();

        file.updateStatus("approved");

        ApprovalHistory approvalHistory = new ApprovalHistory(
            approverId, file.authorId, fileId
        );
        this.approvalHistories.add(approvalHistory);

        return approvalHistory.getHistoryId();
    }
}
