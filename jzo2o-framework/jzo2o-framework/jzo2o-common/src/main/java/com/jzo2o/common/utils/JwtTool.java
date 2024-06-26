package com.jzo2o.common.utils;

import cn.hutool.json.JSON;
import cn.hutool.jwt.JWT;
import com.jzo2o.common.model.CurrentUserInfo;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

public class JwtTool {
    // 便于联调token有效期设置一个月 todo
    private static final Duration JWT_TOKEN_TTL = Duration.ofMinutes(24 * 60 * 30);
    private static final String PAYLOAD_USER_KEY = "user";
    private static final String UserType = "userType";

    private byte[] key;

    public JwtTool(String keyStr) {
        key = keyStr.getBytes();
    }

    /**
     * 创建 jwttoken
     *
     * @param currentUserId 用户id
     * @param name          用户姓名/昵称
     * @param avatar        用户头像
     * @return jwt token
     */
    public String createToken(Long currentUserId, String name, String avatar, int userType) {
        // 名称base64编码，防止token无法解析
        String encodeName = StringUtils.isEmpty(name) ? null : Base64Utils.encodeStr(name);
        // 1.生成jws
        return JWT.create()
                .setPayload(PAYLOAD_USER_KEY, new CurrentUserInfo(currentUserId, encodeName, avatar, userType))
                .setExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_TTL.toMillis()))
                .setCharset(Charset.forName("UTF-8"))
                .setKey(key)
                .sign();
    }

    /**
     * 从访问token中获取用户信息
     *
     * @param token 访问token
     * @return 用户信息
     */
    public CurrentUserInfo parseToken(String token) {
        try {
            JWT jwt = JWT.of(token)
                    .setKey(key);
            Object payload = jwt.getPayload(PAYLOAD_USER_KEY);
            return BeanUtils.isEmpty(payload) ? null : BeanUtils.copyBean(payload, CurrentUserInfo.class,((originObject, targetObject) -> {
                // base64解码
                if(StringUtils.isNotEmpty(targetObject.getName())) {
                    targetObject.setName(Base64Utils.decodeStr(targetObject.getName()));
                }
            }));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 从token中获取服务获取用户类型
     *
     * @param token 访问token
     * @return 用户类型
     */
    public static Integer getUserType(String token) {
        // 1.解码
        byte[] decode = Base64.getMimeDecoder().decode(token.split("\\.")[1]);
        // token明文字符串
        String tokenPlainText = new String(decode);
        // token详情字符串
        String tokenInfo = tokenPlainText.substring(0, tokenPlainText.lastIndexOf('}') + 1);
        JSON json = JsonUtils.parse(tokenInfo);
        JSON user = json.getByPath(PAYLOAD_USER_KEY, JSON.class);
        return user.getByPath(UserType, Integer.class);
    }

}
