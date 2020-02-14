import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic TODO
 *
 * @author 181700141_呼叫哆啦A梦
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        int length = args.length;
        if (length == 0)
            return;
        else if (args[0].equals("list")) {
            ListCommand command = new ListCommand();
            try {
                command.dealParameter(args);
                command.carryOutActions();
            } catch (IllegalException e) {
                System.out.println(e);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }

        } else {
            System.out.println("不存在" + args[0] + "指令");
        }

    }
}

//当参数或参数值非法时将抛出该异常类
class IllegalException extends Exception {
    // 记录错误原因
    private String message;

    public IllegalException(String tMessage) {
        message = tMessage;
    }

    public String toString() {
        return message;
    }
}

/**
 * 
 * @author 181700141_呼叫哆啦A梦
 * 
 *         处理list命令
 * 
 *         -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径 。
 * 
 *         -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件。
 * 
 *         -type 可选择[ip： infection patients 感染患者，sp： suspected patients
 *         疑似患者，cure：治愈 ，dead：死亡患者]， 使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp
 *         cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况， 不指定该项默认会列出所有情况。
 * 
 *         -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江。
 */

class ListCommand {
    // 存储各省感染患者人数
    private LinkedHashMap<String, Integer> ipMap = new LinkedHashMap<>();
    // 存储各省疑似患者人数
    private LinkedHashMap<String, Integer> spMap = new LinkedHashMap<>();
    // 存储各省治愈患者人数
    private LinkedHashMap<String, Integer> cureMap = new LinkedHashMap<>();
    // 存储各省死亡人数
    private LinkedHashMap<String, Integer> deadMap = new LinkedHashMap<>();
    // 存储全国情况
    private LinkedHashMap<String, Integer> countryMap = new LinkedHashMap<>();

    // 记录日志目录路径
    private String inDirectory = null;
    // 记录输出目录路径
    private String outDirectory = null;
    // 记录日期：date的参数值
    private String date = null;
    // 记录type的值，如果集合为空，默认列出全部情况
    private HashSet<String> type = new HashSet<String>();
    // 记录要列出的省
    private HashSet<String> provinces = new HashSet<String>();

    private boolean dateIsExist = false;
    private boolean typeIsExist = false;
    private boolean provinceIsExist = false;

    // 定义正则表达式，表达式内的空格不可随意修该，否则会影响读取处理
    String s1 = "\\s*\\S+ 新增 感染患者 \\d+人\\s*";
    String s2 = "\\s*\\S+ 新增 疑似患者 \\d+人\\s*";
    String s3 = "\\s*\\S+ 感染患者 流入 \\S+ \\d+人\\s*";
    String s4 = "\\s*\\S+ 疑似患者 流入 \\S+ \\d+人\\s*";
    String s5 = "\\s*\\S+ 死亡 \\d+人\\s*";
    String s6 = "\\s*\\S+ 治愈 \\d+人\\s*";
    String s7 = "\\s*\\S+ 疑似患者 确诊感染 \\d+人\\s*";
    String s8 = "\\s*\\S+ 排除 疑似患者 \\d+人\\s*";

    public ListCommand() {

        String[] provinces = { "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南",
                "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江" };
        for (int i = 0; i < provinces.length; i++) {
            ipMap.put(provinces[i], 0);
            spMap.put(provinces[i], 0);
            cureMap.put(provinces[i], 0);
            deadMap.put(provinces[i], 0);
        }
        countryMap.put("感染患者", 0);
        countryMap.put("疑似患者", 0);
        countryMap.put("治愈", 0);
        countryMap.put("死亡", 0);
    }

