package org.mzj.test.smack;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.junit.Test;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

public class TestSmack extends SmackTestBase {

	@Test
	public void 注册() {
		try {
			AccountManager manager = AccountManager.getInstance(connection);
			manager.sensitiveOperationOverInsecureConnection(true);
			Localpart username = Localpart.from("zhang3");
			String password = "123";
			manager.createAccount(username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 登录加退出() {
		final String userName = "zhang3";
		final String password = "123";
		try {
			new Thread(new Runnable() {
				public void run() {
					try {
						connection.login(userName, password);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {Thread.sleep(1000 * 60 * 1);} catch (InterruptedException e) {}

		try {
			connection.instantShutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 聊天() {
		try {
			connection.login("mzj", "313");
			ChatManager chatManager = ChatManager.getInstanceFor(connection);
			// 接收人jid
			EntityBareJid jid = JidCreate.entityBareFrom("ljw@desktop-3857fhg");
			// 获取聊天chat
			Chat chat = chatManager.chatWith(jid);
			// 创建消息对象，消息类型是Message.Type.chat
			Message message = new Message(jid, Message.Type.chat);
			message.setBody("hi");
			// 发送消息
			chat.send(message);
			
			chatManager.addIncomingListener(new IncomingChatMessageListener(){
				public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
					System.err.println(message.getFrom() + ": " + message.getBody());
				}
			});
			chatManager.addOutgoingListener(new OutgoingChatMessageListener() {
				public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
					System.out.println(message.getFrom() + ": " + message.getBody());
				}
			});
			
			while(true) {
				InputStreamReader reader = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(reader);
				chat.send(br.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {Thread.sleep(1000 * 60 * 5);} catch (InterruptedException e) {}
	}
}
