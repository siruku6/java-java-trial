package repository;

public class ApprovalHistory {
    private static int historyIdCounter = 1;

    private int historyId;
    private Integer approverId;
    private Integer authorId;
    private String approvedFileId;

    public ApprovalHistory(Integer approverId, Integer authorId, String approvedFileId) {
        this.historyId = historyIdCounter;
        this.approverId = approverId;
        this.authorId = authorId;
        this.approvedFileId = approvedFileId;
    }

    public int getHistoryId() {
        return this.historyId;
    }

    public void showHistory() {
    }
}
