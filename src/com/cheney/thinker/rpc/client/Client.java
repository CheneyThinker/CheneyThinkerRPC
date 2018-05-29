package com.cheney.thinker.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

@SuppressWarnings("unchecked")
public class Client {
	
	public static <T> T getRemoteProxyObj(Class<?> serviceInterface, InetSocketAddress inetSocketAddress) {
		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Socket socket = null;
				ObjectOutputStream outputStream = null;
				ObjectInputStream inputStream = null;
				try {
					socket = new Socket();
					socket.connect(inetSocketAddress);
					outputStream = new ObjectOutputStream(socket.getOutputStream());
					outputStream.writeUTF(serviceInterface.getName());
					outputStream.writeUTF(method.getName());
					outputStream.writeObject(method.getParameterTypes());
					outputStream.writeObject(args);
					inputStream = new ObjectInputStream(socket.getInputStream());
					return inputStream.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != socket) {
							socket.close();
						}
						if (null != outputStream) {
							outputStream.close();
						}
						if (null != inputStream) {
							inputStream.close();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				return null;
			}
		};
		return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[] { serviceInterface }, handler);
	}

}
