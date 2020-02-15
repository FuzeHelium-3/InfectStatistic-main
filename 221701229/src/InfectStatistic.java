/**
 * InfectStatistic
 * TODO
 *
 * @author 221701229
 * @version 1.0
 * @since 2020
 */
import sun.rmi.log.LogHandler;

import java.io.*;
import java.util.*;
import java.util.regex.*;

class InfectStatistic {
    public static void main(String[] args) {
                /*String s="list -date 2020-01-22 -log D:/log/ -out D:/output.txt";
                String[] sl=s.split("\\s+");
                for (String ss:sl)
                {
                    System.out.println(ss);
                }
                CommandManager cm=new CommandManager(sl);
                cm.getArguments();*/


        }
    }


/**
 *命令接口
 */
interface Command{

     void execute(String[] args);

     String getCmdName();
}


/**
 *不同类型日志行
 */
class LogLine {

    private Pattern reg;
    private String logline;
    LogLine(String arg)
    {
        logline=arg;
        reg=Pattern.compile(arg);
    }

    public Pattern getReg() {
        return reg;
    }

    public String getLogline() {
        return logline;
    }
}

/**
 * 解析命令行命令
 */
class CommandManager {
    private String[] args;  //传入的命令行数组
    private Command cmd;  //当前解析出的命令
    private List<Command> CommandList=new ArrayList<>();  //支持的命令队列
    private List<List> Arguments=new ArrayList<>();  //命令的各参数及其选项的队列

    CommandManager(String[] args) {
        this.args=args;
        CommandList.add(new list());
    }

    //添加新的命令
    public void addCommand(Command cmd)
    {
        CommandList.add(cmd);
    }

    //获取命令
    public void setCommand()
    {
        //match表示开头找到的命令是否存在
        boolean match=false;
        //遍历允许的命令列表
        Iterator it=CommandList.iterator();
        while (it.hasNext())
        {
            cmd=(Command)it.next();
            //迭代判断输入的命令是否跟支持的命令匹配
            if(args[0].equals(cmd.getCmdName()))
            {
                match=true;
                break;
            }
        }
        if(match)
        {
            //执行命令
            cmd.execute(args);
        }
        else
        {
            /*
            *
            *错误输出
            *
            */
        }
    }

    public Command getCommand()
    {
        return cmd;
    }

    //解析出参数和对应的选项
    public void setArguments()
    {
        int len=args.length;//命令解析数组长度
        for(int i=1;i<len;)
        {
            //找到以'-'开头的命令
            if(args[i].startsWith("-"))
            {
                List<String> arg=new ArrayList<>();
                //System.out.println("find arg"+args[i]);
                //把参数加入给每个参数创建的数组
                arg.add(args[i]);
                i++;
                //把对应参数的所有选项加入对应数组
                while (i<len&&!args[i].startsWith("-"))
                {
                    //System.out.println("find opt"+args[i]);
                    arg.add(args[i]);
                    i++;
                }

                //把参数及选项队列加入
                Arguments.add(arg);
            }
        }
    }

    public List getArguments()
    {
        return Arguments;
    }

}

/**
 命令的解析与调用执行
 * */
class CommandInvoker{
    String[] args;
        //从初始命令中解析出字符串数组
        public static void getCommandArgs(String cmd)
        {
            String [] spString = cmd.split("\\s+");
        }
        CommandManager cm=new CommandManager(args);

}

/**
 list 命令的实现
 * */
class list implements Command{
    final String cmdname="list";
    private String log=null;  //日志文件夹路径
    private String out=null;  //输出文件路径
    private HashMap<String,Integer> infected;
    private HashMap<String,Integer> suspected;
    private HashMap<String,Integer> cured;
    private HashMap<String,Integer> died;
    private LogHandle handler;
    list()
    {
        handler=new LogHandle();
        infected=new HashMap<>();
        suspected=new HashMap<>();
        cured=new HashMap<>();
        died=new HashMap<>();
    }

