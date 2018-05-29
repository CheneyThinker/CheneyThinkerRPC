import java.net.InetSocketAddress;

import com.cheney.thinker.rpc.client.Client;
import com.cheney.thinker.rpc.service.WelcomService;

public class ClientTest {
	
	public static void main(String[] args) {
		WelcomService service = Client.getRemoteProxyObj(WelcomService.class, new InetSocketAddress("127.0.0.1", 364));
		System.err.println(service.welcom("CheneyThinker"));
	}

}
