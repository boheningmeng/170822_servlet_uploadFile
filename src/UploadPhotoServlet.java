import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 
public class UploadPhotoServlet extends HttpServlet {
 
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        String filename = null;
        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            factory.setSizeThreshold(1024*1024);
            List items = null;
            try{
                items = upload.parseRequest(request);
            }catch (FileUploadException e){
                e.printStackTrace();
            }
          Iterator iterator = items.iterator();
          while(iterator.hasNext()) {
              FileItem fileItem = (FileItem) iterator.next();
              // isFormField���������ж�FileItem������װ��������һ����ͨ�ı����ֶΣ�����һ���ļ����ֶΣ��������ͨ���ֶ��򷵻�true��
              //���򷵻�false����ˣ�����ʹ�ø÷����ж��Ƿ�Ϊ��ͨ���򣬻����ļ��ϴ�����
              if (!fileItem.isFormField()) {
                  filename = System.currentTimeMillis() + ".jpg";
                  String photoFolder = "e:\\project\\j2ee\\web\\uploaded";
                  //�����ļ����ļ�·�����ļ���
                  //File f=new File(dir,fileName); �����dir·�����ַ�����fileName·�����ַ�������һ���� Fileʵ����
                  // Ҳ����˵����ΪfileName��ʾ���Ǹ��ļ�, ��Java��ΪfileName�����Ǹ��ļ���Ŀ¼·����
                  //������������f.mkdirs()���ͻᴴ��f��ʾ��·����ָ����Ŀ¼(����dir��fileName)����Ϊ����ȷ��Ҫ����Ŀ¼��
                  File file = new File(photoFolder, filename);
                  file.getParentFile().mkdirs();
                  // FileItem.getInputStream ��������ʽ�����ϴ��ļ����������ݡ�
                  InputStream is = fileItem.getInputStream();
                  //FileInputStream ���ڶ�ȡ�����ļ��е��ֽ����ݣ��̳���InputStream��
                  //FileOutputStream���ڽ��ֽ�����д�����ļ����̳���OutputStream��
                  //    ����һ����ָ�� File �����ʾ���ļ���д�����ݵ��ļ��������
                  FileOutputStream fos = new FileOutputStream(file);
                  byte b[] = new byte[1024 * 1024];
                  int length = 0;
                  while (-1 != (length = is.read(b))) {
                      fos.write(b, 0, length);
                  }
                  fos.close();
              } else {
                  System.out.println(fileItem.getFieldName());
                  String value = fileItem.getString();
                  value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                  System.out.println(value);
              }


              String html = "<img width='200' height='150' src='uploaded/%s' />";
              response.setContentType("text/html");
              PrintWriter pw = response.getWriter();

              pw.format(html, filename);
          }
 
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}