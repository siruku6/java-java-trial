package repository;

public class ApprovalHistory {
    private Integer historyId;
    private Integer approverId;
    private Integer authorId;
    private Integer approvedFileId;

    public ApprovalHistory(Integer historyId, Integer approverId, Integer authorId, Integer approvedFileId) {
        this.historyId = historyId;
        this.approverId = approverId;
        this.authorId = authorId;
        this.approvedFileId = approvedFileId;
    }

    public void showHistory() {
    }
}
