package com.jzo2o.mysql.interceptor;

import com.jzo2o.common.handler.UserInfoHandler;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.common.utils.ReflectUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.SQLException;

import static com.jzo2o.mysql.constants.DbFiledConstants.CREATE_BY;
import static com.jzo2o.mysql.constants.DbFiledConstants.UPDATE_BY;

/**
 * @author itcast
 */
public class MyBatisAutoFillInterceptor implements InnerInterceptor {

    private final UserInfoHandler userInfoHandler;

    public MyBatisAutoFillInterceptor(UserInfoHandler userInfoHandler) {
        this.userInfoHandler = userInfoHandler;
    }

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        //1.更新操作
        updateExe(parameter);
        //2.插入操作
        insertExe(ms, parameter);
    }

    private void insertExe(MappedStatement ms, Object parameter){
        //1.判断当前操作是否是插入操作
        if(ms.getSqlCommandType().compareTo(SqlCommandType.INSERT) == 0) {
            //2.判断是否有updater字段，如果
            if(ObjectUtils.isNotNull(parameter) && ReflectUtils.containField(CREATE_BY, parameter.getClass())){

                //3.有userId也存在并设置updater
                Long userId = currentUserId();
                if(ObjectUtils.isNotNull(userId)){
                    //4.当前操作人设置到创建人字段
                    ReflectUtils.setFieldValue(parameter, CREATE_BY, currentUserId());
                }
            }
        }
    }

    private void updateExe(Object parameter){
        //1.判断是否有updater字段
        if(ObjectUtils.isNotNull(parameter) && ReflectUtils.containField(UPDATE_BY, parameter.getClass())){
            Long userId = currentUserId();
            //2.如果有userId也存在并设置updater
            if(ObjectUtils.isNotNull(userId)){
                //3.当前用户设置到更新人字段
                ReflectUtils.setFieldValue(parameter, UPDATE_BY, userId);
            }
        }
    }

    private Long currentUserId() {
        if(ObjectUtils.isNull(userInfoHandler)){
            return null;
        }
        CurrentUserInfo currentUserInfo = userInfoHandler.currentUserInfo();
        return currentUserInfo != null ? currentUserInfo.getId() : null;
    }
}
