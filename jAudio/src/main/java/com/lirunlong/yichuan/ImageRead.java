package com.lirunlong.yichuan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageRead {
    public static void main(String[] s) {
        System.out.println("test yichuan ..");
        TestDrawShell();
    }

    public static void Read() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        try {
            // 通过ImageIO类读取文件
            img = ImageIO.read(new File("./out/1.jpg"));
            img.getRGB(1, 2);

            // 获取图片信息
            System.out.println(img.getHeight() + " " + img.getWidth() + " ");

            OutputStream out = new FileOutputStream(new File("C:/file/2.jpg"));

            ImageIO.write(img, "jpg", out); // 将图片写入指定文件
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DrawInMemery(){
        try {
            BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_BGR);
            DrawTriagleToBuffer(img,100);
            OutputStream out = new FileOutputStream(new File("./2.jpg"));
            ImageIO.write(img, "jpg", out); 
            System.out.println("img writed");
        } catch (Exception e) {
            e.printStackTrace();
		}
    }

    public static void DrawTriagleToBuffer(BufferedImage img,int num){
            Graphics g =  img.getGraphics();
            for(int i = 0;i<num;i++){
                RandomTri tri =  new RandomTri(512);
                g.setColor(tri.col);
                g.fillPolygon(tri.x, tri.y, 3);
            }
    }

    public static void TestDrawShell(){
        for(int i =0;i<1;i++){
            Shell shell = new Shell();
        }
    }
}

class RandomTri{
    int[] x;
    int[] y;
    int[] color;
    Color col;
    RandomTri(int w){
        x =  new int[3];
        y =  new int[3];
        color =  new int[4];
        for(int i = 0;i<3 ;i++){
            x[i]  = getRandomNum(w);
            y[i]  = getRandomNum(w);
            color[i] = getRandomNum(255);
        }
        color[3] = getRandomNum(255);
        col =  new Color(color[0], color[1], color[2], color[3]);
    }
    private int getRandomNum(int Max){
        double n = Math.random();
        return (int)(Max * n);
    }
}

class Shell implements Comparable {
    public static long idFlag = 0;
    long id = 0;
    RandomTri[] tris = new RandomTri[100];
    BufferedImage img;
    int width =  128;
    Shell(){
        idFlag++;
        id = idFlag;
        img = new BufferedImage(width, width, BufferedImage.TYPE_INT_BGR);
        for(int i=0;i<tris.length;i++){
            tris[i]= new RandomTri(width);
        }
        DrawTriagleToBuffer();
    }
    public void DrawTriagleToBuffer(){
        Graphics g =  img.getGraphics();
        for(int i=0;i<tris.length;i++){
            g.setColor(tris[i].col);
            g.fillPolygon(tris[i].x, tris[i].y, 3);
        }
    }

    public void print(){
        try {
            OutputStream out = new FileOutputStream(new File("./out/"+id+".jpg"));
            ImageIO.write(img, "jpg", out); 
            System.out.println("img "+id+" printed");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
		}
    }
    int[] rgb =  new int[3];
    public void like(BufferedImage originImg){
        for(int i = 0;i<width;i++){
            for(int j = 0;j<width;j++){
                int pixel = img.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}

class Beach{
    List<Shell> shells;
    Beach(){
        shells =  new ArrayList<Shell>();
    }
}