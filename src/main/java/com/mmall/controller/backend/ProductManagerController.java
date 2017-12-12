package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.VO.ProductDetailsVo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by misleadingrei on 12/7/17.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse insertOrUpdateProduct(HttpSession session, Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.insertOrUpdateProduct(product);
        }
        else
            return ServerResponse.createByErrorMsg("not admin");

    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    // use this method to set product onsale or not for sale .
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }
        else
            return ServerResponse.createByErrorMsg("not admin");

    }


    @RequestMapping("details.do")
    @ResponseBody
    // use this method to set product onsale or not for sale .
    public ServerResponse<ProductDetailsVo> getDetails(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        if(iUserService.checkAdminRole(user).isSuccess()){
            // success then do then business
            return iProductService.manageProductDetails(productId);
        }
        else
            return ServerResponse.createByErrorMsg("not admin");

    }

    @RequestMapping("get_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session, @RequestParam(value="pageNUm",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        if(iUserService.checkAdminRole(user).isSuccess()){
            // success then do then business
            return iProductService.getProductList(pageNum, pageSize);

        }
        else
            return ServerResponse.createByErrorMsg("not admin");

    }


    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse searchProduct(HttpSession session,String  productName,Integer productId,@RequestParam(value="pageNUm",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        if(iUserService.checkAdminRole(user).isSuccess()){
            // success then do then business
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);

        }
        else
            return ServerResponse.createByErrorMsg("not admin");
    }

    @RequestMapping("upload.do")
    @ResponseBody
    //return the changed uploaded filaname(URI) and it's url to front-end
    public ServerResponse uploadProduct(HttpServletRequest request, @RequestParam(value="uploadFile",required=false) MultipartFile file){
        User user = (User)request.getSession().getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        if(iUserService.checkAdminRole(user).isSuccess()){
            // success then do then business
            String path = request.getSession().getServletContext().getRealPath("upload");
            String fileName =iFileService.upload(path,file);
            String prefix = PropertiesUtil.getProperty("ftp.server.http.prefix");

            // put URL and URI in the hashMap
            Map fileMap = Maps.newHashMap();
            fileMap.put("URI",fileName);
            fileMap.put("URL",prefix+fileName);

            return  ServerResponse.createBySuccessData(fileMap);

        }
        else
            return ServerResponse.createByErrorMsg("not admin");

    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload (HttpServletRequest request, @RequestParam(value="uploadFile",required=false) MultipartFile file, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User)request.getSession().getAttribute(Const.CURRENT_USER);
        if(user==null) {
            resultMap.put("success",false);
            resultMap.put("msg","please login in as admin first");
            return resultMap;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String fileName =iFileService.upload(path,file);
            String prefix = PropertiesUtil.getProperty("ftp.server.http.prefix");
            resultMap.put("success",true);
            resultMap.put("msg","upload success");
            resultMap.put("file_path",prefix+fileName);

            // because we use simditor so we add header in the httpServletResponse here
            response.addHeader("Access-Control-Allow-headers","X-File-Name");
            return resultMap;

        }
        else
        {
            resultMap.put("success",false);
            resultMap.put("msg","not admin");
            return resultMap;
        }


    }
}
