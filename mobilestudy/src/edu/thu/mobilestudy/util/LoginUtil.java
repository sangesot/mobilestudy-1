package edu.thu.mobilestudy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.thu.mobilestudy.model.User;

/**
 * 登录工具类
 * 
 * @author hujiawei
 * 
 */
public class LoginUtil {

	public static final String fileName = "/user.config";

	public static final int NOT_LOGIN = 0;
	public static final int AUTO_LOGIN = 1;

	// 写出用户信息到文件中
	public static void writeUserInfo(User user) {
		System.out.println("write user info");
		File file;
		ObjectOutputStream oos;
		try {
			file = new File(CommonUtil.SDFOLDER.getCanonicalPath() + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(user);
			oos.close();
		} catch (Exception e) {
			// e.printStackTrace();//
		}
	}

	// 从文件中读取用户信息
	public static User readUserInfo() {
		System.out.println("read user info");
		File file;
		ObjectInputStream ois;
		User user = null;
		try {
			file = new File(CommonUtil.SDFOLDER.getCanonicalPath() + fileName);
			if (!file.exists()) {
				return null;
			}
			ois = new ObjectInputStream(new FileInputStream(file));
			user = (User) ois.readObject();
			ois.close();
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
		return user;
	}

	// delete
	public static void deleteUserInfo() {
		System.out.println("delete user info");
		File file;
		try {
			file = new File(CommonUtil.SDFOLDER.getCanonicalPath() + fileName);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
