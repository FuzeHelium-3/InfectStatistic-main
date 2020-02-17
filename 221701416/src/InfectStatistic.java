import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
	public static String []province=new String[31];
    public static void main(String[] args) throws IOException {
    	CoronavirusDetail CD=new CoronavirusDetail();
    	CD.Init();
    	AnalysisCommand(args);
    	CD.ReadAll(log,out,date,type,province);
    	CD.Printdetail(log,out,date,type,province);
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
    	for(int i=0;i<31;i++) province[i]=$nothing;
    	
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
	String $nothing=new String("nothing");
	Map ProvinceMap= new HashMap();	
	Map TypeMap=new HashMap();
	String[] ProvinceStr= {
			"����"  ,"����"  ,"����"  ,"����"  ,"����"  ,
			"�㶫"  ,"����"  ,"����"  ,"����"  ,"�ӱ�"  ,
			"����"  ,"������","����"  ,"����"  ,"����"  ,
			"����"  ,"����"  ,"����"  ,"���ɹ�","����"  ,
			"�ຣ"  ,"ɽ��"  ,"ɽ��"  ,"����"  ,"�Ϻ�"  ,
			"�Ĵ�"  ,"���"  ,"����"  ,"�½�"  ,"����"  ,"�㽭" ,"ȫ��"
	};
	String[] TypeStr={
		"ip" , "sp", "curu", "dead"
	};
	String[] TypeStrCn={
			"��Ⱦ����" , "���ƻ���", "����", "����"
		};
	int provincenum=32;
	int detailnum=4;
	public int [][] detail=new int[provincenum][detailnum];
	//��ʼ��ʡ����Ϣ
	public void Init() {
		//����ʡ����Ϣ31��
		//��ʡ��ע��Map�з������ʹ��
		for(int i=0;i<ProvinceStr.length;i++) {
			ProvinceMap.put(ProvinceStr[i], Integer.valueOf(i));
		}
		
		for(int i=0;i<TypeStr.length;i++) {
			TypeMap.put(TypeStr[i],Integer.valueOf(i));
		}
		
		for(int i=0;i<provincenum;i++)
			for(int j=0;j<detailnum;j++) detail[i][j]=0;
	}
	//���������Ϣ
    public void Printdetail(String log,String out,String date,String [] type,String [] province) {
    	for(int i=0;i<province.length;i++) if(!province[i].equals($nothing)){
    		System.out.print(province[i]+" ");
    		Integer pronum=(Integer) ProvinceMap.get(province[i]);
    		//System.out.print(pronum);
    		for(int j=0;j<TypeStr.length;j++) {
    			
    			System.out.print(TypeStrCn[j]+detail[pronum][(Integer) TypeMap.get(TypeStr[j])]+" ");
    		}
    		System.out.println();
    	}
    }
    
	//��ȡָ������֮ǰ���ļ���
	public static ArrayList<String> getFilesName(String path,String date){
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> beforefiles = new ArrayList<String>();
	    File file = new File(path);
	    File[] tempList = file.listFiles();

	    for (int i = 0; i < tempList.length; i++) {
	        if (tempList[i].isFile()) {
//	              System.out.println("�� ����" + tempList[i]);
	            files.add(tempList[i].toString());
	        }
	        //�õ���ǰ����֮ǰ���ļ���
	        File tempFile=new File(files.get(i).trim());
	        String fileName=tempFile.getName().substring(0,10);
	        if(date.compareTo(fileName)>=0){
	        	beforefiles.add(files.get(i));
	        }
	    }
	    return beforefiles;
	}
	
	//��ȡ����ָ������֮ǰ���ļ�
	public void ReadAll(String log,String out,String date,String [] type,String [] province) throws IOException{
		//System.out.println(log);
		//System.out.println(out);
		//System.out.println(date);
		//�趨������ʽ����
		String matString_1="(\\S+) ���� ��Ⱦ���� (\\d+)��";
		String splitString_1=" ���� ��Ⱦ���� |��";
		
		String matString_2="(\\S+) ���� ���ƻ��� (\\d+)��";
		String splitString_2=" ���� ���ƻ��� |��";
		
		String matString_3="(\\S+) ��Ⱦ���� ���� (\\S+) (\\d+)��";
		String splitString_3=" ��Ⱦ���� ���� | |��";
		
		String matString_4="(\\S+) ���ƻ��� ���� (\\S+) (\\d+)��";
		String splitString_4=" ���ƻ��� ���� | |��";
		
		String matString_5="(\\S+) ���� (\\d+)��";
		String splitString_5=" ���� |��";
		
		String matString_6="(\\S+) ���� (\\d+)��";
		String splitString_6=" ���� |��";
		
		String matString_7="(\\S+) ���ƻ��� ȷ���Ⱦ (\\d+)��";
		String splitString_7=" ���ƻ��� ȷ���Ⱦ |��";
		
		String matString_8="(\\S+) �ų� ���ƻ��� (\\d+)��";
		String splitString_8=" �ų� ���ƻ��� |��";
		
//		System.out.println(txtString.matches(matString_3));
//		String [] reStrings=txtString.split(splitString_3);
//		for(int i=0;i<reStrings.length;i++) {
//			System.out.println(reStrings[i]);
//		}
		
		for(int i=0;i<4;i++) {
			//System.out.println(type[i]);
		}
		for(int i=0;i<31;i++) {
			//System.out.println(province[i]);
		}
		
		//�õ���Ҫ���ļ�·�����������
		ArrayList<String> teArrayList=getFilesName(log,date);
		for(int i=0;i<teArrayList.size();i++) {
			System.out.println(teArrayList.get(i));

			BufferedReader inBufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(teArrayList.get(i)), "UTF-8"));
			String nowString;
			while((nowString=inBufferedReader.readLine())!=null) {
				System.out.print(nowString);
				System.out.println("???");
				//<ʡ> ���� ��Ⱦ���� n��
				if(nowString.matches(matString_1)) {
					String [] Strings_1=nowString.split(splitString_1);
					detail[(Integer) ProvinceMap.get(Strings_1[0])][0]+=Integer.parseInt(Strings_1[1]);
				}
				//<ʡ> ���� ���ƻ��� n��
				else if(nowString.matches(matString_2)) {
					String [] Strings_2=nowString.split(splitString_2);
					detail[(Integer) ProvinceMap.get(Strings_2[0])][1]+=Integer.parseInt(Strings_2[1]);
				}
				//<ʡ1> ��Ⱦ���� ���� <ʡ2> n��
				else if(nowString.matches(matString_3)) {
					String [] Strings_3=nowString.split(splitString_3);
					detail[(Integer) ProvinceMap.get(Strings_3[0])][0]-=Integer.parseInt(Strings_3[2]);
					detail[(Integer) ProvinceMap.get(Strings_3[1])][0]+=Integer.parseInt(Strings_3[2]);
				}
				//<ʡ1> ���ƻ��� ���� <ʡ2> n��
				else if(nowString.matches(matString_4)) {
					String [] Strings_4=nowString.split(splitString_4);
					detail[(Integer) ProvinceMap.get(Strings_4[0])][1]-=Integer.parseInt(Strings_4[2]);
					detail[(Integer) ProvinceMap.get(Strings_4[1])][1]+=Integer.parseInt(Strings_4[2]);
				}
				//<ʡ> ���� n��
				else if(nowString.matches(matString_5)) {
					String [] Strings_5=nowString.split(splitString_5);
					detail[(Integer) ProvinceMap.get(Strings_5[0])][3]+=Integer.parseInt(Strings_5[1]);
					detail[(Integer) ProvinceMap.get(Strings_5[0])][0]-=Integer.parseInt(Strings_5[1]);
				}
				//<ʡ> ���� n��
				else if(nowString.matches(matString_6)) {
					String [] Strings_6=nowString.split(splitString_6);
					detail[(Integer) ProvinceMap.get(Strings_6[0])][2]+=Integer.parseInt(Strings_6[1]);
					detail[(Integer) ProvinceMap.get(Strings_6[0])][0]-=Integer.parseInt(Strings_6[1]);
				}
				//<ʡ> ���ƻ��� ȷ���Ⱦ n��
				else if(nowString.matches(matString_7)) {
					String [] Strings_7=nowString.split(splitString_7);
					detail[(Integer) ProvinceMap.get(Strings_7[0])][0]+=Integer.parseInt(Strings_7[1]);
					detail[(Integer) ProvinceMap.get(Strings_7[0])][1]-=Integer.parseInt(Strings_7[1]);
				}
				//<ʡ> �ų� ���ƻ��� n��
				else if(nowString.matches(matString_8)) {
					String [] Strings_8=nowString.split(splitString_8);
					detail[(Integer) ProvinceMap.get(Strings_8[0])][1]-=Integer.parseInt(Strings_8[1]);
				}
				System.out.println("+++++");
			}
		}
	}
}