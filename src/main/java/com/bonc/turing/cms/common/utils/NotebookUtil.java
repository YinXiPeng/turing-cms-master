package com.bonc.turing.cms.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class NotebookUtil {

    private static final Logger logger = LoggerFactory.getLogger(NotebookUtil.class);

    private static String privateDocker;

    private static String publicDocker;

    private static String CONTEXT_URL = "api/tuling/contents";

    private static String OPEN_WEBSOCKET_URL = "api/tuling/sessions";

    private static String CLOSE_WEBSOCKET_URL = "api/sessions";

    private static String LOG_URL = "api/log";

    private static String FORK_URL = "api/forks";

    private static String CREATE_URL = "api/notebook";


    @Value("${notebook.private}")
    public void setPrivateDocker(String privateDocker){
        this.privateDocker = privateDocker;
    }

    @Value("${notebook.public}")
    public void setPublicDocker(String publicDocker){
        this.publicDocker = publicDocker;
    }

    /**
     * 
     * @param path notebook在镜像中的路径
     * @return 
     * @author yanggang
     * @description: 获取notebook文件内容
     * @date 2019/6/16 17:40
     */
    public static JSONObject getContent(String path) throws Exception{
        String url = publicDocker + CONTEXT_URL + path;
        logger.info("request notebook content : {}",url);
        JSONObject content = HttpUtil.sendGetRequest(url);
        logger.info("response notebook content : {}",content);
        if(1000 != content.getInteger("code")){
            return null;
        }else {
            content = content.getJSONObject("data");
            return content;
        }
    }

    /**
     * 
     * @param path 此时用的是私有镜像，故path为文件的相对路径
     * @param username 启动镜像使用的用户名
     * @return 
     * @author yanggang
     * @description: 获取连接websocket的session和kernel
     * @date 2019/6/16 17:45
     */
    public static JSONObject openWebsocket(String path , String username) throws Exception{
        String url = privateDocker + username +  "/" + OPEN_WEBSOCKET_URL;
        JSONObject sessionParam1 = new JSONObject();
        sessionParam1.put("path",path);
        sessionParam1.put("type","notebook");
        sessionParam1.put("name","");
        JSONObject sessionParam2 = new JSONObject();
        sessionParam2.put("id",null);
        sessionParam2.put("name","python3");
        sessionParam1.put("kernel",sessionParam2);
        logger.info("request open notebook websocket : {} param : {}",url,sessionParam1);
        JSONObject session = HttpUtil.sendPostRequestByJson(url, sessionParam1);
        logger.info("response open notebook websocket : {}",session);
        if(1000 != session.getInteger("code")){
            return null;
        }else {
            session = session.getJSONObject("data");
            return session;
        }
    }


    /**
     * 
     * @param id 连接websocket的sessionId
     * @param username 启动镜像使用的用户名
     * @author yanggang
     * @description: 关闭websocket
     * @date 2019/6/16 17:59
     */
    public static void closeWebsocket(String id,String username)throws Exception{
        String url = privateDocker + username + "/" + CLOSE_WEBSOCKET_URL + "/" + id;
        logger.info("request close notebook websocket : {}",url);
        HttpUtil.delete(url);
    }

    /**
     * 
     * @param username 启动镜像使用的用户名
     * @param id notebookId
     * @param notebookName
     * @return 
     * @author yanggang
     * @description: 生成运行日志
     * @date 2019/6/16 18:14
     */
    public static List<String> getLog(String username, String id, String notebookName) throws Exception{
        String url = privateDocker + username + "/" + LOG_URL;
        MultiValueMap<String, Object> param = new LinkedMultiValueMap();
        param.add("version_name",notebookName);
        param.add("version_id",id);
        logger.info("request notebook log : {}, param : {}",url,param);
        JSONObject log = HttpUtil.sendPostRequestByFormData(url, param);
        List<String> data = Arrays.asList(log.getString("data").split("-->"));
        return data;
    }

    /**
     * 
     * @param oriUsername 原用户名
     * @param oriNotebookName 原用户的notebook文件名
     * @param newUsername 现用户名
     * @param newNotebookName 现用户的notebook文件名
     * @return
     * @author yanggang
     * @description: 从A用户的文件变成B用户的文件
     * @date 2019/6/16 18:30
     */
    public static void fork(String oriUsername,String oriNotebookName,String newUsername,String newNotebookName) throws Exception{
        String url = publicDocker + FORK_URL;
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("ori_username",oriUsername);
        param.add("new_username",newUsername);
        param.add("ori_notebook_name",oriNotebookName+ ".ipynb");
        param.add("new_notebook_name",newNotebookName+ ".ipynb");
        logger.info("request notebook fork : {}, param : {}",url,param);
        JSONObject result = HttpUtil.sendPostRequestByFormData(url, param);
        logger.info("response notebook fork : {}",result);
        if(1000 != result.getInteger("code")){
            throw new Exception();
        }
    }


    /**
     * 
     * @param username 启动镜像使用的用户名
     * @param path 此时用的是私有镜像，故path为文件的相对路径
     * @param content notebook文件内容
     * @return
     * @author yanggang
     * @description: 保存代码
     * @date 2019/6/16 18:39
     */
    public static void saveContent(String username,String path,JSONObject content) throws Exception{
        String url;
        if(null == username){
            url = publicDocker + CONTEXT_URL + path;
        }else {
            url = privateDocker + username + "/" + CONTEXT_URL + path;
        }
        logger.info("request notebook save content : {}, param : {}",url,content);
        JSONObject result = HttpUtil.sendPostRequestByJson(url, content);
        logger.info("response notebook save content : {}",result);
        if(1000 != result.getInteger("code")){
            throw new Exception();
        }
    }

    /**
     * 
     * @param username 启动镜像使用的用户名
     * @param notebookName
     * @return
     * @author yanggang
     * @description: 根据模版新建notebook文件，只生成一次
     * @date 2019/6/16 18:51
     */
    public static void create(String username,String notebookName) throws Exception{
        String url = privateDocker + username + "/" + CREATE_URL;
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("username",username);
        param.add("notebook_name",notebookName);
        logger.info("request create notebook : {}, param : {}",url,param);
        JSONObject result = HttpUtil.sendPostRequestByFormData(url, param);
        logger.info("response create notebook : {}",result);
        if(1000 != result.getInteger("code")){
            throw new Exception();
        }
    }
    
    /**
     * 
     * @param file notebook原生文件内容
     * @return 
     * @author yanggang
     * @description: 获取文件中的图片、代码数、图片数、前8行markdown内容
     * @date 2019/6/17 10:25
     */
    public static JSONObject parse(JSONObject file){
        JSONObject result = new JSONObject();
        JSONObject content = file.getJSONObject("content");
        JSONArray cells = content.getJSONArray("cells");
        List list = new ArrayList<>();
        Integer codeNum = 0;
        Integer i = 0;
        JSONObject markdownCell = new JSONObject();
        String markdownSource = "";
        for (Object cell : cells) {
            String cell_type = JSONObject.parseObject(cell.toString()).getString("cell_type");
            if("code".equals(cell_type)){
                String source = JSONObject.parseObject(cell.toString()).getString("source");
                String[] split = source.split("\\n");
                for (String s : split) {
                    if(!StringUtils.isEmpty(s)){
                        codeNum ++;
                    }
                }
                JSONArray outputs = JSONObject.parseObject(cell.toString()).getJSONArray("outputs");
                if(outputs.size() > 0){
                    for (Object output : outputs) {
                        String dataOfOutputs = JSONObject.parseObject(output.toString()).getString("data");
                        JSONObject dataObject = JSONObject.parseObject(dataOfOutputs);
                        if(dataObject != null){
                            String image = dataObject.getString("image/png");
                            if(!StringUtils.isEmpty(image)){
                                list.add("data:image/png;base64," + image);
                            }
                        }
                    }
                }
            }else if("markdown".equals(cell_type)){
                if(i == 0){
                    markdownCell = (JSONObject) cell;
                    String source = JSONObject.parseObject(cell.toString()).getString("source");
                    String[] split = source.split("\\n");

                    int j = 1;
                    for (String s : split) {
                        if(!StringUtils.isEmpty(s)){
                            markdownSource += s + "\\n";
                            j += 1;
                            if(j==8) break;
                        }
                    }
                    JSONObject markdown = JSONObject.parseObject(markdownCell.toString());
                    markdown.put("source",markdownSource);
                    markdownCell = JSONObject.parseObject(markdown.toJSONString());

                }
                i += 1;
            }
        }
        result.put("imageList",list);
        result.put("codeNum",codeNum);
        result.put("imageNum",list.size());
        cells.clear();
        JSONObject object = new JSONObject();
        if(!markdownCell.isEmpty()){
            cells.add(markdownCell);
        }
        object.put("cells",cells);
        result.put("markdown",object);
        return result;
    }
}
