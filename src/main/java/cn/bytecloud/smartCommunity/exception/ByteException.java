package cn.bytecloud.smartCommunity.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteException extends Exception {
    private Integer status;

    public ByteException(Integer status, String... arr) {
        super(getMessage(status, arr));
        this.status = status;
    }

    public ByteException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public ByteException(Integer status) {
        super(getMessage(status));
        this.status = status;
    }

    public ByteException(String message) {
        super(message);
        this.status = ErrorCode.FAILURE;
    }

    public static String getMessage(int status, String... arr) {
        switch (status) {
            case ErrorCode.FAILURE:
                return "请求失败";
            case ErrorCode.USERNAME_OR_PASSWORD:
                return "用户名或密码错误";
            case ErrorCode.VERIFICATION_CODE:
                return "验证码不正确";
            case ErrorCode.PARAMETER:
                return "参数错误";
            case ErrorCode.TOKEN_REPIRED:
                return "Token已失效";
            case ErrorCode.AUTHORIZATION:
                return "权限错误";
            case ErrorCode.UPLOAD:
                return "上传失败";
            case ErrorCode.DOWNLOAD:
                return "下载失败";
            case ErrorCode.NULL_PARAMETER:
                return arr[0] + "为空";
            case ErrorCode.EXISTS_PARAMETER:
                return arr[0] + "已经存在";
            case ErrorCode.DATA_PARAMETER:
                return "数据已经存在";
            case ErrorCode.NOT_FOUND:
                return "数据未找到";
            case ErrorCode.DELETION_FORBIDDEN:
                return "禁止删除";
            case ErrorCode.IMPORT_ERROR:
                return "导入错误，第" + arr[0] + "行，字段：" + arr[1];
            case ErrorCode.FILE_SIZE_ERROR:
                return "文件大小超过限制";
            case ErrorCode.IMAGE_ERROR:
                return "图片尺寸不符合要求";
            case ErrorCode.PARAMETER_ERROR:
                return "参数" + arr[0] + "不符合要求";
            case ErrorCode.PARAMETER_NOT_LOGIN:
                return "未登录";
            default:
                return "请求失败";

        }
    }

    public static String getMessage(int status) {
        return getMessage(status, null);
    }

    public Integer getStatus() {
        return this.status;
    }
}
