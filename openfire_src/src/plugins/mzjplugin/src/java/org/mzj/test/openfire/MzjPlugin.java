package org.mzj.test.openfire;

import java.io.File;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

public class MzjPlugin implements Plugin {
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		System.out.println("MzjPlugin初始化成功");
	}

	public void destroyPlugin() {
		System.out.println("MzjPlugin销毁完毕");
	}
}