import java.io.*;
import java.util.*;
/**
 * InfectStatistic
 * TODO
 *
 * @author 181700141_���ж���A��
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
                System.out.println(e);
            }

        } else {
            System.out.println("������" + args[0] + "ָ��");
        }

    }
}

//����list����
class ListCommand {
  // ������������������ļ���
  private List<AbstractListHandler> handlers = new ArrayList<AbstractListHandler>();

  /**
   * -log ָ����־Ŀ¼��λ�ã�����ػḽ������ֱ��ʹ�ô����·�����������Լ�����·�� ��
   * 
   * -out ָ������ļ�·�����ļ���������ػḽ������ֱ��ʹ�ô����·�����������Լ�����·�� ��
   * 
   * -date ָ�����ڣ���������Ĭ��Ϊ���ṩ��־���µ�һ�졣����Ҫȷ���㴦����ָ������֮ǰ������log�ļ���
   * 
   * -type ��ѡ��[ip�� infection patients ��Ⱦ���ߣ�sp�� suspected patients ���ƻ��ߣ�cure������
   * ��dead����������]�� ʹ����дѡ���� -type ip ��ʾֻ�г���Ⱦ���ߵ������-type sp cure��ᰴ˳��sp,
   * cure���г����ƻ��ߺ��������ߵ������ ��ָ������Ĭ�ϻ��г����������
   * 
   * -province ָ���г���ʡ����-province ��������ֻ�г�������-province ȫ�� �㽭��ֻ���г�ȫ�����㽭��
   * 
   * ����Ĳ�����������ʶ�Ƿ��Ѿ���������Ӧ�Ĳ��������ࡣ
   */
  private boolean logIsExist = false;
  private boolean outIsExist = false;
  private boolean dateIsExist = false;
  private boolean typeIsExist = false;
  private boolean provinceIsExist = false;

  /**
   * ����list����ĸ��������Ը���������ʼ���䴦���ࡣ
   * 
   * @param �û�����������list
   * @throws �����������list�����磺list����δ�ṩ�ò�����ִ�з��� ͬһ�������ֶ�Σ�������ڵĲ���������ʱ���׳�IllegalException
   */
  public void dealParameter(String[] args) throws IllegalException {
      int l = args.length;
      // �洢����ֵ
      String[] parameterValues;
      for (int i = 1; i < l; i++) {
          switch (args[i]) {
          case "-log":
              if (logIsExist)
                  throw new IllegalException("�����ظ����� -log����");
              if (i == l - 1 || args[i + 1].charAt(0) == '-')
                  throw new IllegalException("����δ�ṩ-log����ֵ");
              // ��������log�����Ķ���
              logIsExist = true;
              parameterValues = new String[1];
              parameterValues[0] = args[++i];
              handlers.add(new LogHandler(parameterValues));
              break;
          case "-out":
              if (outIsExist)
                  throw new IllegalException("�����ظ�����-out����");
              if (i == l - 1 || args[i + 1].charAt(0) == '-')
                  throw new IllegalException("����δ�ṩ-out����ֵ");
              // ��������out�����Ķ���
              outIsExist = true;
              parameterValues = new String[1];
              parameterValues[0] = args[++i];
              handlers.add(new OutHandler(parameterValues));
              break;
          case "-date":

              break;
          case "-type":

              break;
          case "-province":

              break;
          default:
              if (args[i].charAt(0) == '-')
                  throw new IllegalException("����list���֧��" + args[i] + "����");
              throw new IllegalException("���󣬲���ֵ" + args[i] + "�Ƿ�");
          }// end of switch
      } // end of for
      if (!logIsExist && !outIsExist)
          throw new IllegalException("���󣬲���-log��-outҪ��������");
  }

  // ִ�и�������Ҫ��Ĳ���
  public void carryOutActions() throws Exception {
      for (AbstractListHandler handler : handlers) {
          handler.handle();
      }
  }

}

//����list�������������Ļ���
abstract class AbstractListHandler {
  // ��¼����ֵ
  protected String[] parameterValues;
  // ��һ��������
  protected AbstractListHandler nextHandler;

  // �жϲ���ֵ�Ƿ�Ϸ�
  // public abstract boolean isLegal();
  // ����ò�����Ҫ��Ĳ���
  public abstract void handle() throws Exception;
}

/**
* ����list����log��������
*/
class LogHandler extends AbstractListHandler {
  public LogHandler(String[] tParameterValues) {
      parameterValues = tParameterValues;
  }

  /**
   * ����-log������Ҫ��Ĳ���
   * 
   * @throws Exception
   */
  public void handle() throws Exception {
      File file = new File(parameterValues[0]);
      if (!file.exists() && file.isDirectory())
          throw new IllegalException("���󣬸���־Ŀ¼������");
  }
}

