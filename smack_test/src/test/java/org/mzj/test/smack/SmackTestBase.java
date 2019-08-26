package org.mzj.test.smack;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.junit.Before;

public class SmackTestBase {
	XMPPTCPConnection connection;

	@Before
	public void setUp() {
		try {
			if (connection == null) {
				XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
						// 服务器IP地址
						.setHost("127.0.0.1")
						// 服务器端口
//						.setPort(5222)
						// 服务器名称(管理界面的 主机名)
						.setXmppDomain("desktop-3857fhg")
						// 是否开启安全模式*
						.setSecurityMode(SecurityMode.disabled)
						// 是否开启压缩
						.setCompressionEnabled(false)
						.setSendPresence(true)
						// 开启调试模式
						.setDebuggerEnabled(true).build();
				System.out.println(SmackConfiguration.getVersion());
				SmackConfiguration.DEBUG=true;
				connection = new XMPPTCPConnection(config);
				connection.connect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
