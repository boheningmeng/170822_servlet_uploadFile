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
              // isFormField方法用于判断FileItem类对象封装的数据是一个普通文本表单字段，还是一个文件表单字段，如果是普通表单字段则返回true，
              //否则返回false。因此，可以使用该方法判断是否为普通表单域，还是文件上传表单域。
              if (!fileItem.isFormField()) {
                  filename = System.currentTimeMillis() + ".jpg";
                  String photoFolder = "e:\\project\\j2ee\\web\\uploaded";
                  //创建文件，文件路径，文件名
                  //File f=new File(dir,fileName); 这根据dir路径名字符串和fileName路径名字符串创建一个新 File实例，
                  // 也就是说你认为fileName表示的是个文件, 而Java认为fileName可以是个文件和目录路径；
                  //所以这样调用f.mkdirs()，就会创建f表示的路径名指定的目录(包括dir和fileName)，因为你明确的要创建目录；
                  File file = new File(photoFolder, filename);
                  file.getParentFile().mkdirs();
                  // FileItem.getInputStream 以流的形式返回上传文件的数据内容。
                  InputStream is = fileItem.getInputStream();
                  //FileInputStream 用于读取本地文件中的字节数据，继承自InputStream类
                  //FileOutputStream用于将字节数据写出到文件。继承自OutputStream类
                  //    创建一个向指定 File 对象表示的文件中写入数据的文件输出流。
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