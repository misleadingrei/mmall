package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by misleadingrei on 12/10/17.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{
    Logger logger =  LoggerFactory.getLogger(FileServiceImpl.class);

    public String  upload(String path, MultipartFile file){
        String fileName = file.getName();

        String extensionName = fileName.substring(fileName.lastIndexOf(".")+1);

        String uploadFileName = UUID.randomUUID().toString()+"."+extensionName;

        File uploadFile = new File(path,uploadFileName);


        //not existed
        if(!uploadFile.exists()){
            uploadFile.setWritable(true);
            uploadFile.mkdirs();
        }

        try {

            // transfer uploadfile to the "upload" dir
            file.transferTo(uploadFile);

            // uploaded to the ftp server
            FTPUtil.uploadFile(Lists.newArrayList(uploadFile));

            //delete store file in the tomcat
            uploadFile.delete();

        } catch (IOException e) {
            logger.error("upload failed");
            return null;
        }
        finally {
            return uploadFile.getName();
        }

    }
}
