function checkForm() {
    // 获取用户名输入项
    let customerName = document.getElementById("customerName");
    let uName = customerName.value;
    if (uName.length < 1 || uName.length > 10) {
        alert("账号长度为1-10位!!");
        return false;
    }

    //密码长度大于6 和确认必须一致
    let password = document.getElementById("password");
    let userPass = password.value;
    if (userPass.length < 6) {
        alert("密码的长度必须大于6位!");
        return false;
    }

    //获取确认密码框的值 var
    let surePassword = document.getElementById("surePassword");
    let surePass = surePassword.value;
    if (userPass !== surePass) {
        alert("两次密码不一致!!");
        return false;
    }

    //校验邮箱:/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
    let inputEmail = document.getElementById("email");
    let uEmail = inputEmail.value;
    if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/.test(uEmail)) {
        alert("邮箱不合法!");
        return false;
    }

    // 校验自我介绍
    let phoneText = document.getElementById("phone");
    let phone = phoneText.value;
    if (phone.length !== 11) {
        alert("手机号不合法");
        return false;
    }

    $.post(
        "/customer/register",
        {
            "customerName": customerName,
            "pwd": userPass,
            "email": uEmail,
            "phone": phone
        },
        function (data) {
            data = $.toJSON(data);
            if (data.code) {
                alert(data.msg);
            } else {
                window.location.href = "/customer/login";
            }
        }
    );
    return true;
}

function clearForm() {
}