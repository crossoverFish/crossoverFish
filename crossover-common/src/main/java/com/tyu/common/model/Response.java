    /**
    * @Title: Response.java
    * @Description: 数据接口统一返回实体类
    */
    package com.tyu.common.model;

    import org.apache.commons.lang3.StringUtils;

    import java.io.Serializable;

    /**
     * @ClassName: Response
     * @Description: 数据接口统一返回实体类
     * @author cbb
     */
    public class Response<T> extends BaseEntity implements Serializable {

        /**
         * @Fields SUCCESS : 默认成功码
         */
        public static final String SUCCESS_CODE = "0000";

        /**
         * @Fields SUCCESS : 默认成功信息
         */
        public static final String SUCCESS_MSG = "Success";

        /**
         * @Fields SUCCESS : 默认失败码
         */
        public static final String FAIL_CODE = "8888";

        /**
         * @Fields SUCCESS : 默认失败信息
         */
        public static final String FAIL_MSG = "活动太火爆了，请稍后再试！";

        /**
        * @Fields serialVersionUID
        */
        private static final long serialVersionUID = 1L;

        /**
         * 创建成功返回实体
        * <p>Title: </p>
        * <p>Description: </p>
        * @param data
         */
        public Response(T data) {
            this.setResultCode(SUCCESS_CODE);
            this.setResultMsg(SUCCESS_MSG);
            if(data!=null)
                this.setData(data);
        }
        public Response(T data, String resultMsg) {
            this.data = data;
            this.resultCode = SUCCESS_CODE;
            this.resultMsg = resultMsg;
        }
        /**
         * 创建失败返回实体
        * <p>Title: </p>
        * <p>Description: </p>
        * @param errcode
        * @param errmsg
         */
        public Response(String errcode, String errmsg) {
            this.setResultCode(errcode);
            this.setResultMsg(errmsg);
        }

        public Response() {
            this.setResultCode(SUCCESS_CODE);
            this.setResultMsg(SUCCESS_MSG);
        }

        /**
         * 返回码
         */
        private String resultCode;

        /**
         * 返回描述
         */
        private String resultMsg;

        /**
         * 返回数据对象
         */
        private T data;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        /**
         *
        * @Title: setSuccessData
        * @Description: 返回成功数据
        * @param data 数据
         */
        public void setSuccessData(T data) {
            this.setResultCode(SUCCESS_CODE);
            this.setResultMsg(SUCCESS_MSG);
            this.setData(data);
        }

        /**
         *
        * @Title: setErrorData
        * @Description: 返回失败数据
        * @param errcode 返回错误码
        * @param errmsg 返回错误描述
         */
        public void setErrorData(String errcode, String errmsg) {
            this.setResultCode(errcode);
            this.setResultMsg(errmsg);
        }



        /**
         * 返回成功响应
         * @param <T>
         * @return
         */
        public static <T> Response<T> success() {
            return new Response<>();
        }

        public static <T> Response<T> success(T data) {
            return new Response<>(data);
        }

        public static <T> Response<T> success(T data,String resultMsg) {
            return new Response<>(data,resultMsg);
        }

        /**
         * 返回失败响应
         * @param errMsg
         * @param <T>
         * @return response
         */
        public static <T> Response<T> fail(String errMsg){
            return new Response<>(FAIL_CODE, StringUtils.isBlank(errMsg) ? FAIL_MSG : errMsg);
        }

        public static <T> Response<T> fail(String errCode, String errMsg) {
            return new Response<>(errCode, StringUtils.isBlank(errMsg) ? FAIL_MSG : errMsg);
        }

        @Override
        public String toString() {
            return "Response{" +
                    "resultCode='" + resultCode + '\'' +
                    ", resultMsg='" + resultMsg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
