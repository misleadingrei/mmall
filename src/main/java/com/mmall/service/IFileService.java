package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by misleadingrei on 12/10/17.
 */
public interface IFileService {
   String upload(String path,MultipartFile file);
}