    public void execute(String[] args)
    {
        CommandManager cm=new CommandManager(args);
        cm.setArguments();
        List Arguments=cm.getArguments();
        Iterator it=Arguments.iterator();
        //提取两个必需的参数
        while (it.hasNext())
        {
            if(((ArrayList)it.next()).get(0).equals("-log"))
            {
                log=(String) ((ArrayList)it.next()).get(1);
                it.remove();
            }
            else if(((ArrayList)it.next()).get(0).equals("-out"))
            {
                out=(String) ((ArrayList)it.next()).get(1);
                it.remove();
            }
        }
        if(log==null||out==null)
        {
            /*
            *
            * 必要参数缺失错误
            *
            * */
        }

        File logFiles=new File(log);
        if(!logFiles.exists())
        {
            /*
            * 文件不存在错误
            *
            * */
        }
        File[] files=logFiles.listFiles();
        //遍历提供的文件夹下的日志
        for (File f:files)
        {
            try
            {
                FileReader fr=new FileReader(f.getName());
                BufferedReader br=new BufferedReader(fr);
                while (br.readLine()!=null)
                {
                    //把读出来的日志行交给下面处理
                    handler.logHandlerList(br.readLine(),infected,suspected,cured,died);

                }
                br.close();
            }
            catch (FileNotFoundException fe)
            {
                System.out.println("file not found");
                System.exit(1);
            }
            catch (IOException ie)
            {
                System.out.println("null content");
            }
        }

    }

    public String getCmdName()
    {
        return cmdname;
    }
}

/**
 对日志的处理
 * */
class LogHandle{
    private ArrayList<MyPatterns> pat=new ArrayList<>();
    private MyPatterns mypattern;
    //命令的处理
    LogHandle()
    {
        pat.add(new InfectedPatients());
        pat.add(new SuspectedPatients());
        pat.add(new InfectedGo());
        pat.add(new SuspectedGo());
        pat.add(new PatientsDie());
        pat.add(new PatientsCure());
        pat.add(new SuspectedDiagnosis());
        pat.add(new SuspectedExclude());
    }
    public void logHandlerList(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        Iterator it=pat.iterator();
        while(it.hasNext())
        {
            mypattern=(MyPatterns)it.next();
            //根据日志行跟哪个类的正则匹配选择类执行统计
            if(line.matches(mypattern.getReg()))
            {
                mypattern.doCount(line,infected,suspected,cured,died);
                break;
            }
        }
    }

}

abstract class MyPatterns
{
    String InfectedPatients="([\\u4e00-\\u9fa5])+ 新增 感染患者 (\\d+)人";//<省> 新增 感染患者 n人
    String SuspectedPatients="([\\u4e00-\\u9fa5])+ 新增 疑似患者 (\\d+)人";//<省> 新增 疑似患者 n人
    String InfectedGo="([\\u4e00-\\u9fa5])+ 感染患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 感染患者 流入 <省2> n人
    String SuspectedGo="([\\u4e00-\\u9fa5])+ 疑似患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 疑似患者 流入 <省2> n人
    String PatientsDie="([\\u4e00-\\u9fa5])+ 死亡 (\\d+)人";//<省> 死亡 n人
    String PatientsCure="([\\u4e00-\\u9fa5])+ 治愈 (\\d+)人";//<省> 治愈 n人
    String SuspectedDiagnosis="([\\u4e00-\\u9fa5])+ 疑似患者 确诊感染 (\\d+)人";//<省> 疑似患者 确诊感染 n人
    String SuspectedExclude="([\\u4e00-\\u9fa5])+ 排除 疑似患者 (\\d+)人";//<省> 排除 疑似患者 n人
    abstract void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died);
    abstract String getReg();
}

