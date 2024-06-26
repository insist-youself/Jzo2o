package com.jzo2o.common.expcetions;


import static com.jzo2o.common.constants.ErrorInfo.Msg.PROCESS_FAILD;
import static java.net.HttpURLConnection.HTTP_SERVER_ERROR;

/**
 * @author itcast
 */
public class DBException extends CommonException {
    public DBException() {
        super(HTTP_SERVER_ERROR, PROCESS_FAILD);
    }

    public DBException( String message) {
        super(HTTP_SERVER_ERROR, message);
    }

    public DBException(Throwable throwable, String message) {
        super(throwable, HTTP_SERVER_ERROR, message);
    }

    public DBException(Throwable throwable) {
        super(throwable, HTTP_SERVER_ERROR, PROCESS_FAILD);
    }
}
