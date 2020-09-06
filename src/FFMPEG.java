import java.io.File;
import java.util.List;

/**  
 * @Title: FFMPEG.java
 * @Package 
 * @author doublexi
 * @date 2020年7月26日
 * @version V1.0  
 */

/**
 * @ClassName: FFMPEG
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author doublexi
 * @date 2020年7月26日
 * @since JDK 1.8
 */
public class FFMPEG {
	/**
     * 获得视频缩略图，获取成功返回true，获取失败返回false
     * @param ffmpegPath  是ffmpeg.exe存放的路径
     * @param path   是视频文件的存放路径
     * @param outImagePath 输出缩略图的保存路径
     * @return
     */
    public static boolean getVideoImage(String path,String outImagePath) {
    	String ffmpegPath="ffmpeg/bin/ffmpeg.exe";
    	 path="video/1.mp4";
    	 outImagePath="image/1.jpg";
        File file = new File(path);
        if (!file.exists()) {//判断视频文件是否存在
            System.err.println("路径[" + path + "]对应的视频文件不存在!");
            return false;
        }
        //设置参数
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpegPath);//这里设置ffmpeg.exe存放的路径
        commands.add("-ss");
        commands.add("0");//这里设置的是要截取视频开始播放多少秒后的图，可以自己设置时间
        commands.add("-i");
        commands.add(path);//这里是设置要截取缩略图的视频的路径
        commands.add("-y");
        commands.add("-f");
        commands.add("image2");
        
//        commands.add("-t");
//        commands.add("5");
//        commands.add("-s");
//        commands.add("1920x1080");//这里设置输出图片的大小
        commands.add("image/%d.jpg");//这里设置输出的截图的保存路径
     
        try {
            //截取缩略图并保存 
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
