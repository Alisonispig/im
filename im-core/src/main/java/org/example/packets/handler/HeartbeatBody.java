/**
 * 
 */
package org.example.packets.handler;

import lombok.Data;

/**
 * @author WChao
 *
 */
@Data
public class HeartbeatBody  {
	
	private static final long serialVersionUID = -1773817279179288833L;

	private byte hbbyte;
	
	public HeartbeatBody(){}
	public HeartbeatBody(byte hbbyte){
		this.hbbyte = hbbyte;
	}

	
}
