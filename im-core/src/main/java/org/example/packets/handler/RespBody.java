/**
 *
 */
package org.example.packets.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.example.enums.CommandEnum;

import java.io.Serializable;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年7月26日 上午11:31:48
 */
@Data
public class RespBody implements Serializable {

    protected static final long serialVersionUID = 1L;

    /**
     * 响应状态码;
     */
    protected Boolean success;
    /**
     * 响应状态信息提示;
     */
    protected String msg;
    /**
     * 响应cmd命令码;
     */
    protected int command;
    /**
     * 响应数据;
     */
    protected Object data;

    protected int page;

    protected int count;

    public RespBody(CommandEnum command) {
        this.command = command.getValue();
    }

    public static String success(CommandEnum command) {
        return success(command, null, 0, 0);
    }

    public static String success(CommandEnum command, Object data) {
        return success(command, data, 0, 0);
    }

    public static String successPage(CommandEnum command, Object data, int page, int count) {
        return success(command, data, page, count);
    }

    public static String success(CommandEnum command, Object data, int page, int count) {
        RespBody respBody = new RespBody(command);
        respBody.setData(data);
        respBody.setSuccess(true);
        respBody.setMsg("操作成功");
        respBody.setPage(page);
        respBody.setCount(count);

        return JSON.toJSONString(respBody, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String fail(CommandEnum command, String msg) {
        RespBody respBody = new RespBody(command);
        respBody.setData(null);
        respBody.setSuccess(false);
        respBody.setMsg(msg);
        return JSON.toJSONString(respBody, SerializerFeature.DisableCircularReferenceDetect);
    }

    public RespBody setData(Object data) {
        this.data = data;
        return this;
    }
}
