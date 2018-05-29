package com.cheney.thinker.rpc.server;

/**
 * @description 服务中心
 * @author CheneyThinker
 * @time 2018年5月22日
 */
public interface CheneyServer {
	void start();
	void stop();
	void register(Class<?> service, Class<?> serviceImpl);
}
