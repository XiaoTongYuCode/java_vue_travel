package com.javahomework.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.*;
import com.javahomework.service.IOrderFormService;
import com.javahomework.service.IScenicService;
import com.javahomework.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author com
 * @since 2024-09-07
 */
@RestController
@RequestMapping("/order")
public class OrderFormController {
    @Autowired
    private IOrderFormService iOrderFormService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IScenicService iScenicService;

    @Autowired
    private HttpServletRequest request;

    //新增
    @PostMapping("/add")
    public Result save(@RequestBody OrderForm orderForm) {
        orderForm.setId(System.currentTimeMillis() / 1000);
        if(orderForm.getChecks() == null || !orderForm.getChecks().equals("y")){
            orderForm.setChecks("w");
        }

        User user =  iUserService.lambdaQuery().eq(User::getNo, orderForm.getNo()).list().get(0);
        user.setMoney((int) (user.getMoney() - orderForm.getTickets() * 100));
        iUserService.updateById(user);

        return iOrderFormService.save(orderForm) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String pid) {
        return iOrderFormService.removeById(pid) ? Result.suc() : Result.fail();
    }

    //模糊查询
    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String scenic_name = (String) param.get("scenic_name");
        String name = (String) param.get("name");
        String no = (String) param.get("no");
        String checks = (String) param.get("checks");

        Page<OrderForm> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<OrderForm> lambdaQueryWrapper = new LambdaQueryWrapper();

        if (checks != null && checks.equals("Y")){
            lambdaQueryWrapper.eq(OrderForm::getChecks, "w");
        }
        if (scenic_name != null && !scenic_name.isEmpty()){
            lambdaQueryWrapper.eq(OrderForm::getScenicName, scenic_name);
        }
        if (name != null&& !name.isEmpty()){
            lambdaQueryWrapper.eq(OrderForm::getName, name);
        }
        if (StringUtils.isNotBlank(no)) {
            lambdaQueryWrapper.eq(OrderForm::getNo, no);
            List<OrderForm> orderFormList =  iOrderFormService.list(lambdaQueryWrapper);
            return Result.suc(orderFormList);
        }

        // 按 id 倒序排列
        lambdaQueryWrapper.orderByDesc(OrderForm::getPid);


        IPage<OrderForm> result = iOrderFormService.page(page, lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }

    @PostMapping("/checks")
    public Result checks(@RequestBody HashMap map) {
        Integer pid = (Integer) map.get("pid");
        String check_type = (String) map.get("type");

        try {
            // 获取订单信息
            OrderForm orderForm = iOrderFormService.getById(pid);
            if (orderForm == null) {
                return Result.fail();
            }
            // 更新订单检查类型
            orderForm.setChecks(check_type);
            iOrderFormService.updateById(orderForm);
            // 如果检查类型为 "n"，更新用户的余额
            if (check_type.equals("n")) {
                User user = iUserService.getById(orderForm.getNo());
                // 更新用户余额
                user.setMoney((int) (user.getMoney() + orderForm.getTickets() * 100));
                iUserService.updateById(user);
            }

            return Result.suc();
        } catch (Exception e) {
            // 捕获任何异常并返回失败消息
            return Result.fail("Operation failed: " + e.getMessage());
        }
    }



}
