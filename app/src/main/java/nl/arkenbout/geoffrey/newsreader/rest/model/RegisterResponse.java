package nl.arkenbout.geoffrey.newsreader.rest.model;

public class RegisterResponse {
    private boolean Success;
    private String Message;

    public boolean getSuccess() {
        return this.Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
