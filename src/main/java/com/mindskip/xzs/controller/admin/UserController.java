package com.mindskip.xzs.controller.admin;



import com.alibaba.excel.EasyExcel;
import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.excel.ExcelUser;
import com.mindskip.xzs.domain.other.KeyValue;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.UserEventLog;
import com.mindskip.xzs.domain.enums.UserStatusEnum;
import com.mindskip.xzs.listener.EasyExcelListener;
import com.mindskip.xzs.repository.UserEventLogMapper;
import com.mindskip.xzs.service.AuthenticationService;
import com.mindskip.xzs.service.UserEventLogService;
import com.mindskip.xzs.service.UserService;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.viewmodel.admin.user.*;
import com.mindskip.xzs.utility.PageInfoHelper;
import com.github.pagehelper.PageInfo;


import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.Proxy;
import java.util.*;
import java.util.stream.Collectors;


@RestController("AdminUserController")
@RequestMapping(value = "/api/admin/user")
public class UserController extends BaseApiController {


    private final UserService userService;
    private final UserEventLogService userEventLogService;
    private final AuthenticationService authenticationService;

    private UserEventLogMapper userEventLogMapper;
    @Autowired
    public UserController(UserService userService, UserEventLogService userEventLogService, AuthenticationService authenticationService, UserEventLogMapper userEventLogMapper) {
        this.userService = userService;
        this.userEventLogService = userEventLogService;
        this.authenticationService = authenticationService;
        this.userEventLogMapper = userEventLogMapper;
    }


