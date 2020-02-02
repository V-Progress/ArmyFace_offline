package com.yunbiao.armyface_offline.model;

public class ImportResponse {
    private int code;
    private String msg;
    private Data data;

    @Override
    public String toString() {
        return "ImportResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String resousePath;

        @Override
        public String toString() {
            return "Data{" +
                    "resousePath='" + resousePath + '\'' +
                    '}';
        }

        public String getResousePath() {
            return resousePath;
        }

        public void setResousePath(String resousePath) {
            this.resousePath = resousePath;
        }
    }
}
