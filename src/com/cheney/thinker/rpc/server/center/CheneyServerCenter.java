package com.cheney.thinker.rpc.server.center;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cheney.thinker.rpc.server.CheneyServer;

public class CheneyServerCenter implements CheneyServer {
	
	private static Map<String, Class<?>> register = new HashMap<String, Class<?>>();
	private int port = 364;
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private static boolean isRunning = false;
	
	public CheneyServerCenter() {
	}
	
	public CheneyServerCenter(int port) {
		this.port = port;
	}

	public void start() {
		ServerSocket serverSocket = null;
		try {
			isRunning = true;
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(port));
			System.err.println("CheneyThinkerRPC is Running!");
			while (isRunning) {
				Socket socket = serverSocket.accept();
				executor.execute(new ServiceTask(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		isRunning = false;
		executor.shutdown();
	}

	public void register(Class<?> service, Class<?> serviceImpl) {
		register.put(service.getName(), serviceImpl);
	}
	
	private static class ServiceTask implements Runnable {
		
		private Socket socket;
		
		public ServiceTask(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			ObjectInputStream inputStream = null;
			ObjectOutputStream outputStream = null;
			try {
				inputStream = new ObjectInputStream(socket.getInputStream());
				String serviceName = inputStream.readUTF();
				String methodName = inputStream.readUTF();
				Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
				Object[] arguments = (Object[]) inputStream.readObject();
				Class<?> serviceClzss = register.get(serviceName);
				Method method = serviceClzss.getMethod(methodName, parameterTypes);
				Object result = method.invoke(serviceClzss.newInstance(), arguments);
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				outputStream.writeObject(result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
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
		}
		
	}

}
