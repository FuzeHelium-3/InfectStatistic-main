/**
 * InfectStatistic
 * TODO
 *
 * @author _LMG
 * @version xxx
 * @since xxx
 */
package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class InfectStatistic {
	//����ӿ�
    public interface Command{
        public void excute();
    }

    //��������
    public static class CmdCommand implements Command{
    	private final String s;
    	
    	public CmdCommand(String s) {
    		this.s = s;
    	}
    	@Override
    	public void excute() {
    		//���������
    		//ִ�������
    	}
    }
    
    
    
    //ִ����
    public static class Runner{
    	private Command command;
    	
    	public Runner(Command command) {
    		this.command = command;
    	}
    	public void Run() {
    		command.excute();
    	}
    }
    
    public static void main(String[] args) {
        for(String s : args){
            System.out.println(s+"\n");
            Command command = new CmdCommand(s);
            Runner runner = new Runner(command);
            runner.Run();
        }
        System.out.println('1');
    }
}
