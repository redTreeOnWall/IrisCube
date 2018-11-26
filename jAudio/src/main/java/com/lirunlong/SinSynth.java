package com.lirunlong;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SinSynth {
    //
    protected static final int SAMPLE_RATE = 16 * 1024;

    public static byte[] createSinWaveBuffer(double freq, int ms) {
        int samples = (int) ((ms * SAMPLE_RATE) / 1000);
        byte[] output = new byte[samples];
        //
        double period = (double) SAMPLE_RATE / freq;
        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * i / period;
            output[i] = (byte) (Math.sin(angle) * 127f);
        }

        return output;
    }

    private static byte[] getWave(double t){
            byte[] b = new byte[8];
// byte[] b = new byte[8];
            for (int j = 0; j < b.length; j++) {
                t = t + 0.01;
                // b[j] = (byte) (j / 128f);
                double volume = 128.0 * Math.sin(t);// + 30.0 * Math.sin(0.5*t) *  30.0 * Math.sin(0.5*t);
                b[j] = (byte) volume;
            }
            return b;
    }

    static double sinWave(double t){
        return 0.0;
    }

    public static void Beep() throws LineUnavailableException {
        final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, SAMPLE_RATE);
        line.start();
        double t = 0;
        // 控制频率
        double speed = 0.01;
        boolean sound =true;
        while (sound) {
            if (speed >= 1.0) {
                speed = 0.01;
            }
            speed = speed + 0.0005;
            byte[] b = getWave(t);
            // byte[] b = new byte[8];
            line.write(b, 0, b.length);
            // try {
            // Thread.sleep(10l);
            // } catch (InterruptedException e) { // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }

        // boolean forwardNotBack = false;

        // for (double freq = 400; freq <= 800;) {
        // byte[] toneBuffer = createSinWaveBuffer(freq, 50);
        // int count = line.write(toneBuffer, 0, toneBuffer.length);

        // if (forwardNotBack) {
        // freq += 20;
        // forwardNotBack = false;
        // } else {
        // freq -= 10;
        // forwardNotBack = true;
        // }
        // try {
        // Thread.sleep(1000l);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // }
        line.drain();
        line.close();
    }

    public static void BeepSample() throws LineUnavailableException {
        final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, SAMPLE_RATE);
        line.start();

        boolean forwardNotBack = true;

        for (double freq = 400; freq <= 800;) {
            byte[] toneBuffer = createSinWaveBuffer(freq, 50);
            int count = line.write(toneBuffer, 0, toneBuffer.length);

            if (forwardNotBack) {
                freq += 20;
                forwardNotBack = false;
            } else {
                freq -= 10;
                forwardNotBack = true;
            }
        }

        line.drain();
        line.close();
    }

} 