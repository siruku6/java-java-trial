package repository;

public class ApprovalHistory {
    private static int historyIdCounter = 1;

    private int historyId;
    private Integer approverId;
    private Integer authorId;
    private String approvedFileId;
    private String approvedAction;

    public ApprovalHistory(
        Integer approverId,
        Integer authorId,
        String approvedFileId,
        String approvedAction
    ) {
        this.historyId = historyIdCounter;
        this.approverId = approverId;
        this.authorId = authorId;
        this.approvedFileId = approvedFileId;
        this.approvedAction = approvedAction;
    }

    public int getHistoryId() {
        return this.historyId;
    }

    public void showHistory() {
    }
}
