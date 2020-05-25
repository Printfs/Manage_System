package com.hang.manage.system.util;

import com.hang.manage.system.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomCodeUtil {


    public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";  //放到resdis中的key
    private String randString = "0123456789";    //随机产生只有数字的字符串 private String
    //private String randString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生只有字母的字符串
    //private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生数字与字母组合的字符串
    private int width = 95;// 图片宽
    private int height = 25;// 图片高
    private int lineSize = 40;// 干扰线数量
    private int stringNum = 4;// 随机产生字符数量

    private static final Logger logger = LoggerFactory.getLogger(RandomCodeUtil.class);

    private Random random = new Random();

    String value;  // 产生的字符串

    /**
     * 获得字体
     */
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
    }

    /**
     * 获得颜色
     */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 生成随机图片
     */
    public void getRandcode(HttpServletRequest request, HttpServletResponse response) {

        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类，TYPE_INT_BGR整数像素的 8 位 RGB 颜色
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
       // 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        Graphics g = image.getGraphics();
        //1.图片大小
        g.fillRect(0, 0, width, height);
        //2.字体大小
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
        //3.字体颜色
        g.setColor(getRandColor(110, 133));
        //4.绘制干扰线,参数为两点的坐标 a(x,y) b(x,y)
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        //5.绘制随机字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            randomString = drowString(g, randomString, i);
        }

        value = randomString;
        //6.释放资源
        g.dispose();
        try {
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
            logger.error("将内存中的图片通过流动形式输出到客户端失败>>>>   ", e);
        }

    }

    public String key() {
        return RANDOMCODEKEY;
    }

    public String value() {
        return value;
    }

    /**
     * 绘制字符串
     */
    private String drowString(Graphics g, String randomString, int i) {

        g.setFont(getFont());  //字体
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
                .nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString
                .length())));
        randomString += rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13 * i, 16);
        return randomString;
    }

    /**
     * 绘制干扰线
     */
    private void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机的字符
     */
    public String getRandomString(int num) {
        return String.valueOf(randString.charAt(num));
    }
}
