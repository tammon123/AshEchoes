<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="email code">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<!--邮箱验证码模板-->
<body>
<div style="background-color:#ECECEC; padding: 35px;">
    <table cellpadding="0" align="center"
           style="width: 400px;height: 100%; margin: 0 auto; text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse; background-position: initial initial; background-repeat: initial initial;background:#fff;">
        <tbody>
        <tr>
            <th valign="middle"
                style="height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #3467ff; background-color: #3467ff; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;">
                <font face="微软雅黑" size="5" style="color: rgb(255, 255, 255); ">{0}</font>
            </th>
        </tr>
        <tr>
            <td style="word-break:break-all">
                <div style="padding:25px 35px 40px; background-color:#fff;opacity:0.8;">
                    <h2 style="margin: 5px 0; ">
                        <font color="#333333" style="line-height: 20px; ">
                            <font style="line-height: 22px; " size="4">
                                当前密码找回验证码为：</font>
                        </font>
                    </h2>
                    <p style='color:red'>
                        {1}
                    </p>
                    <p style='color:#3467ff'>
                        该验证码当天有效
                    </p>
                    <h2 style="margin: 5px 0; ">
                        <font color="#333333" style="line-height: 20px; ">
                            <font style="line-height: 22px; " size="4">
                                已有账号列表：</font>
                        </font>
                    </h2>

                    <p>
                        {2}
                    </p>
                    <div style="width:100%;margin:0 auto;">
                        <div style="padding:10px 10px 0 0;border-top:1px solid #3467ff;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;">
                            <p>白荆小助手</p>
                            <p>联系QQ群：865686593</p>
                            <br>
                            <p>此为系统邮件，请勿回复<br>
                            </p>
                        </div>
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>

