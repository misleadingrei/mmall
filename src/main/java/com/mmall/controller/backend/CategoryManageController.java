package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by misleadingrei on 12/2/17.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    //spring autowired by Tpye prior to by Name
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value="add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //check login
        if(user==null) return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"need login first");

        //check admin
      if(!iUserService.checkAdminRole(user).isSuccess())
          //not admin
      {
          return ServerResponse.createByErrorMsg("not admin no privilege");
      }
      // is admin

         return iCategoryService.addCategory(categoryName,parentId);

    }


    @RequestMapping(value="update_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse updateCategoryName(HttpSession session,String newName,Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //check login
        if(user==null) return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"need login first");

        //check admin
        if(!iUserService.checkAdminRole(user).isSuccess())
        //not admin
        {
            return ServerResponse.createByErrorMsg("not admin and have no privilege");
        }
        return iCategoryService.updateCategoryName(newName,categoryId);
    }

    @RequestMapping(value="get_children_parallel_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getChildrenParallelCategory (HttpSession session, @RequestParam(value="parentId", defaultValue="0") Integer parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //check login
        if(user==null) return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"need login first");

        //check admin
        if(!iUserService.checkAdminRole(user).isSuccess())
        //not admin
        {
            return ServerResponse.createByErrorMsg("not admin and have no privilege");
        }
        //is admin
        return iCategoryService.getChildrenParallelCategory(parentId);
    }

    @RequestMapping(value="get_category_and_deep_child_category.do",method=RequestMethod.POST)
    @ResponseBody
    public  ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value="categoryId", defaultValue="0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //check login
        if(user==null) return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"need login first");

        //check admin
        if(!iUserService.checkAdminRole(user).isSuccess())
        //not admin
        {
            return ServerResponse.createByErrorMsg("not admin and have no privilege");
        }
        //is admin
        return  iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
    }
}
