package com.demo.helpers;

import java.util.UUID;

public class SecurityCodeHelper {
	public static String generate() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
