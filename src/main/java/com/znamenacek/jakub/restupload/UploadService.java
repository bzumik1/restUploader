package com.znamenacek.jakub.restupload;

import com.znamenacek.jakub.restupload.parserSAX.HandlerMeasurementStartTime;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@org.springframework.stereotype.Service
public class UploadService {

    public void saveUploadedFiles(List<MultipartFile> files) {
        files.stream().forEach(this::saveFile);
    }

    private void saveFile(MultipartFile file) {
        if (!file.isEmpty()) {
            //Checks if the file is valid
            if(!isValid(file)){
                System.out.println("File isn't valid.");  //CORRECT TO EXCEPTION
            }
            try {
                byte[] bytes = file.getBytes(); //gets bytes form stream
                //Path to file which should be created
                Path path = Paths.get(System.getProperty("user.home"), "uploaded",file.getOriginalFilename());

                //Checks if the directory exists
                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                    System.out.println("DIRECTORY CREATED"); // JUST FOR TESTING
                }else{
                    //Checks if the file isn't in directory
                    if(Files.exists(path)){
                        System.out.println("File with same name found."); //CORRECT TO EXCEPTION
                    }
                }

                //Saves the file
                Files.write(path, bytes);
                rename(path);
                System.out.println("SAVED"); //JUST FOR TESTING

            } catch (IOException e) {
                System.out.println("Temporary store failed."); //CORRECT TO EXCEPTION
            }
        }
    }



    private static long parseMeasurementStartTime(Path path) throws Exception{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        // The handler that will listen to the SAX event during
        // the xml traversal.

        HandlerMeasurementStartTime handler = new HandlerMeasurementStartTime();


        try(ZipFile zipFile = new ZipFile(path.toFile());) {
            ZipEntry zipEntry = zipFile.getEntry("traceinfo.xml");
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            parser.parse(inputStream,handler);

            inputStream.close(); // TRY WITH RESOURCES???
             return handler.getMeasurementStartTime();
        }catch (IOException e){
            e.getStackTrace();
        }
        return 0;
    }

    private void rename(Path path){
        try {
            long timestamp = parseMeasurementStartTime(path);
            try{
                Files.move(path, path.resolveSibling("WebTrace_xxx_"+convertTime(timestamp)+".wtrc"));
            }catch (FileAlreadyExistsException e){
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }



    }

    private boolean isValid(MultipartFile file){
        return FilenameUtils.getExtension(file.getOriginalFilename()).equals("wtrc");
    }

    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return format.format(date);
    }






}
