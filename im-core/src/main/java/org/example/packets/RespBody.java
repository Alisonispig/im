/**
 *
 */
package org.example.packets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    public RespBody(CommandEnum command) {
        this.command = command.getValue();
    }

    public static String success(CommandEnum command) {
        return success(command, null);
    }

    public static String success(CommandEnum command, Object data) {
        RespBody respBody = new RespBody(command);
        respBody.setData(data);
        respBody.setSuccess(true);
        respBody.setMsg("操作成功");

        return JSON.toJSONString(respBody);
    }

    public RespBody setData(Object data) {
        this.data = data;
        return this;
    }
}
