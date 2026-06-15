package com.example.chatly;

public class MsgModel {
    String msgModelStr, senderIdModel;
    Long timeStampModel;

    public MsgModel(){}

    public MsgModel(String msgModelStr, String senderIdModel, Long timeStampModel)
    {
        this.msgModelStr= msgModelStr;
        this.senderIdModel= senderIdModel;
        this.timeStampModel= timeStampModel;
    }

    public String getMsgModelStr(){
        return msgModelStr;
    }

    public void setMsgModelStr(String msgModelStr){
        this.msgModelStr=msgModelStr;
    }

    public String getSenderIdModel(){
        return senderIdModel;
    }

    public void setSenderIdModel(String senderIdModel) {
        this.senderIdModel = senderIdModel;
    }

    public Long getTimeStampModel() {
        return timeStampModel;
    }

    public void setTimeStampModel(Long timeStampModel) {
        this.timeStampModel = timeStampModel;
    }
}