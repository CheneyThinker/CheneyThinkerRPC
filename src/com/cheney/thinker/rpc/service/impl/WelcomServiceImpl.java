package com.cheney.thinker.rpc.service.impl;

import com.cheney.thinker.rpc.service.WelcomService;

public class WelcomServiceImpl implements WelcomService {

	public String welcom(String name) {
		return "Welcom ".concat(name);
	}

}
