/**
 * 
 */
package com.xej.xhjy.tools;

import cn.passguard.PassGuardEdit;

/**
 * @author zhangbaoxin
 * 
 *         2016-9-28下午4:26:26
 */
public class PassGuardUtils {

//	private static final String LICENSE_TEST = "W5ETm1zemtXUU1mMWRTckVQcmp3UUpYRG1FTXJLM1BwVGtvVjNndVBVdm9JYlpZNnZ5R0xpVk1jbHlxWG9yM09HbTloSkdiQmg5cGg0Ykg0OXdXYytyN2VYZERHYnJDOGVicitqRmZ5Y1RHc2JwL1dTZHIxaVhaRzRMRSthQTIyL2t4ejR0TUF4WjJyN2lNM1l5WDJpd0Y1L3FMOVpwRVlMQ3JWa1dlTDhFPXsiaWQiOjAsInR5cGUiOiJ0ZXN0IiwicGxhdGZvcm0iOjIsIm5vdGJlZm9yZSI6IjIwMTkwNTMxIiwibm90YWZ0ZXIiOiIyMDE5MDgzMSJ9";
	private static final String LICENSE = "VW8vZmdMeEhTdUxBKzVHRUNMRHlwVWdxUFFXejJFczY4cE1xcTkzLzBTdkMxdG4yZnJ6bmFNYXhlK08vSTZUR0hmSjNZVW9zam5VQTg4YkdwQkhycjdWUE9oM2ljV1ZxNEtzVDNLYjBkN3oycE12TUE1N3diaFFsenVhY2NFR3pMc2ZPdzhiQmtOWW1IRzlhNDVzSjZVRVdkZ2dXN3hxck9NMjNrQTA5b3g4PXsiaWQiOjAsInR5cGUiOiJwcm9kdWN0IiwicGFja2FnZSI6WyJjb20ueGVqLnhoankiXSwiYXBwbHluYW1lIjpbIiJdLCJwbGF0Zm9ybSI6Mn0=";
	private static final String PUBLIC_KEY = "3081890281810092d9d8d04fb5f8ef9b8374f21690fd46fdbf49b40eeccdf416b4e2ac2044b0cfe3bd67eb4416b26fd18c9d3833770a526fd1ab66a83ed969af74238d6c900403fc498154ec74eaf420e7338675cad7f19332b4a56be4ff946b662a3c2d217efbe4dc646fb742b8c62bfe8e25fd5dc59e7540695fa8b9cd5bfd9f92dfad009d230203010001";
	private static final String ECC_KEY = "04D0D16D729A76B82AF423B4D3CE9B89FDD89DAF7E6ADC966DE8B8AD3A337C6A|7992E7C0A2F61E42A5D8C5AD5CA06B719AB0A3D2BAFF864C3F94BAD1645A8110";

	private static final String MATCHER = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![-/:;()$&@\"\\.,?!\']+$)[0-9A-Za-z-/:;()$&@\"\\.,?!\']{6,18}$";

	public static void initialize(PassGuardEdit edit) {
		PassGuardEdit.setLicense(LICENSE);
		// 用于AES/SM4等的加密
		// edit.setCipherKey(temp);
		// 设置RSA公钥
		// edit.setPublicKey(PUBLIC_KEY);
		// 用于SM2算法
		// edit.setEccKey(ECC_KEY);
		edit.setEncrypt(true);// 设置是否加密 false为普通输入框
		edit.setMaxLength(18);
		edit.setButtonPress(true);
		edit.setReorder(0);// 0默认不乱序 1、初始化后只乱一次2、每次点击键盘是乱序一次。
//		edit.setInputRegex("\\w");
//		edit.setMatchRegex(MATCHER);
		edit.setWatchOutside(true);//键盘外点击关闭键盘
		edit.setButtonPressAnim(true);//设置键盘点击效果
		// doAction action = new doAction() {
		// public void doActionFunction() {
		// Toast.makeText(
		// context,
		// edit.isKeyBoardShowing() ? "KeyBoardShow"
		// : "KeyBoardHide", Toast.LENGTH_LONG).show();
		// if (edit.isKeyBoardShowing()) {
		// int b = edit.getKeyboardHeight();
		// // tvShow.setText("高度为：" + ""+b);
		// }
		// }
		// };
		//
		// edit.setKeyBoardHideAction(action);
		// edit.setKeyBoardShowAction(action);
		edit.initPassGuardKeyBoard();
	}

	public static String getCipherText(PassGuardEdit edit) {
		return edit.getRSAAESCiphertext();
	}

}
