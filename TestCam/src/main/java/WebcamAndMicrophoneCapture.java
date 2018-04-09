import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.BufferUnderflowException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameRecorder.Exception;
import javax.imageio.ImageIO;
public class WebcamAndMicrophoneCapture {

    public static final String FILENAME = "output.mp4";
    static final int MAX_THREADS = 15;

    public static void main(String[] args) throws Exception, FrameGrabber.Exception {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        CanvasFrame canvasFrame = null;
        try (OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0)) {
            grabber.start();

            Frame grabbedImage = grabber.grab();

            canvasFrame = new CanvasFrame("Cam");
            canvasFrame.setCanvasSize(grabbedImage.imageWidth, grabbedImage.imageHeight);

            System.out.println("framerate = " + grabber.getFrameRate());
            grabber.setFrameRate(grabber.getFrameRate());
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(FILENAME, grabber.getImageWidth(), grabber.getImageHeight());

            recorder.setVideoCodec(13);
            recorder.setFormat("mp4");
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            recorder.setFrameRate(30);
            recorder.setVideoBitrate(10 * 1024 * 1024);

            recorder.start();
            int i = 0;
            while (canvasFrame.isVisible() && (grabbedImage = grabber.grab()) != null) {
                i++;
                canvasFrame.showImage(grabbedImage);
                if(i % 10 ==0) {
                    //we are passing the image to the lookup api
                    //note that if we don't externalize the thread for this operation
                    // the stream screen will freeze while it does the lookup
                    Runnable r1 = new CamGrab(grabbedImage);
                    /*Thread thread = new Thread(r1);
                    thread.start();*/
                    pool.execute(r1);
                }
                recorder.record(grabbedImage);
            }

            recorder.stop();
            grabber.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(BufferUnderflowException e){

        }
        canvasFrame.dispose();
    }



}

class CamGrab implements Runnable{

    Frame frame;

    CamGrab(Frame camGrab){
        frame = camGrab;
    }
    @Override
    public void run() {
        try{
            BufferedImage bufferedImage = getImage(frame, false);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",baos);
            ImageIO.setUseCache(false);
            byte[] imageInByte=baos.toByteArray();
            ClarifaiLookup.lookupImage(imageInByte);
        }catch (IOException | BufferUnderflowException e){
            //System.out.println(e.getMessage());
        }
    }

    /*
       This function gets the buffered image for a given frame
    */
    public static BufferedImage getImage(Frame image, boolean flipChannels) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage myimage = converter.getBufferedImage(image, Java2DFrameConverter.getBufferedImageType(image) == 0 ? 1.0D : 1.0D, flipChannels, (ColorSpace)null);
        return myimage;
    }
}