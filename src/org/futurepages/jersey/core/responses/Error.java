package org.futurepages.jersey.core.responses;

public class Error {

    private boolean success;
    private Object obj;

    public Error(String... msgs){
        success = false;
        if(msgs.length==1){
            obj = msgs[0];
        }else{
            obj = msgs;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}