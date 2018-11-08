package xin.keep.utils;

import xin.keep.constants.Constants;

import java.util.UUID;

public class TokenUtils {
    public static String getMemberToken(){
        return Constants.MEMBER_TOKEN+"-"+ UUID.randomUUID();
    }
}