    /**
     * 处理list命令的各参数，对各个参数初始化其处理类。
     * 
     * @param 用户输入的命令，含list
     * @throws 如果初步解析list命令如：list命令未提供该参数的执行方法 同一参数出现多次，必须存在的参数不存在时将抛出IllegalException
     */
    public void dealParameter(String[] args) throws IllegalException {
        int l = args.length;
        // 存储参数值
        String[] parameterValues;
        for (int i = 1; i < l; i++) {
            switch (args[i]) {
            case "-log":
                if (inDirectory != null)
                    throw new IllegalException("错误，重复出现 -log参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-log参数值");
                // 给日志目录路径赋值
                inDirectory = args[++i];
                break;
            case "-out":
                if (outDirectory != null)
                    throw new IllegalException("错误，重复出现-out参数");
                if (i == l - 1 || args[i + 1].charAt(0) == '-')
                    throw new IllegalException("错误，未提供-out参数值");
                // 给输出目录路径赋值
                outDirectory = args[++i];
                break;
            case "-date":
                if (dateIsExist)
                    throw new IllegalException("错误，重复出现-date参数");
                if (i < l - 1 && args[i + 1].charAt(0) != '-')
                    date = args[++i];
                if (date != null && !date.matches("\\d{4}-\\d{2}-\\d{2}"))
                    throw new IllegalException("错误，-date参数值非法，须符合XXXX-XX-XX格式（X为0-9）");
                dateIsExist = true;
                break;
            case "-type":
                if (typeIsExist)
                    throw new IllegalException("错误，重复出现-type参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    if (type.contains(args[j]))
                        throw new IllegalException("错误，参数-type出现重复参数值");
                    i = j;
                    type.add(args[j]);
                }
                typeIsExist = true;
                break;
            case "-province":
                if (provinceIsExist)
                    throw new IllegalException("错误，重复出现-province参数");
                for (int j = i + 1; j < l; j++) {
                    if (args[j].charAt(0) == '-')
                        break;
                    if (provinces.contains(args[j]))
                        throw new IllegalException("错误，参数-province出现重复参数值");
                    i = j;
                    provinces.add(args[j]);
                }
                provinceIsExist = true;
                break;
            default:
                if (args[i].charAt(0) == '-')
                    throw new IllegalException("错误，list命令不支持" + args[i] + "参数");
                throw new IllegalException("错误，参数值" + args[i] + "非法");
            }// end of switch
        } // end of for
        if (outDirectory == null || inDirectory == null)
            throw new IllegalException("错误，参数-log及-out要求必须存在");
    }

    // 执行各参数所要求的操作
    public void carryOutActions() throws Exception {

        /*
         * System.out.println("log:" + inDirectory + "\nout:" + outDirectory + "\ndate:"
         * + date + "\ntype:"); for (String s : type) System.out.print("  " + s);
         * System.out.println("\nprovince:\n"); for (String s : province)
         * System.out.print("  " + s);
         */

        File file = new File(inDirectory);
        if (!file.exists() || !file.isDirectory())
            throw new IllegalException("错误，日志目录" + inDirectory + "不存在");
        handleFiles(file);

    }

    // 读取目录下的日志文件
    public void handleFiles(File file) throws Exception {
        String[] logFiles = file.list();
        Arrays.sort(logFiles);
        int l = logFiles.length;
        if (date != null && date.compareTo(logFiles[l - 1]) > 0)
            throw new IllegalException("错误，日期非法，超出日志最晚时间");
        if (date == null)
            date = logFiles[l - 1];
        else
            date = date + ".log.txt";
        for (int i = 0; i < l; i++) {
            if (logFiles[i].matches("\\S+\\.log\\.txt") && date.compareTo(logFiles[i]) >= 0) {
                handleFile(inDirectory + "/" + logFiles[i]);
            }
        }

    }

