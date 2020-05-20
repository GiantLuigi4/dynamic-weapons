package tfc.dynamic_weaponary.Utils;

import java.lang.reflect.Method;

public class ReflectionHelper {
	public static class MethodHolder {
		String ClassPath;
		String MethodName;
		
		public MethodHolder(String classPath, String methodName) {
			ClassPath = classPath;
			MethodName = methodName;
		}
		
		public MethodHolder(String cfgString) {
			ClassPath = cfgString.substring(0, cfgString.lastIndexOf('.'));
			MethodName = cfgString.substring(cfgString.lastIndexOf('.') + 1);
		}
		
		@Override
		public String toString() {
			return "MethodHolder{" +
					"ClassPath='" + ClassPath + '\'' +
					", MethodName='" + MethodName + '\'' +
					'}';
		}
		
		public Object execute(Object obj1, Object obj2, Object obj3) {
			try {
				Class<Object> clazz = (Class<Object>) Class.forName(ClassPath);
				Method method = null;
				try {
					method = clazz.getMethod(MethodName, obj1.getClass(), obj2.getClass(), obj3.getClass());
				} catch (Exception err) {
					try {
						method = clazz.getDeclaredMethod(MethodName, obj1.getClass(), obj2.getClass(), obj3.getClass());
					} catch (Exception err2) {
						try {
							for (Method method2 : clazz.getMethods()) {
								if (method2.getName().equals(MethodName)) {
									method = method2;
								}
								if (!method.equals(null)) {
									throw new RuntimeException();
								}
							}
						} catch (Exception err3) {
							for (Method method2 : clazz.getDeclaredMethods()) {
								if (method2.getName().equals(MethodName)) {
									method = method2;
								}
							}
						}
					}
				}
				try {
					return method.invoke(clazz.newInstance(), obj1, obj2, obj3);
				} catch (Exception err) {
					return method.invoke(null, obj1, obj2, obj3);
				}
			} catch (Exception err) {
			}
			return null;
		}
	}
}
