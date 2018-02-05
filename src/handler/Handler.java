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
	
	private static final Map<String,Object> map=new HashMap<String,Object>();//�����ü�ֵ�Ե���ʽ���治ͬ��control����

	public static void init(String root,String pack) {
		loadfiles(root,pack);//ע������ķ����ǻ�ȡ��ǰ�ļ��ĸ�·���ͱ�����.class�ļ�����һ��Ŀ¼���������Servlet�����õ��ļ�
	}
	private static void loadfiles(String root,String pack) {
		String path="/WEB-INF/classes/";//���������Ӹ�·����Servlet�������ļ����м���,ע�������ǰ������"/"
		String all=root+path+pack;//�����Ǳ��������.class�ļ��ĸ�Ŀ¼,��һ����Ϊ�˺���ı����ļ���׼��
		File file=new File(all);
		/*
		 * �����ļ���ʽ,�����ļ���·��,�����Ǿ���·����������Ҫע�������ļ�·����ȡ��ʽ,
		 * һ��ʼ�������ʵ���ǻ�srcԴ�ļ��ľ���·��,����Ļ�ȡ�ֽ����ļ�
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
		 * ������listFiles()���������ʽ��ȡĿ¼�µ��ļ�
		 * ������Ҫע��FileFilter()�������������ǹ��˵����ã�ֻ�з���accept()�����Ĳ���ͨ��
		 */
		for(File i:files) {		//����File����
			String fil=i.getName();//��ȡ������ÿ���ļ����ļ���
			System.out.println(fil);
			String packName=pack.replace("\\/", "\\.")+".";
			/*
			 * ������ʵ���ǻ�ȡsrc�����Դ�ļ��ľ���·��
			 * ��"/"ת��Ϊ".",��ʵ����Ϊ�˵���.class�ļ�
			 * �����Լ�д��ʱ����һ������,����ͻ�ȡ������.class�ļ���ͬ,
			 * ������ʵ�ǻ�ȡԴ�ļ��������ֽ����ļ�������·������ȡsrc����·��
			 */
			packName+=fil.substring(0,fil.length()-6);//����������ʵ�ǽ�ȡsrc�ļ��µ�Դ�ļ��ľ���·��
			try {
				Class cls=Class.forName(packName);
				/*
				 * ��ȡ�����ַ�����ȡClass��,���� ע�⵽���ǻ�ȡ����classes�ļ��������·��
				 * ��ʵ��ȡclasses������ļ����ǻ�ȡsr�ļ��µ��ֽ����ļ�����·��ansrcԴ�ļ�ȡ
				 * �����Լ�д��ʱ��ͷ���һ�������Լ���Ϊ�ǻ�ȡ���Ե�·������������Ǵ����
				 */
				if(Modifier.isAbstract(cls.getModifiers())) {
					continue;
				}
				/*
				 * Modifier�ķ����ж��ǲ��ǳ���������÷���API�ĵ�
				 */
				Annotation a=(Annotation) cls.getAnnotation(Annotation.class);
				/*
				 * ������Բ鿴API�ĵ��ľ����÷�,getAnnotation(Class<A> annotationClass)����
				 * �����ǻ�ȡ�����ָ�����͵�ע��,����������ע�⿴,����ͨ��ע���ֵ���ɼ�ֵ�Ե�String��
				 */
				if(null==a) {
					continue;
				}
				else {
					String str=a.value();//value()������ȡע�͵�ֵ,�ں�����Ϊ��ֵ�Եļ�
					System.out.println(str);
					Object object=cls.newInstance();//newInstance()��ȡ����Ķ��󣬾�����÷���API�ĵ�,������Ϊ��ֵ�Ե�ֵ
					System.out.println(object);
					map.put(str, object);
				}
			} catch (Exception e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static Object getControl(String str) { 	//�����Ǹ����ַ�����ȡ����
		
		return map.get(str);
	}
	
}