class InfectedPatients extends   MyPatterns{
    public String reg="([\\u4e00-\\u9fa5])+ 新增 感染患者 (\\d+)人";//<省> 新增 感染患者 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int sum;
        //获取对应省份的感染人数
        if(infected.containsKey(str[0]))
        {
            sum=infected.get(str[0]);
        }
        else
        {
            infected.put(str[0],0);
            sum=0;
        }
        //把日志行的新增人数加入总数
        sum+=Integer.parseInt(str[3].substring(0,str[3].indexOf("人")));
        infected.put(str[0],sum);
    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedPatients extends MyPatterns{
    public String reg="([\\u4e00-\\u9fa5])+ 新增 疑似患者 (\\d+)人";//<省> 新增 疑似患者 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int sum;
        //获取对应省份的疑似人数
        if(suspected.containsKey(str[0]))
        {
            sum=suspected.get(str[0]);
        }
        else
        {
            suspected.put(str[0],0);
            sum=0;
        }
        //把日志行的新增人数加入总数
        sum+=Integer.parseInt(str[3].substring(0,str[3].indexOf("人")));
        suspected.put(str[0],sum);
    }

    public String getReg()
    {
        return reg;
    }
}

class InfectedGo extends MyPatterns{
    //private Pattern patt=Pattern.compile(InfectedGo);
    public String reg="([\\u4e00-\\u9fa5])+ 感染患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 感染患者 流入 <省2> n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=infected.get(str[0])-Integer.parseInt(str[4].substring(0,str[4].indexOf("人")));//流动人数
        //流出省份感染人数相应减少
        infected.put(str[0],infected.get(str[0])-change);
        //流入省份感染人数相应增加
        if(infected.containsKey(str[3]))
        {
            infected.put(str[3],change+infected.get(str[3]));
        }
        else
        {
            infected.put(str[3],change);
        }
    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedGo extends MyPatterns{
    //private Pattern patt=Pattern.compile(SuspectedGo);
    public String reg="([\\u4e00-\\u9fa5])+ 疑似患者 流入 ([\\u4e00-\\u9fa5])+ (\\d+)人";//<省1> 疑似患者 流入 <省2> n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");
        int change=suspected.get(str[0])-Integer.parseInt(str[4].substring(0,str[4].indexOf("人")));//流动人数
        //流出省份疑似人数相应减少
        suspected.put(str[0],suspected.get(str[0])-change);
        //流入省份疑似人数相应增加
        if(suspected.containsKey(str[3]))
        {
            suspected.put(str[3],change+suspected.get(str[3]));
        }
        else
        {
            suspected.put(str[3],change);
        }
    }

    public String getReg()
    {
        return reg;
    }
}

class PatientsDie extends MyPatterns{
    //private Pattern patt=Pattern.compile(PatientsDie);
    public String reg="([\\u4e00-\\u9fa5])+ 死亡 (\\d+)人";//<省> 死亡 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {
        String[] str=line.split("\\s+");




    }

    public String getReg()
    {
        return reg;
    }
}

class PatientsCure extends MyPatterns{
    //private Pattern patt=Pattern.compile(PatientsCure);
    public String reg="([\\u4e00-\\u9fa5])+ 治愈 (\\d+)人";//<省> 治愈 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {



    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedDiagnosis extends MyPatterns{
    //private Pattern patt=Pattern.compile(SuspectedDiagnosis);
    public String reg="([\\u4e00-\\u9fa5])+ 疑似患者 确诊感染 (\\d+)人";//<省> 疑似患者 确诊感染 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {



    }

    public String getReg()
    {
        return reg;
    }
}

class SuspectedExclude extends MyPatterns{
   // private Pattern patt=Pattern.compile(SuspectedExclude);
   public String reg="([\\u4e00-\\u9fa5])+ 排除 疑似患者 (\\d+)人";//<省> 排除 疑似患者 n人
    //根据
    public void doCount(String line,HashMap<String,Integer> infected,HashMap<String,Integer> suspected,HashMap<String,Integer> cured,HashMap<String,Integer> died)
    {



    }

    public String getReg()
    {
        return reg;
    }
}
