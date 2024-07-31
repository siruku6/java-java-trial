package repository;

import java.util.List;


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

    public Repository returnClone(boolean onlyApproved) {
        Repository clone = new Repository();
        clone.repositoryId = this.repositoryId;
        for (File file : this.files) {
            clone.addFile(file.clone());
        }

        System.out.println("The type of cloned files" + clone.getFiles().getClass().getSimpleName());


        // if (onlyApproved) {
        //     clone.files = clone
        //         .getFiles()
        //         .stream()
        //         .filter(f -> f.status.equals("approved"));
        // }
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

    public void addApprovalHistory(ApprovalHistory approvalHistory) {
        this.approvalHistories.add(approvalHistory);
    }
}
