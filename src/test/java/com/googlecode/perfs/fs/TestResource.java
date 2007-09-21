package com.googlecode.perfs.fs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.googlecode.perfs.util.UUIDResource;

public class TestResource {

	@Test
	public void uuidEquals() throws Exception {

		UUIDResource u = new UUIDResource();
		assertEquals(u, u);
		assertEquals(u.hashCode(), u.hashCode());

		
		UUIDResource u2 = new UUIDResource();
		assertFalse(u.hashCode() == u2.hashCode());

		UUIDResource uCopy = new UUIDResource(u.getUUIDString());
		assertTrue(u.hashCode() == uCopy.hashCode());
	}
	

}
