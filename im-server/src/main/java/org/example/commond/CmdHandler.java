package org.example.commond;


import org.example.enums.CommandEnum;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * @author : WChao 创建时间: 2017年9月8日 下午4:29:38
 */
public interface CmdHandler
{
	/**
	 * 功能描述：[命令主键]
	 * @author：WChao 创建时间: 2017年7月17日 下午2:31:51
	 * @return
	 */
	CommandEnum command();

	/**
	 * 处理Cmd命令
	 * @date 2016年11月18日 下午1:08:45
	 */
	WsResponse handler(Packet packet, ChannelContext channelContext);
	
}
