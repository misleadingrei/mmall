package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by misleadingrei on 11/27/17.
 */
public class Const {
    public static  final String CURRENT_USER="CURRENT_USER";

    public interface Role{
        int ROLE_CUSTOMER=0;
        int ROLE_ADMIN=1;
    }
   public static String USERNAME="USERNAME";
   public static String EMAIL="EMAIL";

    public interface  productListOrderBy{
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("PRICE_DESC","PRICE_ASC");
    }

    public interface  productCheck{
        int CHECKED=0;
        int UNCHECKED=1;
    }

    public interface limitQuantity{
        int LIMIT_QUANTITY_SUCCESS =0;
        int LIMIT_QUANTITY_FAILED=1;
    }

    // use this enum to record sale status
   public enum ProductStatusEnum{
       On_SALE("ON_SALE",1);


       public String getValue() {
           return value;
       }

       public int getCode() {
           return code;
       }

        private String value;
        private int code ;

      ProductStatusEnum(String value ,int code){
           this.value=value;
           this.code=code;

       }


    }

}