    /**
     * 读取日志文件，更新相应人数
     * 
     * @param route 待读取日志文件路径
     * @throws Exception
     */
    public void handleFile(String route) throws Exception {
        FileInputStream fstream = new FileInputStream(new File(route));
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            if (strLine.matches(s1)) {
                int index = strLine.indexOf(" 新增 感染患者");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出感染人数
                int sum = getAmount(strLine);
//////////////////////////////////////////////////////////////////////////////
                System.out.println("\n" + strLine);
                System.out.println(province + "原感染人数：" + ipMap.get(province) + "  " + "新增感染患者 " + sum + "人");
/////////////////////////////////////////////////////////////////////////////
                // 修改人数
                sum += ipMap.get(province);
                ipMap.put(province, sum);

//////////////////////////////////////////////////////////////////////////////
                System.out.println(province + "最新感染患者 " + ipMap.get(province) + "人");
/////////////////////////////////////////////////////////////////////////////
            } else if (strLine.matches(s2)) {
                int index = strLine.indexOf(" 新增 疑似患者");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出疑似患者人数
                int sum = getAmount(strLine);
//////////////////////////////////////////////////////////////////////////////
                System.out.println("\n" + strLine);
                System.out.println(province + "原疑似人数：" + spMap.get(province) + "  " + "新增感染患者 " + sum + "人");
/////////////////////////////////////////////////////////////////////////////                

                // 修改人数
                sum += spMap.get(province);
                spMap.put(province, sum);

//////////////////////////////////////////////////////////////////////////////
                System.out.println(province + "最新疑似患者 " + spMap.get(province) + "人");
/////////////////////////////////////////////////////////////////////////////
            } else if (strLine.matches(s3)) {
                // String s3 = "\\s*\\S+ 感染患者 流入 \\S+ \\d+人\\s*";
                int index = strLine.indexOf(" 感染患者 流入");
                // 取出流出省份，省份前面可能有空格
                String outProvince = strLine.substring(0, index);
                // 去掉全部空格
                outProvince.replace(" ", "");

                // 取出流出人数
                int sum = getAmount(strLine);
                index = strLine.indexOf(Integer.toString(sum));
                // 取出流入省份
                String inProvince = strLine.substring(strLine.lastIndexOf("流入") + 3, index-1);
////////////////////////////////////////////////////////////////////////////////////
                System.out.println("\n" + strLine);
                System.out.println("输出省" + outProvince + "原感染人数" + ipMap.get(outProvince) + "  输入省" + inProvince
                        + "原感染人数：" + ipMap.get(inProvince) + " 流入人口" + sum);
//////////////////////////////////////////////////////////////////////////////////////                

                ipMap.put(outProvince, ipMap.get(outProvince) - sum);
                ipMap.put(inProvince, ipMap.get(inProvince) + sum);

////////////////////////////////////////////////////////////////////////////////////////
                System.out.println("输出省" + outProvince + "现感染人数为：" + ipMap.get(outProvince) + "  输入省" + inProvince
                        + "现感染人数：" + ipMap.get(inProvince));
////////////////////////////////////////////////////////////               
            } else if (strLine.matches(s4)) {
                // String s4 = "\\s*\\S+ 疑似患者 流入 \\S+ \\d+人\\s*";
                int index = strLine.indexOf(" 疑似患者 流入");
                // 取出流出省份，省份前面可能有空格
                String outProvince = strLine.substring(0, index);
                // 去掉全部空格
                outProvince.replace(" ", "");

                // 取出流出人数
                int sum = getAmount(strLine);
                index = strLine.indexOf(Integer.toString(sum));
                // 取出流出省份
                String inProvince = strLine.substring(strLine.lastIndexOf("流入") + 3, index-1);
////////////////////////////////////////////////////////////////////////////////////
                System.out.println("\n" + strLine);
                System.out.println("输出省" + outProvince + "原疑似人数" + spMap.get(outProvince) + "  输入省" + inProvince
                        + "原疑似人数：" + spMap.get(inProvince) + "  流入人口" + sum);
//////////////////////////////////////////////////////////////////////////////////////
                spMap.put(outProvince, spMap.get(outProvince) - sum);
                spMap.put(inProvince, spMap.get(inProvince) + sum);

////////////////////////////////////////////////////////////////////////////////////////
                System.out.println("输出省" + outProvince + "现疑似人数为：" + spMap.get(outProvince) + "  输入省" + inProvince
                        + "疑似染人数：" + spMap.get(inProvince));
////////////////////////////////////////////////////////////

            } else if (strLine.matches(s5)) {
                int index = strLine.indexOf(" 死亡");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出死亡人数
                int deadSum = getAmount(strLine);
                // 获得感染人数
                int ipSum = ipMap.get(province);
////////////////////////////////////////////////////////////////////////////////////
System.out.println("\n" + strLine);
System.out.println(province+"原感染人数："+ipMap.get(province)+"  原死亡人数："+deadMap.get(province)+"  现死亡："+deadSum);
////////////////////////////////////////////////////////////
                // 更新感染人数
                ipMap.put(province, ipSum - deadSum);

                deadSum += deadMap.get(province);
                deadMap.put(province, deadSum);
///////////////////////////////////////////////////////////
                System.out.println(province+"现感染人数："+ipMap.get(province)+"  现死亡人数："+deadMap.get(province));
////////////////////////////////////////////////////////////
            } else if (strLine.matches(s6)) {
                // s6 = "\\s*\\S+ 治愈 \\d+人\\s*";
                int index = strLine.indexOf(" 治愈");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                // 取出治愈人数
                int cureSum = getAmount(strLine);
                // 获得感染人数
                int ipSum = ipMap.get(province);
                
////////////////////////////////////////////////////////////////////////////////////
System.out.println("\n" + strLine);
System.out.println(province+"原感染人数："+ipMap.get(province)+"  原治愈人数："+cureMap.get(province)+"  现治愈："+cureSum);
////////////////////////////////////////////////////////////
                // 更新感染人数
                ipMap.put(province, ipSum - cureSum);
                cureSum += cureMap.get(province);
                cureMap.put(province, cureSum);
///////////////////////////////////////////////////////////
System.out.println(province+"现感染人数："+ipMap.get(province)+"  现治愈人数："+cureMap.get(province));
////////////////////////////////////////////////////////////

            } else if (strLine.matches(s7)) {
                // String s7 = "\\s*\\S+ 疑似患者 确诊感染 \\d+人\\s*";
                int index = strLine.indexOf(" 疑似患者 确诊感染");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");
                int ipSum = getAmount(strLine);
////////////////////////////////////////////////////////////////////////////////////
System.out.println("\n" + strLine);
System.out.println(province+"原疑似患者人数："+spMap.get(province)+"  原感染人数："+ipMap.get(province)+" 现确诊："+ipSum);
//////////////////////////////////////////////////////////////////////////////
                spMap.put(province, spMap.get(province) - ipSum);
                ipMap.put(province, ipMap.get(province) + ipSum);
///////////////////////////////////////////////////////////////////////////////////
                System.out.println(province+"现疑似人数："+spMap.get(province)+" 现感染人数："+ipMap.get(province));
/////////////////////////////////////////////////////////////////////////////////////
            } else if (strLine.matches(s8)) {
                // String s8 = "\\s*\\S+ 排除 疑似患者 \\d+人\\s*";
                int index = strLine.indexOf(" 排除 疑似患者");
                // 取出省份，省份前面可能有空格
                String province = strLine.substring(0, index);
                // 去掉全部空格
                province.replace(" ", "");

                int excludeSum = getAmount(strLine);
////////////////////////////////////////////////////////////////////////////////////
System.out.println("\n" + strLine);
System.out.println(province+"原疑似患者人数："+spMap.get(province)+" 现排除："+excludeSum);
//////////////////////////////////////////////////////////////////////////////
                spMap.put(province, spMap.get(province) - excludeSum);
///////////////////////////////////////////////////////////////////////////////////
System.out.println(province+"现疑似人数："+spMap.get(province));
/////////////////////////////////////////////////////////////////////////////////////
            }
        }
    }

    /**
     * 取出s里面的数字，s里面仅有一处地方有数字且是整数
     * 
     * @param s：满足handleFile函数中正则表达式s1-s8的字符串
     * @return 返回s里面的整数
     */
    public int getAmount(String s) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        m.find();
        return Integer.parseInt(s.substring(m.start(), m.end()));
    }

}
