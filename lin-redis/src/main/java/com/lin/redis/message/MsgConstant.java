package com.lin.redis.message;

public class MsgConstant {

    public final static String LIST_PUSH_POP_MSG = "msg:list-push-pop";
    public final static String LIST_PUSH_POP_BACKUP_MSG = "msg:list-push-pop-backup";

    public final static String PUB_SUB_MSG = "msg:pub-sub";
    // 模糊匹配
    public final static String PUB_SUB_MSG_FUZZY = "msg:*";
    public final static int PUB_SUB_TIME_OUT = 10;

    public final static String SORTED_SET_MSG = "msg:sorted-set";

    //stream名称
    public final static String STREAM_KEY = "msg:stream-name";
    //消费组，包含多个消费者
    public final static String STREAM_GROUP = "msg:stream-group-1";
    //消费者
    public final static String STREAM_CONSUMER = "msg:stream-consumer-1";

}
