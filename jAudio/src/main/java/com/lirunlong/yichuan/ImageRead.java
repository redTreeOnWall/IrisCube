package com.lirunlong.yichuan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.file.CopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageRead {
    public static void main(String[] s) {
        System.out.println("test yichuan ..");
        // TestDrawShell();
        // testSame();
        testBeach();
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

    public static void DrawInMemery() {
        try {
            BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_BGR);
            DrawTriagleToBuffer(img, 100);
            OutputStream out = new FileOutputStream(new File("./2.jpg"));
            ImageIO.write(img, "jpg", out);
            System.out.println("img writed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DrawTriagleToBuffer(BufferedImage img, int num) {
        Graphics g = img.getGraphics();
        for (int i = 0; i < num; i++) {
            RandomTri tri = new RandomTri(512);
            g.setColor(tri.col);
            g.fillPolygon(tri.x, tri.y, 3);
        }
    }

    public static void TestDrawShell() {
        List<Shell> shells = new ArrayList<Shell>();
        for (int i = 0; i < 10; i++) {
            Shell shell = new Shell(128);
            shells.add(shell);
            shell.print("1");
        }

        for (Shell shell : shells) {
            shell.RandomChange(0.1f);
            shell.print("2");
        }
    }

    public static void testSame() {
        Shell shell = new Shell(getMod().getWidth());
        shell.DrawTriagleToBuffer();
        shell.like(getMod());
        double same = shell.same();
        System.out.println("same:" + shell.dif);
    }

    public static void testBeach() {
        Beach b = new Beach(1000, 20, getMod());
        b.loop(1000);
    }

    static BufferedImage mod;

    public static BufferedImage getMod() {
        if (mod == null) {
            try {
                mod = ImageIO.read(new File("./out/mod.jpg"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mod;
    }

    public static void testList() {
        List<Te> li = new LinkedList<Te>();
        for (int i = 0; i < 10; i++) {
            Te t = new Te();
            t.setI(i);
            li.add(t);
        }

        for (int i = 0; i < li.size(); i++) {
            Te te = new Te();
            te.setI(100);
            li.set(i, te);
        }

        for (Te tt : li) {
            System.out.println(tt.i);
        }

    }
}

class Te {
    static int si = 0;
    int i = 0;

    public void setI(int i) {
        this.i = i;
    }
}

class RandomTri implements ICopyable<RandomTri> {
    int[] x;
    int[] y;
    int[] color;
    Color col;

    RandomTri(int w) {
        x = new int[3];
        y = new int[3];
        color = new int[4];
        SetThisRandom(w);
    }

    RandomTri(RandomTri tri) {
    }

    private int getRandomNum(int Max) {
        double n = Math.random();
        return (int) (Max * n);
    }

    void SetThisRandom(int w) {
        for (int i = 0; i < 3; i++) {
            x[i] = getRandomNum(w);
            y[i] = getRandomNum(w);
            color[i] = getRandomNum(255);
        }
        color[3] = getRandomNum(255);
        col = new Color(color[0], color[1], color[2], color[3]);
    }

    @Override
    public RandomTri copy() {
        RandomTri tri = new RandomTri(this);
        tri.x = new int[3];
        tri.y = new int[3];
        tri.color = new int[4];
        for (int i = 0; i < 3; i++) {
            tri.x[i] = this.x[i];
            tri.y[i] = this.y[i];
            tri.color[i] = this.color[i];
        }
        tri.color[3] = this.color[3];
        tri.col = new Color(tri.color[0], tri.color[1], tri.color[2], tri.color[3]);
        return tri;
    }
}

class Shell implements ICopyable<Shell> {
    public static long idFlag = 0;
    long id = 0;
    RandomTri[] tris = new RandomTri[100];
    BufferedImage img;
    int width = 128;

    Shell(int width) {
        this.width = width;
        idFlag++;
        id = idFlag;
        img = new BufferedImage(width, width, BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < tris.length; i++) {
            tris[i] = new RandomTri(width);
        }
    }

    @Override
    public Shell copy() {
        Shell sh = new Shell(this);
        sh.width = this.width;
        sh.img = new BufferedImage(sh.width, sh.width, BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < sh.tris.length; i++) {
            sh.tris[i] = this.tris[i].copy();
        }
        return sh;
    }

    Shell(Shell father) {
        this.id = idFlag++;
    }

    public void DrawTriagleToBuffer() {
        Graphics g = img.getGraphics();
        g.clearRect(0, 0, width, width);
        for (int i = 0; i < tris.length; i++) {
            g.setColor(tris[i].col);
            g.fillPolygon(tris[i].x, tris[i].y, 3);
        }
    }

    public void print(String nameAdd) {
        try {
            DrawTriagleToBuffer();
            OutputStream out = new FileOutputStream(new File("./out/" + nameAdd + "_" + id + ".jpg"));
            ImageIO.write(img, "jpg", out);
            System.out.println("img " + id + " printed");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int[] rgb = new int[3];
    int[] rgb1 = new int[3];
    int dif;

    public void like(BufferedImage originImg) {
        dif = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = img.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);

                int pixel1 = originImg.getRGB(i, j);
                rgb1[0] = (pixel1 & 0xff0000) >> 16;
                rgb1[1] = (pixel1 & 0xff00) >> 8;
                rgb1[2] = (pixel1 & 0xff);
                for (int c = 0; c < 3; c++) {
                    dif = dif + Math.abs(rgb[c] - rgb1[c]);
                }
            }
        }
    }

    public double same() {
        double all = width * width * 3 * 256;
        return dif / all;
    }

    public void RandomChange(float changePer) {
        int changeNum = (int) (tris.length * changePer);
        for (int i = 0; i < changeNum; i++) {
            int triIndex = (int) (Math.random() * tris.length);
            tris[triIndex].SetThisRandom(width);
        }
    }

}

class Beach {
    int day = 0;
    List<Shell> shells;
    int maxNum;
    int selectNum;
    BufferedImage mod;

    Beach(int maxNum, int selectNum, BufferedImage mod) {
        this.mod = mod;
        this.maxNum = maxNum;
        this.selectNum = selectNum;
        shells = new ArrayList<Shell>();
        for (int i = 0; i < maxNum; i++) {
            Shell sh = new Shell(mod.getWidth());
            shells.add(sh);
        }
    }

    public void upDate() {
        day++;
        for (Shell sh : shells) {
            // 生长
            sh.DrawTriagleToBuffer();
            // 评估生存力
            sh.like(mod);
        } 
        // 选择
        Collections.sort(shells, new Comparator<Shell>() {
            @Override
            public int compare(Shell o1, Shell o2) {
                return o1.dif - o2.dif;
            }
        });
        int selectedIndex = 0;
        for (int i = 0; i < shells.size(); i++) {
            // System.out.println(sh.dif);
            if (i < selectNum) {

            } else {
                // 生产后代
                if (selectedIndex >= selectNum) {
                    selectedIndex = 0;
                }
                Shell newShell = shells.get(selectedIndex).copy();
                // 突变
                newShell.RandomChange(0.1f);
                shells.set(i,newShell);
            }
            selectedIndex++;
        }

    }

    void loop(int days){
        while(day<days){
            upDate();
            //记录 每一代最好的那个
            shells.get(0).print(day+"_");
            System.out.println("day:"+day+"  dif:"+shells.get(0).dif);
        }
    }
}

interface ICopyable<T> {
    T copy();
}