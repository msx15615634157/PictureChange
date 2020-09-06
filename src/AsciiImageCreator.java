import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//字符
//' ', '`', '.', '^', ',', ':', '~', '"', '<', '!', 'c', 't', '+', '{', 'i', '7', '?','u', '3', '0', 'p', 'w', '4', 'A', '8', 'D', 'X', '%', '#', 'H', 'W', 'M',
//对应的字符
//0, 5, 7, 9, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43,45, 47, 49, 51, 53, 55, 59, 61, 63, 66, 68, 70
public class AsciiImageCreator {
    public static void create(File srcImgFile, File destAsciiImgFile) {
//       final String base = "@#&$%*o!;.";
    	
       final String base = "MWH#%XD8A4wp03u?7i{+tc!<\"~:,^.";
        String result = "";
        try {
            BufferedImage bufferedImage = ImageIO.read(srcImgFile);
            for (int i = 0; i < bufferedImage.getHeight(); i += 32) {
                for (int j = 0; j < bufferedImage.getWidth(); j += 16) {
                    int pixel = bufferedImage.getRGB(j, i); // 下面三行代码将一个数字转换为RGB数字
                    int red = (pixel & 0xff0000) >> 16;
                    int green = (pixel & 0xff00) >> 8;
                    int blue = (pixel & 0xff);
                    float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                    int index = Math.round(gray * (base.length() + 1) / 255);
                    result += index >= base.length() ? " " : String.valueOf(base.charAt(index));
                }
                result += "\r\n";
            }
            System.out.println(result);
            FileWriter fileWriter = new FileWriter(destAsciiImgFile);
            fileWriter.write(result);
            fileWriter.flush();
            fileWriter.close();
//            System.out.print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void create(String srcImgFile, String destAsciiImgFile) {
        create(new File(srcImgFile),new File(destAsciiImgFile));
    }
}