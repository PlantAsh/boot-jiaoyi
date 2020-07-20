package cn.senlin.jiaoyi.entity.util;

import cn.senlin.jiaoyi.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

public class Reply<T> implements Serializable {
    private static final long serialVersionUID = -9113106580865726761L;

    private final static  Integer RES_SUCCESS = 0;
    private final static  Integer RES_ERROR = 1;
    private Integer res;
    private String msg;
    private T data;
    private byte[] bytes;
    private String seqNo;
    private String time;

    public Reply() {
        this.res = 0;
        this.time = DateUtil.getNowTimeStr();
    }

    public Reply(Integer res, String msg) {
        this.res = res;
        this.msg = msg;
        this.time = DateUtil.getNowTimeStr();
    }

    public Reply(Integer res, String msg, T data) {
        this.data = data;
        this.res = res;
        this.msg = msg;
        this.time = DateUtil.getNowTimeStr();
    }

    public static Reply<Object> ok(Object data) {
        return builder().res(RES_SUCCESS).msg("success").data(data).time(DateUtil.getNowTimeStr()).build();
    }

    public static Reply<Object> ok() {
        return builder().res(RES_SUCCESS).msg("success").time(DateUtil.getNowTimeStr()).build();
    }

    public static Reply<Object> fail(int res, String msg) {
        return builder().res(res).msg(msg).data(null).time(DateUtil.getNowTimeStr()).build();
    }

    public static Reply<Object> fail(String msg) {
        return builder().res(RES_ERROR).msg(msg).data(null).time(DateUtil.getNowTimeStr()).build();
    }

    private static <T> ReplyBuilder<Object> builder() {
        return new ReplyBuilder<>();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Reply)) {
            return false;
        } else {
            Reply<?> other = (Reply)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label75: {
                    Object this$res = this.getRes();
                    Object other$res = other.getRes();
                    if (this$res == null) {
                        if (other$res == null) {
                            break label75;
                        }
                    } else if (this$res.equals(other$res)) {
                        break label75;
                    }

                    return false;
                }

                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg != null) {
                        return false;
                    }
                } else if (!this$msg.equals(other$msg)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                if (!Arrays.equals(this.getBytes(), other.getBytes())) {
                    return false;
                } else {
                    Object this$seqNo = this.getSeqNo();
                    Object other$seqNo = other.getSeqNo();
                    if (this$seqNo == null) {
                        if (other$seqNo != null) {
                            return false;
                        }
                    } else if (!this$seqNo.equals(other$seqNo)) {
                        return false;
                    }

                    Object this$time = this.getTime();
                    Object other$time = other.getTime();
                    if (this$time == null) {
                        return other$time == null;
                    }

                    return this$time.equals(other$time);
                }
            }
        }
    }

    private boolean canEqual(final Object other) {
        return other instanceof Reply;
    }

    @Override
    public String toString() {
        return "Reply(res=" + this.getRes() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ", bytes=" + Arrays.toString(this.getBytes()) + ", seqNo=" + this.getSeqNo() + ", time=" + this.getTime() + ")";
    }

    private Reply(final Integer res, final String msg, final T data, final byte[] bytes, final String seqNo, final String time) {
        this.res = res;
        this.msg = msg;
        this.data = data;
        this.bytes = bytes;
        this.seqNo = seqNo;
        this.time = time;
    }

    public static class ReplyBuilder<T> {
        private Integer res;
        private String msg;
        private T data;
        private byte[] bytes;
        private String seqNo;
        private String time;

        ReplyBuilder() {
        }

        public ReplyBuilder<T> res(final Integer res) {
            this.res = res;
            return this;
        }

        ReplyBuilder<T> msg(final String msg) {
            this.msg = msg;
            return this;
        }

        public ReplyBuilder<T> data(final T data) {
            this.data = data;
            return this;
        }

        public ReplyBuilder<T> bytes(final byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public ReplyBuilder<T> seqNo(final String seqNo) {
            this.seqNo = seqNo;
            return this;
        }

        public ReplyBuilder<T> time(final String time) {
            this.time = time;
            return this;
        }

        public Reply<T> build() {
            return new Reply<>(this.res, this.msg, this.data, this.bytes, this.seqNo, this.time);
        }

        public String toString() {
            return "Reply.ReplyBuilder(res=" + this.res + ", msg=" + this.msg + ", data=" + this.data + ", bytes=" + Arrays.toString(this.bytes) + ", seqNo=" + this.seqNo + ", time=" + this.time + ")";
        }
    }

    public Integer getRes() {
        return res;
    }

    public void setRes(Integer res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