    @RequestMapping(value = "/page/list", method = RequestMethod.POST)
    public RestResponse<PageInfo<UserResponseVM>> pageList(@RequestBody UserPageRequestVM model) {
        PageInfo<User> pageInfo = userService.userPage(model);
        PageInfo<UserResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> UserResponseVM.from(d));
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/event/download")
    public void evenDownload(HttpServletResponse response,@RequestParam String type) throws IOException {

//        String path = fileName+".xlsx";
//        System.out.println(path);
//        response.setContentType("application/force-download"); //强制下载
//        response.setHeader("Content-Disposition","attachement;filename=demo.xlsx");
//        ClassPathResource resource = new ClassPathResource(path);
//        InputStream input = resource.getInputStream();
//        byte data[] = new byte[1024];
//        int len = 0;
//        while ((len = input.read(data)) != -1){
//            response.getOutputStream().write(data,0,len);
//        }
        List<List<String>> head = new ArrayList<>();
        //head.add(Arrays.asList("id", "用户id", "用户名","真实姓名","内容","时间"));
        ArrayList<String> as=new ArrayList<>();as.add("id");
        ArrayList<String> as1=new ArrayList<>();as1.add("用户id");
        ArrayList<String> as2=new ArrayList<>();as2.add("用户名");
        ArrayList<String> as3=new ArrayList<>();as3.add("真实姓名");
        ArrayList<String> as4=new ArrayList<>();as4.add("内容");
        ArrayList<String> as5=new ArrayList<>();as5.add("时间");
        head.add(as);head.add(as1);head.add(as2);head.add(as3);head.add(as4);head.add(as5);

        List<UserEventLog> userEventLogList=new ArrayList<>();
        if(type.equals("seven")){
            userEventLogList=userEventLogMapper.getSevenEvent();
        }else if(type.equals("mouth")){
            userEventLogList=userEventLogMapper.getMouthEvent();
        }else{
            userEventLogList=userEventLogMapper.getAllEvent();
        }
        List<List<String>> data=userEventLogList.stream().map(userEventLog ->
                Arrays.asList(String.valueOf(userEventLog.getId()),String.valueOf(userEventLog.getUserId()),userEventLog.getUserName(),userEventLog.getRealName(),userEventLog.getContent(),userEventLog.getCreateTime().toString())).collect(Collectors.toList());
        ServletOutputStream outputStream = response.getOutputStream();
        EasyExcel.write(outputStream)
                .head(head)
                .sheet("日志记录")
                .doWrite(data);
        response.setContentType("application/force-download"); //强制下载
        response.setHeader("Content-Disposition","attachement;filename=demo.xlsx");
        // 刷新输出流，确保数据被发送到客户端
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = "/event/page/list", method = RequestMethod.POST)
    public RestResponse<PageInfo<UserEventLogVM>> eventPageList(@RequestBody UserEventPageRequestVM model) {
        PageInfo<UserEventLog> pageInfo = userEventLogService.page(model);
        PageInfo<UserEventLogVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            UserEventLogVM vm = modelMapper.map(d, UserEventLogVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(d.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<UserResponseVM> select(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        UserResponseVM userVm = UserResponseVM.from(user);
        return RestResponse.ok(userVm);
    }

    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public RestResponse<UserResponseVM> current() {
        User user = getCurrentUser();
        UserResponseVM userVm = UserResponseVM.from(user);
        return RestResponse.ok(userVm);
    }

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public RestResponse<List<com.mindskip.xzs.viewmodel.student.user.UserEventLogVM>> log(@RequestBody User user1) {
        User user = userService.getUserByUserName(user1.getUserName());
        List<UserEventLog> userEventLogs = userEventLogService.getUserEventLogByUserId(user.getId());
        List<com.mindskip.xzs.viewmodel.student.user.UserEventLogVM> userEventLogVMS = userEventLogs.stream().map(d -> {
            com.mindskip.xzs.viewmodel.student.user.UserEventLogVM vm = modelMapper.map(d, com.mindskip.xzs.viewmodel.student.user.UserEventLogVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(d.getCreateTime()));
            return vm;
        }).collect(Collectors.toList());
        return RestResponse.ok(userEventLogVMS);
    }

    @RequestMapping(value = "/download")
    public void download(HttpServletResponse response,@RequestParam String fileName) throws IOException {
        String path = fileName+".xlsx";
        System.out.println(path);
        response.setContentType("application/force-download"); //强制下载
        response.setHeader("Content-Disposition","attachement;filename=demo.xlsx");
        ClassPathResource resource = new ClassPathResource(path);
        InputStream input = resource.getInputStream();
        byte data[] = new byte[1024];
        int len = 0;
        while ((len = input.read(data)) != -1){
            response.getOutputStream().write(data,0,len);
        }
        input.close();
        response.getOutputStream().close();
    }

    @RequestMapping(value = "/batchInsertUser")
    public RestResponse<User> batchInsert(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        InputStream inputStream =  file.getInputStream();
        List<ExcelUser> list =  EasyExcel.read(inputStream, ExcelUser.class,new EasyExcelListener()).doReadAllSync();
        list.forEach(System.out::println);
        for(int i=0;i<list.size();i++)
        {
            ExcelUser eu=list.get(i);
            User existUser = userService.getUserByUserName(eu.getUserName());
            if(existUser!=null) continue;
            if (StringUtils.isBlank(eu.getBirthDay())) {
                eu.setBirthDay(null);
            }
            User user = modelMapper.map(eu, User.class);
            String encodePwd = authenticationService.pwdEncode(eu.getPassword());
            user.setPassword(encodePwd);
            user.setUserUuid(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setLastActiveTime(new Date());
            user.setDeleted(false);
            user.setRole(1);
            user.setStatus(1);
            System.out.println(user);
            userService.insertByFilter(user);
        }
        return RestResponse.ok();
    }
    @RequestMapping(value = "/batchInsertAdmin")
    public RestResponse<User> batchInsertAdmin(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        InputStream inputStream =  file.getInputStream();
        List<ExcelUser> list =  EasyExcel.read(inputStream, ExcelUser.class,new EasyExcelListener()).doReadAllSync();
        for(int i=0;i<list.size();i++)
        {
            ExcelUser eu=list.get(i);
            User existUser = userService.getUserByUserName(eu.getUserName());
            if(existUser!=null) continue;
            if (StringUtils.isBlank(eu.getBirthDay())) {
                eu.setBirthDay(null);
            }
            User user = modelMapper.map(eu, User.class);
            String encodePwd = authenticationService.pwdEncode(eu.getPassword());
            user.setPassword(encodePwd);
            user.setUserUuid(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setLastActiveTime(new Date());
            user.setDeleted(false);
            user.setRole(3);
            user.setStatus(1);
            System.out.println(user);
            userService.insertByFilter(user);
        }
        return RestResponse.ok();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse<User> edit(@RequestBody @Valid UserCreateVM model) {
        if (model.getId() == null) {  //create
            User existUser = userService.getUserByUserName(model.getUserName());
            if (null != existUser) {
                return new RestResponse<>(2, "用户已存在");
            }

            if (StringUtils.isBlank(model.getPassword())) {
                return new RestResponse<>(3, "密码不能为空");
            }
        }
        if (StringUtils.isBlank(model.getBirthDay())) {
            model.setBirthDay(null);
        }
        User user = modelMapper.map(model, User.class);

        if (model.getId() == null) {
            String encodePwd = authenticationService.pwdEncode(model.getPassword());
            user.setPassword(encodePwd);
            user.setUserUuid(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setLastActiveTime(new Date());
            user.setDeleted(false);
            userService.insertByFilter(user);
        } else {
            if (!StringUtils.isBlank(model.getPassword())) {
                String encodePwd = authenticationService.pwdEncode(model.getPassword());
                user.setPassword(encodePwd);
            }
            user.setModifyTime(new Date());
            userService.updateByIdFilter(user);
        }
        return RestResponse.ok(user);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResponse update(@RequestBody @Valid UserUpdateVM model) {
        User user = userService.selectById(getCurrentUser().getId());
        modelMapper.map(model, user);
        user.setModifyTime(new Date());
        user.setPassword(authenticationService.pwdEncode(user.getPassword()));
        userService.updateByIdFilter(user);
        return RestResponse.ok();
    }


    @RequestMapping(value = "/changeStatus/{id}", method = RequestMethod.POST)
    public RestResponse<Integer> changeStatus(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        UserStatusEnum userStatusEnum = UserStatusEnum.fromCode(user.getStatus());
        Integer newStatus = userStatusEnum == UserStatusEnum.Enable ? UserStatusEnum.Disable.getCode() : UserStatusEnum.Enable.getCode();
        user.setStatus(newStatus);
        user.setModifyTime(new Date());
        userService.updateByIdFilter(user);
        return RestResponse.ok(newStatus);
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        user.setDeleted(true);
        userService.updateByIdFilter(user);
        return RestResponse.ok();
    }


    @RequestMapping(value = "/selectByUserName", method = RequestMethod.POST)
    public RestResponse<List<KeyValue>> selectByUserName(@RequestBody String userName) {
        List<KeyValue> keyValues = userService.selectByUserName(userName);
        return RestResponse.ok(keyValues);
    }
    @RequestMapping(value = "/test1/{s}",produces = "application/json;charset=utf-8")
    public String test1(@PathVariable String s)
    {
        System.out.println("test1方法被调用了");
        //国内需要代理
        Proxy proxy = Proxys.http("127.0.0.1", 7890);
        //socks5 代理
        // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();

        String res = chatGPT.chat(s);
        System.out.println(res);

        return res;

    }

}
