import java.io.IOException;
import java.util.*;
import java.io.File;
/**
 * InfectStatistic
 *
 * @author Hanani
 * @version v1.0.0
 * @since xxx
 */
class InfectStatistic {
	public static String log=new String();
	public static String out=new String();
	public static String date=new String("0000-00-00");
	public static String []type= new String[4];
	public static String []province=new String[40];
    public static void main(String[] args) throws IOException {
    	CoronavirusDetail CD=new CoronavirusDetail();
    	CD.Init();
    	AnalysisCommand(args);
    	CD.ReadAll(log,out,date,type,province);
    }
    //�������������д���ֽ�
    public static void AnalysisCommand(String[] command) {
    	int len=command.length;
    	String $list= new String("list");
    	String $log= new String("-log");
    	String $out= new String("-out");
    	String $date= new String("-date");
    	String $type= new String("-type");
    	String $province= new String("-province");
    	String $nothing=new String("nothing");
    	//��ʼ��type��province
    	for(int i=0;i<4;i++) type[i]=$nothing;
    	for(int i=0;i<40;i++) province[i]=$nothing;
    	
    	//list���
		if(!(command[0].equals($list))) {
			System.out.println("��ʹ��list������в���");
			System.exit(0);
		}
		//��ȡlogĿ¼
		for(int i=1;i<len;i++) {
			if(command[i].equals($log)) {
				log=command[i+1];
				break;
			}
		}
		//��ȡoutĿ¼
		for(int i=1;i<len;i++) {
			if(command[i].equals($out)) {
				out=command[i+1];
				break;
			}
		}
		//��ȡdate��ֵ
		for(int i=1;i<len;i++) {
			if(command[i].equals($date)) {
				date=command[i+1];
				break;
			}
		}
		//��ȡtype�Ĳ���
		int typepos=0;
		for(int i=1;i<len;i++) {
			if(command[i].equals($type)) {
				typepos=i;
				break;
			}
		}
		int typeindex=0;
		for(int i=typepos+1;i<len;i++) {
			if(command[i].charAt(0)!='-') {
				type[typeindex++]=command[i];
			}else break;
		}
		//��ȡprovince�Ĳ���
		int provincepos=0;
		for(int i=1;i<len;i++) {
			if(command[i].equals($province)) {
				provincepos=i;
				break;
			}
		}
		int provinceindex=0;
		for(int i=provincepos+1;i<len;i++) {
			if(command[i].charAt(0)!='-') {
				province[provinceindex++]=command[i];
			}else break;
		}
    }
}
/**
 * CoronavirusDetail
 *
 * @author Hanani
 * @version v1.0.0
 * @since xxx
 */
class CoronavirusDetail{
	Map ProvinceMap= new HashMap();	
	public void Init() {
		//����ʡ����Ϣ
		String[] ProvinceStr= {
				"����"  ,"����"  ,"����"  ,"����"  ,"����"  ,
				"�㶫"  ,"����"  ,"����"  ,"����"  ,"�ӱ�"  ,
				"����"  ,"������","����"  ,"����"  ,"����"  ,
				"����"  ,"����"  ,"����"  ,"���ɹ�","����"  ,
				"�ຣ"  ,"ɽ��"  ,"ɽ��"  ,"����"  ,"�Ϻ�"  ,
				"�Ĵ�"  ,"���"  ,"����"  ,"�½�"  ,"����"  ,"�㽭"  
		};
		//��ʡ��ע��Map�з������ʹ��
		for(int i=0;i<ProvinceStr.length;i++) {
			ProvinceMap.put(ProvinceStr[i], Integer.valueOf(i));
		}
	}
	public static ArrayList<String> getFiles(String path,String date){
		ArrayList<String> files = new ArrayList<String>();
	    File file = new File(path);
	    File[] tempList = file.listFiles();

	    for (int i = 0; i < tempList.length; i++) {
	        if (tempList[i].isFile()) {
//	              System.out.println("��     ����" + tempList[i]);
	            files.add(tempList[i].toString());
	        }
	        if (tempList[i].isDirectory()) {
//	              System.out.println("�ļ��У�" + tempList[i]);
	        }
	        //�õ���ǰ����֮ǰ���ļ���
	        File tempFile=new File(files.get(i).trim());
	        String fileName=tempFile.getName().substring(0,10);
	        if(date.compareTo(fileName)>=0){
		        System.out.println(fileName);
	        }
	    }
	    return files;
	}
	public void ReadAll(String log,String out,String date,String [] type,String [] province) throws IOException{
		System.out.println(log);
		System.out.println(out);
		System.out.println(date);
		for(int i=0;i<4;i++) {
			System.out.println(type[i]);
		}
		for(int i=0;i<40;i++) {
			System.out.println(province[i]);
		}
		ArrayList<String> teArrayList=getFiles(log,date);
		for(int i=0;i<teArrayList.size();i++) {
			System.out.println(teArrayList.get(i));
		}
	}
	
}