import java.io.IOException;
import java.util.*;
/**
 * InfectStatistic
 *
 * @author Hanani
 * @version v1.0.0
 * @since xxx
 */
class InfectStatistic {
	public String path=new String();
    public static void main(String[] args) throws IOException {
    	CoronavirusDetail CD=new CoronavirusDetail();
    	CD.Init();
    	AnalysisCommand(args);
    	CD.ReadAll();
    }
    public static void AnalysisCommand(String[] command) {
    	String $list= new String("list");
    	for(int i=0;i<command.length;i++) {
    		if((i==0) && !(command[i].equals($list))) {
    			System.out.println("��ʹ��list������в���");
    			System.exit(0);
    		}
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
	public void ReadAll() throws IOException{
		
	}
}