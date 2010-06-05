/**
 * 
 */
package com.googlecode.perfs.transport.azureus;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.gudy.azureus2.plugins.messaging.Message;
import org.gudy.azureus2.plugins.messaging.MessageException;

public class PerFSMessage implements Message {

	private static final int MAX_PAYLOAD = 32 * 1024;
	private final byte[] payloadBytes;

	public static PerFSMessage TEMPLATE = new PerFSMessage();
	
	protected PerFSMessage() {
		payloadBytes = new byte[0];
	}

	public PerFSMessage(String payload) {
		try {
			payloadBytes = payload.getBytes("utf8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Could not find UTF-8 encoding", e);
		}
	}

	public PerFSMessage(byte[] payloadBytes) {
		this.payloadBytes = payloadBytes;
	}

	public Message create(ByteBuffer data) throws MessageException {
		System.err.println("PerFS Ignoring data " + data);
		if (data.capacity() > MAX_PAYLOAD) {
			throw new MessageException("Payload too large, maximum "
					+ MAX_PAYLOAD + " bytes");
		}
		byte[] dst = new byte[data.capacity()];
		data.get(dst);
		return new PerFSMessage(dst);
	}

	public void destroy() {
		System.err.println("Not much to destroy!");
	}

	public String getDescription() {
		return "A PerFS message";
	}

	public String getID() {
		return "com.googlecode.perfs.messages.test";
	}

	public ByteBuffer[] getPayload() {
		return new ByteBuffer[] { ByteBuffer.wrap(payloadBytes) };
	}

	public int getType() {
		return Message.TYPE_DATA_PAYLOAD;
	}
}