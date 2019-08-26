package org.mzj.test.openfire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.AuthFactory;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.WebManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

public class BroadcastServlet extends HttpServlet {
	private static final long serialVersionUID = -7036991361912047744L;
	private WebManager webManager = new WebManager();

	// http://localhost:9090/plugins/mzjplugin/broadcast?username=admin&password=123456&body=广播消息&subject=主题
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		webManager.init(req, resp, req.getSession(), req.getServletContext());// 初始化webManager
		Collection<User> users = webManager.getUserManager().getUsers();// 获取所有用户
		String usernameFrom = req.getParameter("username");
		String passwordFrom = req.getParameter("password");
		String pass = null;
		try {
			pass = AuthFactory.getPassword(usernameFrom);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		
		if (passwordFrom != null && passwordFrom.equals(pass)) {
			String body = req.getParameter("body");
			String subject = req.getParameter("subject");
			
			final Message message = new Message();
			message.setType(Message.Type.chat);
			message.setBody(body);
			String domain = JiveGlobals.getProperty("xmpp.domain");
			String from = usernameFrom + "@" + domain;//"admin@desktop-3857fhg"
			message.setFrom(from);// 目前不加from则会导致客户端不能自动获取离线消息，除主动获取。
			message.setSubject(subject);
			
			//用户登录多次的时候，这种方式只能把消息发送给最后活跃的那台机器上
			PresenceManager presenceManager = webManager.getPresenceManager();
			for (User user : users) {
				String username = user.getUsername();
				String to = username + "@" + domain;
				Message messageCopy = message.createCopy();
				messageCopy.setTo(to);
				if (presenceManager.isAvailable(user)) {
					JID jid = new JID(to);
					XMPPServer.getInstance().getRoutingTable().routePacket(jid , messageCopy, true);
				} else {
					if (!username.equals(usernameFrom)) {
						XMPPServer.getInstance().getOfflineMessageStrategy().storeOffline(messageCopy);
					}
				}
			}
			
			//这种方式所以端都会收到
			message.setBody("[广播消息]" + body);
			XMPPServer.getInstance().getRoutingTable().broadcastPacket(message, false);//广播
			
			writer.write("broadcast success!");
		} else {
			writer.write("broadcast fail!");
		}
	}
	
	private static final String SERVICE_NAME = "mzjplugin/broadcast";
	public void destroy() {
        AuthCheckFilter.removeExclude(SERVICE_NAME);
    }

    public void init() throws ServletException {
        AuthCheckFilter.addExclude(SERVICE_NAME);
    }
}
