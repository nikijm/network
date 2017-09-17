package com.jk.ndt.etl.utility;

/**
 * Created by 朱生 on 2017/5/17.
 * 请求返回格式
 */
public class Message {


    public Message() {
    }

    public Message(String message, int code, Object data) {
        this.message = message;
//        this.code = code;
        this.data = data;
    }

    public static Message ok(String msg){
        Message message=new Message();
//        message.setCode(RestfulStatus.SUCCESS);
        message.setMessage(msg);
        return  message;
    }

    public static Message failed(String msg){
        Message message=new Message();
//        message.setCode(RestfulStatus.FAILED);
        message.setMessage(msg);
        return  message;
    }

    //提示信息
    private String message;

    //返回数据
    private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static void main(String[] args) {
//        User user=new User();
//        user.setUsername("");
//        JSONObject result=new JSONObject();
//        JSONObject error=new JSONObject();
//        error.put(user.getUsername(),"用户名不能为空！");
//        result.put("error",error);
//        System.out.println(result.toJSONString());

    }


}
