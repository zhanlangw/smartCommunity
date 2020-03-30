package cn.bytecloud.smartCommunity.exception;

public class ErrorCode {
    //成功
    public static final int SUCCESS = 0;
    //请求失败
    public static final int FAILURE = 1001;
    //用户名或密码错误
    public static final int USERNAME_OR_PASSWORD = 1002;
    //验证码不正确
    public static final int VERIFICATION_CODE = 1003;
    //参数错误
    public static final int PARAMETER = 1004;
    //Token已失效
    public static final int TOKEN_REPIRED = 1005;
    //权限错误
    public static final int AUTHORIZATION = 1006;
    //上传失败
    public static final int UPLOAD = 1007;
    //下载失败
    public static final int DOWNLOAD = 1008;
    //参数为空
    public static final int NULL_PARAMETER = 1009;
    //参数已经存在
    public static final int EXISTS_PARAMETER = 1010;
    //数据已经存在
    public static final int DATA_PARAMETER = 1011;
    //数据未找到
    public static final int NOT_FOUND = 1012;
    //禁止删除
    public static final int DELETION_FORBIDDEN = 1013;
    //导入错误
    public static final int IMPORT_ERROR = 1014;
    //文件大小超过限制
    public static final int FILE_SIZE_ERROR = 1015;
    //图片尺寸不符合要求
    public static final int IMAGE_ERROR = 1016;
    //参数{key}不符合要求
    public static final int PARAMETER_ERROR = 1017;
    //未登录
    public static final int PARAMETER_NOT_LOGIN = 1018;
}
