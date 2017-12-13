package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.VO.CartProductVo;
import com.mmall.VO.CartVo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by misleadingrei on 12/13/17.
 */
@Service("ICartServiceImpl")
public class CartServiceImpl implements ICartService{
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    public ServerResponse<CartVo> add (Integer userId,Integer productId,Integer count){
        //args judge
        if(userId==null||productId==null||count==null){
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
       Cart cart = cartMapper.selectByProductIdAndUserId(userId,productId);
        //cart in database does not contains such a product ,then insert the product into new cart
        if(cart==null){
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            //insert cartItem
            cartMapper.insert(cartItem);
        }
        // cart in database contains such a product .update new number
        else
        {
            count+=cart.getQuantity();
            cart.setQuantity(count);
            //update cart
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        // return cartVo wtih limit using getCartVoWithLimit
        CartVo cartVo = this.getCartVoWtihLimit(userId);
        return ServerResponse.createBySuccessData(cartVo);

    }

    //this method is used to  return the cartVo wtih limit
    //inout param : userId
    // use userId to select all cart from mmall_cart
    private CartVo getCartVoWtihLimit (Integer userId){
        //cartvo contains List<CartProductVo> cartProductVoList
        CartVo cartVo = new CartVo();

        List<Cart> cartList = cartMapper.selectCartlistByUserId(userId);

        List<CartProductVo> cartProductVoList =Lists.newArrayList();

        //attention!
        // see source code of BigDecimal
        // the way of use  bigDecimal(Double num) is somewhat not incorrect
        // must use bigDecimal(String num)
        /*The results of this constructor can be somewhat unpredictable.
                * One might assume that writing {@code new BigDecimal(0.1)} in
                * Java creates a {@code BigDecimal} which is exactly equal to
                * 0.1 (an unscaled value of 1, with a scale of 1), but it is
                * actually equal to
        * 0.1000000000000000055511151231257827021181583404541015625.
                * This is because 0.1 cannot be represented exactly as a
        * {@code double} (or, for that matter, as a binary fraction of
        * any finite length).  Thus, the value that is being passed
                * <i>in</i> to the constructor is not exactly equal to 0.1,
        * appearances notwithstanding.*/
        //beacauce the price stored in db is always double
        //for convenience we create BigDecimalUtil to carry out this double to String convert

        //use this filed th record all price of cart
        BigDecimal totalPrice =new BigDecimal("0");


        if(CollectionUtils.isNotEmpty(cartList)){
         for(Cart cart : cartList){
             CartProductVo cartProductVo = new CartProductVo();
             //because we use userId so id and userId to select cart
             // the cart we get should be non empty
             cartProductVo.setId(cart.getId());
             cartProductVo.setUserId(cart.getUserId());
             cartProductVo.setProductId(cart.getProductId());

             //product value ,first we need to select product and check wether it is empty
             Product productItem = productMapper.selectByPrimaryKey(cart.getProductId());
             if(productItem!=null){
                 cartProductVo.setMainImage(productItem.getMainImage());
                 cartProductVo.setProductName(productItem.getName());
                 cartProductVo.setProductSubTitle(productItem.getSubtitle());
                 cartProductVo.setProductPrice(productItem.getPrice());
                 cartProductVo.setProductStatus(productItem.getStatus());
                 cartProductVo.setProductStock(productItem.getStock());

                 //judge stock and limitCount
                 int buyLimitCount = 0;
                 //this method will be improved by using mutex
                 //for simiplicty we don't use it here
                 if(productItem.getStock()>=cart.getQuantity()){
                     buyLimitCount=cart.getQuantity();
                     cartProductVo.setLimitQuantity(Const.limitQuantity.LIMIT_QUANTITY_SUCCESS);
                 }
                 else{
                     buyLimitCount=productItem.getStock();
                     cartProductVo.setLimitQuantity(Const.limitQuantity.LIMIT_QUANTITY_FAILED);
                     //cart update stock
                     Cart tempCartForUpdate = new Cart();
                     tempCartForUpdate.setQuantity(buyLimitCount);
                     tempCartForUpdate.setId(cart.getId());

                     cartMapper.updateByPrimaryKeySelective(tempCartForUpdate);
                 }
                 cartProductVo.setQuantity(buyLimitCount);

             }
             //calculate total price
             cartProductVo.setTotalPrice(BigDecimalUtil.multiply(cartProductVo.getQuantity(),
                     cartProductVo.getProductPrice().doubleValue()));
             //set checked
             cartProductVo.setProductCheck(cart.getChecked());
             //if this product is checked ,it should be added to total price
             if(cartProductVo.getProductCheck().equals(Const.productCheck.CHECKED))
             {
                 totalPrice.add(cartProductVo.getTotalPrice()));
             }
             cartProductVoList.add(cartProductVo);
         }
        }
        //set total price of this cart
        cartVo.setTotalCartPrice(totalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        //set checked status of
        cartVo.setAllChecked(this.getAllCheckStatus(userId));

    }

    // wether products are all checked
   private boolean getAllCheckStatus(Integer userId){
       if(userId==null) return false;
       //if  all of this carts are checked
       // return result is zero
       return cartMapper.selectProductUncheckStatusByUserId(userId)==0;

   }

}
