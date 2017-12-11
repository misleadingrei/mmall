package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    //return the changed uploaded filaname and it's url to front-end
    public ServerResponse uploadProduct(HttpServletRequest request, MultipartFile file){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName =iFileService.upload(path,file);
        String prefix = PropertiesUtil.getProperty("ftp.server.http.prefix");

    }
}
