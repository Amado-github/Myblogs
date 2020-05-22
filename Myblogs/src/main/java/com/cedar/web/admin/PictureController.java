package com.cedar.web.admin;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

    @RequestMapping("upload/picture")
    public JSONObject hello(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "editormd-image-file", required = false) MultipartFile attach) {

        JSONObject jsonObject=new JSONObject();
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        try {
//            request.setCharacterEncoding("utf-8");
//            response.setHeader("Content-Type", "text/html");
//            String rootPath = request.getSession().getServletContext().getRealPath("/static/img/blog/");
//
//            System.out.println("editormd上传图片："+rootPath);
//
//            /**
//             * 文件路径不存在则需要创建文件路径
//             */
//            File filePath = new File(rootPath);
//            if (!filePath.exists()) {
//                filePath.mkdirs();
//            }
//
//            // 最终文件名
//            File realFile = new File(rootPath + File.separator + attach.getOriginalFilename());
//            FileUtils.copyInputStreamToFile(attach.getInputStream(), realFile);
//
//            // 下面response返回的json格式是editor.md所限制的，规范输出就OK

            jsonObject.put("success", 1);
            jsonObject.put("message", "上传成功");
            jsonObject.put("url", "www.baidu.com");//+attach.getOriginalFilename()
            System.out.println(jsonObject.get("url"));
        } catch (Exception e) {
            jsonObject.put("success", 0);
        }

        return jsonObject;
    }
    /**
     * 图片上传
     * @param photo
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> uploadPhoto(MultipartFile photo, HttpServletRequest request) {
        Map<String, String> ret = new HashMap<String, String>();
        if (photo == null) {
            ret.put("type", "error");
            ret.put("msg", "选择要上传的文件！");
            return ret;
        }
        if (photo.getSize() > 1024 * 1024 * 10) {
            ret.put("type", "error");
            ret.put("msg", "文件大小不能超过10M！");
            return ret;
        }
        //获取文件后缀
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1, photo.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            ret.put("type", "error");
            ret.put("msg", "请选择jpg,jpeg,gif,png格式的图片！");
            return ret;
        }
        //获取项目根目录加上图片目录 webapp/static/imgages/upload/
        String savePath = request.getSession().getServletContext().getRealPath("/") + "/static/images/upload/";
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            savePathFile.mkdir();
        }
        String filename = new Date().getTime() + "." + suffix;
        try {
            //将文件保存指定目录
            photo.transferTo(new File(savePath + filename));
        } catch (Exception e) {
            ret.put("type", "error");
            ret.put("msg", "保存文件异常！");
            e.printStackTrace();
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "上传图片成功！");
        ret.put("filepath", request.getSession().getServletContext().getContextPath() + "/static/images/upload/");
        ret.put("filename", filename);
        return ret;
    }
}
