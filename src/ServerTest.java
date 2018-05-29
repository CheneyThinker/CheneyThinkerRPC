import com.cheney.thinker.rpc.server.CheneyServer;
import com.cheney.thinker.rpc.server.center.CheneyServerCenter;
import com.cheney.thinker.rpc.service.WelcomService;
import com.cheney.thinker.rpc.service.impl.WelcomServiceImpl;

public class ServerTest {
	
	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				CheneyServer server = new CheneyServerCenter();
				server.register(WelcomService.class, WelcomServiceImpl.class);
				server.start();
			}
		}).start();
	}

}
