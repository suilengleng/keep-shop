package xin.keep.constants;

public interface Constants {
	// 响应code
	String HTTP_RES_CODE_NAME = "code";
	// 响应msg
	String HTTP_RES_CODE_MSG = "msg";
	// 响应data
	String HTTP_RES_CODE_DATA = "data";
	// 响应请求成功
	String HTTP_RES_CODE_200_VALUE = "success";
	// 系统错误
	String HTTP_RES_CODE_500_VALUE = "fial";
	// 响应请求成功code
	Integer HTTP_RES_CODE_200 = 200;
	// 系统错误
	Integer HTTP_RES_CODE_500 = 500;
	// 未关联qq用户
	Integer HTTP_RES_CODE_410 = 410;

	// 发送邮件
	String MSG_EMAIL = "sms_mail";
	// token
	String MEMBER_TOKEN = "MEMBER_TOKEN";
	// token有效期
	Long MEMBER_TOKEN_TIME = (long)60*60*24*90;

	//cookie名称
	String COOKIE_MEMBER_TOKEN = "COOKIE_MEMBER_TOKEN";
	int COOKIE_MEMBER_TOKEN_TIME = 60*60*24*90;
}