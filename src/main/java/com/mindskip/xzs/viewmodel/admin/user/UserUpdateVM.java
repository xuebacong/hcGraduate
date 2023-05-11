package com.mindskip.xzs.viewmodel.admin.user;



import javax.validation.constraints.NotBlank;


public class UserUpdateVM {

    @NotBlank
    private String realName;

    @NotBlank
    private String phone;

    private String password;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword(){return password;}

    public void setPassword(String password) {
        this.password = password;
    }
}
