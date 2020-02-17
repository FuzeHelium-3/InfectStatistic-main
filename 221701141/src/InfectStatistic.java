import java.util.ArrayList;

/**
 * InfectStatistic
 * TODO
 *
 * @author massizhi
 * @version 1.0
 */
class InfectStatistic
{
    public static void main(String[] args)
    {
    	//����ʡ�������飨����ȫ����
    	String province[]={"ȫ��","����","����","����","����","����","�㶫","����",
    			"����","����","�ӱ�","����","������","����","����","����","����",
    			"����","����","���ɹ�","����","�ຣ","ɽ��","ɽ��","����","�Ϻ�",
    			"�Ĵ�","���","����","�½�","����","�㽭"};
    	//��¼��ʡ������ȫ�����������͵��˵�����
    	int[][] number=new int[32][4];
    	//������Ÿ����������в�����ArrayList
    	ArrayList<String> logList=new ArrayList<String>();
		ArrayList<String> outList=new ArrayList<String>();
		ArrayList<String> dateList=new ArrayList<String>();
		ArrayList<String> typeList=new ArrayList<String>();
		ArrayList<String> provinceList=new ArrayList<String>();
		//��ʼ����ά����number
    	for (int i=0;i<32;i++)
    	{
    		for (int j=0;j<4;j++)
    		{
    			number[i][j]=0;
    		}
    	}
    	//�õ���Ÿ����������в�����ArrayList����ֵ
    	for (int i=0;i<args.length;i++)
    	{
    		if (args[i].equals("-log")) {
    			for (int j=i+1;j<args.length;j++) {
    				if (!args[j].equals("-out")&&!args[j].equals("-date")&&
    					!args[j].equals("-type")&&!args[j].equals("-province"))
    				{
    					logList.add(args[j]);
    				}
    				else 
    				{
    					i=j-1;
    					break;
    				}
    			}
    		}
    		else if (args[i].equals("-out")) {
    			for (int j=i+1;j<args.length;j++) {
    				if (!args[j].equals("-log")&&!args[j].equals("-date")&&
        				!args[j].equals("-type")&&!args[j].equals("-province"))
    				{
    					outList.add(args[j]);
    				}
    				else 
    				{
    					i=j-1;
    					break;
    				}
    			}
    		}
    		else if (args[i].equals("-date")) {
    			for (int j=i+1;j<args.length;j++) {
    				if (!args[j].equals("-out")&&!args[j].equals("-log")&&
    					!args[j].equals("-type")&&!args[j].equals("-province"))
    				{
    					dateList.add(args[j]);
    				}
    				else 
    				{
    					i=j-1;
    					break;
    				}
    			}
    		}
    		else if (args[i].equals("-type")) {
    			for (int j=i+1;j<args.length;j++) {
    				if (!args[j].equals("-out")&&!args[j].equals("-date")&&
    					!args[j].equals("-log")&&!args[j].equals("-province"))
    				{
    					typeList.add(args[j]);
    				}
    				else 
    				{
    					i=j-1;
    					break;
    				}
    			}
    		}
    		else if (args[i].equals("-province")) {
    			for (int j=i+1;j<args.length;j++) {
    				if (!args[j].equals("-out")&&!args[j].equals("-date")&&
    					!args[j].equals("-type")&&!args[j].equals("-log"))
    				{
    					provinceList.add(args[j]);
    				}
    				else 
    				{
    					i=j-1;
    					break;
    				}
    			}
    		}
		}//��ȡArrayListѭ������
    	
    	//test
    	for (int i=0;i<logList.size();i++)
    		System.out.print(logList.get(i));
    	System.out.println();
    	for (int i=0;i<outList.size();i++)
    		System.out.print(outList.get(i));
    	System.out.println();
    	for (int i=0;i<dateList.size();i++)
    		System.out.print(dateList.get(i));
    	System.out.println();
    	for (int i=0;i<typeList.size();i++)
    		System.out.print(typeList.get(i));
    	System.out.println();
    	for (int i=0;i<provinceList.size();i++)
    		System.out.print(provinceList.get(i));
    	System.out.println();
    	//list -date 2020-01-22 -log D:/log/ -out D:/output.txt -province jingsu fujian  -type a b c -date 123 234 345
    }
}