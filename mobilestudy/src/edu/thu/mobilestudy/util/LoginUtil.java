package edu.thu.mobilestudy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.thu.mobilestudy.model.User;

/**
 * ��¼������
 * 
 * @author hujiawei
 * 
 */
public class LoginUtil {

	public static final String fileName = "/user.config";

	public static final int NOT_LOGIN = 0;
	public static final int AUTO_LOGIN = 1;

	// д���û���Ϣ���ļ���
	public static void writeUserInfo(User user) {
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
//			e.printStackTrace();//
		}
	}

	// ���ļ��ж�ȡ�û���Ϣ
	public static User readUserInfo() {
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
//			e.printStackTrace();
			return null;
		}
		return user;
	}

}
