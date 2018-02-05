package handler;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import annotation.Annotation;
import control.cardControl;
import control.control;
import control.userControl;

public class Handler {
	
	private static final Map<String,Object> map=new HashMap<String,Object>();//这里用键值对的形式保存不同的control对象

	public static void init(String root,String pack) {
		loadfiles(root,pack);//注意这里的方法是获取当前文件的根路径和编译后的.class文件的上一级目录，这就是在Servlet中配置的文件
	}
	private static void loadfiles(String root,String pack) {
		String path="/WEB-INF/classes/";//这里是连接根路径和Servlet的配置文件的中间量,注意这里的前后两个"/"
		String all=root+path+pack;//这里是保存编译后的.class文件的父目录,这一步是为了后面的遍历文件作准备
		File file=new File(all);
		/*
		 * 建立文件格式,载入文件的路径,这里是绝对路径，这里需要注意后面的文件路径获取方式,
		 * 一开始不理解其实就是获src源文件的绝对路径,后面的获取字节码文件
		 */
		File [] files=file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if(pathname.getName().endsWith("class")) {
					return true;
				}
				return false;
			}
		});
		/*
		 * 这里用listFiles()用数组的形式获取目录下的文件
		 * 这里需要注意FileFilter()方法它的作用是过滤的作用，只有符合accept()方法的才能通过
		 */
		for(File i:files) {		//遍历File数组
			String fil=i.getName();//获取遍历的每个文件的文件名
			System.out.println(fil);
			String packName=pack.replace("\\/", "\\.")+".";
			/*
			 * 这里其实就是获取src下面的源文件的绝对路径
			 * 将"/"转化为".",其实就是为了导入.class文件
			 * 这里自己写的时候犯了一个错误,这里和获取编译后的.class文件不同,
			 * 这里其实是获取源文件编译后的字节码文件，所以路径还是取src绝对路径
			 */
			packName+=fil.substring(0,fil.length()-6);//这里作用其实是截取src文件下的源文件的绝对路径
			try {
				Class cls=Class.forName(packName);
				/*
				 * 获取根据字符串获取Class类,这里 注意到的是获取类名classes文件夹下面的路径
				 * 其实获取classes下面的文件就是获取sr文件下的字节码文件所以路径ansrc源文件取
				 * 这里自己写的时候就犯了一个错误，自己以为是获取绝对的路径，这个方法是错误的
				 */
				if(Modifier.isAbstract(cls.getModifiers())) {
					continue;
				}
				/*
				 * Modifier的方法判断是不是抽象类具体用法见API文档
				 */
				Annotation a=(Annotation) cls.getAnnotation(Annotation.class);
				/*
				 * 这里可以查看API文档的具体用法,getAnnotation(Class<A> annotationClass)方法
				 * 具体是获取该类的指定类型的注释,这里可以配合注解看,后面通过注解的值构成键值对的String键
				 */
				if(null==a) {
					continue;
				}
				else {
					String str=a.value();//value()方法获取注释的值,在后面作为键值对的键
					System.out.println(str);
					Object object=cls.newInstance();//newInstance()获取该类的对象，具体的用法见API文档,这里作为键值对的值
					System.out.println(object);
					map.put(str, object);
				}
			} catch (Exception e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static Object getControl(String str) { 	//这里是根据字符串获取对象
		
		return map.get(str);
	}
	
}
