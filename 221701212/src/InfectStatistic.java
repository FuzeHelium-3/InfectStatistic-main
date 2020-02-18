import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * InfectStatistic
 * TODO
 *
 * @author hx_wang
 * @version 1.0
 * @since 2020.02.15
 */
class InfectStatistic {
	int argc;
	int PROVIENCE_NUM = 32;
	int[] ip;
	int[] sp;
	int[] cure;
	int[] dead;
	boolean[] flg;
	String[] argv;
	String[] provience_array;
	String dir4log = null;
	String dir4out = null;
	String cmd4data = null;
	Vector<String> cmd4type;
	Vector<String> cmd4provience;
	List<String> listFile4log;
	List<String> listFile4out;
	boolean flg4provience = false;
	boolean flg4data = false;
	boolean flg4type = false;
	boolean flg4ip = false;
	boolean flg4sp = false;
	boolean flg4cure = false;
	
	public InfectStatistic(int argc, String[] argv) {
		this.argc = argc;
		this.argv = argv;
		flg = new boolean[PROVIENCE_NUM + 1];
		ip = new int[PROVIENCE_NUM];
		sp = new int[PROVIENCE_NUM];
		cure = new int[PROVIENCE_NUM];
		dead = new int[PROVIENCE_NUM];
		cmd4type = new Vector<String>();
		cmd4provience = new Vector<String>();
		provience_array = new String[] {"ȫ��","����","����","����","����","����","�㶫","����",
				"����","����","�ӱ�","����","������","����","����","����","����","����","����","���ɹ�",
				"����","�ຣ","ɽ��","ɽ��","����","�Ϻ�","�Ĵ�","���","����","�½�","����","�㽭"};
		
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			flg[i] = false;
			ip[i] = sp[i] = cure[i] = dead[i] = 0;
		}
	}
	
	public void initArgument() {
		getArgument();
		if(flg4provience == true) {
			for(int j = 0; j < cmd4provience.size(); j++) {
				for(int i = 0; i < PROVIENCE_NUM; i++) {
					if(cmd4provience.get(j).equals(provience_array[i])) {
						flg[i] = true;
						break;
					}
				}
			}
		} else {
			flg[PROVIENCE_NUM] = true;
		}
		
	}

	public void getArgument() {
		for(int i = 1; i < argc; i++) {
			if(argv[i].equals("-log")) {
				i++;
				dir4log = argv[i];
			} else if(argv[i].equals("-out")) {
				i++;
				dir4out = argv[i];
			} else if(argv[i].equals("-data")) {
				i++;
				cmd4data = argv[i] + ".log.txt";
				flg4data = true;
			} else if(argv[i].equals("-type")) {
				i++;
				flg4type = true;
				while(!argv[i].startsWith("-")) {
					cmd4type.add(argv[i]);
					i++;
				}
			} else if(argv[i].equals("-provience")) {
				i++;
				flg4provience = true;
				while(!argv[i].startsWith("-")) {
					cmd4provience.add(argv[i]);
					i++;
				}
			}
		}
	}
	
	private void statistic() {
		listFile4log = getPath(dir4log);
		Collections.sort(listFile4log);
		for(int i = 0; i < listFile4log.size(); i++) {
			getData(listFile4log.get(i));
		}
		calculate_total();
		writeData(dir4out);
	}
	
  public List<String> getPath(String path) {
    List<String> files = new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();
    String rexp1 = "((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\.log\\.txt";
    
    for (int i = 0; i < tempList.length; i++) {
    	if (tempList[i].getName().matches(rexp1) == true && tempList[i].isFile()) {
      	if(flg4data == true && tempList[i].getName().compareTo(cmd4data) <= 0) {
          files.add(tempList[i].toString());
          //System.out.println(tempList[i].getName());
      	} else if(flg4data == false) {
          files.add(tempList[i].toString());
          //System.out.println(tempList[i].getName());
      	}
      }
    }
    return files;
}
	
  public void getData(String path) {
  	try {
  		//BufferedReader br = new BufferedReader(new FileReader(path));
  		FileInputStream fis = new FileInputStream(path);   
  		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");   
  		BufferedReader br = new BufferedReader(isr);  
  		String dataLine = br.readLine();
  		
  		while(dataLine != null) {
  			if(!dataLine.startsWith("//")) {
  				headleData(dataLine);
  			}
  			dataLine = br.readLine();
  			System.out.println(dataLine);
  		}
  	} catch(Exception e) {
  		e.printStackTrace();
  	}
  }
  
	public void headleData(String dataLine) {
		String status1 = "(\\S+) ���� ��Ⱦ���� (\\d+)��";
    String status2 = "(\\S+) ��Ⱦ���� ���� (\\S+) (\\d+)��";
    String status3 = "(\\S+) ���� (\\d+)��";
    String status4 = "(\\S+) ���� (\\d+)��";
		String status5 = "(\\S+) ���� ���ƻ��� (\\d+)��";
    String status6 = "(\\S+) ���ƻ��� ���� (\\S+) (\\d+)��";
    String status7 = "(\\S+) ���ƻ��� ȷ���Ⱦ (\\d+)��";
    String status8 = "(\\S+) �ų� ���ƻ��� (\\d+)��";
    
    if(dataLine.matches(status1)) {
    	ip_inc(dataLine);
    } else if(dataLine.matches(status2)) {
    	ip_flow(dataLine);
    } else if(dataLine.matches(status3)) {
    	ip_cure(dataLine);
    } else if(dataLine.matches(status4)) {
    	ip_die(dataLine);
    } else if(dataLine.matches(status5)) {
    	sp_inc(dataLine);
    } else if(dataLine.matches(status6)) {
    	sp_flow(dataLine);
    } else if(dataLine.matches(status7)) {
    	sp_conf(dataLine);
    } else if(dataLine.matches(status8)) {
    	sp_exclude(dataLine);
    }
	}

	public void sp_exclude(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				sp[i] -= Integer.parseInt(str[3].substring(0, str[3].indexOf("��")));
				if(flg4provience == false) {
					flg[i] = true;
				}
			}
		}
	}

	public void sp_conf(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				sp[i] -= Integer.parseInt(str[3].substring(0, str[3].indexOf("��")));
				ip[i] += Integer.parseInt(str[3].substring(0, str[3].indexOf("��")));
				if(flg4provience == false) {
					flg[i] = true;
				}
			}
		}
	}

	public void sp_flow(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				for(int j = 0; j < PROVIENCE_NUM; j++) {
					if(str[3].equals(provience_array[j])) {
						sp[i] -= Integer.parseInt(str[4].substring(0, str[4].indexOf("��")));
						sp[j] += Integer.parseInt(str[4].substring(0, str[4].indexOf("��")));
						if(flg4provience == false) {
							flg[i] = flg[j] = true;
						}
					}
				}
			}
		}
	}

	public void sp_inc(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				sp[i] += Integer.parseInt(str[3].substring(0, str[3].indexOf("��")));
				if(flg4provience == false) {
					flg[i] = true;
				}
			}
		}
	}

	public void ip_die(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				ip[i] -= Integer.parseInt(str[2].substring(0, str[2].indexOf("��")));
				dead[i] += Integer.parseInt(str[2].substring(0, str[2].indexOf("��")));
				if(flg4provience == false) {
					flg[i] = true;
				}
			}
		}
	}

	public void ip_cure(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				ip[i] -= Integer.parseInt(str[2].substring(0, str[2].indexOf("��")));
				cure[i] += Integer.parseInt(str[2].substring(0, str[2].indexOf("��")));
				if(flg4provience == false) {
					flg[i] = true;
				}
			}
		}
	}

	public void ip_flow(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				for(int j = 0; j < PROVIENCE_NUM; j++) {
					if(str[3].equals(provience_array[j])) {
						ip[i] -= Integer.parseInt(str[4].substring(0, str[4].indexOf("��")));
						ip[j] += Integer.parseInt(str[4].substring(0, str[4].indexOf("��")));
						if(flg4provience == false) {
							flg[i] = flg[j] = true;
						}
					}
				}
			}
		}
	}

	public void ip_inc(String dataLine) {
		String[] str = dataLine.split(" ");
		for(int i = 0; i < PROVIENCE_NUM; i++) {
			if(str[0].equals(provience_array[i])) {
				ip[i] += Integer.parseInt(str[3].substring(0, str[3].indexOf("��")));
				if(flg4provience == false) {
					flg[i] = true;
				}
			}
		}
	}

	public void calculate_total() {
		for(int i = 1; i < PROVIENCE_NUM; i++) {
			ip[0] += ip[i];
			sp[0] += sp[i];
			cure[0] += cure[i];
			dead[0] += dead[i];
		}
	}

	public void writeData(String path) {
		try {
			FileOutputStream outputStream = new FileOutputStream(path);   
	    OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream, "utf-8");
	    BufferedWriter writer = new BufferedWriter(outputWriter);
	    String out;

	    for(int i = 0; i < PROVIENCE_NUM; i++) {
	    	if(flg[i] == true && flg4type == false) {
	    		writer.write("\n");
	    		out = provience_array[i] + "��Ⱦ����" + ip[i] + "�� ���ƻ���" + sp[i] + 
	    				"�� ����" + cure[i] + "�� ����" + dead[i] + "��";
	    		writer.write("out");
	    	} else if(flg[i] == true && flg4type == true) {
	    		writer.write("\n");
	    		out = provience_array[i];
	    		for(int j = 0; j < cmd4type.size(); j ++) {
	    			if(cmd4type.get(j).equals("ip")) {
	    				out += " ��Ⱦ����" + ip[i] + "��";
	    			} else if(cmd4type.get(j).equals("sp")) {
	    				out += " ���ƻ���" + sp[i] + "��";
	    			} else if(cmd4type.get(j).equals("cure")) {
	    				out += " ����" + cure[i] + "��";
	    			} else if(cmd4type.get(j).equals("dead")) {
	    				out += " ����" + dead[i] + "��";
	    			} 
	    		}
	    	}
	    }
	    writer.write("\n");
	    writer.write("// ���ĵ�������ʵ���ݣ���������ʹ��");
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void printData() {
		String out;
    for(int i = 0; i < PROVIENCE_NUM; i++) {
    	if(flg[i] == true && flg4type == false) {
    		out = provience_array[i] + "��Ⱦ����" + ip[i] + "�� ���ƻ���" + sp[i] + 
    				"�� ����" + cure[i] + "�� ����" + dead[i] + "��";
    		System.out.println(out);
    	} else if(flg[i] == true && flg4type == true) {
    		out = provience_array[i];
    		for(int j = 0; j < cmd4type.size(); j ++) {
    			if(cmd4type.get(j).equals("ip")) {
    				out += " ��Ⱦ����" + ip[i] + "��";
    			} else if(cmd4type.get(j).equals("sp")) {
    				out += " ���ƻ���" + sp[i] + "��";
    			} else if(cmd4type.get(j).equals("cure")) {
    				out += " ����" + cure[i] + "��";
    			} else if(cmd4type.get(j).equals("dead")) {
    				out += " ����" + dead[i] + "��";
    			} 
      		System.out.println(out);
    		}
    		
    	}
    }
	}
	
	public static void main(String[] args) {
		InfectStatistic IS = new InfectStatistic(args.length, args);
		IS.initArgument();
		IS.statistic();
	}
}
