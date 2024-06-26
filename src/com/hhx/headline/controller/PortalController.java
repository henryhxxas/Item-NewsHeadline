package com.hhx.headline.controller;

import com.hhx.headline.common.Result;
import com.hhx.headline.pojo.NewsType;
import com.hhx.headline.pojo.vo.HeadlineDetailVo;
import com.hhx.headline.pojo.vo.HeadlinePageVo;
import com.hhx.headline.pojo.vo.HeadlineQueryVo;
import com.hhx.headline.service.NewsHeadlineService;
import com.hhx.headline.service.NewsTypeService;
import com.hhx.headline.service.impl.NewsHeadlineServiceImpl;
import com.hhx.headline.service.impl.NewsTypeServiceImpl;
import com.hhx.headline.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hhx
 * @Date: 2024/4/1 11:08
 * @Description: 门户控制器：那些不需要登录、不需要做增删改的门户页的请求都放在这里
 * @Version: 1.0
 */
@WebServlet("/portal/*")
public class PortalController extends BaseController{
    private NewsTypeService typeService=new NewsTypeServiceImpl();
    private NewsHeadlineService headlineService=new NewsHeadlineServiceImpl();
    /**
     * 查询所有头条类型的业务接口实现
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void findAllTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询所有新闻类型，装入Result响应给客户端
        List<NewsType> newsTypeList=typeService.findAll();
        WebUtil.writeJson(resp,Result.ok(newsTypeList));
    }

    /**
     * 分页查询头条信息的业务接口实现
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void findNewsPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收请求中的参数
        HeadlineQueryVo headlineQueryVo = WebUtil.readJson(req, HeadlineQueryVo.class);
        //将参数传递给服务层 进行分页查询
        /**pageInfo包含以下信息：
         * pageData:[
         *  {
         *     	"hid":"1",                     // 新闻id
         *     	"title":"尚硅谷宣布 ... ...",   // 新闻标题
         *     	"type":"1",                    // 新闻所属类别编号
         *     	"pageViews":"40",              // 新闻浏览量
         *     	"pastHours":"3" ,              // 发布时间已过小时数
         *     	"publisher":"1"                // 发布用户ID
         *    }
         * ],
         * pageNum:1,
         * pagesize:1,
         * totalPage:1,
         * totalSize:1
         */
        Map pageInfo=headlineService.findPage(headlineQueryVo);
        Map data=new HashMap();
        data.put("pageInfo",pageInfo);
        //将分页查询的结果转换成json响应给客户端
        WebUtil.writeJson(resp,Result.ok(data));
    }

    /**
     *查询头条详情的业务接口实现
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void showHeadlineDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收要查询头条的hid
        int hid = Integer.parseInt(req.getParameter("hid"));
        //调用服务层完成查询处理
        HeadlineDetailVo headlineDetailVo = headlineService.findHeadlineDetail(hid);
        //将查询道德信息响应给客户端
        Map data=new HashMap();
        data.put("headline",headlineDetailVo);
        WebUtil.writeJson(resp,Result.ok(data));
    }
}
