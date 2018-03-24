package cn.it8090.solrdemo.service;


import cn.it8090.solrdemo.po.Products;
import cn.it8090.solrdemo.po.ProductsQuery;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @PoweredBy Amour Mars
 * @Date 20:52 2018/1/23
 */
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private String solrUrl = "http://localhost:8080/solr";

    @Override
    public List<Products> searchProducts(ProductsQuery productsQuery) throws Exception {

        // 调用solrj进行搜索
        // 创建solr服务对象，通过此对象向solr服务发起请求

        // 进行参数的合法性校验
        if (productsQuery == null) {
            productsQuery = new ProductsQuery();
        }
        // 如果当前页码为空
        if (productsQuery.getCurPage() == null) {
            // 指定当前第一页
            productsQuery.setCurPage(1);
        }
        // 如果每页显示个数为空
        if (productsQuery.getRows() == null) {
            // 指定每页显示16个记录
            productsQuery.setRows(16);
        }

        // 调用solrj进行搜索
        // 创建solr服务对象，通过此对象向solr服务发起请求
        HttpSolrServer server = new HttpSolrServer(solrUrl);

        // 创建查询对象
        SolrQuery query = new SolrQuery();

        // 如果没有输入关键字查询全部
        if (productsQuery.getKeywords() == null
                || productsQuery.getKeywords().equals("")) {
            query.set("q", "*:*");
        } else {
            query.set("q", "product_keywords:" + productsQuery.getKeywords());
        }

        //商品分类 搜索
        //使用过虑
        if(productsQuery.getCatalog_name()!=null && !productsQuery.getCatalog_name().equals("")){
            query.add("fq", "product_catalog_name:"+productsQuery.getCatalog_name());
        }

        //价格过虑
        //下边要拼接product_price:[1 TO 3]
        String price_filterString = null;

        if(productsQuery.getPrice_start()!=null){
            price_filterString = "product_price:["+productsQuery.getPrice_start();
            if(productsQuery.getPrice_end()!=null){
                price_filterString +=" TO "+productsQuery.getPrice_end() + "]";
            }else{
                price_filterString +=" TO *]";
            }
        }
        if(price_filterString !=null){//表示进行价格范围查询
            query.add("fq", price_filterString);
        }

        //价格排序
        if(productsQuery.getSortField()!=null && !productsQuery.getSortField().equals("")){
            if(productsQuery.getSortType()!=null && productsQuery.getSortType().equals("asc")){//升序
                query.addSort(productsQuery.getSortField(), SolrQuery.ORDER.asc);
            }else if(productsQuery.getSortType()!=null && productsQuery.getSortType().equals("desc")){
                query.addSort(productsQuery.getSortField(), SolrQuery.ORDER.desc);
            }
        }

        // 分页
        int start = productsQuery.getRows() * (productsQuery.getCurPage() - 1);

        // 设置分页参数
        query.setStart(start);
        query.setRows(productsQuery.getRows());



        // 开启高亮
        query.setHighlight(true);
        // 设置高亮 参数
        query.addHighlightField("product_name");
        // 可以添加多个域高亮
        // query.addHighlightField(f)
        // 设置高亮前缀和后缀
        query.setHighlightSimplePre("<span style=\"color:red\">");
        query.setHighlightSimplePost("</span>");

        // 执行查询
        QueryResponse response = server.query(query);

        // 从响应中得到结果

        SolrDocumentList docs = response.getResults();

        // 匹配到的总记录数
        long numFound = docs.getNumFound();
        // 将总记录数传到页面
        productsQuery.setRecordCount(numFound);

        System.out.println("匹配到的总记录数:" + numFound);

        // 得出总记录数，根据总记录数和每页显示的个数计算出总页数
        int pageCount = (int) Math.ceil(numFound * 1.0
                / productsQuery.getRows());
        // 用于将pageCount传到页面
        productsQuery.setPageCount(pageCount);
        //从响应中获取高亮信息
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        // 记录列表
        List<Products> products = new ArrayList<Products>();
        for (SolrDocument doc : docs) {

            // 商品信息
            Products products_index = new Products();

            // 商品信息id主键
            if (doc.get("id") != null) {
                products_index.setId(doc.get("id").toString());
            }

            // 商品名称
            if (doc.get("product_name") != null) {
                products_index.setName(doc.get("product_name").toString());
            }

            // 商品价格
            if (doc.get("product_price") != null) {
                products_index.setPrice(Float.parseFloat(doc.get(
                        "product_price").toString()));
            }
            // 商品分类名称
            if (doc.get("product_catalog_name") != null) {
                products_index.setCatalog_name(doc.get("product_catalog_name")
                        .toString());
            }
            // 商品图片
            if (doc.get("product_picture") != null) {
                products_index
                        .setPicture(doc.get("product_picture").toString());
            }

            //要获取高亮的信息
            if(highlighting!=null){
                //根据主键获取高亮信息
                Map<String, List<String>> map = highlighting.get(doc.get("id"));
                if(map!=null){
                    //获取高亮信息
                    List<String> list = map.get("product_name");
                    if(list!=null){
                        products_index.setName(list.get(0));

                    }

                }
            }
            products.add(products_index);

        }

        return products;
    }
}
