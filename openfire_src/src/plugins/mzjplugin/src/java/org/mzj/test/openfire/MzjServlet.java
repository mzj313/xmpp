package org.mzj.test.openfire;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mzj.test.openfire.processor.BaseProcessor;

public class MzjServlet extends HttpServlet {
	private static final long serialVersionUID = -7036991361912047744L;

	// http://localhost:9090/plugins/mzjplugin/mzjweb
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pre = "/plugins/mzjplugin/mzjweb";
		String uri = req.getRequestURI();//plugins/mzjplugin/mzjweb/a
		System.out.println(uri);
		String path = uri.replace(pre, "");
		if(path != null && path.length() > 0) {
			if(path.equals("/reload")) {
				BaseProcessor.reload();
			} else {
				try {
					// Processor子类规范：路径用ReqMapping注解，方法参数统一用req,resp
					BaseProcessor.getMethodByPath(path).invoke(BaseProcessor.getProcessorByPath(path), req, resp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else {
			String name = req.getParameter("name");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter writer = resp.getWriter();
			writer.write("hello, " + name);
		}
	}
	
}
